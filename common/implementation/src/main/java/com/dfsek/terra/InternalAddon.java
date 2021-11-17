package com.dfsek.terra;

import com.dfsek.terra.api.addon.BaseAddon;


public class InternalAddon implements BaseAddon {
    @Override
    public String getID() {
        return "terra";
    }
}
