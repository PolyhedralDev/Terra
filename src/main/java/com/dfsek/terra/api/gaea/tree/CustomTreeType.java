package com.dfsek.terra.api.gaea.tree;

import com.dfsek.terra.api.gaea.tree.fractal.FractalTree;
import com.dfsek.terra.api.gaea.tree.fractal.TreeGetter;
import com.dfsek.terra.api.gaea.tree.fractal.trees.Cactus;
import com.dfsek.terra.api.gaea.tree.fractal.trees.IceSpike;
import com.dfsek.terra.api.gaea.tree.fractal.trees.OakTree;
import com.dfsek.terra.api.gaea.tree.fractal.trees.ShatteredPillar;
import com.dfsek.terra.api.gaea.tree.fractal.trees.ShatteredTree;
import com.dfsek.terra.api.gaea.tree.fractal.trees.SmallShatteredPillar;
import com.dfsek.terra.api.gaea.tree.fractal.trees.SmallShatteredTree;
import com.dfsek.terra.api.gaea.tree.fractal.trees.SpruceTree;
import com.dfsek.terra.api.generic.world.vector.Location;

import java.util.Random;

public enum CustomTreeType implements TreeGetter {
    SHATTERED_SMALL {
        @Override
        public FractalTree getTree(Location l, Random r) {
            return new SmallShatteredTree(l, r);
        }
    },
    SHATTERED_LARGE {
        @Override
        public FractalTree getTree(Location l, Random r) {
            return new ShatteredTree(l, r);
        }
    },
    GIANT_OAK {
        @Override
        public FractalTree getTree(Location l, Random r) {
            return new OakTree(l, r);
        }
    },
    GIANT_SPRUCE {
        @Override
        public FractalTree getTree(Location l, Random r) {
            return new SpruceTree(l, r);
        }
    },
    SMALL_SHATTERED_PILLAR {
        @Override
        public FractalTree getTree(Location l, Random r) {
            return new SmallShatteredPillar(l, r);
        }
    },
    LARGE_SHATTERED_PILLAR {
        @Override
        public FractalTree getTree(Location l, Random r) {
            return new ShatteredPillar(l, r);
        }
    },
    CACTUS {
        @Override
        public FractalTree getTree(Location l, Random r) {
            return new Cactus(l, r);
        }
    },
    ICE_SPIKE {
        @Override
        public FractalTree getTree(Location l, Random r) {
            return new IceSpike(l, r);
        }
    }
}
