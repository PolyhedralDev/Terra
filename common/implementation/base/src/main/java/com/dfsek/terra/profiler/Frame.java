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

package com.dfsek.terra.profiler;

public class Frame {
    private final String id;
    private final long start;
    
    public Frame(String id) {
        this.id = id;
        this.start = System.nanoTime();
    }
    
    @Override
    public String toString() {
        return id;
    }
    
    public String getId() {
        return id;
    }
    
    public long getStart() {
        return start;
    }
}
