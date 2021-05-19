package com.dfsek.terra.forge.listener;

import com.dfsek.terra.api.util.mutable.MutableInteger;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.forge.TerraForgePlugin;
import com.dfsek.terra.forge.generation.TerraLevelType;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ForgeWorldTypeScreens;
import net.minecraftforge.common.world.ForgeWorldType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientListener {
    private static final TerraForgePlugin INSTANCE = TerraForgePlugin.getInstance();

    @SubscribeEvent
    public static void register(FMLClientSetupEvent event) {
        INSTANCE.logger().info("Client setup...");

        ForgeWorldType world = TerraLevelType.FORGE_WORLD_TYPE;
        ForgeWorldTypeScreens.registerFactory(world, (returnTo, dimensionGeneratorSettings) -> new Screen(world.getDisplayName()) {
            private final MutableInteger num = new MutableInteger(0);
            private final List<ConfigPack> packs = new ArrayList<>();
            private final Button toggle = new Button(width/2, 25, 120, 20, new StringTextComponent(""), button -> {
                num.increment();
                if(num.get() >= packs.size()) num.set(0);
                button.setMessage(new StringTextComponent("Pack: " + packs.get(num.get()).getTemplate().getID()));
            });

            @Override
            protected void init() {
                packs.clear();
                INSTANCE.getConfigRegistry().forEach((Consumer<ConfigPack>) packs::add);
                addButton(new Button(0, 0, 120, 20, new StringTextComponent("Close"), btn -> Minecraft.getInstance().setScreen(returnTo)));
                toggle.setMessage(new StringTextComponent("Pack: " + packs.get(num.get()).getTemplate().getID()));
                addButton(toggle);
            }

            @Override
            public void render(@NotNull MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
                renderBackground(p_230430_1_);
                super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
            }
        });
    }
}
