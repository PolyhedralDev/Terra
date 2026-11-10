package attribute.minecraft;

import ca.solostudios.strata.Versions;
import ca.solostudios.strata.version.Version;
import ca.solostudios.strata.version.VersionRange;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.addons.attribute.minecraft.MinecraftAttributes;
import com.dfsek.terra.addons.attribute.minecraft.MinecraftAttributesAddon;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.event.EventManager;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.inject.Injector;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.event.EventManagerImpl;
import com.dfsek.terra.registry.CheckedRegistryImpl;
import com.dfsek.terra.registry.OpenRegistryImpl;
import com.dfsek.terra.world.attribute.AttributeHandleImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;


class MinecraftAttributesAddonTest {
    private static final BaseAddon ADDON = new BaseAddon() {
        @Override
        public String getID() {
            return "minecraft-attributes";
        }

        @Override
        public Version getVersion() {
            return null;
        }
    };

    private static final VersionRange ONE = Versions.getVersionRange(Versions.getVersion(1, 0, 0), true, Versions.getVersion(2, 0, 0),
        false);

    private static Platform platform(EventManager events, AttributeHandle handle) {
        return (Platform) Proxy.newProxyInstance(Platform.class.getClassLoader(), new Class<?>[]{ Platform.class },
            (proxy, method, args) -> switch(method.getName()) {
                case "getEventManager" -> events;
                case "getAttributeHandle" -> handle;
                default -> throw new UnsupportedOperationException(method.getName());
            });
    }

    private static ConfigPack pack(Map<BaseAddon, VersionRange> addons) {
        Map<TypeKey<?>, CheckedRegistry<?>> registries = new HashMap<>();
        return (ConfigPack) Proxy.newProxyInstance(ConfigPack.class.getClassLoader(), new Class<?>[]{ ConfigPack.class },
            (proxy, method, args) -> switch(method.getName()) {
                case "addons" -> addons;
                case "getOrCreateRegistry" -> registries.computeIfAbsent((TypeKey<?>) args[0],
                    type -> new CheckedRegistryImpl<>(new OpenRegistryImpl<>(type)));
                default -> throw new UnsupportedOperationException(method.getName());
            });
    }

    @Test
    void dependentPacksOnly() {
        EventManager events = new EventManagerImpl();
        AttributeHandle handle = new AttributeHandleImpl();
        MinecraftAttributesAddon initializer = new MinecraftAttributesAddon();
        Injector<Platform> platformInjector = Injector.get(platform(events, handle));
        platformInjector.addExplicitTarget(Platform.class);
        platformInjector.inject(initializer);
        Injector<BaseAddon> addonInjector = Injector.get(ADDON);
        addonInjector.addExplicitTarget(BaseAddon.class);
        addonInjector.inject(initializer);
        initializer.initialize();

        ConfigPack dependent = pack(Map.of(ADDON, ONE));
        ConfigPack other = pack(Map.of());
        events.callEvent(new ConfigPackPreLoadEvent(dependent, template -> { }));
        events.callEvent(new ConfigPackPreLoadEvent(other, template -> { }));

        CheckedRegistry<Attribute<?>> registered = dependent.getOrCreateRegistry(Attribute.REGISTRY_TYPE);
        assertEquals(79, registered.entries().size());
        assertTrue(other.getOrCreateRegistry(Attribute.REGISTRY_TYPE).entries().isEmpty());
        Attribute<?> fog = MinecraftAttributes.VISUAL_FOG_COLOR.lookup(registered).orElseThrow();
        assertSame(fog, registered.get(fog.getRegistryKey()).orElseThrow());

        ConfigPack second = pack(Map.of(ADDON, ONE));
        events.callEvent(new ConfigPackPreLoadEvent(second, template -> { }));
        assertSame(fog, MinecraftAttributes.VISUAL_FOG_COLOR.lookup(second.getOrCreateRegistry(Attribute.REGISTRY_TYPE)).orElseThrow());
        assertEquals(79, handle.attributeCount());
    }
}
