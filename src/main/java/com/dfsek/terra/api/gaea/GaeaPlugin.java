package com.dfsek.terra.api.gaea;

import com.dfsek.terra.api.gaea.lang.Language;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class GaeaPlugin extends JavaPlugin {
    public abstract boolean isDebug();
    public abstract Language getLanguage();
}
