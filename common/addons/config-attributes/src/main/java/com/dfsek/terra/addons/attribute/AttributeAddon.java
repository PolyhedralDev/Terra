package com.dfsek.terra.addons.attribute;

import org.slf4j.Logger;
import org.slf4j.helpers.MessageFormatter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.dfsek.terra.addons.attribute.AttributeReader.Problem;
import com.dfsek.terra.addons.attribute.AttributeReader.Result;
import com.dfsek.terra.addons.attribute.AttributeReader.Scope;
import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.event.events.config.ConfigurationLoadEvent;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPostLoadEvent;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.properties.Context;
import com.dfsek.terra.api.properties.PropertyKey;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.registry.key.TagKey;
import com.dfsek.terra.api.util.generic.either.Either;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.AttributeMap;
import com.dfsek.terra.api.world.attribute.DecodeContext;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.api.world.attribute.PackAttributes;
import com.dfsek.terra.api.world.attribute.schedule.AttributeSchedule;
import com.dfsek.terra.api.world.attribute.schedule.ScheduleDecodeResult;
import com.dfsek.terra.api.world.biome.Biome;


/**
 * Biome configs load in parallel: the biome handler may only read shared immutable state and write to its own biome.
 */
public class AttributeAddon implements AddonInitializer {
    private static final String ATTRIBUTE_WARNING = "Pack \"{}\", config \"{}\": attribute \"{}\" skipped: {}";
    private static final String SCHEDULE_SKIPPED = "Pack \"{}\": schedule \"{}\" skipped: {}";
    private static final String TRACK_DROPPED = "Pack \"{}\": schedule \"{}\" track \"{}\" dropped: {}";
    private static final String PARENT_WARNING = "Pack \"{}\", config \"{}\": \"{}\" ignored: {}";
    private static final String PACK_CONFIG = "pack.yml";
    private static final String VANILLA = "vanilla";
    private static final String VANILLA_GENERATION = "vanilla-generation";

    @Inject
    private Platform platform;

    @Inject
    private BaseAddon addon;

    @Inject
    private Logger logger;

