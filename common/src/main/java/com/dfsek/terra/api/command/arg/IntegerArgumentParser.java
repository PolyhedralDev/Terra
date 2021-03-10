package com.dfsek.terra.api.command.arg;

import com.dfsek.terra.api.platform.CommandSender;

public class IntegerArgumentParser implements ArgumentParser<Integer> {
    @Override
    public Integer parse(CommandSender sender, String arg) {
        return arg == null ? null : Integer.parseInt(arg);
    }
}
