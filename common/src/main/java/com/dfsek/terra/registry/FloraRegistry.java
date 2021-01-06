package com.dfsek.terra.registry;

import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.world.flora.Flora;
import com.dfsek.terra.population.items.flora.ConstantFlora;
import com.dfsek.terra.util.MaterialSet;

import java.util.Arrays;
import java.util.Collections;

public class FloraRegistry extends TerraRegistry<Flora> {
    private final TerraPlugin main;

    public FloraRegistry(TerraPlugin main) {
        this.main = main;
        MaterialSet grassy = MaterialSet.get(create("minecraft:grass_block"), create("minecraft:podzol"));
        addItem("TALL_GRASS", new ConstantFlora(grassy, Arrays.asList(data("minecraft:tall_grass[half=lower]"), data("minecraft:tall_grass[half=upper]"))));
        addItem("TALL_FERN", new ConstantFlora(grassy, Arrays.asList(data("minecraft:large_fern[half=lower]"), data("minecraft:large_fern[half=upper]"))));
        addItem("SUNFLOWER", new ConstantFlora(grassy, Arrays.asList(data("minecraft:sunflower[half=lower]"), data("minecraft:sunflower[half=upper]"))));
        addItem("ROSE_BUSH", new ConstantFlora(grassy, Arrays.asList(data("minecraft:rose_bush[half=lower]"), data("minecraft:rose_bush[half=upper]"))));
        addItem("LILAC", new ConstantFlora(grassy, Arrays.asList(data("minecraft:lilac[half=lower]"), data("minecraft:lilac[half=upper]"))));
        addItem("PEONY", new ConstantFlora(grassy, Arrays.asList(data("minecraft:peony[half=lower]"), data("minecraft:peony[half=upper]"))));
        addItem("GRASS", new ConstantFlora(grassy, Collections.singletonList(data("minecraft:grass"))));
        addItem("FERN", new ConstantFlora(grassy, Collections.singletonList(data("minecraft:fern"))));
        addItem("AZURE_BLUET", new ConstantFlora(grassy, Collections.singletonList(data("minecraft:azure_bluet"))));
        addItem("LILY_OF_THE_VALLEY", new ConstantFlora(grassy, Collections.singletonList(data("minecraft:lily_of_the_valley"))));
        addItem("BLUE_ORCHID", new ConstantFlora(grassy, Collections.singletonList(data("minecraft:blue_orchid"))));
        addItem("POPPY", new ConstantFlora(grassy, Collections.singletonList(data("minecraft:poppy"))));
        addItem("DANDELION", new ConstantFlora(grassy, Collections.singletonList(data("minecraft:dandelion"))));
        addItem("WITHER_ROSE", new ConstantFlora(grassy, Collections.singletonList(data("minecraft:wither_rose"))));
        addItem("DEAD_BUSH", new ConstantFlora(MaterialSet.get(create("minecraft:terracotta"), create("minecraft:black_terracotta"),
                create("minecraft:blue_terracotta"), create("minecraft:brown_terracotta"), create("minecraft:cyan_terracotta"),
                create("minecraft:gray_terracotta"), create("minecraft:green_terracotta"), create("minecraft:light_blue_terracotta"),
                create("minecraft:light_gray_terracotta"), create("minecraft:lime_terracotta"), create("minecraft:magenta_terracotta"),
                create("minecraft:orange_terracotta"), create("minecraft:pink_terracotta"), create("minecraft:purple_terracotta"),
                create("minecraft:red_terracotta"), create("minecraft:white_terracotta"), create("minecraft:yellow_terracotta"),
                create("minecraft:red_sand"), create("minecraft:sand")), Collections.singletonList(data("minecraft:dead_bush"))));
        addItem("RED_TULIP", new ConstantFlora(grassy, Collections.singletonList(data("minecraft:red_tulip"))));
        addItem("ORANGE_TULIP", new ConstantFlora(grassy, Collections.singletonList(data("minecraft:orange_tulip"))));
        addItem("WHITE_TULIP", new ConstantFlora(grassy, Collections.singletonList(data("minecraft:white_tulip"))));
        addItem("PINK_TULIP", new ConstantFlora(grassy, Collections.singletonList(data("minecraft:pink_tulip"))));
        addItem("OXEYE_DAISY", new ConstantFlora(grassy, Collections.singletonList(data("minecraft:oxeye_daisy"))));
        addItem("ALLIUM", new ConstantFlora(grassy, Collections.singletonList(data("minecraft:allium"))));
        addItem("CORNFLOWER", new ConstantFlora(grassy, Collections.singletonList(data("minecraft:cornflower"))));
        addItem("LILY_PAD", new ConstantFlora(MaterialSet.get(create("minecraft:water")), Collections.singletonList(data("minecraft:lily_pad"))));
        MaterialSet mushroom = MaterialSet.get(create("minecraft:grass_block"), create("minecraft:stone"), create("minecraft:podzol"), create("minecraft:netherrack"), create("minecraft:mycelium"));
        addItem("RED_MUSHROOM", new ConstantFlora(mushroom, Collections.singletonList(data("minecraft:red_mushroom"))));
        addItem("BROWN_MUSHROOM", new ConstantFlora(mushroom, Collections.singletonList(data("minecraft:brown_mushroom"))));
    }

    private MaterialData create(String s) {
        return main.getWorldHandle().createMaterialData(s);
    }

    private void addItem(String id, ConstantFlora flora) {
        try {
            add(id, flora);
        } catch(IllegalArgumentException e) {
            main.getLogger().warning("Failed to load Flora item: " + id + ": " + e.getMessage());
        }
    }

    private BlockData data(String s) {
        return main.getWorldHandle().createBlockData(s);
    }


    @Override
    public Flora get(String id) {
        return super.get(id);
    }
}
