package com.dfsek.terra.addons.image.operator;

import net.jafama.FastMath;

import com.dfsek.terra.addons.image.image.Image;
import com.dfsek.terra.addons.image.util.ColorUtil;
import com.dfsek.terra.addons.image.util.ColorUtil.Channel;
import com.dfsek.terra.api.noise.NoiseSampler;

import static com.dfsek.terra.addons.image.util.MathUtil.lerp;


/**
 * Computes a 2D distance transform of a given image and stores the result in a 2D array of distances.
 * Implementation based on the algorithm described in the paper
 * <a href="https://cs.brown.edu/people/pfelzens/papers/dt-final.pdf">Distance Transforms of Sampled Functions</a>
 * by Pedro F. Felzenszwalb and Daniel P. Huttenlocher.
 */
public class DistanceTransform {
    
    private final double[][] distances;
    
    /**
     * Size bounds matching the provided image.
     */
    private final int width, height;
    
    /**
     * Min and max distances of the distance computation. These may change after {@link #normalize(Normalization)} calls.
     */
    private double minDistance, maxDistance;
    
    private static final double MAX_DISTANCE_CAP = 10_000_000; // Arbitrarily large value, doubtful someone would
                                                               // ever use an image large enough to exceed this.
    public DistanceTransform(Image image, Channel channel, int threshold, boolean clampToMaxEdgeDistance, CostFunction costFunction, boolean invertThreshold) {
        // Construct binary image based on threshold value
        boolean[][] binaryImage = new boolean[image.getWidth()][image.getHeight()];
        for(int x = 0; x < image.getWidth(); x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                binaryImage[x][y] = ColorUtil.getChannel(image.getRGB(x, y), channel) > threshold ^ invertThreshold;
            }
        }
        
