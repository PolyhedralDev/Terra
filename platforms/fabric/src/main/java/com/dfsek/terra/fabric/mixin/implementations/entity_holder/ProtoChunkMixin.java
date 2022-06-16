package com.dfsek.terra.fabric.mixin.implementations.entity_holder;

import com.dfsek.terra.fabric.mixin_ifaces.entity.DelegateEntity;
import com.dfsek.terra.fabric.mixin_ifaces.entity.DelegateEntityHolder;

import net.minecraft.world.chunk.ProtoChunk;
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
