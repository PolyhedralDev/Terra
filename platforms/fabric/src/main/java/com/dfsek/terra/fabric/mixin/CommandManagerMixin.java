package com.dfsek.terra.fabric.mixin;

import com.dfsek.terra.api.command.exception.CommandException;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.fabric.FabricEntryPoint;
import com.dfsek.terra.fabric.TerraFabricPlugin;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

@Mixin(CommandManager.class)
public abstract class CommandManagerMixin {
    @Shadow
    @Final
    private CommandDispatcher<ServerCommandSource> dispatcher;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/CommandDispatcher;findAmbiguities(Lcom/mojang/brigadier/AmbiguityConsumer;)V", remap = false))
    private void injectTerraCommands(CommandManager.RegistrationEnvironment environment, CallbackInfo ci) {
        com.dfsek.terra.api.command.CommandManager manager = FabricEntryPoint.getTerraPlugin().getManager();
        int max = manager.getMaxArgumentDepth();
        RequiredArgumentBuilder<ServerCommandSource, String> arg = argument("arg" + (max - 1), StringArgumentType.word());
        for(int i = 0; i < max; i++) {
            RequiredArgumentBuilder<ServerCommandSource, String> next = argument("arg" + (max - i - 1), StringArgumentType.word());

            arg = next.then(assemble(arg, manager));
        }

        dispatcher.register(literal("terra").executes(context -> 1).then(assemble(arg, manager)));
        dispatcher.register(literal("te").executes(context -> 1).then(assemble(arg, manager)));
    }

    private RequiredArgumentBuilder<ServerCommandSource, String> assemble(RequiredArgumentBuilder<ServerCommandSource, String> in, com.dfsek.terra.api.command.CommandManager manager) {
        return in.suggests((context, builder) -> {
            List<String> args = parseCommand(context.getInput());
            CommandSender sender = (CommandSender) context.getSource();
            try {
                sender = (Entity) context.getSource().getEntityOrThrow();
            } catch(CommandSyntaxException ignore) {
            }
            try {
                manager.tabComplete(args.remove(0), sender, args).forEach(builder::suggest);
            } catch(CommandException e) {
                sender.sendMessage(e.getMessage());
            }
            return builder.buildFuture();
        }).executes(context -> {
            List<String> args = parseCommand(context.getInput());
            CommandSender sender = (CommandSender) context.getSource();
            try {
                sender = (Entity) context.getSource().getEntityOrThrow();
            } catch(CommandSyntaxException ignore) {
            }
            try {
                manager.execute(args.remove(0), sender, args);
            } catch(CommandException e) {
                context.getSource().sendError(new LiteralText(e.getMessage()));
            }
            return 1;
        });
    }

    private List<String> parseCommand(String command) {
        if(command.startsWith("/terra ")) command = command.substring("/terra ".length());
        else if(command.startsWith("/te ")) command = command.substring("/te ".length());
        List<String> c = new ArrayList<>(Arrays.asList(command.split(" ")));
        if(command.endsWith(" ")) c.add("");
        return c;
    }
}
