package com.dfsek.terra.structure.serialize.block;

import org.bukkit.DyeColor;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import java.io.Serializable;
import java.util.ArrayList;

public class SerializableBanner implements SerializableBlockState {
    public static final long serialVersionUID = 5298928608478640004L;
    private final DyeColor base;
    private final ArrayList<Pattern> patterns = new ArrayList<>();

    public SerializableBanner(Banner banner) {
        this.base = banner.getBaseColor();
        for(org.bukkit.block.banner.Pattern bukkitPattern : banner.getPatterns()) {
            patterns.add(new Pattern(bukkitPattern.getPattern(), bukkitPattern.getColor()));
        }
    }

    @Override
    public BlockState getState(BlockState orig) {
        if(!(orig instanceof Banner)) throw new IllegalArgumentException("Provided BlockState is not a banner.");
        Banner banner = (Banner) orig;
        banner.setBaseColor(base);
        for(Pattern pattern : patterns) {
            banner.addPattern(new org.bukkit.block.banner.Pattern(pattern.getColor(), pattern.getType()));
        }
        return banner;
    }
    private static final class Pattern implements Serializable {
        private final DyeColor color;
        private final PatternType type;
        public Pattern(PatternType type, DyeColor color) {
            this.color = color;
            this.type = type;
        }

        public DyeColor getColor() {
            return color;
        }

        public PatternType getType() {
            return type;
        }
    }
}
