/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.command.arg;

import com.dfsek.terra.api.entity.CommandSender;


public class StringArgumentParser implements ArgumentParser<String> {
    @Override
    public String parse(CommandSender sender, String arg) {
        return arg;
    }
}
