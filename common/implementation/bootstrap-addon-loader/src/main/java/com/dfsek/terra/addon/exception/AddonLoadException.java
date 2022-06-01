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

package com.dfsek.terra.addon.exception;

import java.io.Serial;


public class AddonLoadException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -4949084729296580176L;
    
    public AddonLoadException(String message) {
        super(message);
    }
    
    public AddonLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
