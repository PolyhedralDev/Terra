package com.dfsek.terra.addons.terrascript.sampler;

import java.util.function.Supplier;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;
import com.dfsek.terra.api.noise.NoiseSampler;


public class SamplerFunction implements Function<Number> {
    private final Returnable<Number> x, y, z;
    private final Returnable<String> function;
    
    private final java.util.function.Function<Supplier<String>, NoiseSampler> samplerFunction;
    
    private final boolean twoD;
    private final Position position;
    
    public SamplerFunction(Returnable<String> function,
                           Returnable<Number> x,
                           Returnable<Number> y,
                           Returnable<Number> z,
                           java.util.function.Function<Supplier<String>, NoiseSampler> samplerFunction,
                           boolean twoD,
                           Position position) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.function = function;
        this.samplerFunction = samplerFunction;
        this.twoD = twoD;
        this.position = position;
    }
    
    @Override
    public Number apply(ImplementationArguments implementationArguments, Scope scope) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;
        double x = this.x.apply(implementationArguments, scope).doubleValue();
        
        double z = this.z.apply(implementationArguments, scope).doubleValue();
        
        NoiseSampler sampler = samplerFunction.apply(() -> function.apply(implementationArguments, scope));
        if(twoD) {
            return sampler.noise(arguments.getWorld().getSeed(), x, z);
        } else {
            double y = this.y.apply(implementationArguments, scope).doubleValue();
            return sampler.noise(arguments.getWorld().getSeed(), x, y, z);
        }
    }
    
    @Override
    public Position getPosition() {
        return position;
    }
    
    @Override
    public ReturnType returnType() {
        return ReturnType.NUMBER;
    }
}