    @Override
    public void initialize() {
        AttributeHandle handle = platform.getAttributeHandle();
        // Before any parallel load: Context.create is not thread safe.
        PropertyKey<BiomeAttributeEntries> biomeKey = Context.create(BiomeAttributeEntries.class);
        PropertyKey<PackAttributes> packKey = Context.create(PackAttributes.class);

        platform.getEventManager()
            .getHandler(FunctionalEventHandler.class)
            .register(addon, ConfigPackPreLoadEvent.class)
            .then(event -> {
                for(Class<?> type : ConfigDecodeContext.CAPTURED_TYPES) event.getPack().applyLoader(type, LazyConfigValue.LOADER);
            })
            .failThrough();

        platform.getEventManager()
            .getHandler(FunctionalEventHandler.class)
            .register(addon, ConfigurationLoadEvent.class)
            .then(event -> {
                if(!event.is(Biome.class)) return;
                Map<String, LazyConfigValue> raw = event.load(new BiomeAttributesTemplate()).getAttributes();
                AttributeReader reader = new AttributeReader(event.getPack().getRegistry(Attribute.REGISTRY_TYPE), handle);
                Result result = reader.readConfig(raw, Scope.BIOME);
                List<String> warnings = new ArrayList<>();
                for(Problem problem : result.problems()) {
                    warnings.add(warn(ATTRIBUTE_WARNING, event.getPack(), event.getConfiguration().getID(), problem.key(),
                        problem.reason()));
                }
                event.getLoadedObject(Biome.class).getContext().put(biomeKey, new BiomeAttributeEntries(result.map(), warnings));
            })
            .failThrough();

        platform.getEventManager()
            .getHandler(FunctionalEventHandler.class)
            .register(addon, ConfigPackPostLoadEvent.class)
            .then(event -> {
                ConfigPack pack = event.getPack();
                Registry<Attribute<?>> attributes = pack.getRegistry(Attribute.REGISTRY_TYPE);
                PackAttributesTemplate template = event.loadTemplate(new PackAttributesTemplate());
                List<String> warnings = new ArrayList<>();

                Result result = new AttributeReader(attributes, handle).readConfig(template.getAttributes(), Scope.PACK);
                for(Problem problem : result.problems()) {
                    warnings.add(warn(ATTRIBUTE_WARNING, pack, PACK_CONFIG, problem.key(), problem.reason()));
                }

                Map<String, RegistryKey> parents = new LinkedHashMap<>();
                parent(parents, VANILLA, template.getVanilla(), pack, warnings);
                parent(parents, VANILLA_GENERATION, template.getVanillaGeneration(), pack, warnings);

                List<Either<RegistryKey, TagKey>> schedules = new ArrayList<>();
                for(String schedule : template.getSchedules()) {
                    try {
                        schedules.add(ScheduleIds.reference(schedule));
                    } catch(IllegalArgumentException e) {
                        warnings.add(warn(SCHEDULE_SKIPPED, pack, schedule, e.getMessage()));
                    }
                }

                Map<RegistryKey, AttributeSchedule> definitions = new LinkedHashMap<>();
                Set<RegistryKey> claimed = new HashSet<>();
                RegistryAttributeLookup lookup = new RegistryAttributeLookup(attributes);
                template.getScheduleDefinitions().forEach((idString, lazy) -> {
                    RegistryKey id;
                    try {
                        id = ScheduleIds.claim(idString, claimed);
                    } catch(IllegalArgumentException e) {
                        warnings.add(warn(SCHEDULE_SKIPPED, pack, idString, e.getMessage()));
                        return;
                    }
                    DecodeContext context;
                    Object value;
                    try {
                        context = lazy == null ? handle.plainContext() : lazy.context(handle.plainContext());
                        value = lazy == null ? null : context.resolve(lazy.raw());
                    } catch(DecodeException e) {
                        warnings.add(warn(SCHEDULE_SKIPPED, pack, idString, e.getMessage()));
                        return;
                    }
                    if(!(value instanceof Map<?, ?> map) || !map.keySet().stream().allMatch(String.class::isInstance)) {
                        warnings.add(warn(SCHEDULE_SKIPPED, pack, idString, "a schedule must be a map"));
                        return;
                    }
                    @SuppressWarnings("unchecked")
                    Map<String, Object> raw = (Map<String, Object>) map;
                    ScheduleDecodeResult schedule = handle.decodeSchedule(id, raw, lookup, context);
                    for(ScheduleDecodeResult.Problem problem : schedule.droppedTracks()) {
                        warnings.add(warn(TRACK_DROPPED, pack, idString, problem.key(), problem.reason()));
                    }
                    schedule.skipped().ifPresent(problem -> warnings.add(warn(SCHEDULE_SKIPPED, pack, idString, problem.reason())));
                    schedule.schedule().ifPresent(definition -> definitions.put(id, definition));
                });

                pack.getContext().put(packKey, new PackAttributesImpl(handle, platform.getAttributeParents(), attributes,
                    result.map(),
                    parents,
                    schedules,
                    definitions,
                    biomeEntries(pack, biomeKey),
                    warnings));
            })
            .failThrough();
    }

    private static AttributeMap[] biomeEntries(ConfigPack pack, PropertyKey<BiomeAttributeEntries> biomeKey) {
        Registry<Biome> biomes = pack.getRegistry(Biome.class);
        if(biomes == null) return new AttributeMap[0];
        AttributeMap[] entries = new AttributeMap[pack.getBiomeTagIndex().size()];
        biomes.forEach(biome -> {
            if(biome.getContext().has(biomeKey)) entries[biome.getIntID()] = biome.getContext().get(biomeKey).entries();
        });
        return entries;
    }

    private void parent(Map<String, RegistryKey> parents, String key, Optional<String> value, ConfigPack pack, List<String> warnings) {
        if(value.isEmpty()) return;
        try {
            parents.put(key, RegistryKey.parseNamespaced(value.get()));
        } catch(IllegalArgumentException e) {
            warnings.add(warn(PARENT_WARNING, pack, PACK_CONFIG, key, e.getMessage()));
        }
    }

    private String warn(String format, ConfigPack pack, Object... args) {
        Object[] all = new Object[args.length + 1];
        all[0] = pack.getID();
        System.arraycopy(args, 0, all, 1, args.length);
        logger.warn(format, all);
        return MessageFormatter.arrayFormat(format, all).getMessage();
    }
}
