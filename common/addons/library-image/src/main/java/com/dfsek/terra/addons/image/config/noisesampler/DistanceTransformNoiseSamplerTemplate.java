package com.dfsek.terra.addons.image.config.noisesampler;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.image.image.Image;
import com.dfsek.terra.addons.image.operator.DistanceTransform;
import com.dfsek.terra.addons.image.operator.DistanceTransform.CostFunction;
import com.dfsek.terra.addons.image.operator.DistanceTransform.Normalization;
import com.dfsek.terra.addons.image.util.ColorUtil.Channel;
import com.dfsek.terra.api.noise.NoiseSampler;


public class DistanceTransformNoiseSamplerTemplate implements ObjectTemplate<NoiseSampler> {

    @Value("image")
    private Image image;

    /**
     * The threshold value applied to the channel specified in the 'channel' parameter that splits
     * the image into a binary image. This parameter is only used for cost functions that utilize
     * a binary image.
     */
    @Value("threshold")
    @Default
    private int threshold = 127;

    /**
     * If set to true, distances calculated will be clamped to stay above the largest
     * distance calculated on the edges of the image. This ensures output values do not
     * appear to be cut off at the image boundaries. It is recommended to leave padding
     * around the image if this is in use, such that larger evaluated distances do not
     * get cut out by smaller evaluated distances close to borders. Doing so will yield
     * better results.
     */
    @Value("clamp-to-max-edge")
    @Default
    private boolean clampToEdge = false;

    /**
     * The target channel to run distance calculations on.
     */
    @Value("channel")
    @Default
    private Channel channel = Channel.GRAYSCALE;

    /**
     * The method of image processing applied to the specified image prior to calculating
     * distances.
     */
    @Value("cost-function")
    @Default
    private CostFunction costFunction = CostFunction.Channel;

    /**
     * Inverts the resulting binary image that may be used as a cost function.
     */
    @Value("invert-threshold")
    @Default
    private boolean invertThreshold = false;

    /**
     * How the final distance calculation should be redistributed.
     */
    @Value("normalization")
    @Default
    private Normalization normalization = Normalization.None;

    @Override
    public NoiseSampler get() {
        return new DistanceTransform.Noise(new DistanceTransform(image, channel, threshold, clampToEdge, costFunction, invertThreshold),
            normalization);
    }
}
