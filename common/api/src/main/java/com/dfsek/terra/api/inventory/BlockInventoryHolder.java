/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.inventory;


import com.dfsek.seismic.type.vector.Vector3;


public interface BlockInventoryHolder extends InventoryHolder {
    Vector3 getPosition();
}
