package com.dfsek.terra.mod.mixin.gameplay;


import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.dfsek.terra.mod.util.FertilizableUtil;
import com.dfsek.terra.mod.util.MinecraftAdapter;


@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {
    private static final Identifier cooldownId = new Identifier("terra", "bone_meal_cooldown");
    
    @Inject(method = "useOnFertilizable", at = @At("HEAD"), cancellable = true)
    private static void injectUseOnFertilizable(ItemStack stack, World world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if(world instanceof ServerWorld) {
            Boolean value = FertilizableUtil.grow((ServerWorld) world, MinecraftAdapter.adapt(world.getRandom()), pos,
                                                  world.getBlockState(pos), cooldownId);
            stack.decrement(1);
            if(value != null) {
                cir.setReturnValue(value);
            }
        }
    }
}
