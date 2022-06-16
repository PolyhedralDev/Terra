package com.dfsek.terra.fabric.mixin.implementations.cache;

import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.fabric.mixin_ifaces.BiomeProviderHolder;

import net.minecraft.world.chunk.ProtoChunk;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(ProtoChunk.class)
@Implements(@Interface(iface = BiomeProviderHolder.class, prefix = "provider$"))
public class ProtoChunkMixin {
    private BiomeProvider biomeProvider;
    
    public void provider$terra$setHeldBiomeProvider(BiomeProvider provider) {
        if(this.biomeProvider != null) {
            throw new IllegalStateException("Already set biome provider for chunk " + this);
        }
        this.biomeProvider = provider;
    }
    
    public BiomeProvider provider$terra$getHeldBiomeProvider() {
        return biomeProvider;
    }
}
