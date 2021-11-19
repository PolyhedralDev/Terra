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

package com.dfsek.terra.commands;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.dfsek.terra.api.entity.CommandSender;


public final class ExecutionState {
    private final Set<String> switches = new HashSet<>();
    private final Map<String, String> args = new HashMap<>();
    private final CommandSender sender;
    
    public ExecutionState(CommandSender sender) {
        this.sender = sender;
    }
    
    public void addSwitch(String flag) {
        switches.add(flag);
    }
    
    public void addArgument(String arg, String value) {
        args.put(arg, value);
    }
    
    public String getArgument(String argument) {
        return args.get(argument);
    }
    
    public boolean hasSwitch(String flag) {
        return switches.contains(flag);
    }
    
    public CommandSender getSender() {
        return sender;
    }
}
