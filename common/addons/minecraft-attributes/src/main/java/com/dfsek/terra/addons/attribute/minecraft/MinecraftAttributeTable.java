package com.dfsek.terra.addons.attribute.minecraft;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;

import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.registry.key.TagKey;
import com.dfsek.terra.api.util.generic.either.Either;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.AttributeKey;
import com.dfsek.terra.api.world.attribute.AttributeType;
import com.dfsek.terra.api.world.attribute.AttributeTypes;
import com.dfsek.terra.api.world.attribute.RawValue;
import com.dfsek.terra.api.world.attribute.value.Color;
import com.dfsek.terra.api.world.attribute.value.TriState;


public final class MinecraftAttributeTable {
    private static final RawValue NO_SLEEP_MESSAGE = new RawValue(Map.of("translate", "block.minecraft.bed.no_sleep"));

    private static final BedRule CAN_SLEEP_WHEN_DARK = new BedRule(BedRule.Rule.WHEN_DARK, BedRule.Rule.ALWAYS, false, false,
        Optional.of(NO_SLEEP_MESSAGE));

    private static final BedRule DESTROY_ON_LEAVE = new BedRule(BedRule.Rule.WHEN_DARK, BedRule.Rule.NEVER, false, true,
        Optional.of(NO_SLEEP_MESSAGE));

    private static final IntProviderValue MONSTER_SPAWN_LIGHT_LEVEL = new IntProviderValue(OptionalInt.empty(), Optional.of(
        new RawValue(Map.of("type", "minecraft:uniform", "min_inclusive", 0, "max_inclusive", 7))));

    private MinecraftAttributeTable() { }

