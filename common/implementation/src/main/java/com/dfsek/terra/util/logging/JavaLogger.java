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


public class JavaLogger implements Logger {
    private final java.util.logging.Logger logger;
    
    public JavaLogger(java.util.logging.Logger logger) {
        this.logger = logger;
    }
    
    @Override
    public void info(String message) {
        logger.info(message);
    }
    
    @Override
    public void warning(String message) {
        logger.warning(message);
    }
    
    @Override
    public void severe(String message) {
        logger.severe(message);
    }
}
