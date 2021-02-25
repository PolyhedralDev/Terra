package com.dfsek.terra.fabric.inventory.meta;

import com.dfsek.terra.api.platform.inventory.item.Damageable;
import net.minecraft.item.ItemStack;

public class FabricDamageable extends FabricItemMeta implements Damageable {
    public FabricDamageable(ItemStack delegate) {
        super(delegate);
    }

    @Override
    public int getDamage() {
        return delegate.getDamage();
    }

    @Override
    public void setDamage(int damage) {
        System.out.println("Setting damage: " + damage);
        delegate.setDamage(damage);
    }

    @Override
    public boolean hasDamage() {
        return delegate.isDamageable();
    }
}
