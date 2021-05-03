package com.dfsek.terra.fabric.mixin.block;

import com.dfsek.terra.api.platform.block.state.Container;
import com.dfsek.terra.api.platform.inventory.Inventory;
import com.dfsek.terra.fabric.inventory.FabricInventory;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LootableContainerBlockEntity.class)
@Implements(@Interface(iface = Container.class, prefix = "terra$"))
public abstract class LootableContainerBlockEntityMixin extends BlockEntityMixin {
    public Inventory terra$getInventory() {
        return new FabricInventory(((LootableContainerBlockEntity) (Object) this));
    }

    public Object terra$getHandle() {
        return this;
    }
}
