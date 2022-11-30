package com.mjimmer.imagestwoworld.world.level.levelgen;

import com.mjimmer.imagestwoworld.I2W;
import com.mjimmer.imagestwoworld.gen.heightmap.HeightMapHelper;
import com.mjimmer.imagestwoworld.gen.structures.StructuresHelper;
import com.mojang.datafixers.util.Pair;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.*;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Stream;

import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.DensityFunctions.BeardifierMarker;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.Nullable;

import net.minecraft.world.level.biome.BiomeResolver;

public class I2WChunkGenerator extends NoiseBasedChunkGenerator {
    public static final Codec<I2WChunkGenerator> CODEC = RecordCodecBuilder.create((instance) -> {
        return commonCodec(instance)
                .and(
                        instance.group(
                                RegistryOps.retrieveRegistry(Registry.NOISE_REGISTRY).forGetter( (i2wChunkGenerator) -> {
                                    return i2wChunkGenerator.noises;
                                }),
                                BiomeSource.CODEC.fieldOf("biome_source")
                                        .forGetter( (i2wChunkGenerator) -> {
                                            return i2wChunkGenerator.biomeSource;
                                        }),
                                NoiseGeneratorSettings.CODEC.fieldOf("settings")
                                        .forGetter( (i2wChunkGenerator) -> {
                                            return i2wChunkGenerator.settings;
                                        })
                        )
                ).apply( instance, instance.stable( I2WChunkGenerator::new ));
    });

    private static final BlockState AIR;
    ////////////////////////////////////////////////////////////
    private static final BlockState[] EMPTY;
    private static final BlockState NETHERRACK;
    private static final BlockState END_STONE;
    private static final BlockState LAVA;

    protected final BlockState defaultFluid;
    ////////////////////////////////////////////////////////////

    protected final BlockState defaultBlock;
    private final Registry<NormalNoise.NoiseParameters> noises;
    protected final Holder<NoiseGeneratorSettings> settings;
    private final Aquifer.FluidPicker globalFluidPicker;

    //////////////////////////////////////////////////
    public HeightMapHelper heightMapHelper = I2W.hHelper;
    private StructuresHelper structuresHelper = null;

    private static int seaLevel = 63;
    //////////////////////////////////////////////////

    public I2WChunkGenerator(Registry<StructureSet> registry,
                             Registry<NormalNoise.NoiseParameters> registry2,
                             BiomeSource biomeSource,
                             Holder<NoiseGeneratorSettings> holder)
    {
        super(registry, registry2, biomeSource, holder);

        this.noises = registry2;
        this.settings = holder;
        NoiseGeneratorSettings noiseGeneratorSettings = this.settings.value();
        this.defaultBlock = noiseGeneratorSettings.defaultBlock();
        /////////
        this.defaultFluid = noiseGeneratorSettings.defaultFluid();
        ////////
        Aquifer.FluidStatus fluidStatus = new Aquifer.FluidStatus(-54, Blocks.LAVA.defaultBlockState());

        int seaLevel1 = noiseGeneratorSettings.seaLevel();
        ////////////////////////////////////////////
        this.seaLevel = seaLevel1;
        if(I2W.CONFIGS.HEIGHTMAP.customHeightMap && this.seaLevel != I2W.CONFIGS.GENERAL.configseaLevel) {
            this.seaLevel = I2W.CONFIGS.GENERAL.configseaLevel;
        }
        /////////////////////////////////
        Aquifer.FluidStatus fluidStatus2 = new Aquifer.FluidStatus( seaLevel1, noiseGeneratorSettings.defaultFluid());
        this.globalFluidPicker = (j, k, l) -> {
            return k < Math.min(-54, seaLevel1) ? fluidStatus : fluidStatus2;
        };
    }

    private BlockState debugPreliminarySurfaceLevel(NoiseChunk noiseChunk, int i, int j, int k, BlockState blockState) {
        return blockState;
    }

    @Override
    public int getGenDepth() {
        return this.settings.value().noiseSettings().height();
    }

    @Override
    public int getSeaLevel() {
        return this.settings.value().seaLevel();
    }

