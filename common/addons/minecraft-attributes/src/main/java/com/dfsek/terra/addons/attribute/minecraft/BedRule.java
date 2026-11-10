package com.dfsek.terra.addons.attribute.minecraft;

import java.util.Optional;

import com.dfsek.terra.api.registry.key.StringIdentifiable;
import com.dfsek.terra.api.world.attribute.RawValue;


public record BedRule(Rule canSleep, Rule canSetSpawn, boolean destroyOnUse, boolean destroyOnLeave, Optional<RawValue> errorMessage) {
    public enum Rule implements StringIdentifiable {
        ALWAYS("always"),
        WHEN_DARK("when_dark"),
        NEVER("never");

        private final String id;

        Rule(String id) {
            this.id = id;
        }

        @Override
        public String getID() {
            return id;
        }
    }
}
