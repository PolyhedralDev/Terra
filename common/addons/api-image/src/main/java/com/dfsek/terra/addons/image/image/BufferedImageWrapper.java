package com.dfsek.terra.addons.image.image;

import java.awt.image.BufferedImage;


public class BufferedImageWrapper implements Image {

    private final BufferedImage image;

    public BufferedImageWrapper(BufferedImage image) {
        this.image = image;
    }

    @Override
    public int getRGB(int x, int y) {
        return image.getRGB(x, y);
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }
}
