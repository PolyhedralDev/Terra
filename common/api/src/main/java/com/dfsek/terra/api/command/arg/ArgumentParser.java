package com.dfsek.terra.api.command.arg;

import com.dfsek.terra.api.entity.CommandSender;

public interface ArgumentParser<T> {
    T parse(CommandSender sender, String arg);
}
