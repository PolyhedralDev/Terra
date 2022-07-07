package com.dfsek.terra.cli.world;

import com.google.common.collect.Streams;
import net.jafama.FastMath;
import net.querz.mca.MCAFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.api.util.vector.Vector2Int;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.chunk.generation.ProtoWorld;
import com.dfsek.terra.cli.NBTSerializable;
import com.dfsek.terra.cli.world.chunk.CLIChunk;


public class CLIWorld implements ServerWorld, NBTSerializable<Stream<Pair<Vector2Int, MCAFile>>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CLIWorld.class);
    private final Region[] regions;
    private final Region[] negativeRegions;
    private final int size;
    private final long seed;
    private final int maxHeight;
    private final int minHeight;
    private final ChunkGenerator chunkGenerator;
    private final BiomeProvider biomeProvider;
    private final ConfigPack pack;
    private final AtomicInteger amount = new AtomicInteger(0);
    
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
    
    public CLIWorld(int size,
                    long seed,
                    int maxHeight,
                    int minHeight,
                    ConfigPack pack) {
        this.size = size;
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        this.seed = seed;
        this.chunkGenerator = pack.getGeneratorProvider().newInstance(pack);
        this.biomeProvider = pack.getBiomeProvider();
        this.pack = pack;
        
        
        size += 1;
        this.regions = new Region[size * size];
        this.negativeRegions = new Region[size * size];
        for(int x = 0; x < size; x++) {
            for(int z = 0; z < size; z++) {
                regions[x + z * size] = new Region(this, x, z);
                negativeRegions[x + z * size] = new Region(this, x, z);
            }
        }
    }
    
    public void generate() {
        int sizeChunks = size * 32;
        List<Future<?>> futures = new ArrayList<>();
        final AtomicLong start = new AtomicLong(System.nanoTime());
        for(int x = 0; x < sizeChunks; x++) {
            for(int z = 0; z < sizeChunks; z++) {
                int finalX = x;
                int finalZ = z;
                futures.add(executor.submit(() -> {
                    try {
                        int num = amount.getAndIncrement();
                        CLIChunk chunk = getChunkAt(finalX, finalZ);
                        BiomeProvider cachingBiomeProvider = pack.getBiomeProvider();
                        chunkGenerator.generateChunkData(chunk, this, cachingBiomeProvider, finalX, finalZ);
                        CLIProtoWorld protoWorld = new CLIProtoWorld(this, cachingBiomeProvider, finalX, finalZ);
                        pack.getStages().forEach(stage -> stage.populate(protoWorld));
                        if(num % 240 == 239) {
                            long time = System.nanoTime();
                            double cps = num / ((double) (time - start.get()) / 1000000000);
                            LOGGER.info("Generating chunk at ({}, {}), generated {} chunks at {}cps", finalX, finalZ, num, cps);
                            amount.set(0);
                            start.set(System.nanoTime());
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }));
            }
        }
        
        for(Future<?> future : futures) {
            try {
                future.get();
            } catch(InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public Object getHandle() {
        return this;
    }
    
    @Override
    public BlockState getBlockState(int x, int y, int z) {
        return getChunkAt(FastMath.floorDiv(x, 16), FastMath.floorDiv(z, 16))
                .getBlock(FastMath.floorMod(x, 16), y, FastMath.floorMod(z, 16));
    }
    
    @Override
    public BlockEntity getBlockEntity(int x, int y, int z) {
        return new BlockEntity() {
            @Override
            public boolean update(boolean applyPhysics) {
                return false;
            }
            
            @Override
            public Vector3 getPosition() {
                return Vector3.of(x, y, z);
            }
            
            @Override
            public int getX() {
                return x;
            }
            
            @Override
            public int getY() {
                return y;
            }
            
            @Override
            public int getZ() {
                return z;
            }
            
            @Override
            public BlockState getBlockState() {
                return CLIWorld.this.getBlockState(x, y, z);
            }
            
            @Override
            public Object getHandle() {
                return this;
            }
        };
    }
    
    @Override
    public CLIChunk getChunkAt(int x, int z) {
        return getRegion(FastMath.floorDiv(x, 32), FastMath.floorDiv(z, 32))
                .get(FastMath.floorMod(x, 32), FastMath.floorMod(z, 32));
    }
    
    public Region getRegion(int x, int z) {
        int key = x + z * size;
        if(key >= 0) return regions[key];
        else return negativeRegions[-key];
    }
    
    @Override
    public long getSeed() {
        return seed;
    }
    
    @Override
    public int getMaxHeight() {
        return maxHeight;
    }
    
    @Override
    public int getMinHeight() {
        return minHeight;
    }
    
    @Override
    public ChunkGenerator getGenerator() {
        return chunkGenerator;
    }
    
    @Override
    public BiomeProvider getBiomeProvider() {
        return biomeProvider;
    }
    
    @Override
    public ConfigPack getPack() {
        return pack;
    }
    
    @Override
    public void setBlockState(int x, int y, int z, BlockState data, boolean physics) {
        getChunkAt(FastMath.floorDiv(x, 16), FastMath.floorDiv(z, 16))
                .setBlock(FastMath.floorMod(x, 16), y, FastMath.floorMod(z, 16), data, physics);
    }
    
    @Override
    public Entity spawnEntity(double x, double y, double z, EntityType entityType) {
        return null;
    }
    
    @Override
    public Stream<Pair<Vector2Int, MCAFile>> serialize() {
        return Streams
                .concat(Arrays.stream(regions), Arrays.stream(negativeRegions))
                .map(region -> Pair.of(Vector2Int.of(region.getX(), region.getZ()), region.serialize()));
    }
    
    private static final class CLIProtoWorld implements ProtoWorld {
        private final CLIWorld delegate;
        private final BiomeProvider biomeProvider;
        private final int x, z;
        
        private CLIProtoWorld(CLIWorld delegate, BiomeProvider biomeProvider, int x, int z) {
            this.delegate = delegate;
            this.biomeProvider = biomeProvider;
            this.x = x;
            this.z = z;
        }
        
        @Override
        public Object getHandle() {
            return this;
        }
        
        @Override
        public BlockState getBlockState(int x, int y, int z) {
            return delegate.getBlockState(x, y, z);
        }
        
        @Override
        public BlockEntity getBlockEntity(int x, int y, int z) {
            return delegate.getBlockEntity(x, y, z);
        }
        
        @Override
        public long getSeed() {
            return delegate.seed;
        }
        
        @Override
        public int getMaxHeight() {
            return delegate.maxHeight;
        }
        
        @Override
        public int getMinHeight() {
            return delegate.minHeight;
        }
        
        @Override
        public ChunkGenerator getGenerator() {
            return delegate.chunkGenerator;
        }
        
        @Override
        public BiomeProvider getBiomeProvider() {
            return biomeProvider;
        }
        
        @Override
        public ConfigPack getPack() {
            return delegate.pack;
        }
        
        @Override
        public void setBlockState(int x, int y, int z, BlockState data, boolean physics) {
            delegate.setBlockState(x, y, z, data, physics);
        }
        
        @Override
        public Entity spawnEntity(double x, double y, double z, EntityType entityType) {
            return delegate.spawnEntity(x, y, z, entityType);
        }
        
        @Override
        public int centerChunkX() {
            return x;
        }
        
        @Override
        public int centerChunkZ() {
            return z;
        }
        
        @Override
        public ServerWorld getWorld() {
            return delegate;
        }
    }
}
