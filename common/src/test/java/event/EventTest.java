package event;

import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.api.core.TerraPlugin;
import com.dfsek.terra.api.core.event.Event;
import com.dfsek.terra.api.core.event.EventListener;
import com.dfsek.terra.api.core.event.TerraEventManager;
import com.dfsek.terra.api.core.event.annotations.Listener;
import com.dfsek.terra.api.platform.handle.ItemHandle;
import com.dfsek.terra.api.platform.handle.WorldHandle;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.config.PluginConfig;
import com.dfsek.terra.config.lang.Language;
import com.dfsek.terra.debug.DebugLogger;
import com.dfsek.terra.registry.ConfigRegistry;
import com.dfsek.terra.world.TerraWorld;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.logging.Logger;

public class EventTest {
    public TerraPlugin main = new TerraPlugin() {
        private final Logger logger = Logger.getLogger("Terra");

        @Override
        public WorldHandle getWorldHandle() {
            return null;
        }

        @Override
        public boolean isEnabled() {
            return false;
        }

        @Override
        public TerraWorld getWorld(World world) {
            return null;
        }

        @Override
        public Logger getLogger() {
            return logger;
        }

        @Override
        public PluginConfig getTerraConfig() {
            return null;
        }

        @Override
        public File getDataFolder() {
            return null;
        }

        @Override
        public boolean isDebug() {
            return false;
        }

        @Override
        public Language getLanguage() {
            return null;
        }

        @Override
        public ConfigRegistry getRegistry() {
            return null;
        }

        @Override
        public void reload() {

        }

        @Override
        public ItemHandle getItemHandle() {
            return null;
        }

        @Override
        public void saveDefaultConfig() {

        }

        @Override
        public String platformName() {
            return null;
        }

        @Override
        public DebugLogger getDebugLogger() {
            return null;
        }

        @Override
        public void register(TypeRegistry registry) {

        }
    };

    @Test
    public void eventTest() {
        TerraEventManager eventManager = new TerraEventManager(main);
        eventManager.registerListener(new TestListener());
        eventManager.registerListener(new TestListener2());

        TestEvent event = new TestEvent(4);
        eventManager.callEvent(event);

        eventManager.registerListener(new TestListenerException());

        TestEvent event2 = new TestEvent(4);
        eventManager.callEvent(event2);
    }

    static class TestListener implements EventListener {
        @Listener
        public void doThing(TestEvent event) {
            System.out.println("Event value: " + event.value);
        }
    }

    static class TestListener2 implements EventListener {
        @Listener
        public void doThing(TestEvent event) {
            System.out.println("Event value 2: " + event.value);
        }
    }

    static class TestListenerException implements EventListener {
        @Listener
        public void doThing(TestEvent event) {
            throw new RuntimeException("bazinga: " + event.value);
        }
    }

    static class TestEvent implements Event {
        private final int value;

        TestEvent(int value) {
            this.value = value;
        }
    }
}
