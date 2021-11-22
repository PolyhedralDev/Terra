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

package com.dfsek.terra.profiler.exception;

import java.io.Serial;


public class MalformedStackException extends ProfilerException {
    @Serial
    private static final long serialVersionUID = -3009539681021691054L;
    
    public MalformedStackException(String message) {
        super(message);
    }
    
    public MalformedStackException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public MalformedStackException(Throwable cause) {
        super(cause);
    }
}
