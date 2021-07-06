package com.dfsek.terra.api.entity;

import com.dfsek.terra.api.Handle;

public interface CommandSender extends Handle {
    void sendMessage(String message);
}
