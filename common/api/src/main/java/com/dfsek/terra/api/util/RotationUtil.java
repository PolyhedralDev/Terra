/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.util;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.properties.base.BooleanProperty;
import com.dfsek.terra.api.block.state.properties.base.EnumProperty;
import com.dfsek.terra.api.block.state.properties.base.Properties;
import com.dfsek.terra.api.block.state.properties.enums.Axis;
import com.dfsek.terra.api.block.state.properties.enums.RailShape;
import com.dfsek.terra.api.block.state.properties.enums.RedstoneConnection;
import com.dfsek.terra.api.block.state.properties.enums.WallHeight;
import com.dfsek.terra.api.util.vector.Vector2;
import com.dfsek.terra.api.util.vector.Vector2.Mutable;


public final class RotationUtil {
    /**
     * Rotate and mirror a coordinate pair.
     *
     * @param orig Vector to rotate.
     * @param r    Rotation
     *
     * @return Rotated vector
     */
    public static Vector2 rotateVector(Vector2 orig, Rotation r) {
        Mutable copy = orig.mutable();
        switch(r) {
            case CW_90 -> copy.setX(orig.getZ()).setZ(-orig.getX());
            case CCW_90 -> copy.setX(-orig.getZ()).setZ(orig.getX());
            case CW_180 -> copy.multiply(-1);
        }
        return copy.immutable();
    }
    
    
}
