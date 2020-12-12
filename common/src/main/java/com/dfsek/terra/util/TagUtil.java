package com.dfsek.terra.util;

import com.dfsek.terra.api.generic.world.block.MaterialData;
import com.dfsek.terra.debug.Debug;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public final class TagUtil {
    private static final Map<String, Set<MaterialData>> tagMap;

    static {
        Debug.info("Loading tags...");
        tagMap = new HashMap<>();

        /*
        Field[] tags = Tag.class.getFields(); // Add Bukkit tags
        for(Field field : tags) {
            if(Modifier.isStatic(field.getModifiers())) {
                try {
                    Tag<Material> tag = (Tag<Material>) field.get(new Object());
                    tagMap.put(tag.getKey().toString(), tag.getValues());
                } catch(IllegalAccessException e) {
                    e.printStackTrace();
                } catch(ClassCastException ignore) {
                }
            }
        }
        putCustomSet("minecraft:base_stone_nether", Material.NETHERRACK, Material.BASALT, Material.BLACKSTONE);
        putCustomSet("minecraft:base_stone_overworld", Material.STONE, Material.GRANITE, Material.DIORITE, Material.ANDESITE);

        Set<Material> snow = new HashSet<>();
        Set<Material> solid = new HashSet<>();
        for(Material m : Material.values()) {
            if(m.isSolid()) solid.add(m);
            String name = m.toString().toLowerCase();
            if(name.contains("slab")
                    || name.contains("stair")
                    || name.contains("wall")
                    || name.contains("fence")
                    || name.contains("lantern")
                    || name.contains("chest")
                    || name.contains("door")
                    || name.contains("repeater")
                    || name.equals("lily_pad")
                    || name.equals("snow")
                    || name.equals("pane")
                    || !m.isSolid()) snow.add(m);
        }
        tagMap.put("com.dfsek.terra:snow_blacklist", snow);
        tagMap.put("com.dfsek.terra:solid", solid);
        Debug.info("Added " + snow.size() + " materials to snow blacklist");
        Debug.info("Added " + solid.size() + " materials to solid list");
        Debug.info("Loaded " + tagMap.size() + " tags.");

         */
    }

    private static Set<MaterialData> getSet(MaterialData... materials) {
        return Stream.of(materials).collect(Collectors.toSet());
    }

    private static void putCustomSet(String key, MaterialData... materials) {
        tagMap.put(key, getSet(materials));
    }

    @NotNull
    public static Set<MaterialData> getTag(String tag) {
        return Objects.requireNonNull(tagMap.get(tag));
    }
}
