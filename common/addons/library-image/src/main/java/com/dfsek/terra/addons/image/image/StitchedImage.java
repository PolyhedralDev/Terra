package com.dfsek.terra.addons.image.image;

public class StitchedImage implements Image {
    
    private final Image[][] images;
    
    private final int[] rowOffsets, columnOffsets;
    
    private final int width, height;
    
    public StitchedImage(Image[][] images, boolean zeroIndexed) throws IllegalArgumentException {
        int width = 0;
        int height = 0;
        int rows = images.length;
        int columns = images[0].length;
        this.rowOffsets = new int[rows];
        this.columnOffsets = new int[columns];
        for(int i = 0; i < rows; i++) {
            int rowHeight = images[i][0].getHeight();
            rowOffsets[i] = height;
            height += rowHeight;
            for(int j = 1; j < columns; j++) {
                if(images[i][j].getHeight() != rowHeight)
                    throw new IllegalArgumentException("Image heights in row " + (i + (zeroIndexed ? 0 : 1)) + " do not match");
            }
        }
        for(int i = 0; i < columns; i++) {
            int columnWidth = images[0][i].getWidth();
            columnOffsets[i] = width;
            width += columnWidth;
            for(int j = 1; j < rows; j++) {
                if(images[i][j].getWidth() != columnWidth)
                    throw new IllegalArgumentException("Image widths in column " + (i + (zeroIndexed ? 0 : 1)) + " do not match");
            }
        }
        
        this.width = width;
        this.height = height;
        this.images = images;
    }
    
    private int getColumn(int x) {
        for(int i = columnOffsets.length-1; i > 0; i--) {
            if(x >= columnOffsets[i])
                return i;
        }
        return 0;
    }
    
    private int getRow(int y) {
        for(int i = rowOffsets.length-1; i > 0; i--) {
            if(y >= rowOffsets[i])
                return i;
        }
        return 0;
    }
    
    @Override
    public int getRGB(int x, int y) {
        int row = getRow(y);
        int column = getColumn(x);
        return images[row][column].getRGB(x-columnOffsets[column], y-rowOffsets[row]);
    }
    
    @Override
    public int getWidth() {
        return width;
    }
    
    @Override
    public int getHeight() {
        return height;
    }
}
