package com.dfsek.terra.fabric;

import com.dfsek.terra.api.command.CommandManager;
import com.dfsek.terra.api.command.exception.CommandException;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.api.platform.entity.Entity;
import com.dfsek.terra.config.builder.BiomeBuilder;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.fabric.config.PackFeatureOptionsTemplate;
import com.dfsek.terra.fabric.mixin.access.BiomeEffectsAccessor;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.block.Blocks;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class FabricUtil {
    public static String createBiomeID(ConfigPack pack, String biomeID) {
        return pack.getTemplate().getID().toLowerCase() + "/" + biomeID.toLowerCase(Locale.ROOT);
    }

    protected static void registerCommands(CommandManager manager) {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
                    int max = manager.getMaxArgumentDepth();
                    RequiredArgumentBuilder<ServerCommandSource, String> arg = argument("arg" + (max - 1), StringArgumentType.word());
                    for(int i = 0; i < max; i++) {
                        RequiredArgumentBuilder<ServerCommandSource, String> next = argument("arg" + (max - i - 1), StringArgumentType.word());

                        arg = next.then(FabricUtil.assemble(arg, manager));
                    }

                    dispatcher.register(literal("terra").executes(context -> 1).then(FabricUtil.assemble(arg, manager)));
                    dispatcher.register(literal("te").executes(context -> 1).then(FabricUtil.assemble(arg, manager)));
                    //dispatcher.register(literal("te").redirect(root));
                }
        );
    }

    private static RequiredArgumentBuilder<ServerCommandSource, String> assemble(RequiredArgumentBuilder<ServerCommandSource, String> in, CommandManager manager) {
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

    private static List<String> parseCommand(String command) {
        if(command.startsWith("/terra ")) command = command.substring("/terra ".length());
        else if(command.startsWith("/te ")) command = command.substring("/te ".length());
        List<String> c = new ArrayList<>(Arrays.asList(command.split(" ")));
        if(command.endsWith(" ")) c.add("");
        return c;
    }

    static Biome createBiome(TerraFabricPlugin.FabricAddon fabricAddon, BiomeBuilder biome, ConfigPack pack) {
        BiomeTemplate template = biome.getTemplate();
        Map<String, Integer> colors = template.getColors();

        Biome vanilla = (Biome) (new ArrayList<>(biome.getVanillaBiomes().getContents()).get(0)).getHandle();

        GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();
        generationSettings.surfaceBuilder(SurfaceBuilder.DEFAULT.withConfig(new TernarySurfaceConfig(Blocks.GRASS_BLOCK.getDefaultState(), Blocks.DIRT.getDefaultState(), Blocks.GRAVEL.getDefaultState()))); // It needs a surfacebuilder, even though we dont use it.
        generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, TerraFabricPlugin.POPULATOR_CONFIGURED_FEATURE);

        PackFeatureOptionsTemplate optionsTemplate = fabricAddon.getTemplates().get(pack);

        if(optionsTemplate.doBiomeInjection()) {
            for(int step = 0; step < vanilla.getGenerationSettings().getFeatures().size(); step++) {
                for(Supplier<ConfiguredFeature<?, ?>> featureSupplier : vanilla.getGenerationSettings().getFeatures().get(step)) {
                    Identifier key = BuiltinRegistries.CONFIGURED_FEATURE.getId(featureSupplier.get());
                    if(!optionsTemplate.getExcludedBiomeFeatures().contains(key)) {
                        generationSettings.feature(step, featureSupplier);
                    }
                }
            }
        }

        BiomeEffectsAccessor accessor = (BiomeEffectsAccessor) vanilla.getEffects();
        BiomeEffects.Builder effects = new BiomeEffects.Builder()
                .waterColor(colors.getOrDefault("water", accessor.getWaterColor()))
                .waterFogColor(colors.getOrDefault("water-fog", accessor.getWaterFogColor()))
                .fogColor(colors.getOrDefault("fog", accessor.getFogColor()))
                .skyColor(colors.getOrDefault("sky", accessor.getSkyColor()))
                .grassColorModifier(accessor.getGrassColorModifier());

        if(colors.containsKey("grass")) {
            effects.grassColor(colors.get("grass"));
        } else {
            accessor.getGrassColor().ifPresent(effects::grassColor);
        }
        if(colors.containsKey("foliage")) {
            effects.foliageColor(colors.get("foliage"));
        } else {
            accessor.getFoliageColor().ifPresent(effects::foliageColor);
        }

        return new Biome.Builder()
                .precipitation(vanilla.getPrecipitation())
                .category(vanilla.getCategory())
                .depth(vanilla.getDepth())
                .scale(vanilla.getScale())
                .temperature(vanilla.getTemperature())
                .downfall(vanilla.getDownfall())
                .effects(effects.build())
                .spawnSettings(vanilla.getSpawnSettings())
                .generationSettings(generationSettings.build())
                .build();
    }
}
