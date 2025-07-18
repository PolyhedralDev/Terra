/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.image.noisesampler;

import com.dfsek.terra.addons.image.colorsampler.image.transform.Alignment;
import com.dfsek.terra.addons.image.image.Image;
import com.dfsek.terra.addons.image.util.KDTree;
import com.dfsek.terra.api.noise.CellularDistanceFunction;
import com.dfsek.terra.api.noise.CellularReturnType;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.vector.Vector2;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * NoiseSampler implementation for A Modified Cellular (Voronoi/Worley) Noise with Image Sampling for Seeding White pixels #FFFFFF being seeds
 */
public class CellularImageSampler implements NoiseSampler {
    private CellularDistanceFunction distanceFunction = CellularDistanceFunction.EuclideanSq;
    private CellularReturnType returnType = CellularReturnType.Distance;
    private NoiseSampler noiseLookup;
    private Image image;
    private KDTree tree;
    private Alignment alignment = Alignment.NONE;
    private static Map<Integer, KDTree> treeMap = new ConcurrentHashMap<>();
    private int hash;


    public void setDistanceFunction(CellularDistanceFunction distanceFunction) {
        this.distanceFunction = distanceFunction;
    }

    public void setNoiseLookup(NoiseSampler noiseLookup) {
        this.noiseLookup = noiseLookup;
    }

    public void setReturnType(CellularReturnType returnType) {
        this.returnType = returnType;
    }

    public void setImage(Image image){
        this.image = image;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    private int getHash(String string){
        return Objects.hash(string);
    }

    public void setHash(String string){
        this.hash = getHash(string);
    }

    public boolean hasTree(String string){
        return treeMap.containsKey(getHash(string));
    }


    public KDTree doKDTree() {
        return treeMap.computeIfAbsent(hash, h -> {
            List<Vector2> whitePixels = extractWhitePixels(image);
            return new KDTree(whitePixels);
        });
    }


    public List<Vector2> extractWhitePixels(Image image) {
        int width = image.getWidth();
        int height = image.getHeight();

        int offsetX = alignment == Alignment.CENTER ? -width / 2 : 0;
        int offsetZ = alignment == Alignment.CENTER ? -height / 2 : 0;

        return IntStream.range(0, height).parallel()
            .boxed()
            .flatMap(y ->
                IntStream.range(0, width)
                    .filter(x -> (image.getRGB(x, y) & 0xFFFFFF) == 0xFFFFFF)
                    .mapToObj(x -> {
                        Vector2 v = Vector2.of(x + offsetX, y + offsetZ);
                        return v;
                    })
            )
            .collect(Collectors.toList());
    }



    @Override
    public double noise(long sl, double x, double z) {
        tree = treeMap.get(hash);

        int xr = (int) Math.round(x);
        int zr = (int) Math.round(z);
        Vector2 query = Vector2.of(xr, zr);

        List<Vector2> nearest = tree.kNearest(query, 3);

        double distance0, distance1, distance2;

        if (distanceFunction == CellularDistanceFunction.Manhattan) {
            distance0 = Math.abs(query.getX() - nearest.get(0).getX()) + Math.abs(query.getZ() - nearest.get(0).getZ());
            distance1 = Math.abs(query.getX() - nearest.get(1).getX()) + Math.abs(query.getZ() - nearest.get(1).getZ());
            distance2 = Math.abs(query.getX() - nearest.get(2).getX()) + Math.abs(query.getZ() - nearest.get(2).getZ());
        } else {
            distance0 = applyDistanceFunction(distanceFunction, query.distanceSquared(nearest.get(0)));
            distance1 = applyDistanceFunction(distanceFunction, query.distanceSquared(nearest.get(1)));
            distance2 = applyDistanceFunction(distanceFunction, query.distanceSquared(nearest.get(2)));
        }

        double distanceX = nearest.get(0).getX();
        double distanceZ = nearest.get(0).getZ();

        CellularReturnType type = returnType;

        double result = switch(type) {
            case Distance -> distance0 - 1;
            case Distance2 -> distance1 - 1;
            case Distance2Add -> (distance1 + distance0) * 0.5 - 1;
            case Distance2Sub -> distance1 - distance0 - 1;
            case Distance2Mul -> distance1 * distance0 * 0.5 - 1;
            case Distance2Div -> distance0 / distance1 - 1;
            case NoiseLookup -> noiseLookup.noise(sl, distanceX, distanceZ);
            case LocalNoiseLookup -> noiseLookup.noise(sl, x - distanceX, z - distanceZ);
            case Distance3 -> distance2 - 1;
            case Distance3Add -> (distance2 + distance0) * 0.5 - 1;
            case Distance3Sub -> distance2 - distance0 - 1;
            case Distance3Mul -> distance2 * distance0 - 1;
            case Distance3Div -> distance0 / distance2 - 1;
            case Angle -> Math.atan2(distanceX - x, distanceZ - z);
            case CellValue -> hashNormalized((int) distanceX, (int) distanceZ);
        };

        return result;
    }


    private double hashNormalized(int x, int z) {
        int h = x * 73428767 ^ z * 912367;
        h ^= (h >>> 13);
        h *= 0x85ebca6b;
        h ^= (h >>> 16);
        return (h & 0x7FFFFFFF) / (double) 0x7FFFFFFF * 2.0 - 1.0;
    }

    private double applyDistanceFunction(CellularDistanceFunction function, double distSq) {
        return switch (function) {
            case Euclidean -> Math.sqrt(distSq);
            case EuclideanSq -> distSq;
            case Manhattan -> Math.sqrt(distSq) * 1.5;
            case Hybrid -> Math.sqrt(distSq) + 0.25 * distSq;
        };
    }

    @Override
    public double noise(long seed, double x, double y, double z) {
        return noise(seed, x, z);
    }
}

