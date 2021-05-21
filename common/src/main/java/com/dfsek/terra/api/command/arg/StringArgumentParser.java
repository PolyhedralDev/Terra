package com.dfsek.terra.api.command.arg;

import com.dfsek.terra.api.platform.CommandSender;

public class StringArgumentParser implements ArgumentParser<String> {
    @Override
    public String parse(CommandSender sender, String arg) {
        return arg;
    }
}
