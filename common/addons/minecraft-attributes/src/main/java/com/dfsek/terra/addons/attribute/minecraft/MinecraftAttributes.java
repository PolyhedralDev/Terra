package com.dfsek.terra.addons.attribute.minecraft;

import java.util.ArrayList;
import java.util.List;

import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.registry.key.TagKey;
import com.dfsek.terra.api.util.generic.either.Either;
import com.dfsek.terra.api.world.attribute.AttributeKey;
import com.dfsek.terra.api.world.attribute.RawValue;
import com.dfsek.terra.api.world.attribute.value.Color;
import com.dfsek.terra.api.world.attribute.value.TriState;


public final class MinecraftAttributes {
    private static List<AttributeKey<?>> building = new ArrayList<>();

    public static final AttributeKey<Color> VISUAL_FOG_COLOR = vanilla("visual/fog_color");
    public static final AttributeKey<Float> VISUAL_FOG_START_DISTANCE = vanilla("visual/fog_start_distance");
    public static final AttributeKey<Float> VISUAL_FOG_END_DISTANCE = vanilla("visual/fog_end_distance");
    public static final AttributeKey<Float> VISUAL_SKY_FOG_END_DISTANCE = vanilla("visual/sky_fog_end_distance");
    public static final AttributeKey<Float> VISUAL_CLOUD_FOG_END_DISTANCE = vanilla("visual/cloud_fog_end_distance");
    public static final AttributeKey<Color> VISUAL_WATER_FOG_COLOR = vanilla("visual/water_fog_color");
    public static final AttributeKey<Float> VISUAL_WATER_FOG_START_DISTANCE = vanilla("visual/water_fog_start_distance");
    public static final AttributeKey<Float> VISUAL_WATER_FOG_END_DISTANCE = vanilla("visual/water_fog_end_distance");
    public static final AttributeKey<Color> VISUAL_SKY_COLOR = vanilla("visual/sky_color");
    public static final AttributeKey<Color> VISUAL_SUNRISE_SUNSET_COLOR = vanilla("visual/sunrise_sunset_color");
    public static final AttributeKey<Color> VISUAL_CLOUD_COLOR = vanilla("visual/cloud_color");
    public static final AttributeKey<Float> VISUAL_CLOUD_HEIGHT = vanilla("visual/cloud_height");
    public static final AttributeKey<Float> VISUAL_SUN_ANGLE = vanilla("visual/sun_angle");
    public static final AttributeKey<Float> VISUAL_MOON_ANGLE = vanilla("visual/moon_angle");
    public static final AttributeKey<Float> VISUAL_STAR_ANGLE = vanilla("visual/star_angle");
    public static final AttributeKey<MoonPhase> VISUAL_MOON_PHASE = vanilla("visual/moon_phase");
    public static final AttributeKey<Float> VISUAL_STAR_BRIGHTNESS = vanilla("visual/star_brightness");
    public static final AttributeKey<Color> VISUAL_BLOCK_LIGHT_TINT = vanilla("visual/block_light_tint");
    public static final AttributeKey<Color> VISUAL_SKY_LIGHT_COLOR = vanilla("visual/sky_light_color");
    public static final AttributeKey<Float> VISUAL_SKY_LIGHT_FACTOR = vanilla("visual/sky_light_factor");
    public static final AttributeKey<Color> VISUAL_NIGHT_VISION_COLOR = vanilla("visual/night_vision_color");
    public static final AttributeKey<Color> VISUAL_AMBIENT_LIGHT_COLOR = vanilla("visual/ambient_light_color");
    public static final AttributeKey<RawValue> VISUAL_DEFAULT_DRIPSTONE_PARTICLE = vanilla("visual/default_dripstone_particle");
    public static final AttributeKey<List<AmbientParticle>> VISUAL_AMBIENT_PARTICLES = vanilla("visual/ambient_particles");
    public static final AttributeKey<BackgroundMusic> AUDIO_BACKGROUND_MUSIC = vanilla("audio/background_music");
    public static final AttributeKey<Float> AUDIO_MUSIC_VOLUME = vanilla("audio/music_volume");
    public static final AttributeKey<AmbientSounds> AUDIO_AMBIENT_SOUNDS = vanilla("audio/ambient_sounds");
    public static final AttributeKey<Boolean> AUDIO_FIREFLY_BUSH_SOUNDS = vanilla("audio/firefly_bush_sounds");
    public static final AttributeKey<Float> GAMEPLAY_SKY_LIGHT_LEVEL = vanilla("gameplay/sky_light_level");
    public static final AttributeKey<Boolean> GAMEPLAY_CAN_START_RAID = vanilla("gameplay/can_start_raid");
    public static final AttributeKey<Boolean> GAMEPLAY_WATER_EVAPORATES = vanilla("gameplay/water_evaporates");
    public static final AttributeKey<BedRule> GAMEPLAY_BED_RULE = vanilla("gameplay/bed_rule");
    public static final AttributeKey<BedRule> GAMEPLAY_STRAW_BED_RULE = vanilla("gameplay/straw_bed_rule");
    public static final AttributeKey<Boolean> GAMEPLAY_RESPAWN_ANCHOR_WORKS = vanilla("gameplay/respawn_anchor_works");
    public static final AttributeKey<Boolean> GAMEPLAY_NETHER_PORTAL_SPAWNS_PIGLIN = vanilla("gameplay/nether_portal_spawns_piglin");
    public static final AttributeKey<Boolean> GAMEPLAY_FAST_LAVA = vanilla("gameplay/fast_lava");
    public static final AttributeKey<Boolean> GAMEPLAY_INCREASED_FIRE_BURNOUT = vanilla("gameplay/increased_fire_burnout");
    public static final AttributeKey<TriState> GAMEPLAY_EYEBLOSSOM_OPEN = vanilla("gameplay/eyeblossom_open");
    public static final AttributeKey<Float> GAMEPLAY_TURTLE_EGG_HATCH_CHANCE = vanilla("gameplay/turtle_egg_hatch_chance");
    public static final AttributeKey<Boolean> GAMEPLAY_PIGLINS_ZOMBIFY = vanilla("gameplay/piglins_zombify");
    public static final AttributeKey<Boolean> GAMEPLAY_SNOW_GOLEM_MELTS = vanilla("gameplay/snow_golem_melts");
    public static final AttributeKey<Boolean> GAMEPLAY_CREAKING_ACTIVE = vanilla("gameplay/creaking_active");
    public static final AttributeKey<Float> GAMEPLAY_SURFACE_SLIME_SPAWN_CHANCE = vanilla("gameplay/surface_slime_spawn_chance");
    public static final AttributeKey<Float> GAMEPLAY_CAT_WAKING_UP_GIFT_CHANCE = vanilla("gameplay/cat_waking_up_gift_chance");
    public static final AttributeKey<Boolean> GAMEPLAY_BEES_STAY_IN_HIVE = vanilla("gameplay/bees_stay_in_hive");
    public static final AttributeKey<Boolean> GAMEPLAY_MONSTERS_BURN = vanilla("gameplay/monsters_burn");
    public static final AttributeKey<Boolean> GAMEPLAY_CAN_PILLAGER_PATROL_SPAWN = vanilla("gameplay/can_pillager_patrol_spawn");
    public static final AttributeKey<MobSpawnSettings> GAMEPLAY_NATURAL_MOB_SPAWNS = vanilla("gameplay/natural_mob_spawns");
    public static final AttributeKey<Float> GAMEPLAY_CREATURE_WORLD_GEN_SPAWN_PROBABILITY =
        vanilla("gameplay/creature_world_gen_spawn_probability");
    public static final AttributeKey<RegistryKey> GAMEPLAY_VILLAGER_ACTIVITY = vanilla("gameplay/villager_activity");
    public static final AttributeKey<RegistryKey> GAMEPLAY_BABY_VILLAGER_ACTIVITY = vanilla("gameplay/baby_villager_activity");

