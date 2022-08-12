package com.dfsek.terra.addons.image.config.picker;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.image.picker.ColorPicker;
import com.dfsek.terra.addons.image.picker.transform.Alignment;


public abstract class ColorPickerTemplate implements ObjectTemplate<ColorPicker> {
    
    @Value("align")
    @Default
    protected Alignment alignment = Alignment.NONE;
    
}
