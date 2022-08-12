package com.dfsek.terra.addons.image.config.picker;

import com.dfsek.terra.addons.image.picker.ColorPicker;
import com.dfsek.terra.addons.image.picker.TileColorPicker;


public class TileColorPickerTemplate extends ColorPickerTemplate {

    @Override
    public ColorPicker get() {
        return new TileColorPicker(alignment);
    }
}
