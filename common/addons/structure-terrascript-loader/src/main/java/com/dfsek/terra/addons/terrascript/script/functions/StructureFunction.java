package com.dfsek.terra.addons.terrascript.script.functions;

import net.jafama.FastMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import com.dfsek.terra.addons.terrascript.buffer.IntermediateBuffer;
import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.parser.lang.variables.Variable;
import com.dfsek.terra.addons.terrascript.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.structure.rotation.Rotation;
import com.dfsek.terra.api.util.RotationUtil;
import com.dfsek.terra.api.vector.Vector2;
import com.dfsek.terra.api.vector.Vector3;


public class StructureFunction implements Function<Boolean> {
    private static final Logger logger = LoggerFactory.getLogger(StructureFunction.class);
    
    private final Registry<Structure> registry;
    private final Returnable<String> id;
    private final Returnable<Number> x, y, z;
    private final Position position;
    private final TerraPlugin main;
    private final List<Returnable<String>> rotations;
    
    public StructureFunction(Returnable<Number> x, Returnable<Number> y, Returnable<Number> z, Returnable<String> id,
                             List<Returnable<String>> rotations, Registry<Structure> registry, Position position, TerraPlugin main) {
        this.registry = registry;
        this.id = id;
        this.position = position;
        this.x = x;
        this.y = y;
        this.z = z;
        this.main = main;
        this.rotations = rotations;
    }
    
    @Override
    public ReturnType returnType() {
        return ReturnType.BOOLEAN;
    }
    
    @Override
    public Boolean apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;
        
        if(arguments.getRecursions() > main.getTerraConfig().getMaxRecursion())
            throw new RuntimeException("Structure recursion too deep: " + arguments.getRecursions());
        
        Vector2 xz = new Vector2(x.apply(implementationArguments, variableMap).doubleValue(),
                                 z.apply(implementationArguments, variableMap).doubleValue());
        
        RotationUtil.rotateVector(xz, arguments.getRotation());
        
        String app = id.apply(implementationArguments, variableMap);
        Structure script = registry.get(app);
        if(script == null) {
            logger.error("No such structure {}", app);
            return null;
        }
        
        Rotation rotation1;
        String rotString = rotations.get(arguments.getRandom().nextInt(rotations.size())).apply(implementationArguments, variableMap);
        try {
            rotation1 = Rotation.valueOf(rotString);
        } catch(IllegalArgumentException e) {
            logger.error("Invalid rotation {}", rotString);
            return null;
        }
        
        Vector3 offset = new Vector3(FastMath.roundToInt(xz.getX()), y.apply(implementationArguments, variableMap).doubleValue(),
                                     FastMath.roundToInt(xz.getZ()));
        
        return script.generate(new IntermediateBuffer(arguments.getBuffer(), offset), arguments.getWorld(), arguments.getRandom(),
                               arguments.getRotation().rotate(rotation1), arguments.getRecursions() + 1);
    }
    
    @Override
    public Position getPosition() {
        return position;
    }
}
