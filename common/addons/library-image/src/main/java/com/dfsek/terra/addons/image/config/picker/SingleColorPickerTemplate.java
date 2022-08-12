package com.dfsek.terra.addons.image.config.picker;

import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.image.picker.ColorPicker;
import com.dfsek.terra.addons.image.picker.SimpleColorPicker;


public class SingleColorPickerTemplate extends ColorPickerTemplate {

    @Value("fallback")
    private int fallback;

    @Override
    public ColorPicker get() {
        return new SimpleColorPicker(fallback, alignment);
    }
}
