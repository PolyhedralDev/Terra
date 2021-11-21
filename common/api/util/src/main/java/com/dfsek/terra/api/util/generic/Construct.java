/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.util.generic;

import java.util.function.Supplier;


public final class Construct {
    public static <T> T construct(Supplier<T> in) {
        return in.get();
    }
}
