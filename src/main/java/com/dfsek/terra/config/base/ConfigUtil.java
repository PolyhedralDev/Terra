package com.dfsek.terra.config.base;

import com.dfsek.terra.Debug;
import com.dfsek.terra.config.exception.ConfigException;
import com.dfsek.terra.util.TagUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public final class ConfigUtil {
    public static Set<Material> toBlockData(List<String> list, String phase, String id) throws InvalidConfigurationException {
        Set<Material> bl = EnumSet.noneOf(Material.class);
        for(String s : list) {
            try {
                if(s.startsWith("#")) {
                    Debug.info("Loading Tag " + s);
                    Set<Material> tag = TagUtil.getTag(s.substring(1));
                    for(Material m : tag) {
                        if(bl.contains(m)) {
                            Bukkit.getLogger().warning("Duplicate material in " + phase + " list: " + m); // Check for duplicates in this tag
                        }
                    }
                    bl.addAll(tag);
                } else {
                    if(bl.contains(Bukkit.createBlockData(s).getMaterial()))
                        Bukkit.getLogger().warning("Duplicate material in " + phase + " list: " + s);
                    bl.add(Bukkit.createBlockData(s).getMaterial());
                }
            } catch(NullPointerException | IllegalArgumentException e) {
                throw new ConfigException("Could not load BlockData data for \"" + s + "\"", id);
            }
        }
        return bl;
    }
}
