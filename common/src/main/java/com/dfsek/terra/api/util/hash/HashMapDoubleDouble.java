/*
Copyright 2009 Sandia Corporation. Under the terms of Contract
DE-AC04-94AL85000 with Sandia Corporation, the U.S. Government
retains certain rights in this software.

BSD Open Source License.
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

   * Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in the
     documentation and/or other materials provided with the distribution.
   * Neither the name of Sandia National Laboratories nor the names of its
     contributors may be used to endorse or promote products derived from
     this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
 */

package com.dfsek.terra.api.util.hash;

import java.io.Serializable;
import java.util.NoSuchElementException;

public class HashMapDoubleDouble extends HashIntrinsic {
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
        for(int i = 0; i < this.table.length; ++i) {
            for(HashMapDoubleDouble.Entry e = this.table[i]; e != null; e = e.next) {
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

    public HashMapDoubleDouble.Entry getEntry(double key) {
        int i = tableIndex(hashCodeDouble(key), this.capMinus1);

        for(HashMapDoubleDouble.Entry e = this.table[i]; e != null; e = e.next) {
            if(key == e.key) {
                return e;
            }
        }

        return null;
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

    private void addEntry(double key, double value, int index) {
        HashMapDoubleDouble.Entry e = this.table[index];
        this.table[index] = new HashMapDoubleDouble.Entry(key, value, e);
        if(this.size++ >= this.threshold) {
            this.resize(2 * this.table.length);
        }

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
        for(int i = 0; i < this.table.length; ++i) {
            this.table[i] = null;
        }

        this.size = 0;
    }

    private HashMapDoubleDouble.Entry[] createTable(int capacity) {
        return new HashMapDoubleDouble.Entry[capacity];
    }

    public long memoryEstimate(int ptrsize) {
        return (long) ptrsize * (long) (this.capMinus1 + this.size + 1) + (long) (this.size * 64 / 4);
    }

    public HashMapDoubleDouble.Iterator iterator() {
        return new HashMapDoubleDouble.Iterator();
    }

    public static class Entry implements Serializable {
        private static final long serialVersionUID = 7972173983741231238L;
        private final double key;
        private double value;
        private HashMapDoubleDouble.Entry next;

        public Entry(double key, double val, HashMapDoubleDouble.Entry n) {
            this.key = key;
            this.value = val;
            this.next = n;
        }

        public final double getKey() {
            return this.key;
        }

        public final double getValue() {
            return this.value;
        }

        public final double setValue(double newValue) {
            double oldValue = this.value;
            this.value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            HashMapDoubleDouble.Entry e = (HashMapDoubleDouble.Entry) o;
            return this.key == e.key && this.value == e.value;
        }

        public final String toString() {
            return this.key + " = " + this.value;
        }

        public final int hashCode() {
            return hashCodeDouble(key) + hashCodeDouble(value);
        }
    }

    public class Iterator {
        HashMapDoubleDouble.Entry next;
        int index;
        HashMapDoubleDouble.Entry current;

        Iterator() {
            if(HashMapDoubleDouble.this.size > 0) {
                while(this.index < HashMapDoubleDouble.this.table.length && (this.next = HashMapDoubleDouble.this.table[this.index++]) == null) {
                }
            }

        }

        public final boolean hasNext() {
            return this.next != null;
        }

        public HashMapDoubleDouble.Entry nextEntry() {
            HashMapDoubleDouble.Entry e = this.next;
            if(e == null) {
                throw new NoSuchElementException();
            } else {
                if((this.next = e.next) == null) {
                    while(this.index < HashMapDoubleDouble.this.table.length && (this.next = HashMapDoubleDouble.this.table[this.index++]) == null) {
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
    }
}
