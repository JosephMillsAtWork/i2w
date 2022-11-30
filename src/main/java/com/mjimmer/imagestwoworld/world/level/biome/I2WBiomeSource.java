package com.mjimmer.imagestwoworld.world.level.biome;

import com.google.common.collect.ImmutableList;
import com.mjimmer.imagestwoworld.I2W;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.SharedConstants;
import net.minecraft.core.*;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.*;
import com.mojang.serialization.Codec;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import org.jetbrains.annotations.Nullable;

public class I2WBiomeSource extends BiomeSource {
    public static final Codec<I2WBiomeSource> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            RegistryOps.retrieveRegistry(Registry.BIOME_REGISTRY).forGetter(bSource -> bSource.biomeRegistry)
                        )
                    .apply(instance, I2WBiomeSource::new )
    );

//            ((MapCodec)BiomeSource.CODEC.fieldOf("biome")).forGetter((Function) getBiome()),
//            ((MapCodec)BiomeSource.CODEC.fieldOf("parameters")).forGetter( (Function) getParameters() ),
//            ((MapCodec)BiomeSource.CODEC.fieldOf("biomeRegistry") ).forGetter( (Function)getBiomeRegistry() )

    private static final List<ResourceKey<Biome>> SPAWN = Collections.singletonList(Biomes.PLAINS);

    private static Climate.ParameterList<Holder<Biome>> parameters = null;

    public static  Holder<Biome> biome;
    private static Registry<Biome> biomeRegistry;
    private static OverworldBiomeBuilder biomeBuilder;

    private static final List<ResourceKey<Biome>> BIOMES = ImmutableList.of(
            Biomes.THE_VOID                      ,
            Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.SNOWY_PLAINS,
            Biomes.ICE_SPIKES,
            Biomes.DESERT,
            Biomes.SWAMP, Biomes.MANGROVE_SWAMP,
            Biomes.FOREST, Biomes.FLOWER_FOREST, Biomes.BIRCH_FOREST, Biomes.DARK_FOREST,
            Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA,
            Biomes.TAIGA, Biomes.SNOWY_TAIGA,
            Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU,
            Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_SAVANNA,
            Biomes.JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE,
            Biomes.BADLANDS, Biomes.ERODED_BADLANDS, Biomes.WOODED_BADLANDS,
            Biomes.MEADOW, Biomes.GROVE,
            Biomes.SNOWY_SLOPES, Biomes.FROZEN_PEAKS, Biomes.JAGGED_PEAKS, Biomes.STONY_PEAKS,
            Biomes.RIVER, Biomes.FROZEN_RIVER,
            Biomes.BEACH, Biomes.SNOWY_BEACH, Biomes.STONY_SHORE,
            Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.OCEAN, Biomes.DEEP_OCEAN, Biomes.COLD_OCEAN,
            Biomes.DEEP_COLD_OCEAN, Biomes.FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN,
            Biomes.MUSHROOM_FIELDS,
            Biomes.DRIPSTONE_CAVES, Biomes.LUSH_CAVES,
            Biomes.DEEP_DARK,
            Biomes.NETHER_WASTES, Biomes.WARPED_FOREST, Biomes.CRIMSON_FOREST, Biomes.SOUL_SAND_VALLEY, Biomes.BASALT_DELTAS,
            Biomes.THE_END, Biomes.END_HIGHLANDS, Biomes.END_MIDLANDS, Biomes.SMALL_END_ISLANDS, Biomes.END_BARRENS
    );


    private static final List<ResourceKey<Biome>> THREE_D_BIOMES = ImmutableList.of(
            Biomes.DRIPSTONE_CAVES,
            Biomes.LUSH_CAVES,
            Biomes.DEEP_DARK
    );

//    public static Climate.ParameterList<Holder<Biome>> getParameters(){return this.parameters; }

    // this should really be a preset and should be passed off to MultiNoise ?
    public I2WBiomeSource(Registry<Biome> biomeRegistry) {
        super(getStartBiomes(biomeRegistry));
        this.biomeRegistry = biomeRegistry;
        this.biome = biomeRegistry.getHolderOrThrow( Biomes.OCEAN );

        this.biomeBuilder = new OverworldBiomeBuilder();
        ImmutableList.Builder builder = ImmutableList.builder();
        biomeBuilder.addBiomes(pair -> {
            builder.add(pair.mapSecond(biomeRegistry::getOrCreateHolderOrThrow));
        });
        this.parameters = new Climate.ParameterList(builder.build());
    }

    public static Registry<Biome> getBiomeRegistry() {
        return biomeRegistry;
    }

    public static Holder<Biome> getBiome(){return biome; };


    private static List<Holder<Biome>> getStartBiomes(Registry<Biome> registry) {
        return BIOMES.stream().map(
                s -> registry.getOrCreateHolderOrThrow( ResourceKey.create(BuiltinRegistries.BIOME.key(), s.location() )
                )
        ).collect(Collectors.toList());
    }

    @Override
    protected Codec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
        // TargetPoint[temperature=3098, humidity=-471, continentalness=9954, erosion=4395, depth=-18135, weirdness=-5841]
        Holder<Biome> ret = this.parameters.findValue( sampler.sample(x, y, z) );
        if( I2W.CONFIGS.BIOMES.customBiomeImage) {
            ResourceKey<Biome>  bKey = ret.unwrapKey().orElseThrow();
            if( !THREE_D_BIOMES.contains(  bKey  )) {
                if (I2W.bHelper.getBiomeDataProvider().isInImage(x*4, z*4) ) {
                    Holder<Biome> imageBiome = I2W.bHelper.getBiomeDataProvider().data(x*4, z*4);
                    for( Pair<Climate.ParameterPoint, Holder<Biome>> pair: this.parameters.values() ) {
                        if( pair.getSecond().unwrapKey().get() == imageBiome.unwrapKey().get() ) {
//                            I2W.LOGGER.log(Level.INFO, "Injecting custom biome at x:" + x + " z:" + z + " Type :"+ imageBiome.toString() );
                            return pair.getSecond();
                        }
                    }
                }
            }
        }
        return ret;
    }


    //   @VisibleForDebug
