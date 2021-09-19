package com.dfsek.terra.api.command.arg;

import com.dfsek.terra.api.entity.CommandSender;


public class DoubleArgumentParser implements ArgumentParser<Double> {
    @Override
    public Double parse(CommandSender sender, String arg) {
        return arg == null ? null : Double.parseDouble(arg);
    }
}