    @Override
    public int getMinY() {
        return this.settings.value().noiseSettings().minY();
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void addDebugScreenInfo(List<String> list, RandomState randomState, BlockPos blockPos) {
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        NoiseRouter noiseRouter = randomState.router();
        DensityFunction.SinglePointContext singlePointContext = new DensityFunction.SinglePointContext(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        double d = noiseRouter.ridges().compute(singlePointContext);
        list.add("I2WGenerator"
                + " T: " + decimalFormat.format(noiseRouter.temperature().compute(singlePointContext))
                + " V: "  + decimalFormat.format(noiseRouter.vegetation().compute(singlePointContext))
                + " C: "  + decimalFormat.format(noiseRouter.continents().compute(singlePointContext))
                + " E: "  + decimalFormat.format(noiseRouter.erosion().compute(singlePointContext))
                + " D: "  + decimalFormat.format(noiseRouter.depth().compute(singlePointContext))
                + " W: "  + decimalFormat.format(d)
                + " PV: " + decimalFormat.format((double) NoiseRouterData.peaksAndValleys((float) d))
                + " AS: " + decimalFormat.format(noiseRouter.initialDensityWithoutJaggedness().compute(singlePointContext))
                + " N: "  + decimalFormat.format(noiseRouter.finalDensity().compute(singlePointContext)));
    }


    ///////////////////////////////
    // NOISE AND HEIGHTMAP
    ///////////////////////////////

    @Override
    public Holder<NoiseGeneratorSettings> generatorSettings() {
        return this.settings;
    }

    // start of noise per-chunk
    private NoiseChunk createNoiseChunk(ChunkAccess chunkAccess, StructureManager structureManager,
            Blender blender, RandomState randomState)
    {
        return NoiseChunk.forChunk(
                chunkAccess,
                randomState,
                Beardifier.forStructuresInChunk(structureManager, chunkAccess.getPos()),
                this.settings.value(),
                this.globalFluidPicker,
                blender
        );
    }

    @Override
    public boolean stable(ResourceKey<NoiseGeneratorSettings> resourceKey) {
        return this.settings.is(resourceKey);
    }

    @Override
    public int getBaseHeight(int i, int j, Heightmap.Types types, LevelHeightAccessor levelHeightAccessor, RandomState randomState) {


        ///////////////////////////////////////////////////////////////////////////////  \/
        if(I2W.CONFIGS.HEIGHTMAP.customHeightMap && heightMapHelper.getHeightMapDataProvider().isInImage(i, j) ){
                int height = this.heightMapHelper.getHeight(i, j) + 1;
                if( types == Heightmap.Types.WORLD_SURFACE_WG ){
                    height = Math.max(height, this.seaLevel + 1);
                }else if( types == Types.WORLD_SURFACE){

                }else if ( types == Types.OCEAN_FLOOR_WG ) {

                }else if ( types == Types.OCEAN_FLOOR){

                }
                return height;
        }

        /////////////////////////////////////////////////////////////////////////////// /\

        return this.iterateNoiseColumn(
                        levelHeightAccessor,
                        randomState,
                        i, // pos.x 
                        j, // pos.z
                        (MutableObject) null,
                        types.isOpaque()
                ).orElse( levelHeightAccessor.getMinBuildHeight() );
    }

    @Override
    public NoiseColumn getBaseColumn(int i, int j, LevelHeightAccessor levelHeightAccessor, RandomState randomState) {
        MutableObject<NoiseColumn> mutableObject = new MutableObject();
        this.iterateNoiseColumn(levelHeightAccessor, randomState, i, j, mutableObject, (Predicate) null);
        return mutableObject.getValue();
    }

    private OptionalInt iterateNoiseColumn(LevelHeightAccessor levelHeightAccessor, RandomState randomState, int i, int j,
                                           @Nullable MutableObject<NoiseColumn> mutableObject, @Nullable Predicate<BlockState> predicate) {
        NoiseSettings noiseSettings = this.settings.value().noiseSettings().clampToHeightAccessor(levelHeightAccessor);
        int yBlkSize = noiseSettings.getCellHeight();
        int minY = noiseSettings.minY();
        int minYFloor = Mth.intFloorDiv(minY, yBlkSize);
        int zBlkSize = Mth.intFloorDiv(noiseSettings.height(), yBlkSize);
        if (zBlkSize > 0) {
            BlockState[] blockStates;
            if (mutableObject == null) {
                blockStates = null;
            } else {
                blockStates = new BlockState[noiseSettings.height()];
                mutableObject.setValue(new NoiseColumn(minY, blockStates));
            }

            int xBlkSize = noiseSettings.getCellWidth();

            int xPoint = Math.floorDiv(i, xBlkSize);
            int zPoint = Math.floorDiv(j, xBlkSize);

            int tmp_xPoint = Math.floorMod(i, xBlkSize);
            int tmp_zPoint = Math.floorMod(j, xBlkSize);

            int firstXPoint = xPoint * xBlkSize;
            int firstZPoint = zPoint * xBlkSize;

            double noiseX = (double) tmp_xPoint / (double) xBlkSize;
            double noiseZ = (double) tmp_zPoint / (double) xBlkSize;

            NoiseChunk noiseChunk = new NoiseChunk(
                    1,
                    randomState,
                    firstXPoint,
                    firstZPoint,
                    noiseSettings,
                    BeardifierMarker.INSTANCE,
                    this.settings.value(),
                    this.globalFluidPicker,
                    Blender.empty()
            );
            noiseChunk.initializeForFirstCellX();
            noiseChunk.advanceCellX(0);

            for (int currentZ = zBlkSize - 1; currentZ >= 0; --currentZ) {
                noiseChunk.selectCellYZ(currentZ, 0);

                for (int currentY = yBlkSize - 1; currentY >= 0; --currentY) {


                    int yblk = (minYFloor + currentZ) * yBlkSize + currentY;

                    double noiseY = (double) currentY / (double) yBlkSize;
                    noiseChunk.updateForY(yblk, noiseY);
                    noiseChunk.updateForX(i,    noiseX);
                    noiseChunk.updateForZ(j,    noiseZ);
                    BlockState blockState = noiseChunk.getInterpolatedState();
                    BlockState blockState2 = blockState == null ? this.defaultBlock : blockState;
                    if (blockStates != null) {
                        int y = currentZ * yBlkSize + currentY;
                        blockStates[y] = blockState2;
                    }

                    if (predicate != null && predicate.test(blockState2)) {
                        noiseChunk.stopInterpolation();
                        return OptionalInt.of(yblk + 1);
                    }
                }
            }

            noiseChunk.stopInterpolation();
        }
        return OptionalInt.empty();
    }


    ////////////////////////////
    // CAVES (HEIGHMAP)
    ////////////////////////
    @Override
    public void applyCarvers(WorldGenRegion worldGenRegion, long l, RandomState randomState, BiomeManager biomeManager,
                             StructureManager structureManager, ChunkAccess chunkAccess, GenerationStep.Carving carving) {
        BiomeManager biomeManager2 = biomeManager.withDifferentSource((ix, jx, kx) -> {
            return this.biomeSource.getNoiseBiome(ix, jx, kx, randomState.sampler());
        });
        WorldgenRandom worldgenRandom = new WorldgenRandom(new LegacyRandomSource(RandomSupport.generateUniqueSeed()));
        ChunkPos chunkPos = chunkAccess.getPos();
        NoiseChunk noiseChunk = chunkAccess.getOrCreateNoiseChunk((chunkAccessx) -> {
            return this.createNoiseChunk(chunkAccessx, structureManager, Blender.of(worldGenRegion), randomState);
        });
        Aquifer aquifer = noiseChunk.aquifer();
        CarvingContext carvingContext = new CarvingContext(
                this,
                worldGenRegion.registryAccess(),
                chunkAccess.getHeightAccessorForGeneration(),
                noiseChunk,
                randomState,
                this.settings.value().surfaceRule()
        );
        CarvingMask carvingMask = ((ProtoChunk) chunkAccess).getOrCreateCarvingMask(carving);

        for (int j = -8; j <= 8; ++j) {
            for (int k = -8; k <= 8; ++k) {
                ChunkPos chunkPos2 = new ChunkPos(chunkPos.x + j, chunkPos.z + k);
                ChunkAccess chunkAccess2 = worldGenRegion.getChunk(chunkPos2.x, chunkPos2.z);
                BiomeGenerationSettings biomeGenerationSettings = chunkAccess2.carverBiome(() -> {
                    return this.getBiomeGenerationSettings(this.biomeSource.getNoiseBiome(QuartPos.fromBlock(chunkPos2.getMinBlockX()),
                            0, QuartPos.fromBlock(chunkPos2.getMinBlockZ()), randomState.sampler()));
                });
                Iterable<Holder<ConfiguredWorldCarver<?>>> iterable = biomeGenerationSettings.getCarvers(carving);
                int m = 0;

                for (Iterator var24 = iterable.iterator(); var24.hasNext(); ++m) {
                    Holder<ConfiguredWorldCarver<?>> holder = (Holder) var24.next();
                    ConfiguredWorldCarver<?> configuredWorldCarver = (ConfiguredWorldCarver) holder.value();
                    worldgenRandom.setLargeFeatureSeed(l + (long) m, chunkPos2.x, chunkPos2.z);
                    if (configuredWorldCarver.isStartChunk(worldgenRandom)) {
                        Objects.requireNonNull(biomeManager2);
                        configuredWorldCarver.carve(carvingContext, chunkAccess, biomeManager2::getBiome, worldgenRandom, aquifer, chunkPos2, carvingMask);
                    }
                }
            }
        }

    }

    ///////////////////////////////
    // SURFACE (HEIGHMAP)
    ///////////////////////////////
    @Override
    public void buildSurface(WorldGenRegion worldGenRegion, StructureManager structureManager, RandomState randomState, ChunkAccess chunkAccess) {
        if (!SharedConstants.debugVoidTerrain(chunkAccess.getPos())) {
            WorldGenerationContext worldGenerationContext = new WorldGenerationContext(this, worldGenRegion);
            this.buildSurface(chunkAccess, worldGenerationContext, randomState, structureManager, worldGenRegion.getBiomeManager(),
                    worldGenRegion.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY), Blender.of(worldGenRegion));

        }
    }

    @VisibleForTesting
    @Override
    public void buildSurface(ChunkAccess chunkAccess, WorldGenerationContext worldGenerationContext, RandomState randomState,
                             StructureManager structureManager, BiomeManager biomeManager, Registry<Biome> registry, Blender blender)
    {
        NoiseGeneratorSettings noiseGeneratorSettings = this.settings.value();
        NoiseChunk noiseChunk = chunkAccess.getOrCreateNoiseChunk((chunkAccessx) -> {
            return this.createNoiseChunk(chunkAccessx, structureManager, blender, randomState);
        });
        randomState.surfaceSystem().buildSurface(randomState, biomeManager, registry, noiseGeneratorSettings.useLegacyRandomSource(),
                worldGenerationContext, chunkAccess, noiseChunk, noiseGeneratorSettings.surfaceRule());
    }


    ////////////////////////////////
    // FILL HEIGHTMAP
    ///////////////////////////////
    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, RandomState randomState,
                                                        StructureManager structureManager, ChunkAccess chunkAccess) {
        NoiseSettings noiseSettings = this.settings.value().noiseSettings()
                .clampToHeightAccessor(chunkAccess.getHeightAccessorForGeneration());
        int i = noiseSettings.minY();
        int j = Mth.intFloorDiv(i, noiseSettings.getCellHeight());
        int k = Mth.intFloorDiv(noiseSettings.height(), noiseSettings.getCellHeight());
        if (k <= 0) {
            return CompletableFuture.completedFuture(chunkAccess);
        } else {
            int l = chunkAccess.getSectionIndex(k * noiseSettings.getCellHeight() - 1 + i);
            int m = chunkAccess.getSectionIndex(i);
            Set<LevelChunkSection> set = Sets.newHashSet();

            for (int n = l; n >= m; --n) {
                LevelChunkSection levelChunkSection = chunkAccess.getSection(n);
                levelChunkSection.acquire();
                set.add(levelChunkSection);
            }

            return CompletableFuture.supplyAsync(Util.wrapThreadWithTaskName("wgen_fill_noise", () -> {
                return this.doFill(blender, structureManager, randomState, chunkAccess, j, k);
            }), Util.backgroundExecutor()).whenCompleteAsync((chunkAccessx, throwable) -> {
                Iterator var3 = set.iterator();

                while (var3.hasNext()) {
                    LevelChunkSection levelChunkSection = (LevelChunkSection) var3.next();
                    levelChunkSection.release();
                }

            }, executor);
        }
    }

