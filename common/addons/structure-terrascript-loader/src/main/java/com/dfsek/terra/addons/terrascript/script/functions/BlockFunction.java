/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.script.functions;

import net.jafama.FastMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.parser.lang.constants.StringConstant;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.tokenizer.SourcePosition;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.util.RotationUtil;
import com.dfsek.terra.api.util.vector.Vector2;
import com.dfsek.terra.api.util.vector.Vector3;


public class BlockFunction implements Function<Void> {
    private static final Logger logger = LoggerFactory.getLogger(BlockFunction.class);
    protected final Expression<Number> x, y, z;
    protected final Expression<String> blockData;
    protected final Platform platform;
    private final Map<String, BlockState> data = new HashMap<>();
    private final Expression<Boolean> overwrite;
    private final SourcePosition position;
    
    public BlockFunction(Expression<Number> x, Expression<Number> y, Expression<Number> z, Expression<String> blockData,
                         Expression<Boolean> overwrite, Platform platform, SourcePosition position) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockData = blockData;
        this.overwrite = overwrite;
        this.platform = platform;
        this.position = position;
    }
    
    @Override
    public Void invoke(ImplementationArguments implementationArguments, Scope scope) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;
        BlockState rot = getBlockState(implementationArguments, scope);
        setBlock(implementationArguments, scope, arguments, rot);
        return null;
    }
    
    @Override
    public SourcePosition getPosition() {
        return position;
    }
    
    @Override
    public ReturnType returnType() {
        return ReturnType.VOID;
    }
    
    void setBlock(ImplementationArguments implementationArguments, Scope scope,
                  TerraImplementationArguments arguments, BlockState rot) {
        Vector2 xz = RotationUtil.rotateVector(Vector2.of(x.invoke(implementationArguments, scope).doubleValue(),
                                                          z.invoke(implementationArguments, scope).doubleValue()), arguments.getRotation());
        try {
            Vector3.Mutable set = Vector3.of(FastMath.roundToInt(xz.getX()),
                                             y.invoke(implementationArguments, scope).doubleValue(),
                                             FastMath.roundToInt(xz.getZ())).mutable().add(arguments.getOrigin());
            BlockState current = arguments.getWorld().getBlockState(set);
            if(overwrite.invoke(implementationArguments, scope) || current.isAir()) {
                arguments.getWorld().setBlockState(set, rot);
            }
        } catch(RuntimeException e) {
            logger.error("Failed to place block at location {}", arguments.getOrigin(), e);
        }
    }
    
    protected BlockState getBlockState(ImplementationArguments arguments, Scope scope) {
        return data.computeIfAbsent(blockData.invoke(arguments, scope), platform.getWorldHandle()::createBlockState);
    }
    
    
    public static class Constant extends BlockFunction {
        private final BlockState state;
        
        public Constant(Expression<Number> x, Expression<Number> y, Expression<Number> z, StringConstant blockData,
                        Expression<Boolean> overwrite, Platform platform, SourcePosition position) {
            super(x, y, z, blockData, overwrite, platform, position);
            this.state = platform.getWorldHandle().createBlockState(blockData.getConstant());
        }
        
        @Override
        protected BlockState getBlockState(ImplementationArguments arguments, Scope scope) {
            return state;
        }
    }
}
