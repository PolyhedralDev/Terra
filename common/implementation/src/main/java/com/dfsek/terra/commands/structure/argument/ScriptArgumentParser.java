package com.dfsek.terra.commands.structure.argument;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.arg.ArgumentParser;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.api.platform.entity.Player;
import com.dfsek.terra.api.structures.script.StructureScript;

public class ScriptArgumentParser implements ArgumentParser<StructureScript> {
    @Inject
    private TerraPlugin main;

    @Override
    public StructureScript parse(CommandSender sender, String arg) {
        return main.getWorld(((Player) sender).getWorld()).getConfig().getRegistry(StructureScript.class).get(arg);
    }
}
