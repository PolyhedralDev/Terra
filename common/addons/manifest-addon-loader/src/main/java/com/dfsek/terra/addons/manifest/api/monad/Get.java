package com.dfsek.terra.addons.manifest.api.monad;

import com.dfsek.terra.addons.manifest.impl.InitInfo;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.EventManager;


public final class Get {
    private Get() {
    
    }
    
    public static Init<EventManager> eventManager() {
        return Init.of(initInfo -> initInfo.platform().getEventManager());
    }
    
    public static Init<BaseAddon> addon() {
        return Init.of(InitInfo::addon);
    }
    
    public static Init<Platform> platform() {
        return Init.of(InitInfo::platform);
    }
}
