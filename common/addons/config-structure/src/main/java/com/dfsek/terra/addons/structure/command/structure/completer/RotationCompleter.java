/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.structure.command.structure.completer;

import java.util.Arrays;
import java.util.List;

import com.dfsek.terra.api.command.tab.TabCompleter;
import com.dfsek.terra.api.entity.CommandSender;


public class RotationCompleter implements TabCompleter {
    @Override
    public List<String> complete(CommandSender sender) {
        return Arrays.asList("0", "90", "180", "270");
    }
}
