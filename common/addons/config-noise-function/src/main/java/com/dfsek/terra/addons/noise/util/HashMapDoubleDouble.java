/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.util;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.NoSuchElementException;


public class HashMapDoubleDouble extends HashIntrinsic {
    @Serial
    private static final long serialVersionUID = 2109458761298324234L;
    private HashMapDoubleDouble.Entry[] table;
    
    public HashMapDoubleDouble(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        this.table = this.createTable(this.capMinus1 + 1);
    }
    
    public HashMapDoubleDouble(int initialCapacity) {
        this(initialCapacity, 0.75F);
    }
    
    public HashMapDoubleDouble() {
        this(16, 0.75F);
    }
    
    public final boolean contains(double key) {
        int i = tableIndex(hashCodeDouble(key), this.capMinus1);
        
        for(HashMapDoubleDouble.Entry e = this.table[i]; e != null; e = e.next) {
            if(e.key == key) {
                return true;
            }
        }
        
        return false;
    }
    
    public boolean containsValue(double value) {
        for(Entry entry : this.table) {
            for(Entry e = entry; e != null; e = e.next) {
                if(value == e.value) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public double get(double key) {
        int i = tableIndex(hashCodeDouble(key), this.capMinus1);
        
        for(HashMapDoubleDouble.Entry e = this.table[i]; e != null; e = e.next) {
            if(key == e.key) {
                return e.value;
            }
        }
        
        return 4.9E-324D;
    }
    
    public double put(double key, double value) {
        int i = tableIndex(hashCodeDouble(key), this.capMinus1);
        
        for(HashMapDoubleDouble.Entry e = this.table[i]; e != null; e = e.next) {
            if(key == e.key) {
                double oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }
        
        this.addEntry(key, value, i);
        return 4.9E-324D;
    }
    
    public void resize(int newCapacity) {
        int oldCapacity = this.table.length;
        if(oldCapacity == 1073741824) {
            this.threshold = 2147483647;
        } else {
            HashMapDoubleDouble.Entry[] newTable = this.createTable(newCapacity);
            this.capMinus1 = newCapacity - 1;
            this.transfer(newTable);
            this.table = newTable;
            this.threshold = (int) ((float) newCapacity * this.loadFactor);
        }
    }
    
    public final HashMapDoubleDouble.Entry remove(double key) {
        int i = tableIndex(hashCodeDouble(key), this.capMinus1);
        HashMapDoubleDouble.Entry prev = this.table[i];
        
        HashMapDoubleDouble.Entry e;
        HashMapDoubleDouble.Entry next;
        for(e = prev; e != null; e = next) {
            next = e.next;
            if(key == e.key) {
                --this.size;
                if(prev == e) {
                    this.table[i] = next;
                } else {
                    prev.next = next;
                }
                
                return e;
            }
            
            prev = e;
        }
        
        return e;
    }
    
    public void clear() {
        Arrays.fill(this.table, null);
        
        this.size = 0;
    }
    
    public long memoryEstimate(int ptrsize) {
        return (long) ptrsize * (long) (this.capMinus1 + this.size + 1) + (this.size * 64L / 4);
    }
    
    public HashMapDoubleDouble.Iterator iterator() {
        return new HashMapDoubleDouble.Iterator();
    }
    
    private void addEntry(double key, double value, int index) {
        HashMapDoubleDouble.Entry e = this.table[index];
        this.table[index] = new HashMapDoubleDouble.Entry(key, value, e);
        if(this.size++ >= this.threshold) {
            this.resize(2 * this.table.length);
        }
        
    }
    
    private void transfer(HashMapDoubleDouble.Entry[] newTable) {
        for(int j = 0; j < this.table.length; ++j) {
            HashMapDoubleDouble.Entry e = this.table[j];
            if(e != null) {
                this.table[j] = null;
                
                HashMapDoubleDouble.Entry next;
                do {
                    next = e.next;
                    int i = tableIndex(hashCodeDouble(e.key), this.capMinus1);
                    e.next = newTable[i];
                    newTable[i] = e;
                    e = next;
                } while(next != null);
            }
        }
        
    }
    
    private HashMapDoubleDouble.Entry[] createTable(int capacity) {
        return new HashMapDoubleDouble.Entry[capacity];
    }
    
    public HashMapDoubleDouble.Entry getEntry(double key) {
        int i = tableIndex(hashCodeDouble(key), this.capMinus1);
        
        for(HashMapDoubleDouble.Entry e = this.table[i]; e != null; e = e.next) {
            if(key == e.key) {
                return e;
            }
        }
        
        return null;
    }
    
    
    public static class Entry implements Serializable {
        @Serial
        private static final long serialVersionUID = 7972173983741231238L;
        private final double key;
        private double value;
        private HashMapDoubleDouble.Entry next;
        
        public Entry(double key, double val, HashMapDoubleDouble.Entry n) {
            this.key = key;
            this.value = val;
            this.next = n;
        }
        
        public final double setValue(double newValue) {
            double oldValue = this.value;
            this.value = newValue;
            return oldValue;
        }
        
        public final double getKey() {
            return this.key;
        }
        
        public final double getValue() {
            return this.value;
        }
        
        public final int hashCode() {
            return hashCodeDouble(key) + hashCodeDouble(value);
        }
        
        public final boolean equals(Object o) {
            HashMapDoubleDouble.Entry e = (HashMapDoubleDouble.Entry) o;
            return this.key == e.key && this.value == e.value;
        }
        
        public final String toString() {
            return this.key + " = " + this.value;
        }
    }
    
    
    public class Iterator {
        HashMapDoubleDouble.Entry next;
        int index;
        HashMapDoubleDouble.Entry current;
        
        Iterator() {
            if(HashMapDoubleDouble.this.size > 0) {
                while(this.index < HashMapDoubleDouble.this.table.length &&
                      (this.next = HashMapDoubleDouble.this.table[this.index++]) == null) {
                }
            }
            
        }
        
        public HashMapDoubleDouble.Entry nextEntry() {
            HashMapDoubleDouble.Entry e = this.next;
            if(e == null) {
                throw new NoSuchElementException();
            } else {
                if((this.next = e.next) == null) {
                    while(this.index < HashMapDoubleDouble.this.table.length &&
                          (this.next = HashMapDoubleDouble.this.table[this.index++]) == null) {
                    }
                }
                
                this.current = e;
                return e;
            }
        }
        
        public double next() {
            return this.nextEntry().value;
        }
        
        public void remove() {
            if(this.current == null) {
                throw new IllegalStateException();
            } else {
                double k = this.current.key;
                this.current = null;
                HashMapDoubleDouble.this.remove(k);
            }
        }
        
        public final boolean hasNext() {
            return this.next != null;
        }
    }
}
