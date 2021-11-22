/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.samplers.noise.simplex;

/**
 * NoiseSampler implementation to provide OpenSimplex2 noise.
 */
public class OpenSimplex2Sampler extends SimplexStyleSampler {
    private static final double SQRT3 = 1.7320508075688772935274463415059;
    
    @Override
    public double getNoiseRaw(long sl, double x, double y) {
        int seed = (int) sl;
        // 2D OpenSimplex2 case uses the same algorithm as ordinary Simplex.
        final double G2 = (3 - SQRT3) / 6;
        
        final double F2 = 0.5f * (SQRT3 - 1);
        double s = (x + y) * F2;
        x += s;
        y += s;
        
        
        int i = fastFloor(x);
        int j = fastFloor(y);
        double xi = x - i;
        double yi = y - j;
        
        double t = (xi + yi) * G2;
        double x0 = xi - t;
        double y0 = yi - t;
        
        i *= PRIME_X;
        j *= PRIME_Y;
        
        double n0, n1, n2;
        
        double a = 0.5 - x0 * x0 - y0 * y0;
        if(a <= 0) n0 = 0;
        else {
            n0 = (a * a) * (a * a) * gradCoord(seed, i, j, x0, y0);
        }
        
        double c = 2 * (1 - 2 * G2) * (1 / G2 - 2) * t + ((-2 * (1 - 2 * G2) * (1 - 2 * G2)) + a);
        if(c <= 0) n2 = 0;
        else {
            double x2 = x0 + (2 * G2 - 1);
            double y2 = y0 + (2 * G2 - 1);
            n2 = (c * c) * (c * c) * gradCoord(seed, i + PRIME_X, j + PRIME_Y, x2, y2);
        }
        
        if(y0 > x0) {
            double x1 = x0 + G2;
            double y1 = y0 + (G2 - 1);
            double b = 0.5 - x1 * x1 - y1 * y1;
            if(b <= 0) n1 = 0;
            else {
                n1 = (b * b) * (b * b) * gradCoord(seed, i, j + PRIME_Y, x1, y1);
            }
        } else {
            double x1 = x0 + (G2 - 1);
            double y1 = y0 + G2;
            double b = 0.5 - x1 * x1 - y1 * y1;
            if(b <= 0) n1 = 0;
            else {
                n1 = (b * b) * (b * b) * gradCoord(seed, i + PRIME_X, j, x1, y1);
            }
        }
        
        return (n0 + n1 + n2) * 99.83685446303647f;
    }
    
    @Override
    public double getNoiseRaw(long sl, double x, double y, double z) {
        int seed = (int) sl;
        // 3D OpenSimplex2Sampler case uses two offset rotated cube grids.
        final double R3 = (2.0 / 3.0);
        double r = (x + y + z) * R3; // Rotation, not skew
        x = r - x;
        y = r - y;
        z = r - z;
        
        
        int i = fastRound(x);
        int j = fastRound(y);
        int k = fastRound(z);
        double x0 = x - i;
        double y0 = y - j;
        double z0 = z - k;
        
        int xNSign = (int) (-1.0 - x0) | 1;
        int yNSign = (int) (-1.0 - y0) | 1;
        int zNSign = (int) (-1.0 - z0) | 1;
        
        double ax0 = xNSign * -x0;
        double ay0 = yNSign * -y0;
        double az0 = zNSign * -z0;
        
        i *= PRIME_X;
        j *= PRIME_Y;
        k *= PRIME_Z;
        
        double value = 0;
        double a = (0.6f - x0 * x0) - (y0 * y0 + z0 * z0);
        
        for(int l = 0; ; l++) {
            if(a > 0) {
                value += (a * a) * (a * a) * gradCoord(seed, i, j, k, x0, y0, z0);
            }
            
            if(ax0 >= ay0 && ax0 >= az0) {
                double b = a + ax0 + ax0;
                if(b > 1) {
                    b -= 1;
                    value += (b * b) * (b * b) * gradCoord(seed, i - xNSign * PRIME_X, j, k, x0 + xNSign, y0, z0);
                }
            } else if(ay0 > ax0 && ay0 >= az0) {
                double b = a + ay0 + ay0;
                if(b > 1) {
                    b -= 1;
                    value += (b * b) * (b * b) * gradCoord(seed, i, j - yNSign * PRIME_Y, k, x0, y0 + yNSign, z0);
                }
            } else {
                double b = a + az0 + az0;
                if(b > 1) {
                    b -= 1;
                    value += (b * b) * (b * b) * gradCoord(seed, i, j, k - zNSign * PRIME_Z, x0, y0, z0 + zNSign);
                }
            }
            
            if(l == 1) break;
            
            ax0 = 0.5 - ax0;
            ay0 = 0.5 - ay0;
            az0 = 0.5 - az0;
            
            x0 = xNSign * ax0;
            y0 = yNSign * ay0;
            z0 = zNSign * az0;
            
            a += (0.75 - ax0) - (ay0 + az0);
            
            i += (xNSign >> 1) & PRIME_X;
            j += (yNSign >> 1) & PRIME_Y;
            k += (zNSign >> 1) & PRIME_Z;
            
            xNSign = -xNSign;
            yNSign = -yNSign;
            zNSign = -zNSign;
            
            seed = ~seed;
        }
        
        return value * 32.69428253173828125;
    }
}
