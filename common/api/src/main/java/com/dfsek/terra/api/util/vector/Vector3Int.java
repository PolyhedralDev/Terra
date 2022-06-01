package com.dfsek.terra.api.util.vector;


public class Vector3Int {
    private static final Vector3Int ZERO = new Vector3Int(0, 0, 0);
    private static final Vector3Int UNIT = new Vector3Int(0, 1, 0);
    protected int x, y, z;
    
    protected Vector3Int(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
    
    public static Vector3Int of(Vector3Int origin, int x, int y, int z) {
        return new Vector3Int(origin.x + x, origin.y + y, origin.z + z);
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
    
    public Vector3 toVector3() {
        return Vector3.of(x, y, z);
    }
    
    public Vector3.Mutable toVector3Mutable() {
        return Vector3.Mutable.of(x, y, z);
    }
    
    public static class Mutable extends Vector3Int {
        protected Mutable(int x, int y, int z) {
            super(x, y, z);
        }
        
        public int getX() {
            return x;
        }
        
        public void setX(int x) {
            this.x = x;
        }
        
        public int getY() {
            return y;
        }
        
        public void setY(int y) {
            this.y = y;
        }
        
        public int getZ() {
            return z;
        }
        
        public void setZ(int z) {
            this.z = z;
        }
        
        public Vector3Int immutable() {
            return Vector3Int.of(x, y, z);
        }
        
        public Mutable add(int x, int y, int z) {
            this.x += x;
            this.y += y;
            this.z += z;
            return this;
        }
        
        public Vector3 toVector3() {
            return Vector3.of(x, y, z);
        }
    }
}
