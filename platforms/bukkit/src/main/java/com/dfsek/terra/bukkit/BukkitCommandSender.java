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

package com.dfsek.terra.bukkit;

import org.bukkit.ChatColor;

import java.util.Optional;

import com.dfsek.terra.api.command.CommandSender;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.Player;
import com.dfsek.terra.bukkit.world.BukkitAdapter;


public class BukkitCommandSender implements CommandSender {
    private final org.bukkit.command.CommandSender delegate;
    
    public BukkitCommandSender(org.bukkit.command.CommandSender delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public void sendMessage(String message) {
        delegate.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
    
    @Override
    public Optional<Entity> getEntity() {
        if(delegate instanceof org.bukkit.entity.Entity entity) {
            return Optional.of(BukkitAdapter.adapt(entity));
        }
        return Optional.empty();
    }
    
    @Override
    public Optional<Player> getPlayer() {
        if(delegate instanceof org.bukkit.entity.Player player) {
            return Optional.of(BukkitAdapter.adapt(player));
        }
        return Optional.empty();
    }
    
    @Override
    public org.bukkit.command.CommandSender getHandle() {
        return delegate;
    }
}
