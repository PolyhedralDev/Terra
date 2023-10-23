package com.dfsek.terra.addons.terrascript.sampler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.dfsek.terra.addons.noise.config.DimensionApplicableNoiseSampler;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable.ReturnType;
import com.dfsek.terra.addons.terrascript.parser.lang.constants.NumericConstant;
import com.dfsek.terra.addons.terrascript.parser.lang.constants.StringConstant;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public class SamplerFunctionBuilder implements FunctionBuilder<com.dfsek.terra.addons.terrascript.parser.lang.functions.Function<Number>> {
    private final Map<String, DimensionApplicableNoiseSampler> samplers2d;
    private final Map<String, DimensionApplicableNoiseSampler> samplers3d;
    
    public SamplerFunctionBuilder(Map<String, DimensionApplicableNoiseSampler> samplers) {
        this.samplers2d = new HashMap<>();
        this.samplers3d = new HashMap<>();
        
        samplers.forEach((id, sampler) -> {
            if(sampler.getDimensions() == 2) {
                samplers2d.put(id, sampler);
            } else if(sampler.getDimensions() == 3) {
                samplers3d.put(id, sampler);
            }
        });
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public com.dfsek.terra.addons.terrascript.parser.lang.functions.Function<Number> build(List<Returnable<?>> argumentList,
                                                                                           Position position) {
        Returnable<String> arg = (Returnable<String>) argumentList.get(0);
        
        if(argumentList.size() == 3) { // 2D
            if(arg instanceof StringConstant constant) {
                return new ConstantSamplerFunction(Objects.requireNonNull(samplers2d.get(constant.getConstant()),
                                                                          "No such 2D noise function " + constant.getConstant())
                                                          .getSampler(),
                                                   (Returnable<Number>) argumentList.get(1),
                                                   new NumericConstant(0, position),
                                                   (Returnable<Number>) argumentList.get(2),
                                                   true,
                                                   position);
            } else {
                return new SamplerFunction((Returnable<String>) argumentList.get(0),
                                           (Returnable<Number>) argumentList.get(1),
                                           new NumericConstant(0, position),
                                           (Returnable<Number>) argumentList.get(2),
                                           s -> Objects.requireNonNull(samplers2d.get(s.get()), "No such 2D noise function " + s.get())
                                                       .getSampler(),
                                           true,
                                           position);
            }
            
        } else { // 3D
            if(arg instanceof StringConstant constant) {
                return new ConstantSamplerFunction(Objects.requireNonNull(samplers3d.get(constant.getConstant()),
                                                                          "No such 3D noise function " + constant.getConstant())
                                                          .getSampler(),
                                                   (Returnable<Number>) argumentList.get(1),
                                                   (Returnable<Number>) argumentList.get(2),
                                                   (Returnable<Number>) argumentList.get(3),
                                                   true,
                                                   position);
            } else {
                return new SamplerFunction((Returnable<String>) argumentList.get(0),
                                           (Returnable<Number>) argumentList.get(1),
                                           (Returnable<Number>) argumentList.get(2),
                                           (Returnable<Number>) argumentList.get(3),
                                           s -> Objects.requireNonNull(samplers3d.get(s.get()), "No such 3D noise function " + s.get())
                                                       .getSampler(),
                                           true,
                                           position);
            }
        }
    }
    
    @Override
    public int argNumber() {
        return -1;
    }
    
    @Override
    public ReturnType getArgument(int position) {
        return switch(position) {
            case 0 -> ReturnType.STRING;
            case 1, 2, 3 -> ReturnType.NUMBER;
            default -> null;
        };
    }
}
