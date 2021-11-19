/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.sponge;

import org.spongepowered.api.Sponge;

import java.io.File;

import com.dfsek.terra.AbstractPlatform;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.util.Logger;
import com.dfsek.terra.sponge.handle.SpongeWorldHandle;


public class PlatformImpl extends AbstractPlatform {
    private final TerraSpongePlugin plugin;
    private final SpongeWorldHandle worldHandle = new SpongeWorldHandle();
    
    public PlatformImpl(TerraSpongePlugin plugin) {
        this.plugin = plugin;
        load();
    }
    
    @Override
    public boolean reload() {
        return false;
    }
    
    @Override
    public String platformName() {
        return "Sponge";
    }
    
    @Override
    public WorldHandle getWorldHandle() {
        return worldHandle;
    }
    
    @Override
    public File getDataFolder() {
        return Sponge.configManager().pluginConfig(plugin.getPluginContainer()).directory().toFile();
    }
    
    @Override
    public ItemHandle getItemHandle() {
        return null;
    }
    
    @Override
    protected Logger createLogger() {
        return new Logger() {
            @Override
            public void info(String message) {
                plugin.getPluginContainer().logger().info(message);
            }
            
            @Override
            public void warning(String message) {
                plugin.getPluginContainer().logger().warn(message);
            }
            
            @Override
            public void severe(String message) {
                plugin.getPluginContainer().logger().error(message);
            }
        };
    }
}
