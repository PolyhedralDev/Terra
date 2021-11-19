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

package com.dfsek.terra.bukkit.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.dfsek.terra.api.command.CommandManager;
import com.dfsek.terra.api.command.exception.CommandException;
import com.dfsek.terra.bukkit.world.BukkitAdapter;


public class BukkitCommandAdapter implements CommandExecutor, TabCompleter {
    private final CommandManager manager;
    
    public BukkitCommandAdapter(CommandManager manager) {
        this.manager = manager;
    }
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> argList = new ArrayList<>(Arrays.asList(args));
        if(argList.isEmpty()) {
            sender.sendMessage("Command requires arguments.");
            return true;
        }
        try {
            manager.execute(argList.remove(0), BukkitAdapter.adapt(sender), argList);
        } catch(CommandException e) {
            sender.sendMessage(e.getMessage());
        }
        return true;
    }
    
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias,
                                                @NotNull String[] args) {
        List<String> argList = new ArrayList<>(Arrays.asList(args));
        
        try {
            return manager.tabComplete(argList.remove(0), BukkitAdapter.adapt(sender), argList).stream()
                          .filter(s -> s.toLowerCase(Locale.ROOT).startsWith(args[args.length - 1].toLowerCase(Locale.ROOT))).sorted(
                            String::compareTo).collect(Collectors.toList());
        } catch(CommandException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
