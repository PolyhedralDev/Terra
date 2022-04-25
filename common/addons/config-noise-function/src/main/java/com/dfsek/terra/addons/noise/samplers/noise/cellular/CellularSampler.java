/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.samplers.noise.cellular;

import com.dfsek.terra.addons.noise.samplers.noise.NoiseFunction;

import net.jafama.FastMath;

import com.dfsek.terra.addons.noise.samplers.noise.simplex.OpenSimplex2Sampler;
import com.dfsek.terra.api.noise.NoiseSampler;


/**
 * NoiseSampler implementation for Cellular (Voronoi/Worley) Noise.
 */
public class CellularSampler extends NoiseFunction {
    private DistanceFunction distanceFunction = DistanceFunction.EuclideanSq;
    private ReturnType returnType = ReturnType.Distance;
    private double jitterModifier = 1.0;
    
    private NoiseSampler noiseLookup;
    
    public CellularSampler() {
        noiseLookup = new OpenSimplex2Sampler();
    }
    
    public void setDistanceFunction(DistanceFunction distanceFunction) {
        this.distanceFunction = distanceFunction;
    }
    
    public void setJitterModifier(double jitterModifier) {
        this.jitterModifier = jitterModifier;
    }
    
    public void setNoiseLookup(NoiseSampler noiseLookup) {
        this.noiseLookup = noiseLookup;
    }
    
    public void setReturnType(ReturnType returnType) {
        this.returnType = returnType;
    }
    
    @Override
    public double getNoiseRaw(long sl, double x, double y) {
        int seed = (int) sl;
        int xr = fastRound(x);
        int yr = fastRound(y);
        
        double distance0 = Double.MAX_VALUE;
        double distance1 = Double.MAX_VALUE;
        double distance2 = Double.MAX_VALUE;
        
        int closestHash = 0;
        
        double cellularJitter = 0.43701595 * jitterModifier;
        
        int xPrimed = (xr - 1) * PRIME_X;
        int yPrimedBase = (yr - 1) * PRIME_Y;
        
        double centerX = x;
        double centerY = y;
        
        for(int xi = xr - 1; xi <= xr + 1; xi++) {
            int yPrimed = yPrimedBase;
            
            for(int yi = yr - 1; yi <= yr + 1; yi++) {
                int hash = hash(seed, xPrimed, yPrimed);
                int idx = hash & (255 << 1);
                
                double vecX = (xi - x) + RandomVectors.RAND_VECS_2D[idx] * cellularJitter;
                double vecY = (yi - y) + RandomVectors.RAND_VECS_2D[idx | 1] * cellularJitter;
                
                double newDistance = distanceFunction.getDistance(vecX, vecY);
                
                distance1 = fastMax(fastMin(distance1, newDistance), distance0);
                if(newDistance < distance0) {
                    distance0 = newDistance;
                    closestHash = hash;
                    centerX = ((xi + RandomVectors.RAND_VECS_2D[idx] * cellularJitter) / frequency);
                    centerY = ((yi + RandomVectors.RAND_VECS_2D[idx | 1] * cellularJitter) / frequency);
                } else if(newDistance < distance1) {
                    distance2 = distance1;
                    distance1 = newDistance;
                } else if(newDistance < distance2) {
                    distance2 = newDistance;
                }
                yPrimed += PRIME_Y;
            }
            xPrimed += PRIME_X;
        }
        
        if(distanceFunction == DistanceFunction.Euclidean && returnType != ReturnType.CellValue) {
            distance0 = fastSqrt(distance0);
            if(returnType != ReturnType.CellValue) {
                distance1 = fastSqrt(distance1);
            }
        }
        
        return switch(returnType) {
            case CellValue -> closestHash * (1 / 2147483648.0);
            case Distance -> distance0 - 1;
            case Distance2 -> distance1 - 1;
            case Distance2Add -> (distance1 + distance0) * 0.5 - 1;
            case Distance2Sub -> distance1 - distance0 - 1;
            case Distance2Mul -> distance1 * distance0 * 0.5 - 1;
            case Distance2Div -> distance0 / distance1 - 1;
            case NoiseLookup -> noiseLookup.noise(sl, centerX, centerY);
            case Distance3 -> distance2 - 1;
            case Distance3Add -> (distance2 + distance0) * 0.5 - 1;
            case Distance3Sub -> distance2 - distance0 - 1;
            case Distance3Mul -> distance2 * distance0 - 1;
            case Distance3Div -> distance0 / distance2 - 1;
            case Angle -> FastMath.atan2(y / frequency - centerY, x / frequency - centerX);
        };
    }
    
