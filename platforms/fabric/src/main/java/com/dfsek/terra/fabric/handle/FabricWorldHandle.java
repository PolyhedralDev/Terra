package com.dfsek.terra.fabric.handle;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.api.platform.entity.Player;
import com.dfsek.terra.api.platform.handle.WorldHandle;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.fabric.block.FabricBlockData;
import com.dfsek.terra.fabric.util.FabricAdapter;
import com.dfsek.terra.fabric.util.WorldEditUtil;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Locale;

public class FabricWorldHandle implements WorldHandle {

    @Override
    public FabricBlockData createBlockData(String data) {
        BlockArgumentParser parser = new BlockArgumentParser(new StringReader(data), true);
        try {
            BlockState state = parser.parse(true).getBlockState();
            if(state == null) throw new IllegalArgumentException("Invalid data: " + data);
            return FabricAdapter.adapt(state);
        } catch(CommandSyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public EntityType getEntity(String id) {
        Identifier identifier = Identifier.tryParse(id);
        if(identifier == null) identifier = Identifier.tryParse("minecraft:" + id.toLowerCase(Locale.ROOT));
        return (EntityType) Registry.ENTITY_TYPE.get(identifier);
    }

    @Override
    public Pair<Location, Location> getSelectedLocation(Player player) {
        try {
            Class.forName("com.sk89q.worldedit.WorldEdit");
        } catch(ClassNotFoundException e) {
            throw new IllegalStateException("WorldEdit is not installed.");
        }
        return WorldEditUtil.getSelection(player);
    }
}
