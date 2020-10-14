package com.dfsek.terra.biome.failsafe;

import com.dfsek.terra.generation.UserDefinedDecorator;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.world.Flora;

public final class FailoverDecorator extends UserDefinedDecorator {
    public FailoverDecorator() {
        super(new ProbabilityCollection<>(), new ProbabilityCollection<>(), 0, 0);
    }
}
