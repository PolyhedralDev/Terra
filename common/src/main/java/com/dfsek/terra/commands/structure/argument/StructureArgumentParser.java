package com.dfsek.terra.commands.structure.argument;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.arg.ArgumentParser;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.api.platform.entity.Player;
import com.dfsek.terra.world.population.items.TerraStructure;

public class StructureArgumentParser implements ArgumentParser<TerraStructure> {
    @Inject
    private TerraPlugin main;

    @Override
    public TerraStructure parse(CommandSender sender, String arg) {
        return main.getWorld(((Player) sender).getWorld()).getConfig().getStructureRegistry().get(arg);
    }
}
