package com.mjimmer.imagestwoworld.image;


import ar.com.hjg.pngj.*;
import com.mjimmer.imagestwoworld.I2W;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class ImageHandler {
    private final String path;
    private PngReader reader;
    private final int maxWidth;
    private final int maxHeight;
    private int width;
    private int height;
    private int wCount;
    private int hCount;
    private final String imageType;

    private int[][] loadedImgPosCache;


    private BufferedImage imageBuf = null;
    private BufferedImage[] loadedImagesCache;

    private final Path filePath;

    public ImageHandler(String path, String imageType) {
        this.path = path;
        this.imageType = imageType;
        this.filePath = Paths.get("", I2W.CONFIG_PATH,  I2W.CONFIGS.GENERAL.configImagesDir, path);
        this.maxWidth =   I2W.CONFIGS.EXTRA.segmentImageWidth;
        this.maxHeight =  I2W.CONFIGS.EXTRA.segmentImageHeight;

        init();
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    public void init(){
        this.reader = new PngReader( this.filePath.toFile());
        this.width = reader.imgInfo.cols;
        this.height = reader.imgInfo.rows;
        this.wCount = (width - 1) / maxWidth + 1;    // how many there will be in width
        this.hCount = (height - 1) / maxHeight + 1; // how many there will be in width
        this.loadedImgPosCache = new int[I2W.CONFIGS.EXTRA.imageCacheSize][2];
        this.loadedImagesCache = new BufferedImage[I2W.CONFIGS.EXTRA.imageCacheSize];
        Arrays.fill(loadedImgPosCache, new int[]{-1,-1});
    }

    // this really needs a better way to check if the function is done
    public boolean segmentImage(String imageType){
        init();
        I2W.LOGGER.log(Level.INFO,"Starting image segmentation for {} - This may take a while !", imageType);
        int hProgress = 0;
        int channels = reader.imgInfo.channels;
        for( int heightCounter = 0; heightCounter < hCount; heightCounter++ ){
            int wProgress = 0;
            int hSpan = height - hProgress;
            int minHeight = Math.min(maxHeight, hSpan);


            I2W.LOGGER.log(Level.INFO, "Reading " + minHeight + " Prog " + hProgress + " span " + hSpan );

            IImageLineSet<?> lines = reader.readRows(minHeight, hProgress,1 );

            for(int widthCounter = 0; widthCounter < wCount; widthCounter++){
                int wSpan = width - wProgress;
                int minWidth = Math.min(maxWidth, wSpan);
                IImageLine lint;
                BufferedImage newImage = new BufferedImage( minWidth,  minHeight, BufferedImage.TYPE_INT_RGB );
                int fHeightCounter = hProgress;

                while ( fHeightCounter < hProgress+minHeight ) {
                    lint = lines.getImageLine(fHeightCounter);

                    for (int fWidthCounter = wProgress; fWidthCounter < wProgress+minWidth; fWidthCounter++ ) {
                        int nextPixel = 0;
                        if(channels == 4){
                            nextPixel = ImageLineHelper.getPixelARGB8(lint, fWidthCounter)&0x00FFFFFF; /*|0xff000000;*/
                        }else if(channels == 3){
                            nextPixel = ImageLineHelper.getPixelRGB8(lint, fWidthCounter);
                        }else if(channels == 1){
                            int[] scanLine = ((ImageLineInt)lint).getScanline();
                            nextPixel = (scanLine[fWidthCounter] << 16) |(scanLine[fWidthCounter] << 8) |(scanLine[fWidthCounter]);/* | 0xFF000000;*/
                        }
                        newImage.setRGB(
                                fWidthCounter - wProgress,
                                fHeightCounter - hProgress,
                                nextPixel);
                    }
                    fHeightCounter += 1;
                }
                File img = Paths.get("", I2W.CONFIG_PATH, I2W.CONFIGS.GENERAL.configGenDir ,
                        I2W.MOD_NAME + "_" + imageType + "_" + widthCounter + "_" + heightCounter + ".png").toFile();
                try {
                    ImageIO.write(newImage,"png",img);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                wProgress += maxWidth;
            }
            hProgress += maxHeight;
        }
        return true;
    }

    public boolean isInLoadedImages(int[] pos){
        for (int[] posCache : this.loadedImgPosCache) {
            if (Arrays.equals(pos, posCache)) {
                return true;
            }
        }
        return false;
    }

    public BufferedImage getImage(int x, int y){
        int wNumber = x/maxWidth;
        int hNumber = y/maxHeight;
        BufferedImage img = null;
        try {
            img = ImageIO.read( Paths.get(
                    "",
                    I2W.CONFIG_PATH,  // configs/i2w
                    I2W.CONFIGS.GENERAL.configGenDir, // configs/i2w/gen
                    I2W.MOD_NAME + "_" +  this.imageType + "_" + wNumber + "_" + hNumber + ".png" ).toFile()
                );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    private void putInCache(int[] pos, BufferedImage image){
        for(int i = 0; i < loadedImgPosCache.length-1; i++){
            this.loadedImgPosCache[i] = this.loadedImgPosCache[i+1];
            this.loadedImagesCache[i] = this.loadedImagesCache[i+1];
        }
        this.loadedImgPosCache[loadedImgPosCache.length-1] = pos;
        this.loadedImagesCache[loadedImagesCache.length-1] = image;
    }

    public int getRGB(int x, int y){
        final int[] pos = {x/maxWidth, y/maxHeight};
        if(!isInLoadedImages(pos)){
            putInCache(pos,getImage(x,y));
        }
        for(int i = 0; i < loadedImgPosCache.length; i++ ){
            if(Arrays.equals(pos,this.loadedImgPosCache[i])){
                int localX = x%maxWidth;
                int localY = y%maxHeight;
                // TODO FIXME this is not th way to get rgba
                // this seems to be the issue :( its returning a bad value for the rgb
                return loadedImagesCache[i].getRGB(localX, localY);
            }
        }
        throw new IllegalStateException("Could not retrieve image for " + x + " , " + y );
    }


    // BGR ONLY
    private static int[][] convertBGR(BufferedImage image) {
        final byte[] pixels = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;
        int[][] ret = new int[height][width];
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel + 3 < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                ret[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                ret[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }
        return ret;
    }


    private static int[][] convertWithGetRGB(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] result = new int[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                result[row][col] = image.getRGB(col, row);
            }
        }
        return result;
    }



//    private void writeImageAs( Path path ){
//        // Create input stream
//        try (ImageInputStream input = ImageIO.createImageInputStream( path.toFile() ) ) {
//            // Get the reader
//            Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
//
//            if (!readers.hasNext()) {
//                throw new IllegalArgumentException("No reader for: " + path.toString()); // Or simply return null
//            }
//
//            ImageReader reader = readers.next();
//
//            try {
//                // Set input
//                reader.setInput(input);
//
//                // Configure the param to use the destination type you want
//                ImageReadParam param = reader.getDefaultReadParam();
//                param.setDestinationType(ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_BYTE_BINARY));
//
//                // Finally read the image, using settings from param
//                return; reader.read(0, param);
//            }
//            finally {
//                // Dispose reader in finally block to avoid memory leaks
//                reader.dispose();
//            }
//        }
//
//
//    }
//
//

}