package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.carving.CarverPalette;
import org.bukkit.Material;
import org.polydev.gaea.math.Range;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class CarverTemplate implements ConfigTemplate {
    @Value("id")
    private String id;

    @Value("step")
    @Abstractable
    @Default
    private int step = 2;

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
    private double radMX;

    @Value("start.radius.y")
    @Abstractable
    private double radMY;

    @Value("start.radius.z")
    @Abstractable
    private double radMZ;

    @Value("start.radius")
    @Abstractable
    private Range radius;

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

    @Value("mutate.radius")
    @Abstractable
    private double mutateRadius;

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
    private Map<Material, Set<Material>> shift = new HashMap<>();

    @Value("update")
    @Abstractable
    @Default
    private Set<Material> update = new HashSet<>();

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

    public double getRadMX() {
        return radMX;
    }

    public double getRadMY() {
        return radMY;
    }

    public double getRadMZ() {
        return radMZ;
    }

    public Range getRadius() {
        return radius;
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

    public double getMutateRadius() {
        return mutateRadius;
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

    public Map<Material, Set<Material>> getShift() {
        return shift;
    }

    public Set<Material> getUpdate() {
        return update;
    }
}
