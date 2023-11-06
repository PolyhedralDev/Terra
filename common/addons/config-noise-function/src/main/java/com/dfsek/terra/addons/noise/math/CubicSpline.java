package com.dfsek.terra.addons.noise.math;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static com.dfsek.terra.api.util.MathUtil.lerp;


public class CubicSpline {
    
    private final double[] fromValues;
    private final double[] toValues;
    private final double[] gradients;
    
    public CubicSpline(List<Point> points) {
        Collections.sort(points);
        
        this.fromValues = new double[points.size()];
        this.toValues = new double[points.size()];
        this.gradients = new double[points.size()];
        
        for(int i = 0; i < points.size(); i++) {
            fromValues[i] = points.get(i).from;
            toValues[i] = points.get(i).to;
            gradients[i] = points.get(i).gradient;
        }
    }
    
    public static double calculate(double in, double[] fromValues, double[] toValues, double[] gradients) {
        int pointIdx = floorBinarySearch(in, fromValues) - 1;
        
        int pointIdxLast = fromValues.length - 1;
        
        if(pointIdx < 0) { // If to left of first point return linear function intersecting said point using point's gradient
            return gradients[0] * (in - fromValues[0]) + toValues[0];
        } else if(pointIdx == pointIdxLast) { // Do same if to right of last point
            return gradients[pointIdxLast] * (in - fromValues[pointIdxLast]) + toValues[pointIdxLast];
        } else {
            double fromLeft = fromValues[pointIdx];
            double fromRight = fromValues[pointIdx + 1];
            
            double toLeft = toValues[pointIdx];
            double toRight = toValues[pointIdx + 1];
            
            double gradientLeft = gradients[pointIdx];
            double gradientRight = gradients[pointIdx + 1];
            
            double fromDelta = fromRight - fromLeft;
            double toDelta = toRight - toLeft;
            
            double t = (in - fromLeft) / fromDelta;
            
            return lerp(t, toLeft, toRight) + t * (1.0F - t) * lerp(t, gradientLeft * fromDelta - toDelta,
                                                                    -gradientRight * fromDelta + toDelta);
        }
    }
    
    private static int floorBinarySearch(double targetValue, double[] values) {
        int left = 0;
        int right = values.length;
        int idx = right - left;
        while(idx > 0) {
            int halfDelta = idx / 2;
            int mid = left + halfDelta;
            if(targetValue < values[mid]) {
                idx = halfDelta;
            } else {
                left = mid + 1;
                idx -= halfDelta + 1;
            }
        }
        return left;
    }
    
    public double apply(double in) {
        return calculate(in, fromValues, toValues, gradients);
    }
    
    
    public record Point(double from, double to, double gradient) implements Comparable<Point> {
        
        @Override
        public int compareTo(@NotNull CubicSpline.Point o) {
            return Double.compare(from, o.from);
        }
    }
}
