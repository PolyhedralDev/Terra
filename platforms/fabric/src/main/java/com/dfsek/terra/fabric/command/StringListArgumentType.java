package com.dfsek.terra.fabric.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringListArgumentType implements ArgumentType<List<String>> {
    @Override
    public List<String> parse(StringReader reader) {
        final String text = reader.getRemaining();
        reader.setCursor(reader.getTotalLength());
        return new ArrayList<>(Arrays.asList(text.split(" ")));
    }
}
