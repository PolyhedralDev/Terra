package com.dfsek.terra.util;

import com.dfsek.terra.Debug;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public final class TagUtil {
    private static final Map<String, Set<Material>> tagMap;

    static {
        Debug.info("Loading tags...");
        tagMap = new HashMap<>();

        Field[] tags = Tag.class.getFields(); // Add Bukkit tags
        for(Field field : tags) {
            if(Modifier.isStatic(field.getModifiers())) {
                try {
                    Tag<Material> tag = (Tag<Material>) field.get(new Object());
                    tagMap.put(tag.getKey().toString(), tag.getValues());
                    Debug.info("Loaded tag: #" + tag.getKey().toString());
                } catch(IllegalAccessException e) {
                    e.printStackTrace();
                } catch(ClassCastException ignore) {
                }
            }
        }
        putCustomSet("minecraft:base_stone_nether", Material.NETHERRACK, Material.BASALT, Material.BLACKSTONE);
        putCustomSet("minecraft:base_stone_overworld", Material.STONE, Material.GRANITE, Material.DIORITE, Material.ANDESITE);
    }

    private static Set<Material> getSet(Material... materials) {
        return Stream.of(materials).collect(Collectors.toSet());
    }

    private static void putCustomSet(String key, Material... materials) {
        tagMap.put(key, getSet(materials));
        Debug.info("Loaded tag: #" + key);
    }

    @NotNull
    public static Set<Material> getTag(String tag) {
        return Objects.requireNonNull(tagMap.get(tag));
    }
}
