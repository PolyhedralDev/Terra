/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.transform;


import java.util.Objects;

import com.dfsek.terra.api.transform.exception.TransformException;


public interface Validator<T> {
    static <T> Validator<T> notNull() {
        return Objects::nonNull;
    }
    
    boolean validate(T value) throws TransformException;
}
