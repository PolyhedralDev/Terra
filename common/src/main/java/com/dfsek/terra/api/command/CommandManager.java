package com.dfsek.terra.api.command;

import java.util.List;

public interface CommandManager {
    void execute(String command, List<String> args);

    void register(String name, Class<? extends CommandTemplate> clazz);
}
