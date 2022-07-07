package com.dfsek.terra.mod.mixin.fix;

import net.minecraft.entity.passive.BeeEntity.MoveToFlowerGoal;
import net.minecraft.entity.passive.BeeEntity.MoveToHiveGoal;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.dfsek.terra.mod.CommonPlatform;


/**
 * Bees spawning uses world.random without synchronization. This causes issues when spawning bees during world generation.
 */
@Mixin({
        MoveToHiveGoal.class,
        MoveToFlowerGoal.class
})
public class BeeMoveGoalsUnsynchronizedRandomAccessFix {
    @Redirect(method = "<init>",
              at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;random:Lnet/minecraft/util/math/random/Random;"))
    public Random redirectRandomAccess(World instance) {
        return new CheckedRandom(CommonPlatform.get().getServer().getTicks()); // replace with new random seeded by tick time.
    }
}
