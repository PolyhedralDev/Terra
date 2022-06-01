/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.util;

import net.jafama.FastMath;


public enum Rotation {
    
    CW_90(90),
    CW_180(180),
    CCW_90(270),
    NONE(0);
    private final int degrees;
    
    Rotation(int degrees) {
        this.degrees = degrees;
    }
    
    public static Rotation fromDegrees(int deg) {
        return switch(FastMath.floorMod(deg, 360)) {
            case 0 -> Rotation.NONE;
            case 90 -> Rotation.CW_90;
            case 180 -> Rotation.CW_180;
            case 270 -> Rotation.CCW_90;
            default -> throw new IllegalArgumentException();
        };
    }
    
    public Rotation inverse() {
        return switch(this) {
            case NONE -> NONE;
            case CCW_90 -> CW_90;
            case CW_90 -> CCW_90;
            case CW_180 -> CW_180;
        };
    }
    
    public Rotation rotate(Rotation rotation) {
        return fromDegrees(this.getDegrees() + rotation.getDegrees());
    }
    
    public int getDegrees() {
        return degrees;
    }
    
    public enum Axis {
        X,
        Y,
        Z
    }
}
