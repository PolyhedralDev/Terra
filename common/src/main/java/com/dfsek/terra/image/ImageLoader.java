package com.dfsek.terra.image;

import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.world.biome.NormalizationUtil;
import com.dfsek.terra.biome.BiomeZone;
import com.dfsek.terra.biome.grid.master.TerraBiomeGrid;
import com.dfsek.terra.debug.gui.DebugGUI;
import net.jafama.FastMath;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageLoader {
    private final BufferedImage image;
    private final Align align;

    public ImageLoader(File file, Align align) throws IOException {
        image = ImageIO.read(file);
        this.align = align;
    }

    public static void debugWorld(boolean genStep, World w, TerraPlugin main) {
        if(!main.isDebug()) return;
        BufferedImage newImg = new WorldImageGenerator(w, 1024, 1024, main).drawWorld(0, 0).getDraw();
        if(genStep) newImg = redrawStepped(newImg, w, Align.CENTER, main);
        DebugGUI debugGUI = new DebugGUI(newImg, main);
        debugGUI.start();
    }

    private static BufferedImage redrawStepped(BufferedImage original, World w, Align align, TerraPlugin main) {
        BufferedImage newImg = copyImage(original);
        TerraBiomeGrid tb = main.getWorld(w).getGrid();
        BiomeZone zone = main.getWorld(w).getZone();
        for(int x = 0; x < newImg.getWidth(); x++) {
            for(int y = 0; y < newImg.getHeight(); y++) {
                double[] noise;
                if(align.equals(Align.CENTER))
                    noise = tb.getGrid(x - original.getWidth() / 2, y - original.getHeight() / 2).getRawNoise(x - original.getWidth() / 2, y - original.getHeight() / 2);
                else noise = tb.getGrid(x, y).getRawNoise(x, y);
                newImg.setRGB(x, y, new Color((int) (NormalizationUtil.normalize(noise[0], tb.getGrid(x, y).getSizeX(), 4) * ((double) 255 / tb.getGrid(x, y).getSizeX())),
                        (int) (NormalizationUtil.normalize(noise[1], tb.getGrid(x, y).getSizeZ(), 4) * ((double) 255 / tb.getGrid(x, y).getSizeZ())),
                        (int) (NormalizationUtil.normalize(zone.getNoise(x, y), zone.getSize(), 4) * ((double) 255 / zone.getSize())))
                        .getRGB());
            }
        }
        return newImg;
    }

    private static int normal(double val) {
        return FastMath.floorToInt(FastMath.min(FastMath.max(val, 255), 0));
    }

    private static BufferedImage copyImage(BufferedImage source) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    public void debug(boolean genStep, World w, TerraPlugin main) {
        if(!main.isDebug()) return;
        BufferedImage newImg = copyImage(image);
        if(genStep) {
            newImg = redrawStepped(image, w, align, main);
        }
        DebugGUI debugGUI = new DebugGUI(newImg, main);
        debugGUI.start();
    }

    public int getNoiseVal(int x, int y, int size, Channel channel) {
        return (size * getChannel(x, y, channel)) / 255;
    }

    public int getChannel(int x, int y, Channel channel) {
        int rgb;
        rgb = align.getRGB(image, x, y);
        switch(channel) {
            case RED:
                return rgb >> 16 & 0xff;
            case GREEN:
                return rgb >> 8 & 0xff;
            case BLUE:
                return rgb & 0xff;
            case ALPHA:
                return rgb >> 24 & 0xff;
            default:
                throw new IllegalArgumentException();
        }
    }

    public Align getAlign() {
        return align;
    }

    public enum Channel {
        RED, GREEN, BLUE, ALPHA
    }

    public enum Align {
        CENTER {
            @Override
            public int getRGB(BufferedImage image, int x, int y) {
                return Align.getRGBNoAlign(image, x - (image.getWidth() / 2), y - (image.getHeight() / 2));
            }
        },
        NONE {
            @Override
            public int getRGB(BufferedImage image, int x, int y) {
                return image.getRGB(FastMath.floorMod(x, image.getWidth()), FastMath.floorMod(y, image.getHeight()));
            }
        };

        private static int getRGBNoAlign(BufferedImage image, int x, int y) {
            return image.getRGB(FastMath.floorMod(x, image.getWidth()), FastMath.floorMod(y, image.getHeight()));
        }

        public abstract int getRGB(BufferedImage image, int x, int y);
    }
}
