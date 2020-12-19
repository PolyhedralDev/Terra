package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.gaea.math.Range;
import com.dfsek.terra.api.platform.world.block.MaterialData;
import com.dfsek.terra.carving.CarverPalette;
import com.dfsek.terra.util.MaterialSet;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class CarverTemplate extends AbstractableTemplate {
    @Value("id")
    private String id;

    @Value("step")
    @Abstractable
    @Default
    private int step = 2;

    @Value("recalculate-magnitude")
    @Default
    @Abstractable
    private double recaclulateMagnitude = 4;

    @Value("recalculate-direction")
    @Abstractable
    @Default
    private Range recalc = new Range(8, 10);

    @Value("length")
    @Abstractable
    private Range length;

    @Value("start.x")
    @Abstractable
    private double startX;

    @Value("start.y")
    @Abstractable
    private double startY;

    @Value("start.z")
    @Abstractable
    private double startZ;

    @Value("start.radius.x")
    @Abstractable
    private String radMX;

    @Value("start.radius.y")
    @Abstractable
    private String radMY;

    @Value("start.radius.z")
    @Abstractable
    private String radMZ;

    @Value("start.height")
    @Abstractable
    private Range height;

    @Value("cut.bottom")
    @Abstractable
    @Default
    private int cutBottom = 0;

    @Value("cut.top")
    @Abstractable
    @Default
    private int cutTop = 0;

    @Value("mutate.x")
    @Abstractable
    private double mutateX;

    @Value("mutate.y")
    @Abstractable
    private double mutateY;

    @Value("mutate.z")
    @Abstractable
    private double mutateZ;

    @Value("palette.top")
    @Abstractable
    private CarverPalette top;

    @Value("palette.bottom")
    @Abstractable
    private CarverPalette bottom;

    @Value("palette.outer")
    @Abstractable
    private CarverPalette outer;

    @Value("palette.inner")
    @Abstractable
    private CarverPalette inner;

    @Value("shift")
    @Abstractable
    @Default
    private Map<MaterialData, MaterialSet> shift = new HashMap<>();

    @Value("update")
    @Abstractable
    @Default
    private MaterialSet update = new MaterialSet();

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

    public Map<MaterialData, MaterialSet> getShift() {
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
