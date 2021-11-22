/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.structure.command.structure;

import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.Subcommand;
import com.dfsek.terra.api.entity.CommandSender;


@Command(
        subcommands = {
                @Subcommand(
                        clazz = StructureLoadCommand.class,
                        value = "load",
                        aliases = "ld"
                ),
                @Subcommand(
                        clazz = StructureLocateCommand.class,
                        value = "locate",
                        aliases = "l"
                )
        },
        usage = "/te structure"
)
public class StructureCommand implements CommandTemplate {
    @Override
    public void execute(CommandSender sender) {
        //LangUtil.send("command.structure.main-menu", sender);
    }
}
