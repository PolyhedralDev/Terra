package com.dfsek.terra.addons.image.config.image;

import com.dfsek.tectonic.api.config.template.ValidatedConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import com.dfsek.tectonic.api.exception.ValidationException;

import java.io.IOException;

import com.dfsek.terra.addons.image.image.Image;
import com.dfsek.terra.addons.image.image.StitchedImage;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.Loader;


public class StitchedImageTemplate implements ObjectTemplate<Image>, ValidatedConfigTemplate {

    @Value("path-format")
    private String path;

    @Value("rows")
    private int rows;

    @Value("columns")
    private int cols;
    
    @Value("zero-indexed")
    @Default
    private boolean zeroIndexed = false;

    private final Loader files;

    private final ConfigPack pack;

    public StitchedImageTemplate(Loader files, ConfigPack pack) {
        this.files = files;
        this.pack = pack;
    }
    
    @Override
    public Image get() {
        Image[][] grid = new Image[rows][cols];
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                try {
                    grid[i][j] = ImageCache.load(getFormattedPath(i, j), pack, files);
                } catch(IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return new StitchedImage(grid, zeroIndexed);
    }
    
    private String getFormattedPath(int row, int column) {
        if (!zeroIndexed) {
            row++;
            column++;
        }
        return path.replaceFirst("\\{row}", String.valueOf(row)).replaceFirst("\\{column}", String.valueOf(column));
    }

    @Override
    public boolean validate() throws ValidationException {
        if(!path.contains("{row}"))
            throw new ValidationException("Path format does not contain sequence '{row}'");
        if(!path.contains("{column}"))
            throw new ValidationException("Path format does not contain sequence '{column}'");
        if(rows < 1)
            throw new ValidationException("Must have at least one row");
        if(cols < 1)
            throw new ValidationException("Must have at least one column");
        return true;
    }
}
