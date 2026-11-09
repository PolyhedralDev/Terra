package com.dfsek.terra.mod.attribute;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.attribute.EnvironmentAttribute;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeKey;


public final class PackAttributeTable {
    public static final PackAttributeTable EMPTY = new PackAttributeTable(List.of(), List.of(), List.of(), List.of(), null, null, null,
                                                                          null, List.of());

    private static final String MINECRAFT = "minecraft";
    private static final String TERRA = "terra";
    private static final String BIOME = "biome/";
    private static final String DIMENSION = "dimension/";
    private static final Set<String> CLIMATE_FIELDS = Set.of("has_precipitation", "temperature", "temperature_modifier", "downfall");
    private static final Set<String> EFFECTS_FIELDS = Set.of("water_color", "foliage_color", "dry_foliage_color", "grass_color",
                                                             "grass_color_modifier");
    private static final RegistryKey VILLAGER_TYPE = AttributeKey.parse("terra:biome/villager_type").getRegistryKey();
    private static final RegistryKey SEA_LEVEL = AttributeKey.parse("terra:world/sea_level").getRegistryKey();
    private static final RegistryKey SPAWN_HEIGHT = AttributeKey.parse("terra:world/spawn_height").getRegistryKey();
    private static final RegistryKey MOB_GENERATION = AttributeKey.parse("terra:world/mob_generation").getRegistryKey();

    private final List<Bound> environment;
    private final List<Field> climate;
    private final List<Field> effects;
    private final List<Field> dimension;
    private final @Nullable Attribute<?> villagerType;
    private final @Nullable Attribute<?> seaLevel;
    private final @Nullable Attribute<?> spawnHeight;
    private final @Nullable Attribute<?> mobGeneration;
    private final List<Attribute<?>> unapplied;

    private PackAttributeTable(List<Bound> environment, List<Field> climate, List<Field> effects, List<Field> dimension,
                               @Nullable Attribute<?> villagerType, @Nullable Attribute<?> seaLevel, @Nullable Attribute<?> spawnHeight,
                               @Nullable Attribute<?> mobGeneration, List<Attribute<?>> unapplied) {
        this.environment = environment;
        this.climate = climate;
        this.effects = effects;
        this.dimension = dimension;
        this.villagerType = villagerType;
        this.seaLevel = seaLevel;
        this.spawnHeight = spawnHeight;
        this.mobGeneration = mobGeneration;
        this.unapplied = unapplied;
    }

    public static PackAttributeTable of(@Nullable Registry<Attribute<?>> registry) {
        if(registry == null) return EMPTY;
        Builder builder = new Builder();
        registry.forEach(builder::add);
        return builder.build();
    }

    public List<Bound> environment() {
        return environment;
    }

    public List<Field> climate() {
        return climate;
    }

    public List<Field> effects() {
        return effects;
    }

    public List<Field> dimension() {
        return dimension;
    }

    public @Nullable Attribute<?> villagerType() {
        return villagerType;
    }

    public @Nullable Attribute<?> seaLevel() {
        return seaLevel;
    }

    public @Nullable Attribute<?> spawnHeight() {
        return spawnHeight;
    }

    public @Nullable Attribute<?> mobGeneration() {
        return mobGeneration;
    }

    public List<Attribute<?>> unapplied() {
        return unapplied;
    }


    public record Bound(Attribute<?> attribute, EnvironmentAttribute<?> vanilla) { }


    public record Field(Attribute<?> attribute, String name) { }


    private static final class Builder {
        private final List<Bound> environment = new ArrayList<>();
        private final List<Field> climate = new ArrayList<>();
        private final List<Field> effects = new ArrayList<>();
        private final List<Field> dimension = new ArrayList<>();
        private final List<Attribute<?>> unapplied = new ArrayList<>();
        private @Nullable Attribute<?> villagerType;
        private @Nullable Attribute<?> seaLevel;
        private @Nullable Attribute<?> spawnHeight;
        private @Nullable Attribute<?> mobGeneration;

        private void add(RegistryKey key, Attribute<?> attribute) {
            String namespace = key.getNamespace();
            String path = key.getID();
            if(namespace.equals(MINECRAFT)) {
                // tryBuild returns null for the upper case Terra allows.
                Identifier id = Identifier.tryBuild(namespace, path);
                EnvironmentAttribute<?> vanilla = id == null ? null : BuiltInRegistries.ENVIRONMENT_ATTRIBUTE.getValue(id);
                if(vanilla != null) {
                    environment.add(new Bound(attribute, vanilla));
                    return;
                }
            } else if(namespace.equals(TERRA)) {
                if(key.equals(VILLAGER_TYPE)) {
                    villagerType = attribute;
                    return;
                } else if(key.equals(SEA_LEVEL)) {
                    seaLevel = attribute;
                    return;
                } else if(key.equals(SPAWN_HEIGHT)) {
                    spawnHeight = attribute;
                    return;
                } else if(key.equals(MOB_GENERATION)) {
                    mobGeneration = attribute;
                    return;
                } else if(path.startsWith(BIOME)) {
                    String name = path.substring(BIOME.length());
                    if(CLIMATE_FIELDS.contains(name)) {
                        climate.add(new Field(attribute, name));
                        return;
                    } else if(EFFECTS_FIELDS.contains(name)) {
                        effects.add(new Field(attribute, name));
                        return;
                    }
                } else if(path.startsWith(DIMENSION) && path.length() > DIMENSION.length()) {
                    dimension.add(new Field(attribute, path.substring(DIMENSION.length())));
                    return;
                }
            }
            unapplied.add(attribute);
        }

        private PackAttributeTable build() {
            return new PackAttributeTable(List.copyOf(environment), List.copyOf(climate), List.copyOf(effects), List.copyOf(dimension),
                                          villagerType, seaLevel, spawnHeight, mobGeneration, List.copyOf(unapplied));
        }
    }
}
