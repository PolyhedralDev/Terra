package com.dfsek.terra.image;

import com.dfsek.terra.biome.BiomeZone;
import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.config.genconfig.BiomeConfig;
import org.bukkit.World;
import org.polydev.gaea.biome.NormalizationUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import javax.imageio.ImageIO;

public class ImageLoader {
    private final BufferedImage image;
    double inverseRoot2 = 0.7071067811865475;
    public ImageLoader(File file) throws IOException {
        image = ImageIO.read(file);
    }


    public int getChannel(int x, int y, Channel channel) {
        int rgb;
        try {
            rgb = image.getRGB(Math.floorMod(x, image.getWidth()), Math.floorMod(y, image.getHeight()));
        } catch(ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Index " + x + "/" + x + "out of bounds for size " + image.getWidth() + "/" + image.getHeight());
        }
        switch(channel) {
            case RED: return rgb >> 16 & 0xff;
            case GREEN: return rgb >> 8 & 0xff;
            case BLUE: return rgb & 0xff;
            case ALPHA: return rgb >> 32 & 0xff;
            default: throw new IllegalArgumentException();
        }
    }

    public void debug(boolean genStep, World w) {
        BufferedImage newImg = copyImage(image);
        TerraBiomeGrid tb = TerraBiomeGrid.fromWorld(w);
        BiomeZone z = BiomeZone.fromWorld(w);
        if(genStep) {
            for(int x = 0; x < newImg.getWidth(); x++) {
                for(int y = 0; y < newImg.getHeight(); y++) {
                    float[] noise = tb.getGrid(x, y).getRawNoise(x, y);
                    newImg.setRGB(x, y, new Color((int) (NormalizationUtil.normalize(noise[0], tb.getGrid(x, y).getSizeX()) * ((double) 255/tb.getGrid(x, y).getSizeX())),
                            (int) (NormalizationUtil.normalize(noise[1], tb.getGrid(x, y).getSizeZ()) * ((double) 255/tb.getGrid(x, y).getSizeZ())),
                            (int) (z.getNoise(x, y) * ((double) 255/32)))
                            .getRGB());
                }
            }
        }
        DebugGUI debugGUI = new DebugGUI(newImg);
        debugGUI.start();
    }

    public double getNoiseVal(int x, int y, Channel channel) {
        return ((double) (getChannel(x, y, channel) - 128)/128)*inverseRoot2;
    }
    private static BufferedImage copyImage(BufferedImage source){
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    public enum Channel {
        RED, GREEN, BLUE, ALPHA
    }
}
