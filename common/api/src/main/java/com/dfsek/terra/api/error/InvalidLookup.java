package com.dfsek.terra.api.error;

public interface InvalidLookup extends Invalid {
    record NoSuchElement(String message) implements InvalidLookup {

    }

    record AmbiguousKey(String message) implements InvalidLookup {

    }
}
