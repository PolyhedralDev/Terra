package com.dfsek.terra.api.world.attribute;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.world.attribute.schedule.AttributeSchedule;
import com.dfsek.terra.api.world.attribute.schedule.ScheduleDecodeResult;


public interface AttributeHandle {
    @NotNull
    @Contract(pure = true)
    AttributeTypes types();

    /**
     * Applies no config preprocessing.
     */
    @NotNull
    @Contract(pure = true)
    DecodeContext plainContext();

    @NotNull
    @Contract(pure = true)
    AttributeCodecs codecs();

    @NotNull
    @Contract("_, _ -> new")
    <T> AttributeType.Builder<T> typeBuilder(@NotNull String id, @NotNull AttributeCodec<T> codec);

    @NotNull
    @Contract("_, _, _ -> new")
    <T> Attribute.Builder<T> attributeBuilder(@NotNull AttributeKey<T> key, @NotNull AttributeType<T> type, @Nullable T defaultValue);

    /**
     * Only grows. Every {@link Attribute#index()} is below it.
     */
    @ApiStatus.Internal
    int attributeCount();

    /**
     * Applies each non-null layer, bottom first, to the default, then sanitizes. A {@code null} value only changes on an override.
     */
    @SuppressWarnings("unchecked")
    @Contract(pure = true)
    <T> @Nullable T resolve(@NotNull Attribute<T> attribute, AttributeEntry<T>... layersBottomToTop);

    /**
     * Allocation-free {@link #resolve(Attribute, AttributeEntry[])}, bottom layer first.
     */
    @Contract(pure = true)
    <T> @Nullable T resolve(@NotNull Attribute<T> attribute, @Nullable AttributeEntry<T> first, @Nullable AttributeEntry<T> second,
                            @Nullable AttributeEntry<T> third);

    /**
     * Allocation-free {@link #resolve(Attribute, AttributeEntry[])}, bottom layer first.
     */
    @Contract(pure = true)
    <T> @Nullable T resolve(@NotNull Attribute<T> attribute, @Nullable AttributeEntry<T> first, @Nullable AttributeEntry<T> second,
                            @Nullable AttributeEntry<T> third, @Nullable AttributeEntry<T> fourth);

    /**
     * Composes two layers into one entry that applies like {@code lower} then {@code upper}. Empty when both are {@code null}
     * or they don't compose; fall back to an override of the {@link #resolve resolved} value. Float results may differ by rounding.
     */
    @NotNull
    @Contract(pure = true)
    <T> Optional<AttributeEntry<T>> compose(@NotNull Attribute<T> attribute, @Nullable AttributeEntry<T> lower,
                                            @Nullable AttributeEntry<T> upper);

    /**
     * Reads a bare value as an override, or a {@code modifier} and {@code argument} map. Resolves {@code raw} first.
     */
    @NotNull
    <T> AttributeEntry<T> decodeEntry(@NotNull Attribute<T> attribute, @Nullable Object raw, @NotNull DecodeContext context)
    throws DecodeException;

    /**
     * @throws IllegalArgumentException if {@code entry} is an override of {@code null}
     */
    @NotNull
    @Contract(pure = true)
    <T> Object encodeEntry(@NotNull Attribute<T> attribute, @NotNull AttributeEntry<T> entry);

    /**
     * {@code raw} must already be {@link DecodeContext#resolve resolved}. A bad track is dropped; other problems skip the schedule.
     */
    @NotNull
    ScheduleDecodeResult decodeSchedule(@NotNull RegistryKey id, @NotNull Map<String, Object> raw, @NotNull AttributeLookup attributes,
                                        @NotNull DecodeContext context);

    /**
     * @throws IllegalArgumentException if a keyframe's argument is {@code null}
     */
    @NotNull
    @Contract(pure = true)
    Map<String, Object> encodeSchedule(@NotNull AttributeSchedule schedule);

    /**
     * Empty unless the {@code config-attributes} addon loaded {@code pack}.
     */
    @NotNull
    Optional<PackAttributes> pack(@NotNull ConfigPack pack);
}