    public static List<Attribute<?>> create(AttributeHandle handle) {
        AttributeTypes t = handle.types();
        MinecraftAttributeTypes m = new MinecraftAttributeTypes(handle);
        AttributeType<TemperatureModifier> temperatureModifier = t.enumType("temperature_modifier", TemperatureModifier.class);
        AttributeType<GrassColorModifier> grassColorModifier = t.enumType("grass_color_modifier", GrassColorModifier.class);
        AttributeType<Skybox> skybox = t.enumType("skybox", Skybox.class);
        AttributeType<CardinalLight> cardinalLight = t.enumType("cardinal_light", CardinalLight.class);
        Table table = new Table(handle);

        table.row(MinecraftAttributes.VISUAL_FOG_COLOR, t.rgbColor(), Color.rgb(0x000000));
        table.row(MinecraftAttributes.VISUAL_FOG_START_DISTANCE, t.floatType(), 0f);
        table.row(MinecraftAttributes.VISUAL_FOG_END_DISTANCE, t.floatType(), 1024f).range(0, Double.POSITIVE_INFINITY);
        table.row(MinecraftAttributes.VISUAL_SKY_FOG_END_DISTANCE, t.floatType(), 512f).range(0, Double.POSITIVE_INFINITY);
        table.row(MinecraftAttributes.VISUAL_CLOUD_FOG_END_DISTANCE, t.floatType(), 2048f).range(0, Double.POSITIVE_INFINITY);
        table.row(MinecraftAttributes.VISUAL_WATER_FOG_COLOR, t.rgbColor(), Color.rgb(0x050533));
        table.row(MinecraftAttributes.VISUAL_WATER_FOG_START_DISTANCE, t.floatType(), -8f);
        table.row(MinecraftAttributes.VISUAL_WATER_FOG_END_DISTANCE, t.floatType(), 96f).range(0, Double.POSITIVE_INFINITY);
        table.row(MinecraftAttributes.VISUAL_SKY_COLOR, t.rgbColor(), Color.rgb(0x000000));
        table.row(MinecraftAttributes.VISUAL_SUNRISE_SUNSET_COLOR, t.argbColor(), Color.argb(0));
        table.row(MinecraftAttributes.VISUAL_CLOUD_COLOR, t.argbColor(), Color.argb(0));
        table.row(MinecraftAttributes.VISUAL_CLOUD_HEIGHT, t.floatType(), 192.33f);
        table.row(MinecraftAttributes.VISUAL_SUN_ANGLE, t.angleDegrees(), 0f);
        table.row(MinecraftAttributes.VISUAL_MOON_ANGLE, t.angleDegrees(), 0f);
        table.row(MinecraftAttributes.VISUAL_STAR_ANGLE, t.angleDegrees(), 0f);
        table.row(MinecraftAttributes.VISUAL_MOON_PHASE, m.moonPhase(), MoonPhase.FULL_MOON);
        table.row(MinecraftAttributes.VISUAL_STAR_BRIGHTNESS, t.floatType(), 0f).range(0, 1);
        table.row(MinecraftAttributes.VISUAL_BLOCK_LIGHT_TINT, t.rgbColor(), Color.rgb(0xffd88c));
        table.row(MinecraftAttributes.VISUAL_SKY_LIGHT_COLOR, t.rgbColor(), Color.rgb(0xffffff));
        table.row(MinecraftAttributes.VISUAL_SKY_LIGHT_FACTOR, t.floatType(), 1f).range(0, 1);
        table.row(MinecraftAttributes.VISUAL_NIGHT_VISION_COLOR, t.rgbColor(), Color.rgb(0x999999));
        table.row(MinecraftAttributes.VISUAL_AMBIENT_LIGHT_COLOR, t.rgbColor(), Color.rgb(0x000000));
        table.row(MinecraftAttributes.VISUAL_DEFAULT_DRIPSTONE_PARTICLE, m.particle(),
            new RawValue(Map.of("type", "minecraft:dripping_dripstone_water")));
        table.row(MinecraftAttributes.VISUAL_AMBIENT_PARTICLES, m.ambientParticles(), List.of());
        table.row(MinecraftAttributes.AUDIO_BACKGROUND_MUSIC, m.backgroundMusic(), BackgroundMusic.EMPTY);
        table.row(MinecraftAttributes.AUDIO_MUSIC_VOLUME, t.floatType(), 1f).range(0, 1);
        table.row(MinecraftAttributes.AUDIO_AMBIENT_SOUNDS, m.ambientSounds(), AmbientSounds.EMPTY);
        table.row(MinecraftAttributes.AUDIO_FIREFLY_BUSH_SOUNDS, t.booleanType(), false);
        table.row(MinecraftAttributes.GAMEPLAY_SKY_LIGHT_LEVEL, t.floatType(), 15f).range(0, 15).packOnly();
        table.row(MinecraftAttributes.GAMEPLAY_CAN_START_RAID, t.booleanType(), true);
        table.row(MinecraftAttributes.GAMEPLAY_WATER_EVAPORATES, t.booleanType(), false);
        table.row(MinecraftAttributes.GAMEPLAY_BED_RULE, m.bedRule(), CAN_SLEEP_WHEN_DARK);
        table.row(MinecraftAttributes.GAMEPLAY_STRAW_BED_RULE, m.bedRule(), DESTROY_ON_LEAVE);
        table.row(MinecraftAttributes.GAMEPLAY_RESPAWN_ANCHOR_WORKS, t.booleanType(), false);
        table.row(MinecraftAttributes.GAMEPLAY_NETHER_PORTAL_SPAWNS_PIGLIN, t.booleanType(), false);
        table.row(MinecraftAttributes.GAMEPLAY_FAST_LAVA, t.booleanType(), false).packOnly();
        table.row(MinecraftAttributes.GAMEPLAY_INCREASED_FIRE_BURNOUT, t.booleanType(), false);
        table.row(MinecraftAttributes.GAMEPLAY_EYEBLOSSOM_OPEN, t.triState(), TriState.DEFAULT);
        table.row(MinecraftAttributes.GAMEPLAY_TURTLE_EGG_HATCH_CHANCE, t.floatType(), 0.002f).range(0, 1);
        table.row(MinecraftAttributes.GAMEPLAY_PIGLINS_ZOMBIFY, t.booleanType(), true);
        table.row(MinecraftAttributes.GAMEPLAY_SNOW_GOLEM_MELTS, t.booleanType(), false);
        table.row(MinecraftAttributes.GAMEPLAY_CREAKING_ACTIVE, t.booleanType(), false);
        table.row(MinecraftAttributes.GAMEPLAY_SURFACE_SLIME_SPAWN_CHANCE, t.floatType(), 0f).range(0, 1);
        table.row(MinecraftAttributes.GAMEPLAY_CAT_WAKING_UP_GIFT_CHANCE, t.floatType(), 0f).range(0, 1);
        table.row(MinecraftAttributes.GAMEPLAY_BEES_STAY_IN_HIVE, t.booleanType(), false);
        table.row(MinecraftAttributes.GAMEPLAY_MONSTERS_BURN, t.booleanType(), false);
        table.row(MinecraftAttributes.GAMEPLAY_CAN_PILLAGER_PATROL_SPAWN, t.booleanType(), true);
        table.row(MinecraftAttributes.GAMEPLAY_NATURAL_MOB_SPAWNS, m.mobSpawnSettings(), MobSpawnSettings.EMPTY);
        table.row(MinecraftAttributes.GAMEPLAY_CREATURE_WORLD_GEN_SPAWN_PROBABILITY, t.floatType(), 0.1f).range(0, 0.9999999);
        table.row(MinecraftAttributes.GAMEPLAY_VILLAGER_ACTIVITY, m.identifier(), RegistryKey.parse("minecraft:idle"));
        table.row(MinecraftAttributes.GAMEPLAY_BABY_VILLAGER_ACTIVITY, m.identifier(), RegistryKey.parse("minecraft:idle"));

        table.row(MinecraftAttributes.BIOME_HAS_PRECIPITATION, t.booleanType(), true);
        table.row(MinecraftAttributes.BIOME_TEMPERATURE, t.floatType(), 0.5f);
        table.row(MinecraftAttributes.BIOME_TEMPERATURE_MODIFIER, temperatureModifier, TemperatureModifier.NONE);
        table.row(MinecraftAttributes.BIOME_DOWNFALL, t.floatType(), 0.5f);
        table.row(MinecraftAttributes.BIOME_WATER_COLOR, t.rgbColor(), Color.rgb(0x3f76e4));
        table.row(MinecraftAttributes.BIOME_FOLIAGE_COLOR, t.rgbColor(), null);
        table.row(MinecraftAttributes.BIOME_DRY_FOLIAGE_COLOR, t.rgbColor(), null);
        table.row(MinecraftAttributes.BIOME_GRASS_COLOR, t.rgbColor(), null);
        table.row(MinecraftAttributes.BIOME_GRASS_COLOR_MODIFIER, grassColorModifier, GrassColorModifier.NONE);
        table.row(MinecraftAttributes.BIOME_VILLAGER_TYPE, m.identifier(), RegistryKey.parse("minecraft:plains"));
        table.row(MinecraftAttributes.DIMENSION_HAS_FIXED_TIME, t.booleanType(), false).packOnly();
        table.row(MinecraftAttributes.DIMENSION_HAS_SKYLIGHT, t.booleanType(), true).packOnly();
        table.row(MinecraftAttributes.DIMENSION_HAS_CEILING, t.booleanType(), false).packOnly();
        table.row(MinecraftAttributes.DIMENSION_HAS_ENDER_DRAGON_FIGHT, t.booleanType(), false).packOnly();
        table.row(MinecraftAttributes.DIMENSION_COORDINATE_SCALE, t.floatType(), 1f).packOnly().range(1e-5, 3e7);
        table.row(MinecraftAttributes.DIMENSION_MIN_Y, t.integerOverrideOnly(), -64).packOnly().range(-2032, 2031).multipleOf(16);
        table.row(MinecraftAttributes.DIMENSION_HEIGHT, t.integerOverrideOnly(), 384).packOnly().range(16, 4064).multipleOf(16);
        table.row(MinecraftAttributes.DIMENSION_LOGICAL_HEIGHT, t.integerOverrideOnly(), 384).packOnly().range(0, 4064);
        table.row(MinecraftAttributes.DIMENSION_INFINIBURN, m.identifierOrTag(),
            Either.right(TagKey.parse("#minecraft:infiniburn_overworld"))).packOnly();
        table.row(MinecraftAttributes.DIMENSION_AMBIENT_LIGHT, t.floatType(), 0f).packOnly();
        table.row(MinecraftAttributes.DIMENSION_MONSTER_SPAWN_LIGHT_LEVEL, m.intProvider(), MONSTER_SPAWN_LIGHT_LEVEL).packOnly();
        table.row(MinecraftAttributes.DIMENSION_MONSTER_SPAWN_BLOCK_LIGHT_LIMIT, t.integer(), 0).packOnly().range(0, 15);
        table.row(MinecraftAttributes.DIMENSION_SKYBOX, skybox, Skybox.OVERWORLD).packOnly();
        table.row(MinecraftAttributes.DIMENSION_CARDINAL_LIGHT, cardinalLight, CardinalLight.DEFAULT).packOnly();
        table.row(MinecraftAttributes.DIMENSION_DEFAULT_CLOCK, m.identifier(), RegistryKey.parse("minecraft:overworld")).packOnly();
        table.row(MinecraftAttributes.WORLD_SEA_LEVEL, t.integer(), 63).packOnly();
        table.row(MinecraftAttributes.WORLD_SPAWN_HEIGHT, t.integer(), 64).packOnly();
        table.row(MinecraftAttributes.WORLD_MOB_GENERATION, t.booleanType(), true).packOnly();

        List<Attribute<?>> attributes = table.build();
        if(!attributes.stream().map(Attribute::key).toList().equals(MinecraftAttributes.all())) {
            throw new IllegalStateException("the Minecraft attribute table does not match MinecraftAttributes.all()");
        }
        return attributes;
    }


    private static final class Table {
        private final AttributeHandle handle;
        private final List<Attribute.Builder<?>> rows = new ArrayList<>();

        private Table(AttributeHandle handle) {
            this.handle = handle;
        }

        private <T> Attribute.Builder<T> row(AttributeKey<T> key, AttributeType<T> type, @Nullable T defaultValue) {
            MinecraftIds.requireLowerCase(key.getRegistryKey());
            Attribute.Builder<T> builder = handle.attributeBuilder(key, type, defaultValue);
            rows.add(builder);
            return builder;
        }

        private List<Attribute<?>> build() {
            List<Attribute<?>> built = new ArrayList<>(rows.size());
            for(Attribute.Builder<?> row : rows) built.add(row.build());
            return List.copyOf(built);
        }
    }
}
