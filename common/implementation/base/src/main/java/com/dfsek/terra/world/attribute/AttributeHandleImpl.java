package com.dfsek.terra.world.attribute;

import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.properties.Context;
import com.dfsek.terra.api.properties.Properties;
import com.dfsek.terra.api.properties.PropertyKey;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeCodec;
import com.dfsek.terra.api.world.attribute.AttributeCodecs;
import com.dfsek.terra.api.world.attribute.AttributeEntry;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.AttributeKey;
import com.dfsek.terra.api.world.attribute.AttributeLookup;
import com.dfsek.terra.api.world.attribute.AttributeType;
import com.dfsek.terra.api.world.attribute.AttributeTypes;
import com.dfsek.terra.api.world.attribute.DecodeContext;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.api.world.attribute.Operation;
import com.dfsek.terra.api.world.attribute.PackAttributes;
import com.dfsek.terra.api.world.attribute.schedule.AttributeSchedule;
import com.dfsek.terra.api.world.attribute.schedule.ScheduleDecodeResult;
import com.dfsek.terra.world.attribute.format.EntryFormat;
import com.dfsek.terra.world.attribute.format.ScheduleFormat;


/**
 * Create one per platform, at construction: {@link Context#create} is not thread safe.
 */
public final class AttributeHandleImpl implements AttributeHandle {
    private final PropertyKey<PackAttributes> packKey = Context.create(PackAttributes.class);
    private final AtomicInteger attributeIndices = new AtomicInteger();

    private static <T extends Properties> Optional<T> read(Context context, PropertyKey<T> key) {
        if(!context.has(key)) return Optional.empty();
        return Optional.ofNullable(context.get(key));
    }

    @Override
    public AttributeTypes types() {
        return AttributeTypesImpl.INSTANCE;
    }

    @Override
    public AttributeCodecs codecs() {
        return AttributeCodecsImpl.INSTANCE;
    }

    @Override
    public DecodeContext plainContext() {
        return PlainDecodeContext.INSTANCE;
    }

    @Override
    public <T> AttributeType.Builder<T> typeBuilder(String id, AttributeCodec<T> codec) {
        return AttributeTypeImpl.builder(id, codec);
    }

    @Override
    public <T> Attribute.Builder<T> attributeBuilder(AttributeKey<T> key, AttributeType<T> type, @Nullable T defaultValue) {
        return AttributeImpl.builder(key, type, defaultValue, attributeIndices);
    }

    @Override
    public int attributeCount() {
        return attributeIndices.get();
    }

    @Override
    @SafeVarargs
    @SuppressWarnings("varargs")
    public final <T> @Nullable T resolve(Attribute<T> attribute, AttributeEntry<T>... layersBottomToTop) {
        return AttributeLayers.resolve(attribute, layersBottomToTop);
    }

    @Override
    public <T> @Nullable T resolve(Attribute<T> attribute, @Nullable AttributeEntry<T> first, @Nullable AttributeEntry<T> second,
                                   @Nullable AttributeEntry<T> third) {
        return AttributeLayers.resolve(attribute, first, second, third, null);
    }

    @Override
    public <T> @Nullable T resolve(Attribute<T> attribute, @Nullable AttributeEntry<T> first, @Nullable AttributeEntry<T> second,
                                   @Nullable AttributeEntry<T> third, @Nullable AttributeEntry<T> fourth) {
        return AttributeLayers.resolve(attribute, first, second, third, fourth);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<AttributeEntry<T>> compose(Attribute<T> attribute, @Nullable AttributeEntry<T> lower,
                                                   @Nullable AttributeEntry<T> upper) {
        if(lower == null) return Optional.ofNullable(upper);
        if(upper == null) return Optional.of(lower);
        if(upper.isOverride()) return Optional.of(upper);
        if(lower.isOverride()) {
            T value = (T) lower.argument();
            if(value == null) return Optional.of(lower);
            return Optional.of(AttributeEntry.override(upper.apply(attribute.type(), value)));
        }
        Operation op = upper.operation();
        if(op != lower.operation()) return Optional.empty();
        return attribute.type().compose(op, lower.argument(), upper.argument()).map(argument -> AttributeEntry.of(op, argument));
    }

    @Override
    public <T> AttributeEntry<T> decodeEntry(Attribute<T> attribute, @Nullable Object raw, DecodeContext context)
    throws DecodeException {
        return EntryFormat.decode(attribute, raw, context);
    }

    @Override
    public <T> Object encodeEntry(Attribute<T> attribute, AttributeEntry<T> entry) {
        return EntryFormat.encode(attribute, entry);
    }

    @Override
    public ScheduleDecodeResult decodeSchedule(RegistryKey id, Map<String, Object> raw, AttributeLookup attributes,
                                               DecodeContext context) {
        return ScheduleFormat.decode(id, raw, attributes, context);
    }

    @Override
    public Map<String, Object> encodeSchedule(AttributeSchedule schedule) {
        return ScheduleFormat.encode(schedule);
    }

    @Override
    public Optional<PackAttributes> pack(ConfigPack pack) {
        return read(pack.getContext(), packKey);
    }
}
