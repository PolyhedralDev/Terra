/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.samplers.noise.simplex;

public class SimplexSampler extends SimplexStyleSampler {
    private static final Double2[] GRAD_2D = {
            new Double2(-1, -1), new Double2(1, -1), new Double2(-1, 1), new Double2(1, 1),
            new Double2(0, -1), new Double2(-1, 0), new Double2(0, 1), new Double2(1, 0),
            };
    private static final Double3[] GRAD_3D = {
            new Double3(1, 1, 0), new Double3(-1, 1, 0), new Double3(1, -1, 0), new Double3(-1, -1, 0),
            new Double3(1, 0, 1), new Double3(-1, 0, 1), new Double3(1, 0, -1), new Double3(-1, 0, -1),
            new Double3(0, 1, 1), new Double3(0, -1, 1), new Double3(0, 1, -1), new Double3(0, -1, -1),
            new Double3(1, 1, 0), new Double3(0, -1, 1), new Double3(-1, 1, 0), new Double3(0, -1, -1),
            };
    
    private static final double F2 = 1.0 / 2.0;
    private static final double F3 = (1.0 / 3.0);
    private static final double G2 = 1.0 / 4.0;
    private static final double G3 = (1.0 / 6.0);
    private static final double G33 = G3 * 3 - 1;
    
    private static final int X_PRIME = 1619;
    private static final int Y_PRIME = 31337;
    private static final int Z_PRIME = 6971;
    
    
    private static double gradCoord3D(int seed, int x, int y, int z, double xd, double yd, double zd) {
        int hash = seed;
        hash ^= X_PRIME * x;
        hash ^= Y_PRIME * y;
        hash ^= Z_PRIME * z;
        
        hash = hash * hash * hash * 60493;
        hash = (hash >> 13) ^ hash;
        
        Double3 g = GRAD_3D[hash & 15];
        
        return xd * g.x + yd * g.y + zd * g.z;
    }
    
    private static double gradCoord2D(int seed, int x, int y, double xd, double yd) {
        int hash = seed;
        hash ^= X_PRIME * x;
        hash ^= Y_PRIME * y;
        
        hash = hash * hash * hash * 60493;
        hash = (hash >> 13) ^ hash;
        
        Double2 g = GRAD_2D[hash & 7];
        
        return xd * g.x + yd * g.y;
    }
    
    @Override
    public double getNoiseRaw(long sl, double x, double y) {
        int seed = (int) sl;
        double t = (x + y) * F2;
        int i = fastFloor(x + t);
        int j = fastFloor(y + t);
        
        t = (i + j) * G2;
        double X0 = i - t;
        double Y0 = j - t;
        
        double x0 = x - X0;
        double y0 = y - Y0;
        
        int i1, j1;
        if(x0 > y0) {
            i1 = 1;
            j1 = 0;
        } else {
            i1 = 0;
            j1 = 1;
        }
        
        double x1 = x0 - i1 + G2;
        double y1 = y0 - j1 + G2;
        double x2 = x0 - 1 + F2;
        double y2 = y0 - 1 + F2;
        
        double n0, n1, n2;
        
        t = 0.5 - x0 * x0 - y0 * y0;
        if(t < 0) {
            n0 = 0;
        } else {
            t *= t;
            n0 = t * t * gradCoord2D(seed, i, j, x0, y0);
        }
        
        t = 0.5 - x1 * x1 - y1 * y1;
        if(t < 0) {
            n1 = 0;
        } else {
            t *= t;
            n1 = t * t * gradCoord2D(seed, i + i1, j + j1, x1, y1);
        }
        
        t = 0.5 - x2 * x2 - y2 * y2;
        if(t < 0) {
            n2 = 0;
        } else {
            t *= t;
            n2 = t * t * gradCoord2D(seed, i + 1, j + 1, x2, y2);
        }
        
        return 50 * (n0 + n1 + n2);
    }
    
    @Override
    public double getNoiseRaw(long sl, double x, double y, double z) {
        int seed = (int) sl;
        double t = (x + y + z) * F3;
        int i = fastFloor(x + t);
        int j = fastFloor(y + t);
        int k = fastFloor(z + t);
        
        t = (i + j + k) * G3;
        double x0 = x - (i - t);
        double y0 = y - (j - t);
        double z0 = z - (k - t);
        
        int i1, j1, k1;
        int i2, j2, k2;
        
        if(x0 >= y0) {
            if(y0 >= z0) {
                i1 = 1;
                j1 = 0;
                k1 = 0;
                i2 = 1;
                j2 = 1;
                k2 = 0;
            } else if(x0 >= z0) {
                i1 = 1;
                j1 = 0;
                k1 = 0;
                i2 = 1;
                j2 = 0;
                k2 = 1;
            } else // x0 < z0
            {
                i1 = 0;
                j1 = 0;
                k1 = 1;
                i2 = 1;
                j2 = 0;
                k2 = 1;
            }
        } else // x0 < y0
        {
            if(y0 < z0) {
                i1 = 0;
                j1 = 0;
                k1 = 1;
                i2 = 0;
                j2 = 1;
                k2 = 1;
            } else if(x0 < z0) {
                i1 = 0;
                j1 = 1;
                k1 = 0;
                i2 = 0;
                j2 = 1;
                k2 = 1;
            } else // x0 >= z0
            {
                i1 = 0;
                j1 = 1;
                k1 = 0;
                i2 = 1;
                j2 = 1;
                k2 = 0;
            }
        }
        
        double x1 = x0 - i1 + G3;
        double y1 = y0 - j1 + G3;
        double z1 = z0 - k1 + G3;
        double x2 = x0 - i2 + F3;
        double y2 = y0 - j2 + F3;
        double z2 = z0 - k2 + F3;
        double x3 = x0 + G33;
        double y3 = y0 + G33;
        double z3 = z0 + G33;
        
        double n0, n1, n2, n3;
        
        t = 0.6 - x0 * x0 - y0 * y0 - z0 * z0;
        if(t < 0) n0 = 0;
        else {
            t *= t;
            n0 = t * t * gradCoord3D(seed, i, j, k, x0, y0, z0);
        }
        
        t = 0.6 - x1 * x1 - y1 * y1 - z1 * z1;
        if(t < 0) {
            n1 = 0;
        } else {
            t *= t;
            n1 = t * t * gradCoord3D(seed, i + i1, j + j1, k + k1, x1, y1, z1);
        }
        
        t = 0.6 - x2 * x2 - y2 * y2 - z2 * z2;
        if(t < 0) {
            n2 = 0;
        } else {
            t *= t;
            n2 = t * t * gradCoord3D(seed, i + i2, j + j2, k + k2, x2, y2, z2);
        }
        
        t = 0.6 - x3 * x3 - y3 * y3 - z3 * z3;
        if(t < 0) {
            n3 = 0;
        } else {
            t *= t;
            n3 = t * t * gradCoord3D(seed, i + 1, j + 1, k + 1, x3, y3, z3);
        }
        
        return 32 * (n0 + n1 + n2 + n3);
    }
    
    private static class Double2 {
        public final double x, y;
        
        public Double2(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
    
    
    private static class Double3 {
        public final double x, y, z;
        
        public Double3(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
