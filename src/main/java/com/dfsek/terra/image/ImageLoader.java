package com.dfsek.terra.image;

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

    public double getNoiseVal(int x, int y, Channel channel) {
        return ((double) (getChannel(x, y, channel) - 128)/128)*inverseRoot2;
    }

    public enum Channel {
        RED, GREEN, BLUE, ALPHA
    }
}
