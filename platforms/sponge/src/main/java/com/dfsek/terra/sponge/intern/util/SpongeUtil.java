package com.dfsek.terra.sponge.intern.util;

import com.dfsek.terra.api.transform.Transformer;
import com.dfsek.terra.api.transform.Validator;
import com.dfsek.terra.config.pack.ConfigPack;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import java.util.Locale;

public final class SpongeUtil {
    public static String createBiomeID(ConfigPack pack, String biomeID) {
        return pack.getTemplate().getID().toLowerCase() + "/" + biomeID.toLowerCase(Locale.ROOT);
    }

    public static final Transformer<String, Biome> BIOME_FIXER = new Transformer.Builder<String, Biome>()
            .addTransform(id -> BuiltinRegistries.BIOME.get(ResourceLocation.tryParse(id)), Validator.notNull())
            .addTransform(id -> BuiltinRegistries.BIOME.get(ResourceLocation.tryParse("minecraft:" + id.toLowerCase())), Validator.notNull()).build();

}
