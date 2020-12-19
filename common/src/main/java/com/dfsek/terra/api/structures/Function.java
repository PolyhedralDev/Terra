package com.dfsek.terra.api.structures;

import java.util.List;

public interface Function {
    void apply();

    String name();

    List<Argument> getArguments();
}
