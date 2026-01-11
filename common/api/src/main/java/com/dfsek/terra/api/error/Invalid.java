package com.dfsek.terra.api.error;

import com.dfsek.terra.api.util.generic.data.types.Either;


public interface Invalid {
    String message();

    default <T> Either<Invalid, T> left() {
        return Either.left(this);
    }

    default IllegalArgumentException toIllegal() {
        return new IllegalArgumentException(message());
    }
}
