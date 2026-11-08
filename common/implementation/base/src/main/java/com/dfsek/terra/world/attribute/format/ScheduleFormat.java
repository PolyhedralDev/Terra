package com.dfsek.terra.world.attribute.format;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;

import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeLookup;
import com.dfsek.terra.api.world.attribute.DecodeContext;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.api.world.attribute.Operation;
import com.dfsek.terra.api.world.attribute.schedule.AttributeSchedule;
import com.dfsek.terra.api.world.attribute.schedule.Easing;
import com.dfsek.terra.api.world.attribute.schedule.Keyframe;
import com.dfsek.terra.api.world.attribute.schedule.ScheduleDecodeResult;
import com.dfsek.terra.api.world.attribute.schedule.ScheduleDecodeResult.Problem;
import com.dfsek.terra.api.world.attribute.schedule.ScheduleTrack;
import com.dfsek.terra.api.world.attribute.schedule.TimeMarker;


public final class ScheduleFormat {
    private static final String CLOCK = "clock";
    private static final String PERIOD_TICKS = "period_ticks";
    private static final String TIME_MARKERS = "time_markers";
    private static final String TRACKS = "tracks";
    private static final String TICKS = "ticks";
    private static final String SHOW_IN_COMMANDS = "show_in_commands";
    private static final String MODIFIER = "modifier";
    private static final String EASE = "ease";
    private static final String CUBIC_BEZIER = "cubic_bezier";
    private static final String KEYFRAMES = "keyframes";
    private static final String VALUE = "value";
    private static final Easing LINEAR = new Easing.Named("linear");

    private ScheduleFormat() { }


    private static final class Invalid extends Exception {
        Invalid(String message) {
            super(message, null, false, false);
        }
    }


    private static final class TrackDropped extends Exception {
        TrackDropped(String message) {
            super(message, null, false, false);
        }
    }

    public static ScheduleDecodeResult decode(RegistryKey id, Map<String, Object> raw, AttributeLookup attributes,
                                              DecodeContext context) {
        List<Problem> dropped = new ArrayList<>();
        try {
            AttributeSchedule schedule = readDefinition(raw, attributes, context, dropped);
            return new ScheduleDecodeResult(Optional.of(schedule), dropped, Optional.empty());
        } catch(Invalid e) {
            return new ScheduleDecodeResult(Optional.empty(), dropped, Optional.of(new Problem(id.toString(), e.getMessage())));
        }
    }

    public static Map<String, Object> encode(AttributeSchedule schedule) {
        Map<String, Object> encoded = new LinkedHashMap<>();
        encoded.put(CLOCK, schedule.clock().toString());
        schedule.periodTicks().ifPresent(period -> encoded.put(PERIOD_TICKS, period));
        if(!schedule.tracks().isEmpty()) {
            Map<String, Object> tracks = new LinkedHashMap<>();
            schedule.tracks().forEach((attribute, track) -> tracks.put(attribute.getRegistryKey().toString(), encodeTrack(attribute, track)));
            encoded.put(TRACKS, tracks);
        }
        if(!schedule.timeMarkers().isEmpty()) {
            Map<String, Object> markers = new LinkedHashMap<>();
            schedule.timeMarkers().forEach((key, marker) -> {
                Map<String, Object> encodedMarker = new LinkedHashMap<>();
                encodedMarker.put(TICKS, marker.ticks());
                if(marker.showInCommands()) encodedMarker.put(SHOW_IN_COMMANDS, true);
                markers.put(key.toString(), encodedMarker);
            });
            encoded.put(TIME_MARKERS, markers);
        }
        return encoded;
    }