        // Get edges of binary image
        boolean[][] binaryImageEdge = new boolean[image.getWidth()][image.getHeight()];
        for(int x = 0; x < image.getWidth(); x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                if(!binaryImage[x][y])
                    binaryImageEdge[x][y] = false;
                else
                    // If cell borders any false cell
                    binaryImageEdge[x][y] = x > 0 && !binaryImage[x-1][y] ||
                                 y > 0 && !binaryImage[x][y-1] ||
                                 x < image.getWidth ()-1 && !binaryImage[x+1][y] ||
                                 y < image.getHeight()-1 && !binaryImage[x][y+1];
            }
        }
        
        double[][] function = new double[image.getWidth()][image.getHeight()];
        for(int x = 0; x < image.getWidth(); x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                function[x][y] = switch (costFunction) {
                    case Channel -> ColorUtil.getChannel(image.getRGB(x, y), channel);
                    case Threshold -> binaryImage[x][y] ? MAX_DISTANCE_CAP : 0;
                    case ThresholdEdge, ThresholdEdgeSigned -> binaryImageEdge[x][y] ? 0 : MAX_DISTANCE_CAP;
                };
            }
        }
        
        distances = calculateDistance2D(function);
        
        if(costFunction == CostFunction.ThresholdEdgeSigned) {
            for(int x = 0; x < image.getWidth(); x++) {
                for(int y = 0; y < image.getHeight(); y++) {
                    distances[x][y] *= binaryImage[x][y] ? 1 : -1;
                }
            }
        }
        
        if(clampToMaxEdgeDistance) {
            // Find largest value on the edge of the image
            double max = Double.NEGATIVE_INFINITY;
            for(int x = 0; x < image.getWidth(); x++) {
                max = Math.max(max, distances[x][0]);
                max = Math.max(max, distances[x][image.getHeight()-1]);
            }
            for(int y = 0; y < image.getHeight(); y++) {
                max = Math.max(max, distances[0][y]);
                max = Math.max(max, distances[image.getWidth()-1][y]);
            }
            // Clamp to that largest value
            for(int x = 0; x < image.getWidth(); x++) {
                for(int y = 0; y < image.getHeight(); y++) {
                    distances[x][y] = Math.max(max, distances[x][y]);
                }
            }
        }

        this.width = image.getWidth();
        this.height = image.getHeight();
        
        setOutputRange();
    }
    
    private double[][] calculateDistance2D(double[][] f) {
        double[][] d = new double[f.length][f[0].length];
        // Distance pass for each column
        for(int x = 0; x < f.length; x++) {
            d[x] = calculateDistance1D(f[x]);
        }
        // Distance pass for each row
        double[] row = new double[f.length];
        for(int y = 0; y < f[0].length; y++) {
            for(int x = 0; x < f[0].length; x++)
                row[x] = d[x][y];
            row = calculateDistance1D(row);
            for(int x = 0; x < f[0].length; x++) {
                d[x][y] = FastMath.sqrt(row[x]);
            }
        }
        return d;
    }
    
    private double[] calculateDistance1D(double[] f) {
        double[] d = new double[f.length];
        int[] v = new int[f.length];
        double[] z = new double[f.length+1];
        int k = 0;
        v[0] = 0;
        z[0] = Integer.MIN_VALUE;
        z[1] = Integer.MAX_VALUE;
        for(int q = 1; q <= f.length-1; q++) {
            double s = ((f[q]+FastMath.pow2(q))-(f[v[k]]+FastMath.pow2(v[k])))/(2*q-2*v[k]);
            while (s <= z[k]) {
                k--;
                s = ((f[q]+FastMath.pow2(q))-(f[v[k]]+FastMath.pow2(v[k])))/(2*q-2*v[k]);
            }
            k++;
            v[k] = q;
            z[k] = s;
            z[k+1] = Integer.MAX_VALUE;
        }
        
        k = 0;
        for(int q = 0; q <= f.length-1; q++) {
            while(z[k+1] < q)
                k++;
            d[q] = FastMath.pow2(q-v[k]) + f[v[k]];
        }
        return d;
    }
    
    /**
     * Redistributes the stored distance computation according to the provided {@link Normalization} method.
     */
    private void normalize(Normalization normalization) {
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                double d = distances[x][y];
                distances[x][y] = switch(normalization) {
                    case None -> distances[x][y];
                    case Linear -> lerp(d, minDistance, -1, maxDistance, 1);
                    case SmoothPreserveZero -> {
                        if(minDistance > 0 || maxDistance < 0) {
                            // Can't preserve zero if it is not contained in range so just lerp
                            yield lerp(distances[x][y], minDistance, -1, maxDistance, 1);
                        } else {
                            if(d > 0) {
                                yield FastMath.pow2(d/maxDistance);
                            } else if(d < 0) {
                                yield -FastMath.pow2(d/minDistance);
                            } else {
                                yield 0;
                            }
                        }
                    }
                };
            }
        }
        setOutputRange();
    }
    
    private void setOutputRange() {
        double minDistance = Double.POSITIVE_INFINITY;
        double maxDistance = Double.NEGATIVE_INFINITY;
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                minDistance = Math.min(minDistance, distances[x][y]);
                maxDistance = Math.max(maxDistance, distances[x][y]);
            }
        }
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
    }
    
    public enum CostFunction {
        Channel,
        Threshold,
        ThresholdEdge,
        ThresholdEdgeSigned,
    }
    
    public enum Normalization {
        /**
         * Return the raw calculated distances.
         */
        None,
        
        /**
         * Redistribute the output values to fit in the range [-1, 1]
         */
        Linear,
        
        /**
         * Redistributes smoothly to the range [-1, 1], such that areas where distance = 0 stay 0.
         * This is only really applicable to signed distance calculations, and will fall back to linear
         * redistribution if the input range does not contain both positive and negative values.
         */
        SmoothPreserveZero,
    }
    
    public static class Noise implements NoiseSampler {
    
        private final DistanceTransform transform;
    
        public Noise(DistanceTransform transform, Normalization normalization) {
            this.transform = transform;
            transform.normalize(normalization);
        }
    
        @Override
        public double noise(long seed, double x, double y) {
            if(x<0 || y<0 || x>=transform.width || y>=transform.height) return transform.minDistance;
            return transform.distances[FastMath.floorToInt(x)][FastMath.floorToInt(y)];
        }
    
        @Override
        public double noise(long seed, double x, double y, double z) {
            return noise(seed, x, z);
        }
    }
}
