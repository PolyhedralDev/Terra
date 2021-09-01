package com.dfsek.terra.commands;

import com.dfsek.terra.api.command.CommandManager;
import com.dfsek.terra.api.command.exception.MalformedCommandException;
import com.dfsek.terra.commands.profiler.ProfileCommand;


public final class CommandUtil {
    public static void registerAll(CommandManager manager) throws MalformedCommandException {
        manager.register("profile", ProfileCommand.class);
        manager.register("reload", ReloadCommand.class);
        manager.register("addons", AddonsCommand.class);
        manager.register("version", VersionCommand.class);
        manager.register("getblock", GetBlockCommand.class);
        manager.register("packs", PacksCommand.class);
        
    }
}