    private static AttributeSchedule readDefinition(Map<String, Object> raw, AttributeLookup attributes, DecodeContext context,
                                                    List<Problem> dropped) throws Invalid {
        RegistryKey clock = identifier(raw.get(CLOCK), "clock");

        OptionalInt period = OptionalInt.empty();
        if(raw.get(PERIOD_TICKS) != null) {
            int ticks = integer(raw.get(PERIOD_TICKS), "period_ticks", context);
            if(ticks <= 0) throw new Invalid("period_ticks must be positive");
            period = OptionalInt.of(ticks);
        }

        Map<RegistryKey, TimeMarker> markers = new LinkedHashMap<>();
        if(raw.get(TIME_MARKERS) != null) {
            for(Map.Entry<?, ?> entry : map(raw.get(TIME_MARKERS), "time_markers").entrySet()) {
                RegistryKey marker = identifier(entry.getKey(), "time marker key");
                markers.put(marker, marker(marker, entry.getValue(), period, context));
            }
        }

        Map<Attribute<?>, ScheduleTrack<?>> tracks = new LinkedHashMap<>();
        if(raw.get(TRACKS) != null) {
            for(Map.Entry<?, ?> entry : map(raw.get(TRACKS), "tracks").entrySet()) {
                String keyString = String.valueOf(entry.getKey());
                Attribute<?> attribute;
                try {
                    attribute = attributes.find(keyString);
                } catch(DecodeException e) {
                    dropped.add(new Problem(keyString, e.getMessage()));
                    continue;
                }
                try {
                    tracks.put(attribute, track(attribute, map(entry.getValue(), "track " + keyString), period, context));
                } catch(TrackDropped e) {
                    dropped.add(new Problem(keyString, e.getMessage()));
                }
            }
        }
        return new AttributeSchedule(clock, period, tracks, markers);
    }

    private static TimeMarker marker(RegistryKey key, Object value, OptionalInt period, DecodeContext context) throws Invalid {
        int ticks;
        boolean show = false;
        if(value instanceof Map<?, ?> map) {
            if(!map.containsKey(TICKS) || !map.keySet().stream().allMatch(k -> TICKS.equals(k) || SHOW_IN_COMMANDS.equals(k))) {
                throw new Invalid("time marker " + key + " must have \"ticks\" and optionally \"show_in_commands\"");
            }
            ticks = integer(map.get(TICKS), "time marker " + key + " ticks", context);
            Object flag = map.get(SHOW_IN_COMMANDS);
            if(flag != null) {
                if(!(flag instanceof Boolean b)) throw new Invalid("time marker " + key + " show_in_commands must be a boolean");
                show = b;
            }
        } else {
            ticks = integer(value, "time marker " + key, context);
        }
        if(ticks < 0) throw new Invalid("time marker " + key + " ticks must not be negative");
        if(period.isPresent() && ticks >= period.getAsInt()) {
            throw new Invalid("time marker " + key + " ticks " + ticks + " must be less than period_ticks " + period.getAsInt());
        }
        return new TimeMarker(ticks, show);
    }

    private static <T> ScheduleTrack<T> track(Attribute<T> attribute, Map<?, ?> raw, OptionalInt period, DecodeContext context)
    throws Invalid, TrackDropped {
        Operation operation = Operation.OVERRIDE;
        if(raw.get(MODIFIER) != null) {
            try {
                operation = Operation.parse(String.valueOf(raw.get(MODIFIER)));
            } catch(IllegalArgumentException e) {
                throw new Invalid(e.getMessage());
            }
        }
        Easing ease = raw.get(EASE) == null ? LINEAR : easing(raw.get(EASE), context);

        if(!(raw.get(KEYFRAMES) instanceof List<?> frames)) throw new Invalid("keyframes must be a list");
        if(frames.isEmpty()) throw new Invalid("keyframes must not be empty");
        List<Integer> ticksList = new ArrayList<>(frames.size());
        int previous = Integer.MIN_VALUE;
        int sameTick = 0;
        for(Object frame : frames) {
            if(!(frame instanceof Map<?, ?> map) || !map.containsKey(TICKS) || !map.containsKey(VALUE)
               || !map.keySet().stream().allMatch(k -> TICKS.equals(k) || VALUE.equals(k))) {
                throw new Invalid("each keyframe must have exactly \"ticks\" and \"value\"");
            }
            int ticks = integer(map.get(TICKS), "keyframe ticks", context);
            if(ticks < 0) throw new Invalid("keyframe ticks must not be negative");
            if(ticks < previous) throw new Invalid("keyframes must be sorted by ticks");
            sameTick = ticks == previous ? sameTick + 1 : 1;
            if(sameTick > 2) throw new Invalid("at most 2 keyframes may share tick " + ticks);
            if(period.isPresent() && ticks > period.getAsInt()) {
                throw new Invalid("keyframe ticks " + ticks + " must be within period_ticks " + period.getAsInt());
            }
            previous = ticks;
            ticksList.add(ticks);
        }

        // From here on, a failure drops only this track.
        if(!attribute.type().operations().contains(operation)) {
            throw new TrackDropped("modifier " + operation.getID() + " is not valid for " + attribute.key());
        }
        List<Keyframe> decoded = new ArrayList<>(frames.size());
        for(int i = 0; i < frames.size(); i++) {
            Object rawValue = ((Map<?, ?>) frames.get(i)).get(VALUE);
            decoded.add(new Keyframe(ticksList.get(i), argument(attribute, operation, rawValue, context)));
        }
        return new ScheduleTrack<>(operation, decoded, ease);
    }

