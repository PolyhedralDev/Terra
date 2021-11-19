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

package com.dfsek.terra.util.logging;

import com.dfsek.terra.api.util.Logger;


public class DebugLogger implements Logger {
    private final Logger logger;
    private boolean debug = false;
    
    public DebugLogger(Logger logger) {
        this.logger = logger;
    }
    
    public void info(String message) {
        if(debug) logger.info(message);
    }
    
    public void warning(String message) {
        if(debug) logger.warning(message);
    }
    
    public void severe(String message) {
        if(debug) logger.severe(message);
    }
    
    public void stack(Throwable e) {
        if(debug) e.printStackTrace();
    }
    
    public boolean isDebug() {
        return debug;
    }
    
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