//    public Holder<Biome> getNoiseBiome(Climate.TargetPoint targetPoint, int x, int y) {
//        // TargetPoint[temperature=3098, humidity=-471, continentalness=9954, erosion=4395, depth=-18135, weirdness=-5841]
//        Holder<Biome> ret = this.parameters.findValue(targetPoint);
//        if( I2W.CONFIGS.BIOMES.customBiomeImage) {
//            int fX = x*4;
//            int fY = y*4;
//            if( !THREE_D_BIOMES.contains(  BuiltinRegistries.BIOME.getResourceKey( ret.value() )  ) ) {
//                if (I2W.bHelper.getBiomeDataProvider().isInImage(x, y) ) {
//                    ret = I2W.bHelper.getBiomeDataProvider().data(x, y);
//                }
//            }
//        }
//        return ret;
//    }

    @Override
    public void addDebugInfo(List<String> list, BlockPos blockPos, Climate.Sampler sampler) {
        int xPos = QuartPos.fromBlock(blockPos.getX());
        int yPos = QuartPos.fromBlock(blockPos.getY());
        int zPos = QuartPos.fromBlock(blockPos.getZ());
        Climate.TargetPoint targetPoint = sampler.sample(xPos, yPos, zPos);
        float continental = Climate.unquantizeCoord(targetPoint.continentalness());
        float erosion = Climate.unquantizeCoord(targetPoint.erosion());
        float temperature = Climate.unquantizeCoord(targetPoint.temperature());
        float humidity = Climate.unquantizeCoord(targetPoint.humidity());
        float weirdness = Climate.unquantizeCoord(targetPoint.weirdness());
        double peaksAndValleys = NoiseRouterData.peaksAndValleys(weirdness);
        list.add("I2W Biome "
                + " PV: " + this.biomeBuilder.getDebugStringForPeaksAndValleys(peaksAndValleys)
                + " C: " + this.biomeBuilder.getDebugStringForContinentalness(continental)
                + " E: " + this.biomeBuilder.getDebugStringForErosion(erosion)
                + " T: " + this.biomeBuilder.getDebugStringForTemperature(temperature)
                + " H: " + this.biomeBuilder.getDebugStringForHumidity(humidity)
    );
    }





    @Override
    @Nullable
    public Pair<BlockPos, Holder<Biome>> findBiomeHorizontal(int x, int y, int z, int radius, int blockCheckInterval,
                                                             Predicate<Holder<Biome>> predicate,
                                                             RandomSource randomSource,
                                                             boolean useRadius, Climate.Sampler sampler)
    {
        int startX = QuartPos.fromBlock(x);
        int startZ = QuartPos.fromBlock(z);
        int startingRadius = QuartPos.fromBlock(radius);
        int startY = QuartPos.fromBlock(y);
        Pair<BlockPos, Holder<Biome>> pair = null;
        int r = 0;
        for (int zCounter = useRadius ? 0 : startingRadius; zCounter <= startingRadius; zCounter += blockCheckInterval) {
            int zBuff;
            zBuff = SharedConstants.debugGenerateSquareTerrainWithoutNoise ? 0 : -zCounter;
            while (zBuff <= zCounter) {
                boolean bl2 = Math.abs(zBuff) == zCounter;
                for (int xBuff = -zCounter; xBuff <= zCounter; xBuff += blockCheckInterval) {
                    int endZ;
                    int endX;
                    Holder<Biome> holder;
                    if (useRadius) {
                        boolean bl3 = Math.abs(xBuff) == zCounter;
                        if (!bl3 && !bl2) continue;
                    }
                    if ( !predicate.test(
                            holder = this.getNoiseBiome(
                                    endX = startX + xBuff,
                                    startY,
                                    endZ = startZ + zBuff,
                                    sampler
                            )
                    )){
                        continue;
                    }
                    if (pair == null || randomSource.nextInt(r + 1) == 0) {
                        BlockPos blockPos = new BlockPos(QuartPos.toBlock(endX), y, QuartPos.toBlock(endZ));
                        if (useRadius) {
                            return Pair.of(blockPos, holder);
                        }
                        pair = Pair.of(blockPos, holder);
                    }
                    ++r;
                }
                zBuff += blockCheckInterval;
            }
        }
        return pair;
    }

}
