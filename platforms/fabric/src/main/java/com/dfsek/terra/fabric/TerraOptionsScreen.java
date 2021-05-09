package com.dfsek.terra.fabric;

import com.dfsek.terra.api.util.mutable.MutableInteger;
import com.dfsek.terra.fabric.generation.FabricChunkGeneratorWrapper;
import com.dfsek.terra.fabric.generation.TerraBiomeSource;
import com.dfsek.terra.fabric.mixin.access.GeneratorTypeAccessor;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.CyclingOption;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

import java.util.ArrayList;
import java.util.List;

public class TerraOptionsScreen extends Screen {
    private final Screen parent;
    private final List<GeneratorType> generatorList = new ArrayList<>();
    private ButtonListWidget buttonListWidget;

    public TerraOptionsScreen(Screen parent) {
        super(new LiteralText("Terra Options"));
        this.parent = parent;
        generatorList.add(GeneratorType.DEFAULT);
        generatorList.add(GeneratorType.AMPLIFIED);

        TerraFabricPlugin.getInstance().getConfigRegistry().forEach(pack -> generatorList.add(new GeneratorType("terra." + pack.getTemplate().getID()) {
            {
                //noinspection ConstantConditions
                ((GeneratorTypeAccessor) (Object) this).setTranslationKey(new LiteralText("Terra:" + pack.getTemplate().getID()));
            }

            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
                return new FabricChunkGeneratorWrapper(new TerraBiomeSource(biomeRegistry, seed, pack), seed, pack);
            }
        }));
    }

    @Override
    protected void init() {
        addButton(new ButtonWidget(width / 2 - 60, height - 30, 120, 20, new TranslatableText("terra.screen.close"), btn -> client.openScreen(parent)));
        this.buttonListWidget = new ButtonListWidget(client, width - 20, height - 20, 32, height - 50, 25);

        buttonListWidget.setLeftPos(10);

        Registry<DimensionType> dimensionTypes = new DynamicRegistryManager.Impl().getDimensionTypes();

        dimensionTypes.forEach(System.out::println);

        buttonListWidget.addSingleOptionEntry(new GeneratorCycler("Overworld"));
        buttonListWidget.addSingleOptionEntry(new GeneratorCycler("Nether"));
        buttonListWidget.addSingleOptionEntry(new GeneratorCycler("End"));

        addChild(buttonListWidget);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.buttonListWidget.render(matrices, mouseX, mouseY, delta);
        DrawableHelper.drawCenteredText(matrices, textRenderer, new TranslatableText("terra.config-screen"), width / 2, 20, 0xffffff);
        super.render(matrices, mouseX, mouseY, delta);
    }

    private final class GeneratorCycler extends CyclingOption {
        private final MutableInteger amount = new MutableInteger(0);

        public GeneratorCycler(String key) {
            super(key, null, null);

        }

        @Override
        public Text getMessage(GameOptions options) {
            return ((BaseText) getDisplayPrefix()).copy().append(new LiteralText(": ")).append(generatorList.get(amount.get()).getTranslationKey().copy());
        }

        @Override
        public void cycle(GameOptions options, int amount) {
            this.amount.addMod(amount, generatorList.size());
        }

    }
}
