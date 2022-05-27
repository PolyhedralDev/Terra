package com.dfsek.terra.api.world;

import java.util.Objects;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.util.vector.Vector3Int;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.util.Interceptors;
import com.dfsek.terra.api.world.util.ReadInterceptor;
import com.dfsek.terra.api.world.util.WriteInterceptor;


/**
 * A {@link WritableWorld} implementation which delegates read/write operations to
 * another {@link WritableWorld}, at an offset.
 */
public class BufferedWorld implements WritableWorld {
    private final WritableWorld delegate;
    private final int offsetX, offsetY, offsetZ;
    
    private final ReadInterceptor readInterceptor;
    private final WriteInterceptor writeInterceptor;
    
    protected BufferedWorld(WritableWorld delegate, int offsetX, int offsetY, int offsetZ,
                            ReadInterceptor readInterceptor, WriteInterceptor writeInterceptor) {
        this.delegate = delegate;
        this.offsetX = offsetX;
        
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.readInterceptor = readInterceptor;
        this.writeInterceptor = writeInterceptor;
    }
    
    protected static Builder builder(WritableWorld world) {
        return new Builder(world);
    }
    
    @Override
    public Object getHandle() {
        return delegate.getHandle();
    }
    
    @Override
    public BlockState getBlockState(int x, int y, int z) {
        return readInterceptor.read(x + offsetX, y + offsetY, z + offsetZ, delegate);
    }
    
    @Override
    public BlockEntity getBlockEntity(int x, int y, int z) {
        return delegate.getBlockEntity(x + offsetX, y + offsetY, z + offsetZ);
    }
    
    @Override
    public long getSeed() {
        return delegate.getSeed();
    }
    
    @Override
    public int getMaxHeight() {
        return delegate.getMaxHeight();
    }
    
    @Override
    public int getMinHeight() {
        return delegate.getMinHeight();
    }
    
    @Override
    public ChunkGenerator getGenerator() {
        return delegate.getGenerator();
    }
    
    @Override
    public BiomeProvider getBiomeProvider() {
        return delegate.getBiomeProvider();
    }
    
    @Override
    public ConfigPack getPack() {
        return delegate.getPack();
    }
    
    @Override
    public void setBlockState(int x, int y, int z, BlockState data, boolean physics) {
        writeInterceptor.write(x + offsetX, y + offsetY, z + offsetZ, data, delegate, physics);
    }
    
    @Override
    public Entity spawnEntity(double x, double y, double z, EntityType entityType) {
        return delegate.spawnEntity(x + offsetX, y + offsetY, z + offsetZ, entityType);
    }
    
    /**
     * Get the world this {@link BufferedWorld} delegates to.
     *
     * @return Delegate world.
     */
    public WritableWorld getDelegate() {
        return delegate;
    }
    
    
    public static final class Builder {
        private final WritableWorld delegate;
        private ReadInterceptor readInterceptor;
        private WriteInterceptor writeInterceptor;
        
        private int x = 0;
        private int y = 0;
        private int z = 0;
        
        private Builder(WritableWorld delegate) {
            this.delegate = delegate;
        }
        
        public Builder read(ReadInterceptor interceptor) {
            this.readInterceptor = interceptor;
            return this;
        }
        
        public Builder write(WriteInterceptor interceptor) {
            this.writeInterceptor = interceptor;
            return this;
        }
        
        public Builder offsetX(int x) {
            this.x = x;
            return this;
        }
        
        public Builder offsetY(int y) {
            this.y = y;
            return this;
        }
        
        public Builder offsetZ(int z) {
            this.z = z;
            return this;
        }
        
        public Builder offset(Vector3Int vector) {
            this.x = vector.getX();
            this.y = vector.getY();
            this.z = vector.getZ();
            return this;
        }
        
        public BufferedWorld build() {
            return new BufferedWorld(delegate,
                                     this.x,
                                     this.y,
                                     this.z,
                                     Objects.requireNonNullElse(readInterceptor, Interceptors.readThrough()),
                                     Objects.requireNonNullElse(writeInterceptor, Interceptors.writeThrough()));
        }
    }
}
