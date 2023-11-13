package com.dfsek.terra.addons.image.image;

import java.util.function.Supplier;


public class SuppliedImage implements Image {

    private final Supplier<Image> imageSupplier;

    public SuppliedImage(Supplier<Image> imageSupplier) {
        this.imageSupplier = imageSupplier;
    }

    @Override
    public int getRGB(int x, int y) {
        return imageSupplier.get().getRGB(x, y);
    }

    @Override
    public int getWidth() {
        return imageSupplier.get().getWidth();
    }

    @Override
    public int getHeight() {
        return imageSupplier.get().getHeight();
    }
}
