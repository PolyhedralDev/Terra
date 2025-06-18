package com.dfsek.terra.addons.terrascript.sampler;

import com.dfsek.seismic.type.sampler.Sampler;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public class ConstantSamplerFunction implements Function<Number> {
    private final Returnable<Number> x, y, z;
    private final Sampler sampler;


    private final boolean twoD;
    private final Position position;

    public ConstantSamplerFunction(Sampler sampler,
                                   Returnable<Number> x,
                                   Returnable<Number> y,
                                   Returnable<Number> z,
                                   boolean twoD,
                                   Position position) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.sampler = sampler;
        this.twoD = twoD;
        this.position = position;
    }

    @Override
    public Number apply(ImplementationArguments implementationArguments, Scope scope) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;
        double x = this.x.apply(implementationArguments, scope).doubleValue();

        double z = this.z.apply(implementationArguments, scope).doubleValue();

        if(twoD) {
            return sampler.getSample(arguments.getWorld().getSeed(), x, z);
        } else {
            double y = this.y.apply(implementationArguments, scope).doubleValue();
            return sampler.getSample(arguments.getWorld().getSeed(), x, y, z);
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
