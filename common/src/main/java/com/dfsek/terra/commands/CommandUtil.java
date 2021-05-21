package com.dfsek.terra.commands;

import com.dfsek.terra.api.command.CommandManager;
import com.dfsek.terra.api.command.exception.MalformedCommandException;
import com.dfsek.terra.commands.biome.BiomeCommand;
import com.dfsek.terra.commands.geometry.GeometryCommand;
import com.dfsek.terra.commands.profiler.ProfileCommand;
import com.dfsek.terra.commands.structure.StructureCommand;

public final class CommandUtil {
    public static void registerAll(CommandManager manager) throws MalformedCommandException {
        manager.register("structure", StructureCommand.class);
        manager.register("profile", ProfileCommand.class);
        manager.register("reload", ReloadCommand.class);
        manager.register("addons", AddonsCommand.class);
        manager.register("version", VersionCommand.class);
        manager.register("getblock", GetBlockCommand.class);
        manager.register("packs", PacksCommand.class);
        manager.register("biome", BiomeCommand.class);
        manager.register("geometry", GeometryCommand.class);
    }
}
