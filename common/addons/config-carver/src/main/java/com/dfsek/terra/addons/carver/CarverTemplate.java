package com.dfsek.terra.addons.carver;


import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Final;
import com.dfsek.tectonic.annotations.Value;

import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.config.AbstractableTemplate;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.util.ConstantRange;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.util.collection.MaterialSet;


@SuppressWarnings({ "unused", "FieldMayBeFinal" })
public class CarverTemplate implements AbstractableTemplate {
    @Value("id")
    @Final
    private String id;
    
    @Value("step")
    @Default
    private @Meta int step = 2;
    
    @Value("recalculate-magnitude")
    @Default
    private @Meta double recaclulateMagnitude = 4;
    
    @Value("recalculate-direction")
    @Default
    private @Meta Range recalc = new ConstantRange(8, 10);
    
    @Value("length")
    private @Meta Range length;
    
    @Value("start.x")
    private @Meta double startX;
    
    @Value("start.y")
    private @Meta double startY;
    
    @Value("start.z")
    private @Meta double startZ;
    
    @Value("start.radius.x")
    private @Meta String radMX;
    
    @Value("start.radius.y")
    private @Meta String radMY;
    
    @Value("start.radius.z")
    private @Meta String radMZ;
    
    @Value("start.height")
    private @Meta Range height;
    
    @Value("cut.bottom")
    @Default
    private @Meta int cutBottom = 0;
    
    @Value("cut.top")
    @Default
    private @Meta int cutTop = 0;
    
    @Value("mutate.x")
    private @Meta double mutateX;
    
    @Value("mutate.y")
    private @Meta double mutateY;
    
    @Value("mutate.z")
    private @Meta double mutateZ;
    
    @Value("palette.top")
    private @Meta CarverPalette top;
    
    @Value("palette.bottom")
    private @Meta CarverPalette bottom;
    
    @Value("palette.outer")
    private @Meta CarverPalette outer;
    
    @Value("palette.inner")
    private @Meta CarverPalette inner;
    
    @Value("shift")
    @Default
    private @Meta Map<@Meta BlockType, @Meta MaterialSet> shift = new HashMap<>();
    
    @Value("update")
    @Default
    private @Meta MaterialSet update = new MaterialSet();
    
    public String getID() {
        return id;
    }
    
    public int getStep() {
        return step;
    }
    
    public Range getLength() {
        return length;
    }
    
    public double getStartX() {
        return startX;
    }
    
    public double getStartY() {
        return startY;
    }
    
    public double getStartZ() {
        return startZ;
    }
    
    public String getRadMX() {
        return radMX;
    }
    
    public String getRadMY() {
        return radMY;
    }
    
    public String getRadMZ() {
        return radMZ;
    }
    
    public Range getHeight() {
        return height;
    }
    
    public int getCutBottom() {
        return cutBottom;
    }
    
    public int getCutTop() {
        return cutTop;
    }
    
    public double getMutateX() {
        return mutateX;
    }
    
    public double getMutateY() {
        return mutateY;
    }
    
    public double getMutateZ() {
        return mutateZ;
    }
    
    public CarverPalette getTop() {
        return top;
    }
    
    public CarverPalette getBottom() {
        return bottom;
    }
    
    public CarverPalette getOuter() {
        return outer;
    }
    
    public CarverPalette getInner() {
        return inner;
    }
    
    public Map<BlockType, MaterialSet> getShift() {
        return shift;
    }
    
    public MaterialSet getUpdate() {
        return update;
    }
    
    public Range getRecalc() {
        return recalc;
    }
    
    public double getRecaclulateMagnitude() {
        return recaclulateMagnitude;
    }
}