    private static <T> Object argument(Attribute<T> attribute, Operation operation, Object raw, DecodeContext context)
    throws TrackDropped {
        try {
            if(raw == null) throw new DecodeException("missing keyframe value");
            Object argument = attribute.type().decodeArgument(operation, raw, context);
            if(operation == Operation.OVERRIDE) {
                @SuppressWarnings("unchecked")
                T value = (T) argument;
                attribute.validate(value);
            }
            return argument;
        } catch(DecodeException | RuntimeException e) {
            throw new TrackDropped(e.getMessage() == null ? "unexpected error" : e.getMessage());
        }
    }

    private static Easing easing(Object raw, DecodeContext context) throws Invalid {
        if(raw instanceof String name) {
            try {
                return new Easing.Named(name);
            } catch(IllegalArgumentException e) {
                throw new Invalid(e.getMessage());
            }
        }
        if(raw instanceof Map<?, ?> map && map.size() == 1 && map.containsKey(CUBIC_BEZIER)
           && map.get(CUBIC_BEZIER) instanceof List<?> list && list.size() == 4) {
            float[] v = new float[4];
            for(int i = 0; i < 4; i++) {
                Object element = list.get(i);
                float f;
                if(element instanceof Number n) {
                    f = n.floatValue();
                } else if(element instanceof String) {
                    try {
                        f = context.<Float>load(Float.class, element);
                    } catch(DecodeException e) {
                        throw new Invalid("cubic_bezier must be a list of 4 numbers: " + e.getMessage());
                    }
                } else {
                    throw new Invalid("cubic_bezier must be a list of 4 numbers");
                }
                if(!Float.isFinite(f)) throw new Invalid("cubic_bezier must be a list of 4 numbers");
                v[i] = f;
            }
            try {
                return new Easing.CubicBezier(v[0], v[1], v[2], v[3]);
            } catch(IllegalArgumentException e) {
                throw new Invalid(e.getMessage());
            }
        }
        throw new Invalid("ease must be an easing name or {cubic_bezier: [x1, y1, x2, y2]}");
    }

    private static RegistryKey identifier(Object raw, String what) throws Invalid {
        if(!(raw instanceof String string)) throw new Invalid(what + " must be an identifier string");
        if(string.startsWith("#")) throw new Invalid(what + " must not be a tag");
        try {
            return RegistryKey.parseNamespaced(string);
        } catch(IllegalArgumentException e) {
            throw new Invalid(what + ": " + e.getMessage());
        }
    }

    private static int integer(Object raw, String what, DecodeContext context) throws Invalid {
        try {
            return context.<Integer>load(Integer.class, raw);
        } catch(DecodeException e) {
            throw new Invalid(what + " must be an integer: " + e.getMessage());
        }
    }

    private static Map<?, ?> map(Object raw, String what) throws Invalid {
        if(raw instanceof Map<?, ?> map) return map;
        throw new Invalid(what + " must be a map");
    }

    private static Map<String, Object> encodeTrack(Attribute<?> attribute, ScheduleTrack<?> track) {
        Map<String, Object> encoded = new LinkedHashMap<>();
        Operation operation = track.operation();
        if(operation != Operation.OVERRIDE) encoded.put(MODIFIER, operation.getID());
        if(!track.ease().equals(LINEAR)) encoded.put(EASE, encodeEasing(track.ease()));
        List<Object> keyframes = new ArrayList<>(track.keyframes().size());
        for(int i = 0; i < track.keyframes().size(); i++) {
            Keyframe keyframe = track.keyframes().get(i);
            if(keyframe.argument() == null) {
                throw new IllegalArgumentException("keyframe " + i + " of " + attribute.key() + ": a null value has no pack-format form");
            }
            Map<String, Object> frame = new LinkedHashMap<>();
            frame.put(TICKS, keyframe.ticks());
            frame.put(VALUE, attribute.type().encodeArgument(operation, keyframe.argument()));
            keyframes.add(frame);
        }
        encoded.put(KEYFRAMES, keyframes);
        return encoded;
    }

    private static Object encodeEasing(Easing ease) {
        return switch(ease) {
            case Easing.Named named -> named.name();
            case Easing.CubicBezier bezier -> Map.of(CUBIC_BEZIER, List.of(bezier.x1(), bezier.y1(), bezier.x2(), bezier.y2()));
        };
    }
}
