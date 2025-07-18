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
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.vector.Vector2;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;


/**
 * NoiseSampler implementation for A Modified Cellular (Voronoi/Worley) Noise with Image Sampling for Seeding White pixels #FFFFFF being seeds
 */
public class CellularImageSampler implements NoiseSampler {
    private DistanceFunction distanceFunction = DistanceFunction.EuclideanSq;
    private ReturnType returnType = ReturnType.Distance;
    private NoiseSampler noiseLookup;
    private Image image;
    private KDTree tree;
    private Alignment alignment = Alignment.NONE;
    private static final Map<Integer, CompletableFuture<KDTree>> treeFutures = new ConcurrentHashMap<>();



    public void setDistanceFunction(DistanceFunction distanceFunction) {
        this.distanceFunction = distanceFunction;
    }

    public void setNoiseLookup(NoiseSampler noiseLookup) {
        this.noiseLookup = noiseLookup;
    }

    public void setReturnType(ReturnType returnType) {
        this.returnType = returnType;
    }

    public void setImage(Image image){
        this.image = image;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    private int hash() {
        return Objects.hash(alignment.name());
    }

    public boolean isTreeSet() {
        CompletableFuture<KDTree> future = treeFutures.get(hash());
        return future != null && future.isDone() && !future.isCompletedExceptionally();
    }

    public void doKDTree() {
        treeFutures.computeIfAbsent(hash(), h -> CompletableFuture.supplyAsync(() -> {
            List<Vector2> whitePixels = extractWhitePixels(image);
            return new KDTree(whitePixels);
        })).thenAccept(tree -> {
            this.tree = tree;
        });
    }


    public List<Vector2> extractWhitePixels(Image image) {
        int width = image.getWidth();
        int height = image.getHeight();

        int offsetX = 0;
        int offsetZ = 0;

        if (alignment == Alignment.CENTER) {
            offsetX = -width / 2;
            offsetZ = -height / 2;
        }

        List<Vector2> points = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y) & 0xFFFFFF;
                if (rgb == 0xFFFFFF) {
                    Vector2 point = Vector2.of(x + offsetX, y + offsetZ);
                    points.add(point);

                }
            }
        }

        return points;
    }

    @Override
    public double noise(long sl, double x, double z) {
        CompletableFuture<KDTree> future = treeFutures.get(hash());

        if (future == null || future.isCompletedExceptionally()) {
            throw new IllegalStateException("KDTree not initialized for image.");
        }

        try {
            tree = future.get();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving KDTree", e);
        }

        int xr = (int) Math.round(x);
        int zr = (int) Math.round(z);
        Vector2 query = Vector2.of(xr, zr);

        List<Vector2> nearest = tree.kNearest(query, 3);

        double distance0, distance1, distance2;

        if (distanceFunction == DistanceFunction.Manhattan) {
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

        ReturnType type = returnType;



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

    private double applyDistanceFunction(DistanceFunction function, double distSq) {
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

    public enum DistanceFunction {
        Euclidean,
        EuclideanSq,
        Manhattan,
        Hybrid
    }

    public enum ReturnType {
        Distance,
        Distance2,
        Distance2Add,
        Distance2Sub,
        Distance2Mul,
        Distance2Div,
        NoiseLookup,
        LocalNoiseLookup,
        Distance3,
        Distance3Add,
        Distance3Sub,
        Distance3Mul,
        Distance3Div,
        Angle,
        CellValue
    }
}

