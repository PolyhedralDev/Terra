package com.dfsek.terra.addons.noise.paralithic;

import com.dfsek.paralithic.eval.tokenizer.ParseException;
import com.dfsek.paralithic.functions.Function;

import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.addons.noise.config.DimensionApplicableNoiseSampler;
import com.dfsek.terra.addons.noise.config.templates.FunctionTemplate;
import com.dfsek.terra.addons.noise.paralithic.defined.UserDefinedFunction;
import com.dfsek.terra.addons.noise.paralithic.noise.NoiseFunction2;
import com.dfsek.terra.addons.noise.paralithic.noise.NoiseFunction3;


public class FunctionUtil {
    private FunctionUtil() {}
    
    public static Map<String, Function> convertFunctionsAndSamplers(Map<String, FunctionTemplate> functions,
                                                                    Map<String, DimensionApplicableNoiseSampler> samplers) throws ParseException {
        Map<String, Function> functionMap = new HashMap<>();
        for(Map.Entry<String, FunctionTemplate> entry : functions.entrySet()) {
            functionMap.put(entry.getKey(), UserDefinedFunction.newInstance(entry.getValue()));
        }
        samplers.forEach((id, sampler) -> functionMap.put(id,
                                                          sampler.getDimensions() == 2 ?
                                                          new NoiseFunction2(sampler.getSampler()) :
                                                          new NoiseFunction3(sampler.getSampler())));
        return functionMap;
    }
}
