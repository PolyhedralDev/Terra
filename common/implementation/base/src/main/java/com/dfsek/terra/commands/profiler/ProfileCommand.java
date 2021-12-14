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

package com.dfsek.terra.commands.profiler;

import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.Subcommand;
import com.dfsek.terra.api.command.annotation.type.DebugCommand;
import com.dfsek.terra.api.command.annotation.type.PlayerCommand;
import com.dfsek.terra.api.command.annotation.type.WorldCommand;
import com.dfsek.terra.api.entity.CommandSender;


@Command(
        subcommands = {
                @Subcommand(value = "query", aliases = "q", clazz = ProfileQueryCommand.class),
                @Subcommand(value = "start", aliases = "s", clazz = ProfileStartCommand.class),
                @Subcommand(value = "stop", aliases = "st", clazz = ProfileStopCommand.class),
                @Subcommand(value = "reset", aliases = "r", clazz = ProfileResetCommand.class)
        },
        usage = "Commands to enable/disable/query/reset the profiler."
)
@WorldCommand
@PlayerCommand
@DebugCommand
public class ProfileCommand implements CommandTemplate {
    @Override
    public void execute(CommandSender sender) {
        sender.sendMessage("""
                           ---------------Terra/profile---------------"
                           - "start - Starts the profiler"
                           - "stop  - Stops the profiler"
                           - "query - Fetches profiler data"
                           - "reset - Resets profiler data""");
    }
}
