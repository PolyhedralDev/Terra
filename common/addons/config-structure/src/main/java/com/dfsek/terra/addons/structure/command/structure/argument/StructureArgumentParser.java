/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.structure.command.structure.argument;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.command.arg.ArgumentParser;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.entity.Player;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.structure.configured.ConfiguredStructure;


public class StructureArgumentParser implements ArgumentParser<ConfiguredStructure> {
    @Inject
    private Platform platform;
    
    @Override
    public ConfiguredStructure parse(CommandSender sender, String arg) {
        return ((Player) sender).world().getPack().getRegistry(ConfiguredStructure.class).get(arg).orElse(null);
    }
}
