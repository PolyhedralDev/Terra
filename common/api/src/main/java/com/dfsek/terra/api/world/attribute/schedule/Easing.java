package com.dfsek.terra.api.world.attribute.schedule;

import java.util.Set;


public sealed interface Easing {
    Set<String> NAMES = Set.of(
        "constant", "linear",
        "in_back", "in_bounce", "in_circ", "in_cubic", "in_elastic", "in_expo", "in_quad", "in_quart", "in_quint", "in_sine",
        "in_out_back", "in_out_bounce", "in_out_circ", "in_out_cubic", "in_out_elastic", "in_out_expo", "in_out_quad",
        "in_out_quart", "in_out_quint", "in_out_sine",
        "out_back", "out_bounce", "out_circ", "out_cubic", "out_elastic", "out_expo", "out_quad", "out_quart", "out_quint",
        "out_sine");


    record Named(String name) implements Easing {
        public Named {
            if(!NAMES.contains(name)) throw new IllegalArgumentException("unknown easing \"" + name + "\"");
        }
    }


    record CubicBezier(float x1, float y1, float x2, float y2) implements Easing {
        public CubicBezier {
            if(!(x1 >= 0f && x1 <= 1f)) throw new IllegalArgumentException("cubic_bezier x1 is outside [0, 1]");
            if(!(x2 >= 0f && x2 <= 1f)) throw new IllegalArgumentException("cubic_bezier x2 is outside [0, 1]");
        }
    }
}
