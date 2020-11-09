package com.dfsek.terra.structure;

public enum Rotation {
    CW_90(90), CW_180(180), CCW_90(270), NONE(0);
    private final int degrees;

    Rotation(int degrees) {
        this.degrees = degrees;
    }

    public static Rotation fromDegrees(int deg) {
        switch(Math.floorMod(deg, 360)) {
            case 0:
                return Rotation.NONE;
            case 90:
                return Rotation.CW_90;
            case 180:
                return Rotation.CW_180;
            case 270:
                return Rotation.CCW_90;
            default:
                throw new IllegalArgumentException();
        }
    }

    public int getDegrees() {
        return degrees;
    }

    public Rotation inverse() {
        switch(this) {
            case NONE:
                return NONE;
            case CCW_90:
                return CW_90;
            case CW_90:
                return CCW_90;
            case CW_180:
                return CW_180;
            default:
                throw new IllegalArgumentException();
        }
    }

    public enum Axis {
        X, Y, Z
    }
}
