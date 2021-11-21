/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.fabric.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Spawner;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;

import com.dfsek.terra.fabric.generation.FabricChunkGeneratorWrapper;


@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    private static final Logger logger = LoggerFactory.getLogger(ServerWorldMixin.class);
    
    @Inject(method = "<init>", at = @At("RETURN"))
    public void injectConstructor(MinecraftServer server, Executor workerExecutor, LevelStorage.Session session,
                                  ServerWorldProperties properties, RegistryKey<World> registryKey, DimensionType dimensionType,
                                  WorldGenerationProgressListener worldGenerationProgressListener, ChunkGenerator chunkGenerator,
                                  boolean debugWorld, long l, List<Spawner> list, boolean bl, CallbackInfo ci) {
        if(chunkGenerator instanceof FabricChunkGeneratorWrapper) {
            ((FabricChunkGeneratorWrapper) chunkGenerator).setWorld((ServerWorld) (Object) this);
            FabricEntryPoint.getPlatform().addWorld((ServerWorld) (Object) this);
            logger.info("Registered world {}", this);
        }
    }
}
