package com.dfsek.terra.fabric.mixin.implementations.block.state;

import net.minecraft.block.entity.LootableContainerBlockEntity;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;

import com.dfsek.terra.api.block.entity.Container;
import com.dfsek.terra.api.inventory.Inventory;
import com.dfsek.terra.fabric.mixin.implementations.block.BlockEntityMixin;


@Mixin(LootableContainerBlockEntity.class)
@Implements(@Interface(iface = Container.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class LootableContainerBlockEntityMixin extends BlockEntityMixin {
    public Inventory terra$getInventory() {
        return (Inventory) this;
    }
    
    @Intrinsic
    public Object terra$getHandle() {
        return this;
    }
}
