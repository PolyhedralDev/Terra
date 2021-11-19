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

package com.dfsek.terra.config.lang;


import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.lang.Message;


public class SingleLineMessage implements Message {
    private final String message;
    
    public SingleLineMessage(String message) {
        this.message = message;
    }
    
    @Override
    public void log(Logger logger, Level level, String... args) {
        logger.log(level, String.format(message, Arrays.asList(args).toArray()));
    }
    
    @Override
    public void send(CommandSender sender, String... args) {
        sender.sendMessage(String.format(message, Arrays.asList(args).toArray()));
    }
    
    @Override
    public boolean isEmpty() {
        return message == null || message.equals("");
    }
}
