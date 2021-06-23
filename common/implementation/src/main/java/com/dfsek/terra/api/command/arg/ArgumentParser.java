package com.dfsek.terra.api.command.arg;

import com.dfsek.terra.api.platform.CommandSender;

public interface ArgumentParser<T> {
    T parse(CommandSender sender, String arg);
}
