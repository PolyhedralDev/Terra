package com.dfsek.terra.api.util.vector.integer;

public class Vector3Int {
    private final int x, y, z;
    
    private static final Vector3Int ZERO = new Vector3Int(0, 0, 0);
    private static final Vector3Int UNIT = new Vector3Int(0, 1, 0);
    
    protected Vector3Int(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getZ() {
        return z;
    }
    
    public Mutable mutable() {
        return new Mutable(x, y, z);
    }
    
    public static Vector3Int unit() {
        return UNIT;
    }
    
    public static Vector3Int zero() {
        return ZERO;
    }
    
    public static Vector3Int of(int x, int y, int z) {
        return new Vector3Int(x, y, z);
    }
    
    public static class Mutable {
        private int x, y, z;
        
        protected Mutable(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    
        public void setX(int x) {
            this.x = x;
        }
    
        public void setY(int y) {
            this.y = y;
        }
    
        public void setZ(int z) {
            this.z = z;
        }
    
        public int getX() {
            return x;
        }
    
        public int getY() {
            return y;
        }
    
        public int getZ() {
            return z;
        }
        
        public Vector3Int immutable() {
            return Vector3Int.of(x, y, z);
        }
    }
}
