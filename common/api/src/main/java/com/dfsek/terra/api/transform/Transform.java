/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.transform;

import com.dfsek.terra.api.transform.exception.TransformException;


/**
 * Interface to transform data from one type to another.
 */
@FunctionalInterface
public interface Transform<F, T> {
    T transform(F input) throws TransformException;
}
