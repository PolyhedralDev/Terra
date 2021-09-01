package com.dfsek.terra.forge.handle;

import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.forge.ForgeAdapter;
import com.dfsek.terra.forge.block.ForgeBlockData;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.BlockState;
import net.minecraft.command.arguments.BlockStateParser;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;


public class ForgeWorldHandle implements WorldHandle {
    
    @Override
    public ForgeBlockData createBlockData(String data) {
        BlockStateParser parser = new BlockStateParser(new StringReader(data), true);
        try {
            BlockState state = parser.parse(true).getState();
            if(state == null) throw new IllegalArgumentException("Invalid data: " + data);
            return ForgeAdapter.adapt(state);
        } catch(CommandSyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }
    
    @Override
    public EntityType getEntity(String id) {
        ResourceLocation identifier = ResourceLocation.tryParse(id);
        if(identifier == null) identifier = ResourceLocation.tryParse("minecraft:" + id.toLowerCase(Locale.ROOT));
        return (EntityType) ForgeRegistries.ENTITIES.getValue(identifier);
    }
}