    private ChunkAccess doFill(Blender blender, StructureManager structureManager, RandomState randomState, ChunkAccess chunkAccess, int i, int j) {
        NoiseChunk noiseChunk = chunkAccess.getOrCreateNoiseChunk((chunkAccessx) -> {
            return this.createNoiseChunk(chunkAccessx, structureManager, blender, randomState);
        });
        Heightmap heightmap = chunkAccess.getOrCreateHeightmapUnprimed(Types.OCEAN_FLOOR_WG);
        Heightmap heightmap2 = chunkAccess.getOrCreateHeightmapUnprimed(Types.WORLD_SURFACE_WG);
        ChunkPos chunkPos = chunkAccess.getPos();
        int startX = chunkPos.getMinBlockX();
        int startZ = chunkPos.getMinBlockZ();

        /////////////////////////////////////ours \/
        int chunkPosX = chunkPos.x;
        int chunkPosZ = chunkPos.z;
        int chunkPosXShift = chunkPosX<<4;
        int chunkPosZShift = chunkPosZ<<4;
        if( chunkPosX == 4 && chunkPosZ == 16 ){
            I2W.LOGGER.log(Level.DEBUG, "NETHER");
        }
        /////////////////////////////////////them /\
        Aquifer aquifer = noiseChunk.aquifer();
        noiseChunk.initializeForFirstCellX();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int sampleHBlkSize = noiseChunk.cellWidth();
        int sampleVBlkSize = noiseChunk.cellHeight();

        // subcunks ?
        int sampleHorizontalBlkLimit = 16 / sampleHBlkSize;
        int sampleHorizontalBlkLimitDepth = 16 / sampleHBlkSize;


        /////////////////////////////////////////////ours \/


        int[][] heightT = new int[16][16];
        Biome[][] biomeT = new Biome[16][16];

        if( I2W.CONFIGS.HEIGHTMAP.customHeightMap ) {
            for(int biomeX = 0;biomeX < 16;biomeX++){
                for(int biomeZ = 0;biomeZ < 16;biomeZ++){

                    if( heightMapHelper.isInImage(  chunkPosXShift+biomeX,chunkPosZShift+biomeZ ))
                    {
                        heightT[biomeX][biomeZ] = heightMapHelper.getHeight(chunkPosXShift+biomeX,chunkPosZShift+biomeZ);
                    }
                    // FIXME need to check out what the I2wBiomeSource wants at this point so we dont have to call rand->sample
                    biomeT[biomeX][biomeZ] = this.biomeSource.getNoiseBiome(
                            QuartPos.fromSection(chunkPosX) + QuartPos.fromBlock(biomeX),
                            80,
                            QuartPos.fromSection(chunkPosZ) + QuartPos.fromBlock(biomeZ),
                            randomState.sampler()
                    ).value();
                }
            }
        }
        ////////////////////////////////////////////them /\

        for (int hBlkLimitCount = 0; hBlkLimitCount < sampleHorizontalBlkLimit; ++hBlkLimitCount) { // loop 4
            noiseChunk.advanceCellX(hBlkLimitCount); // End Noise

            for (int hBlkDepthCounter = 0; hBlkDepthCounter < sampleHorizontalBlkLimitDepth; ++hBlkDepthCounter) {
                LevelChunkSection levelChunkSection = chunkAccess.getSection(chunkAccess.getSectionsCount() - 1);

                for (int vBlkLimitCount = j - 1; vBlkLimitCount >= 0; --vBlkLimitCount) { // loop 48
                    noiseChunk.selectCellYZ(vBlkLimitCount, hBlkDepthCounter);  // corners

                    for (int currentYPos = sampleVBlkSize - 1; currentYPos >= 0; --currentYPos) { // Y (8 loop)
                        int yPos = (i + vBlkLimitCount) * sampleVBlkSize + currentYPos;
                        int y = yPos & 15;
                        int w = chunkAccess.getSectionIndex(yPos);
                        if (chunkAccess.getSectionIndex(levelChunkSection.bottomBlockY()) != w) {
                            levelChunkSection = chunkAccess.getSection(w);
                        }
                        double yNoise = (double) currentYPos / (double) sampleVBlkSize;
                        noiseChunk.updateForY(yPos, yNoise);

                        for (int currentXPos = 0; currentXPos < sampleHBlkSize; ++currentXPos) { // X(4 loop)
                            int xPos = startX + hBlkLimitCount * sampleHBlkSize + currentXPos;
                            int x = xPos & 15;
                            double xNoise = (double) currentXPos / (double) sampleHBlkSize;
                            noiseChunk.updateForX(xPos, xNoise);

                            for (int currentZPos = 0; currentZPos < sampleHBlkSize; ++currentZPos) { // Z(4 loop)
                                int zPos = startZ + hBlkDepthCounter * sampleHBlkSize + currentZPos;
                                int z = zPos & 15;
                                double zNoise = (double) currentZPos / (double) sampleHBlkSize;

                                noiseChunk.updateForZ(zPos, zNoise);

                                BlockState blockState = noiseChunk.getInterpolatedState();
                                ////////////////////////////////////////////  \/
                                if(I2W.CONFIGS.HEIGHTMAP.customHeightMap &&
                                        this.heightMapHelper.getHeightMapDataProvider().isInImage(xPos, zPos) )
                                {
                                        int height = heightT[x][z];
                                        if( height > this.seaLevel ){
                                            blockState = this.defaultBlock;

                                            if( yPos > height){
                                                blockState = AIR;
                                            }
                                        }else{
                                            if( yPos >= seaLevel ) {
                                                blockState = AIR;
                                            } else if (yPos <= height){
                                                blockState = defaultBlock;
                                            } else {
                                                blockState = defaultFluid;
                                            }
                                        }
                                }
                                ////////////////////////////////////////////////// /\

                                if (blockState == null) {
                                    blockState = this.defaultBlock;
                                }
                                blockState = this.debugPreliminarySurfaceLevel(noiseChunk, xPos, yPos, zPos, blockState);
                                if (blockState != AIR && !SharedConstants.debugVoidTerrain(chunkAccess.getPos())) {
                                    if (blockState.getLightEmission() != 0 && chunkAccess instanceof ProtoChunk) {
                                        mutableBlockPos.set(xPos, yPos, zPos);
                                        ((ProtoChunk) chunkAccess).addLight(mutableBlockPos);
                                    }

                                    levelChunkSection.setBlockState(x, y, z, blockState, false);
                                    heightmap.update(x, yPos, z, blockState);
                                    heightmap2.update(x, yPos, z, blockState);
                                    if (aquifer.shouldScheduleFluidUpdate() && !blockState.getFluidState().isEmpty()) {
                                        mutableBlockPos.set(xPos, yPos, zPos);
                                        chunkAccess.markPosForPostprocessing(mutableBlockPos);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            noiseChunk.swapSlices();
        }

        noiseChunk.stopInterpolation();
        return chunkAccess;
    }

    ///////////////////////////////
    // BIOMES see I2WBiomeSource
    ///////////////////////////////
    @Override
    public CompletableFuture<ChunkAccess> createBiomes(Registry<Biome> registry, Executor executor, RandomState randomState, Blender blender,
                                                       StructureManager structureManager, ChunkAccess chunkAccess) {

        return CompletableFuture.supplyAsync(Util.wrapThreadWithTaskName("init_biomes", () -> {
            this.doCreateBiomes(blender, randomState, structureManager, chunkAccess);
            return chunkAccess;
        }), Util.backgroundExecutor());
    }

    private void doCreateBiomes(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunkAccess) {

        NoiseChunk noiseChunk = chunkAccess.getOrCreateNoiseChunk((chunkAccessx) -> {
            return this.createNoiseChunk(chunkAccessx, structureManager, blender, randomState);
        });
        BiomeResolver biomeResolver = BelowZeroRetrogen.getBiomeResolver(blender.getBiomeResolver(this.biomeSource), chunkAccess);
        chunkAccess.fillBiomesFromNoise(
                biomeResolver,
                noiseChunk.cachedClimateSampler(randomState.router(), this.settings.value().spawnTarget())
        );

    }

    // TODO move everything over to a class(I2WStructureManager) that is inhernt of StructureManager
    ///////////////////////////////
    // STRUCTURES
    ///////////////////////////////
    @Override
    public Stream<Holder<StructureSet>> possibleStructureSets() {
        if (this.structureOverrides.isPresent()) {
            return this.structureOverrides.get().stream();
        }
        return this.structureSets.holders().map(Holder::hackyErase);
    }

    // this is the overall generation it runs at startup once before anything is "placed"
    @Override
    protected void generatePositions(RandomState randomState) {
        Set<Holder<Biome>> set = this.biomeSource.possibleBiomes();

        this.possibleStructureSets().forEach( holder -> {
            StructurePlacement structurePlacement;
            StructureSet structureSet = holder.value();
            boolean vaildStructure = false;
            for (StructureSet.StructureSelectionEntry structureSelectionEntry : structureSet.structures()) {
                Structure structure2 = structureSelectionEntry.structure().value();
                // TODO
                // might want to override this as if we want odd structures in say the end.
                if (!structure2.biomes().stream().anyMatch(set::contains)){
                    continue;
                }
                this.placementsForStructure.computeIfAbsent( structure2, structure ->
                        new ArrayList()).add( structureSet.placement() );
                vaildStructure = true;
            }

            // generate strongholds or other structures that are in the RingsStructurePlacement
            if (vaildStructure && (structurePlacement = structureSet.placement()) instanceof ConcentricRingsStructurePlacement) {
                ConcentricRingsStructurePlacement concentricRingsStructurePlacement = (ConcentricRingsStructurePlacement)structurePlacement;
                this.ringPositions.put(
                        concentricRingsStructurePlacement,
                        this.generateRingPositions(holder, randomState, concentricRingsStructurePlacement)
                );
            }
        });
    }




    public boolean genStructure( StructureManager structureManager,
                                 ChunkAccess chunkAccess,
                                 SectionPos sectionPos,
                                 StructureStart structureStart,
                                 Structure structure
    )
    {
        if (structureStart.isValid()) {
            structureManager.setStartForStructure(
                    sectionPos,
                    structure, // this
                    structureStart, // this
                    (StructureAccess)(Object) chunkAccess // StructureAccess structureAccess
            );
            return true;
        }
        return false;
    }

    public static final Predicate<Holder<Biome>> PREDICT_BIOME = biomeHolder -> {
        return true;
    };

    public boolean overrideStructure( StructureManager structureManager,
                                      RandomState randomState,
                                      StructureTemplateManager structureTemplateManager,
                                      long seed,
                                      ChunkAccess chunkAccess,
                                      SectionPos sectionPos,
                                      Holder<Structure> configuredStructure,
                                      Pair<Integer,Integer> integerIntegerPair,
                                      ChunkPos chunkPos,
                                      RegistryAccess registryAccess
                                     )
    {
        StructureSet.StructureSelectionEntry ss = new StructureSet.StructureSelectionEntry(configuredStructure, 1);
        AtomicReference<StructureStart> finalStructureStart = null;
        AtomicBoolean ret = new AtomicBoolean(false);
        this.possibleStructureSets().forEach( holder -> {
            StructurePlacement structurePlacement = holder.value().placement();
            List<StructureSet.StructureSelectionEntry> list = holder.value().structures();
            for (StructureSet.StructureSelectionEntry structureSelectionEntry : list) {
                String ssKey = ss.structure().unwrapKey().map(resourceKey -> resourceKey.location().toString()).orElse("[unregistered]");
                String bbKey = structureSelectionEntry.structure().unwrapKey().map(resourceKey -> resourceKey.location().toString()).orElse("[unregistered]");
                if ( ssKey.equals( bbKey ) && !bbKey.equals( "[unregistered]") ) {
//                    I2W.LOGGER.log(Level.INFO, "FOUND IT "  + ssKey + " " + bbKey );
                    StructureStart structureStart = structureSelectionEntry.structure().value().generate(
                            registryAccess,
                            (ChunkGenerator)(Object)this,
                            this.biomeSource,
                            randomState,
                            structureTemplateManager,
                            seed,
                            chunkPos,
                            0,
                            chunkAccess,
                            PREDICT_BIOME
                    );
                    if (structureStart == null || !structureStart.isValid()) {
                        continue;
                    }

                    if (this.genStructure( structureManager, chunkAccess, sectionPos, structureStart,
                            structureSelectionEntry.structure().value() ) )
                    {
//                        I2W.LOGGER.log(Level.INFO, "GENERATED STRUCTURE");
                        structureManager.setStartForStructure(
                                sectionPos,
                                structureSelectionEntry.structure().value(),
                                structureStart,
                                chunkAccess
                        );
                        ret.set(true);
                    }
                    else
                    {
                        I2W.LOGGER.log(Level.INFO, "FAILED GENERATED STRUCTURE");
                    }
                }
            }
        });
        return ret.get();
    }

    @Override
    public void createStructures(RegistryAccess registryAccess,
                                 RandomState randomState,
                                 StructureManager structureManager,
                                 ChunkAccess chunkAccess,
                                 StructureTemplateManager structureTemplateManager,
                                 long seed)
    {
        ChunkPos chunkPos = chunkAccess.getPos();
        SectionPos sectionPos = SectionPos.bottomOf(chunkAccess);
        if( I2W.CONFIGS.STRUCTURES.customStructures && I2W.sHelper.getStructureDataProvider().isInImage(chunkPos.x, chunkPos.z) ) {
                I2W.sHelper.getStructuresInPos(chunkPos).forEach((integerIntegerPair, structureHolder) -> {
                    if (structureHolder != null) {
                        Holder<Structure> configuredStructure = structureHolder;
                        if ( this.overrideStructure(
                                structureManager, randomState, structureTemplateManager, seed, chunkAccess, sectionPos,
                                configuredStructure, integerIntegerPair, chunkPos, registryAccess ))
                        {
                            String registeredStructure = configuredStructure.unwrapKey().map(resourceKey ->
                                    resourceKey.location().toString()).orElse("[unregistered]");
//                            I2W.LOGGER.log(Level.INFO, "AWESOME generated structure " + registeredStructure +
//                                    " At " +
//                                    chunkPos.x + " " +
//                                    chunkPos.z
//                            );
                        } else {
                            I2W.LOGGER.log(Level.INFO, " Bad news could not generate structure " + configuredStructure.value().toString() +
                                    " At " +
                                    chunkPos.x + " " +
                                    chunkPos.z
                            );
                        }
                    }
                });
        }


//        else // VANILLA
//        {
            this.possibleStructureSets().forEach(holder -> {
                StructurePlacement structurePlacement = holder.value().placement();
                List<StructureSet.StructureSelectionEntry> list = holder.value().structures();
                for (StructureSet.StructureSelectionEntry structureSelectionEntry : list) {
                    StructureStart structureStart = structureManager.getStartForStructure(
                            sectionPos,
                            structureSelectionEntry.structure().value(),
                            chunkAccess
                    );
                    if (structureStart == null || !structureStart.isValid()) {
                        continue;
                    }
                    return;
                }
                if (!structurePlacement.isStructureChunk(this, randomState, seed, chunkPos.x, chunkPos.z)) {
                    return;
                }
                if (list.size() == 1) {
                    this.tryGenerateStructure(
                            list.get(0),
                            structureManager,
                            registryAccess,
                            randomState,
                            structureTemplateManager,
                            seed,
                            chunkAccess,
                            chunkPos,
                            sectionPos);
//                    I2W.LOGGER.log(Level.INFO, "Added(SINGLE) Internal structure" + list.get(0).structure().value().toString());
                    return;
                }
                ArrayList<StructureSet.StructureSelectionEntry> arrayList = new ArrayList<StructureSet.StructureSelectionEntry>(list.size());
                arrayList.addAll(list);
                WorldgenRandom worldgenRandom = new WorldgenRandom(new LegacyRandomSource(0L));
                worldgenRandom.setLargeFeatureSeed(seed, chunkPos.x, chunkPos.z);
                int structureWeight = 0;
                for (StructureSet.StructureSelectionEntry structureSelectionEntry2 : arrayList) {
                    structureWeight += structureSelectionEntry2.weight();
                }
                while (!arrayList.isEmpty()) {
                    //  StructureSet.StructureSelectionEntry structureSelectionEntry3;
                    int randInt = worldgenRandom.nextInt(structureWeight);
                    int structureEntryCounter = 0;
                    Iterator iterator = arrayList.iterator();
                    while (iterator.hasNext() && (randInt -= ((StructureSet.StructureSelectionEntry) iterator.next()).weight()) >= 0) {
                        ++structureEntryCounter;
                    }
                    StructureSet.StructureSelectionEntry structureSelectionEntry4 = arrayList.get(structureEntryCounter);
                    if (this.tryGenerateStructure(
                            structureSelectionEntry4,
                            structureManager,
                            registryAccess,
                            randomState,
                            structureTemplateManager,
                            seed,
                            chunkAccess,
                            chunkPos,
                            sectionPos)
                    ) {
//                        I2W.LOGGER.log(Level.INFO, "Added(MULTI) Internal structure" + structureSelectionEntry4.structure().value().toString());
                        return;
                    }
                    arrayList.remove(structureEntryCounter);
                    structureWeight -= structureSelectionEntry4.weight();
                }
            });
//        }
    }

    // addStructureReferences (createReferenceOrCrash)
    @Override
    public void createReferences(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkAccess chunkAccess){
        int halfSize = 8;
        ChunkPos chunkPos = chunkAccess.getPos();
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;
        int chunkPosMinBlockX = chunkPos.getMinBlockX();
        int chunkPosMinBlockZ = chunkPos.getMinBlockZ();
        SectionPos sectionPos = SectionPos.bottomOf(chunkAccess);
        for (int n = chunkX - 8; n <= chunkX + 8; ++n) {
            for (int o = chunkZ - 8; o <= chunkZ + 8; ++o) {
                long p = ChunkPos.asLong(n, o);
                for (StructureStart structureStart : worldGenLevel.getChunk(n, o).getAllStarts().values()) {
                    try {
                        if (!structureStart.isValid() || !structureStart.getBoundingBox().intersects(
                                chunkPosMinBlockX,
                                chunkPosMinBlockZ,
                                chunkPosMinBlockX + 15,
                                chunkPosMinBlockZ + 15)) continue;
                        structureManager.addReferenceForStructure(sectionPos, structureStart.getStructure(), p, chunkAccess);
                        DebugPackets.sendStructurePacket(worldGenLevel, structureStart);
                    }
                    catch (Exception exception) {
                        CrashReport crashReport = CrashReport.forThrowable(exception, "Generating structure reference");
                        CrashReportCategory crashReportCategory = crashReport.addCategory("Structure");
                        Optional<Registry<Structure>> optional = (Optional<Registry<Structure>>)worldGenLevel.registryAccess().registry(Registry.STRUCTURE_REGISTRY);
                        crashReportCategory.setDetail("Id", () ->
                                optional.map( registry -> registry.getKey(structureStart.getStructure()).toString())
                                .orElse("UNKNOWN")
                        );
                        crashReportCategory.setDetail("Name", () ->
                                Registry.STRUCTURE_TYPES.getKey(structureStart.getStructure().type()).toString()
                        );
                        crashReportCategory.setDetail("Class", () ->
                                structureStart.getStructure().getClass().getCanonicalName()
                        );
                        throw new ReportedException(crashReport);
                    }
                }
            }
        }
    }



    ///////////////////////////////
    // MOBS
    ///////////////////////////////
    @Override
    public void spawnOriginalMobs(WorldGenRegion worldGenRegion) {
        if (!((NoiseGeneratorSettings) this.settings.value()).disableMobGeneration()) {
            ChunkPos chunkPos = worldGenRegion.getCenter();
            Holder<Biome> holder = worldGenRegion.getBiome(chunkPos.getWorldPosition().atY(worldGenRegion.getMaxBuildHeight() - 1));
            WorldgenRandom worldgenRandom = new WorldgenRandom(new LegacyRandomSource(RandomSupport.generateUniqueSeed()));
            worldgenRandom.setDecorationSeed(worldGenRegion.getSeed(), chunkPos.getMinBlockX(), chunkPos.getMinBlockZ());
            NaturalSpawner.spawnMobsForChunkGeneration(worldGenRegion, holder, chunkPos, worldgenRandom);
        }
    }


    static {
        AIR = Blocks.AIR.defaultBlockState();
        ////////////////////////////
        EMPTY = new BlockState[0];
        NETHERRACK = Blocks.NETHERRACK.defaultBlockState();
        LAVA = Blocks.LAVA.defaultBlockState();
        END_STONE = Blocks.END_STONE.defaultBlockState();
        ///////////////////////////



    }





    public void setStructuresSource(StructuresHelper structuresHelper) {
        this.structuresHelper = structuresHelper;
    }

    public StructuresHelper getStructuresSource() {
        return structuresHelper;
    }

    public HeightMapHelper getHeightMapSource() {
        return heightMapHelper;
    }
}
