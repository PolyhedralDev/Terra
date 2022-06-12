package com.dfsek.terra.fabric.mixin.cache;

import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.Chunk;
import com.dfsek.terra.fabric.generation.BiomeProviderHolder;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.ProtoChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;


@Mixin(ChunkRegion.class)
@Implements(@Interface(iface = BiomeProviderHolder.class, prefix = "provider$"))
public class ChunkRegionMixin {
    @Shadow
    @Final
    private net.minecraft.world.chunk.Chunk centerPos;
    private BiomeProvider biomeProvider;
    
    @Inject(method = "<init>", at = @At("RETURN"))
    public void addProvider(ServerWorld world, List<Chunk> chunks, ChunkStatus status, int placementRadius, CallbackInfo ci) {
        if(centerPos instanceof BiomeProviderHolder providerHolder) {
            biomeProvider = providerHolder.getBiomeProvider();
        }
    }
    
    public void provider$setBiomeProvider(BiomeProvider provider) {
        if(this.biomeProvider != null) {
            throw new IllegalStateException("Already set biome provider for chunk " + this);
        }
        this.biomeProvider = provider;
    }
    
    public BiomeProvider provider$getBiomeProvider() {
        return biomeProvider;
    }
}
