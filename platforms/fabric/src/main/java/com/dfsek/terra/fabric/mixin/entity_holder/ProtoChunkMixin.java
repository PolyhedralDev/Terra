package com.dfsek.terra.fabric.mixin.entity_holder;

import com.dfsek.terra.fabric.entity.DelegateEntity;
import com.dfsek.terra.fabric.entity.DelegateEntityHolder;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.chunk.UpgradeData;
import net.minecraft.world.gen.chunk.BlendingData;
import net.minecraft.world.tick.SimpleTickScheduler;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


@Mixin(ProtoChunk.class)
@Implements(@Interface(iface = DelegateEntityHolder.class, prefix = "terra$"))
public class ProtoChunkMixin {
    private AtomicReference<List<DelegateEntity>> terra$entities;
    
    @Inject(method = "<init>*", at = @At("RETURN"))
    public void injectConstructor(CallbackInfo ci) {
        this.terra$entities = new AtomicReference<>(new ArrayList<>());
    }
    
    public List<DelegateEntity> terra$getAndClearTerraEntities() {
        return terra$entities.getAndSet(new ArrayList<>());
    }
    
    public void terra$addTerraEntity(DelegateEntity entity) {
        this.terra$entities.updateAndGet(list -> {
            list.add(entity);
            return list;
        });
    }
}
