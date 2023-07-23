package com.dfsek.terra.addons.terrascript.sampler;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.tokenizer.SourcePosition;
import com.dfsek.terra.api.noise.NoiseSampler;


public class ConstantSamplerFunction implements Function<Number> {
    private final Expression<Number> x, y, z;
    private final NoiseSampler sampler;
    
    
    private final boolean twoD;
    private final SourcePosition position;
    
    public ConstantSamplerFunction(NoiseSampler sampler,
                                   Expression<Number> x,
                                   Expression<Number> y,
                                   Expression<Number> z,
                                   boolean twoD,
                                   SourcePosition position) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.sampler = sampler;
        this.twoD = twoD;
        this.position = position;
    }
    
    @Override
    public Number invoke(ImplementationArguments implementationArguments, Scope scope) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;
        double x = this.x.invoke(implementationArguments, scope).doubleValue();
        
        double z = this.z.invoke(implementationArguments, scope).doubleValue();
        
        if(twoD) {
            return sampler.noise(arguments.getWorld().getSeed(), x, z);
        } else {
            double y = this.y.invoke(implementationArguments, scope).doubleValue();
            return sampler.noise(arguments.getWorld().getSeed(), x, y, z);
        }
    }
    
    @Override
    public SourcePosition getPosition() {
        return position;
    }
    
    @Override
    public ReturnType returnType() {
        return ReturnType.NUMBER;
    }
}