    @Override
    public double getNoiseRaw(long sl, double x, double y, double z) {
        int seed = (int) sl;
        int xr = fastRound(x);
        int yr = fastRound(y);
        int zr = fastRound(z);
        
        double distance0 = Double.MAX_VALUE;
        double distance1 = Double.MAX_VALUE;
        double distance2 = Double.MAX_VALUE;
        int closestHash = 0;
        
        double cellularJitter = 0.39614353 * jitterModifier;
        
        int xPrimed = (xr - 1) * PRIME_X;
        int yPrimedBase = (yr - 1) * PRIME_Y;
        int zPrimedBase = (zr - 1) * PRIME_Z;
        
        double centerX = x;
        double centerY = y;
        double centerZ = z;
        
        for(int xi = xr - 1; xi <= xr + 1; xi++) {
            int yPrimed = yPrimedBase;
            
            for(int yi = yr - 1; yi <= yr + 1; yi++) {
                int zPrimed = zPrimedBase;
                
                for(int zi = zr - 1; zi <= zr + 1; zi++) {
                    int hash = hash(seed, xPrimed, yPrimed, zPrimed);
                    int idx = hash & (255 << 2);
                    
                    double vecX = (xi - x) + RandomVectors.RAND_VECS_3D[idx] * cellularJitter;
                    double vecY = (yi - y) + RandomVectors.RAND_VECS_3D[idx | 1] * cellularJitter;
                    double vecZ = (zi - z) + RandomVectors.RAND_VECS_3D[idx | 2] * cellularJitter;
                    
                    double newDistance = distanceFunction.getDistance(vecX, vecY, vecZ);
                    
                    if(newDistance < distance0) {
                        distance0 = newDistance;
                        closestHash = hash;
                        centerX = ((xi + RandomVectors.RAND_VECS_3D[idx] * cellularJitter) / frequency);
                        centerY = ((yi + RandomVectors.RAND_VECS_3D[idx | 1] * cellularJitter) / frequency);
                        centerZ = ((zi + RandomVectors.RAND_VECS_3D[idx | 2] * cellularJitter) / frequency);
                    } else if(newDistance < distance1) {
                        distance2 = distance1;
                        distance1 = newDistance;
                    } else if(newDistance < distance2) {
                        distance2 = newDistance;
                    }
                    zPrimed += PRIME_Z;
                }
                yPrimed += PRIME_Y;
            }
            xPrimed += PRIME_X;
        }
        
        
        if(distanceFunction == DistanceFunction.Euclidean &&
           returnType != ReturnType.CellValue) { // optimisation: dont compute sqrt until end
            distance0 = fastSqrt(distance0);
            if(returnType != ReturnType.CellValue) {
                distance1 = fastSqrt(distance1);
            }
        }
        
        return switch(returnType) {
            case CellValue -> closestHash * (1 / 2147483648.0);
            case Distance -> distance0 - 1;
            case Distance2 -> distance1 - 1;
            case Distance2Add -> (distance1 + distance0) * 0.5 - 1;
            case Distance2Sub -> distance1 - distance0 - 1;
            case Distance2Mul -> distance1 * distance0 * 0.5 - 1;
            case Distance2Div -> distance0 / distance1 - 1;
            case NoiseLookup -> noiseLookup.noise(sl, centerX, centerY, centerZ);
            case Distance3 -> distance2 - 1;
            case Distance3Add -> (distance2 + distance0) * 0.5 - 1;
            case Distance3Sub -> distance2 - distance0 - 1;
            case Distance3Mul -> distance2 * distance0 - 1;
            case Distance3Div -> distance0 / distance2 - 1;
            case Angle -> FastMath.atan2(y / frequency - centerY, x / frequency - centerX);
        };
    }
    
    public enum DistanceFunction {
        
        Euclidean {
            @Override
            public double getDistance(double x, double y) {
                return x * x + y * y; // optimisation: dont compute sqrt until end
            }
            
            @Override
            public double getDistance(double x, double y, double z) {
                return x * x + y * y + z * z; // optimisation: dont compute sqrt until end
            }
        },
        EuclideanSq {
            @Override
            public double getDistance(double x, double y) {
                return x * x + y * y;
            }
            
            @Override
            public double getDistance(double x, double y, double z) {
                return x * x + y * y + z + z;
            }
        },
        Manhattan {
            @Override
            public double getDistance(double x, double y) {
                return fastAbs(x) + fastAbs(y);
            }
            
            @Override
            public double getDistance(double x, double y, double z) {
                return fastAbs(x) + fastAbs(y) + fastAbs(z);
            }
        },
        Hybrid {
            @Override
            public double getDistance(double x, double y) {
                return (fastAbs(x) + fastAbs(y)) +
                       (x * x + y * y);
            }
            
            @Override
            public double getDistance(double x, double y, double z) {
                return (fastAbs(x) + fastAbs(y) + fastAbs(z)) +
                       (x * x + y * y + z * z);
            }
        };
        
        public abstract double getDistance(double x, double y);
        
        public abstract double getDistance(double x, double y, double z);
    }
    
    
    public enum ReturnType {
        CellValue,
        Distance,
        Distance2,
        Distance2Add,
        Distance2Sub,
        Distance2Mul,
        Distance2Div,
        NoiseLookup,
        Distance3,
        Distance3Add,
        Distance3Sub,
        Distance3Mul,
        Distance3Div,
        Angle
    }
}
