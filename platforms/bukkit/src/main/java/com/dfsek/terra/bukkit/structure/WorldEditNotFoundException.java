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

package com.dfsek.terra.bukkit.structure;

import java.io.Serial;


public class WorldEditNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 3678822468346338227L;
    
    public WorldEditNotFoundException() {
    }
    
    public WorldEditNotFoundException(String message) {
        super(message);
    }
    
    public WorldEditNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public WorldEditNotFoundException(Throwable cause) {
        super(cause);
    }
    
    public WorldEditNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
