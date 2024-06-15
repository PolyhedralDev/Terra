package org.allaymc.terra.allay;

import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;

import lombok.extern.slf4j.Slf4j;
import org.allaymc.api.plugin.Plugin;


/**
 * Terra Project 2024/6/15
 *
 * @author daoge_cmd
 */
@Slf4j
public class TerraAllayPlugin extends Plugin {

    public static TerraAllayPlugin INSTANCE;

    {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        log.info("Starting Terra...");

        var platform = new AllayPlatform();
        platform.getEventManager().callEvent(new PlatformInitializationEvent());

        // TODO: Adapt command manager


    }
}
