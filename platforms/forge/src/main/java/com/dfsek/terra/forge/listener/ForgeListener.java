package com.dfsek.terra.forge.listener;

import com.dfsek.terra.api.command.CommandManager;
import com.dfsek.terra.api.command.exception.CommandException;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.forge.TerraForgePlugin;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;

@Mod.EventBusSubscriber(modid = "terra", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeListener {
    private static final TerraForgePlugin INSTANCE = TerraForgePlugin.getInstance();

    @SuppressWarnings({"unchecked", "rawtypes"})
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        int max = INSTANCE.getManager().getMaxArgumentDepth();
        RequiredArgumentBuilder<CommandSource, String> arg = argument("arg" + (max - 1), StringArgumentType.word());
        for(int i = 0; i < max; i++) {
            RequiredArgumentBuilder<CommandSource, String> next = argument("arg" + (max - i - 1), StringArgumentType.word());

            arg = next.then(assemble(arg, INSTANCE.getManager()));
        }

        event.getDispatcher().register(literal("terra").executes(context -> 1).then((ArgumentBuilder) assemble(arg, INSTANCE.getManager())));
        event.getDispatcher().register(literal("te").executes(context -> 1).then((ArgumentBuilder) assemble(arg, INSTANCE.getManager())));
    }

    public static RequiredArgumentBuilder<CommandSource, String> assemble(RequiredArgumentBuilder<CommandSource, String> in, CommandManager manager) {
        return in.suggests((context, builder) -> {
            List<String> args = parseCommand(context.getInput());
            CommandSender sender = (CommandSender) context.getSource();
            try {
                manager.tabComplete(args.remove(0), sender, args).forEach(builder::suggest);
            } catch(CommandException e) {
                sender.sendMessage(e.getMessage());
            }
            return builder.buildFuture();
        }).executes(context -> {
            List<String> args = parseCommand(context.getInput());
            try {
                manager.execute(args.remove(0), (CommandSender) context.getSource(), args);
            } catch(CommandException e) {
                context.getSource().sendFailure(new StringTextComponent(e.getMessage()));
            }
            return 1;
        });
    }

    private static List<String> parseCommand(String command) {
        if(command.startsWith("/terra ")) command = command.substring("/terra ".length());
        else if(command.startsWith("/te ")) command = command.substring("/te ".length());
        List<String> c = new ArrayList<>(Arrays.asList(command.split(" ")));
        if(command.endsWith(" ")) c.add("");
        return c;
    }
}