    public static final AttributeKey<Boolean> BIOME_HAS_PRECIPITATION = terra("biome/has_precipitation");
    public static final AttributeKey<Float> BIOME_TEMPERATURE = terra("biome/temperature");
    public static final AttributeKey<TemperatureModifier> BIOME_TEMPERATURE_MODIFIER = terra("biome/temperature_modifier");
    public static final AttributeKey<Float> BIOME_DOWNFALL = terra("biome/downfall");
    public static final AttributeKey<Color> BIOME_WATER_COLOR = terra("biome/water_color");
    public static final AttributeKey<Color> BIOME_FOLIAGE_COLOR = terra("biome/foliage_color");
    public static final AttributeKey<Color> BIOME_DRY_FOLIAGE_COLOR = terra("biome/dry_foliage_color");
    public static final AttributeKey<Color> BIOME_GRASS_COLOR = terra("biome/grass_color");
    public static final AttributeKey<GrassColorModifier> BIOME_GRASS_COLOR_MODIFIER = terra("biome/grass_color_modifier");
    public static final AttributeKey<RegistryKey> BIOME_VILLAGER_TYPE = terra("biome/villager_type");
    public static final AttributeKey<Boolean> DIMENSION_HAS_FIXED_TIME = terra("dimension/has_fixed_time");
    public static final AttributeKey<Boolean> DIMENSION_HAS_SKYLIGHT = terra("dimension/has_skylight");
    public static final AttributeKey<Boolean> DIMENSION_HAS_CEILING = terra("dimension/has_ceiling");
    public static final AttributeKey<Boolean> DIMENSION_HAS_ENDER_DRAGON_FIGHT = terra("dimension/has_ender_dragon_fight");
    public static final AttributeKey<Float> DIMENSION_COORDINATE_SCALE = terra("dimension/coordinate_scale");
    public static final AttributeKey<Integer> DIMENSION_MIN_Y = terra("dimension/min_y");
    public static final AttributeKey<Integer> DIMENSION_HEIGHT = terra("dimension/height");
    public static final AttributeKey<Integer> DIMENSION_LOGICAL_HEIGHT = terra("dimension/logical_height");
    public static final AttributeKey<Either<RegistryKey, TagKey>> DIMENSION_INFINIBURN = terra("dimension/infiniburn");
    public static final AttributeKey<Float> DIMENSION_AMBIENT_LIGHT = terra("dimension/ambient_light");
    public static final AttributeKey<IntProviderValue> DIMENSION_MONSTER_SPAWN_LIGHT_LEVEL = terra("dimension/monster_spawn_light_level");
    public static final AttributeKey<Integer> DIMENSION_MONSTER_SPAWN_BLOCK_LIGHT_LIMIT =
        terra("dimension/monster_spawn_block_light_limit");
    public static final AttributeKey<Skybox> DIMENSION_SKYBOX = terra("dimension/skybox");
    public static final AttributeKey<CardinalLight> DIMENSION_CARDINAL_LIGHT = terra("dimension/cardinal_light");
    public static final AttributeKey<RegistryKey> DIMENSION_DEFAULT_CLOCK = terra("dimension/default_clock");
    public static final AttributeKey<Integer> WORLD_SEA_LEVEL = terra("world/sea_level");
    public static final AttributeKey<Integer> WORLD_SPAWN_HEIGHT = terra("world/spawn_height");
    public static final AttributeKey<Boolean> WORLD_MOB_GENERATION = terra("world/mob_generation");

    private static final List<AttributeKey<?>> VIEW = seal();

    private MinecraftAttributes() { }

    public static List<AttributeKey<?>> all() {
        return VIEW;
    }

    private static List<AttributeKey<?>> seal() {
        List<AttributeKey<?>> view = List.copyOf(building);
        building = null;
        return view;
    }

    private static <T> AttributeKey<T> vanilla(String path) {
        return add(RegistryKey.of("minecraft", path));
    }

    private static <T> AttributeKey<T> terra(String path) {
        return add(RegistryKey.of("terra", path));
    }

    private static <T> AttributeKey<T> add(RegistryKey key) {
        AttributeKey<T> attributeKey = AttributeKey.of(key);
        building.add(attributeKey);
        return attributeKey;
    }
}
