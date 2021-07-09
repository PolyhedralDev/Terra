package com.dfsek.terra.addons.structure.command.structure.argument;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.arg.ArgumentParser;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.entity.Player;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.structure.Structure;

public class ScriptArgumentParser implements ArgumentParser<Structure> {
    @Inject
    private TerraPlugin main;

    @Override
    public Structure parse(CommandSender sender, String arg) {
        return main.getWorld(((Player) sender).world()).getConfig().getRegistry(Structure.class).get(arg);
    }
}
