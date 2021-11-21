/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.transform;

public interface Transformer<F, T> {
    /**
     * Translate data from {@code from} type to {@code to} type.
     *
     * @param from Data to translate
     *
     * @return Result
     */
    T translate(F from);
}
