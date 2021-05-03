package com.dfsek.terra.forge.inventory.meta;

import com.dfsek.terra.api.platform.inventory.item.Damageable;
import net.minecraft.item.ItemStack;

public class ForgeDamageable extends ForgeItemMeta implements Damageable {
    public ForgeDamageable(ItemStack delegate) {
        super(delegate);
    }

    @Override
    public int getDamage() {
        return delegate.getDamageValue();
    }

    @Override
    public void setDamage(int damage) {
        delegate.setDamageValue(damage);
    }

    @Override
    public boolean hasDamage() {
        return delegate.isDamaged();
    }
}
