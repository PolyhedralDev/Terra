package org.allaymc.terra.allay;

import com.google.gson.reflect.TypeToken;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.extern.slf4j.Slf4j;
import org.allaymc.api.block.property.type.BlockPropertyType.BlockPropertyValue;
import org.allaymc.api.block.registry.BlockTypeRegistry;
import org.allaymc.api.block.type.BlockState;
import org.allaymc.api.block.type.BlockTypes;
import org.allaymc.api.utils.Identifier;
import org.allaymc.api.utils.JSONUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * Terra Project 2024/6/16
 *
 * @author daoge_cmd
 */
@Slf4j
public final class Mapping {

    private static final Map<String, Map<String, String>> JE_BLOCK_DEFAULT_PROPERTIES = new Object2ObjectOpenHashMap<>();
    private static final Map<BlockState, JeBlockState> BLOCK_STATE_BE_TO_JE = new Object2ObjectOpenHashMap<>();
    private static final Map<Integer, BlockState> BLOCK_STATE_JE_HASH_TO_BE = new Int2ObjectOpenHashMap<>();
    private static final Map<String, String> ITEM_ID_JE_TO_BE = new Object2ObjectOpenHashMap<>();
    private static final Map<String, Integer> BIOME_ID_JE_TO_BE = new Object2IntOpenHashMap<>();
    private static final BlockState BE_AIR_STATE = BlockTypes.AIR_TYPE.getDefaultState();

    public static void init() {
        if(!initBlockStateMapping()) error();
        if (!initJeBlockDefaultProperties()) error();
        if(!initItemMapping()) error();
        if(!initBiomeMapping()) error();
    }

    private static void error() {
        throw new RuntimeException("Mapping not initialized");
    }

    private static boolean initBiomeMapping() {
        try (var stream = Mapping.class.getClassLoader().getResourceAsStream("mapping/biomes.json")) {
            if  (stream == null) {
                log.error("biomes.json not found");
                return false;
            }
            var mappings = JSONUtils.from(stream, new TypeToken<Map<String, Map<String, Integer>>>(){}).entrySet();
            for(var mapping : mappings) {
                BIOME_ID_JE_TO_BE.put(mapping.getKey(), mapping.getValue().get("bedrock_id"));
            }
        } catch(IOException e) {
            log.error("Failed to load mapping", e);
            return false;
        }
        return true;
    }

    private static boolean initItemMapping() {
        try (var stream = Mapping.class.getClassLoader().getResourceAsStream("mapping/items.json")) {
            if  (stream == null) {
                log.error("items.json not found");
                return false;
            }
            var mappings = JSONUtils.from(stream, new TypeToken<Map<String, Map<String, Object>>>(){}).entrySet();
            for(var mapping : mappings) {
                ITEM_ID_JE_TO_BE.put(mapping.getKey(), (String) mapping.getValue().get("bedrock_identifier"));
            }
        } catch(IOException e) {
            log.error("Failed to load mapping", e);
        }
        return true;
    }

    private static boolean initBlockStateMapping() {
        try (var stream = Mapping.class.getClassLoader().getResourceAsStream("mapping/blocks.json")) {
            if (stream == null) {
                log.error("blocks.json not found");
                return false;
            }
            var mappings = (List<Map<String, Map<String, Object>>>) JSONUtils.from(stream, new TypeToken<Map<String, Object>>(){}).get("mappings");
            for(var mapping : mappings) {
                var jeState = createJeBlockState(mapping.get("java_state"));
                var beState = createBeBlockState(mapping.get("bedrock_state"));
                BLOCK_STATE_BE_TO_JE.put(beState, jeState);
                BLOCK_STATE_JE_HASH_TO_BE.put(jeState.getHash(), beState);
            }
        } catch(IOException e) {
            log.error("Failed to load mapping", e);
        }
        return true;
    }

    private static boolean initJeBlockDefaultProperties() {
        try (var stream = Mapping.class.getClassLoader().getResourceAsStream("je_block_default_states.json")) {
            if (stream == null) {
                log.error("je_block_default_states.json not found");
                return false;
            }
            var states = JSONUtils.from(stream, new TypeToken<Map<String, Map<String, String>>>(){});
            for(var entry : states.entrySet()) {
                var identifier = entry.getKey();
                var properties = entry.getValue();
                JE_BLOCK_DEFAULT_PROPERTIES.put(identifier, properties);
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private static BlockState createBeBlockState(Map<String, Object> data) {
        var identifier = new Identifier((String) data.get("bedrock_identifier"));
        // 方块类型
        var blockType = BlockTypeRegistry.getRegistry().get(identifier);
        // 方块属性
        Map<String, Object> state = (Map<String, Object>) data.get("state");
        if (state == null) return blockType.getDefaultState();
        var propertyValues = new BlockPropertyValue<?, ?, ?>[state.size()];
        int index = -1;
        for(var entry : state.entrySet()) {
            index++;
            var propertyName = entry.getKey();
            var propertyType = blockType.getProperties().get(propertyName);
            if (propertyType == null) {
                log.warn("Unknown property type: {}", propertyName);
                return BlockTypes.AIR_TYPE.getDefaultState();
            }
            try {
                propertyValues[index] = propertyType.tryCreateValue(entry.getValue());
            } catch (IllegalArgumentException e) {
                log.warn("Failed to create property value for {}: {}", propertyName, entry.getValue());
                return BE_AIR_STATE;
            }
        }
        return blockType.ofState(propertyValues);
    }

    private static JeBlockState createJeBlockState(Map<String, Object> data) {
        var identifier = (String) data.get("Name");
        var properties = (Map<String, String>) data.getOrDefault("Properties", Map.of());
        return JeBlockState.create(identifier, new TreeMap<>(properties));
    }

    public static JeBlockState blockStateBeToJe(BlockState beBlockState) {
        return BLOCK_STATE_BE_TO_JE.get(beBlockState);
    }

    public static BlockState blockStateJeToBe(JeBlockState jeBlockState) {
        var result = BLOCK_STATE_JE_HASH_TO_BE.get(jeBlockState.getHash());
        if(result == null) {
            log.warn("Failed to find be block state for {}", jeBlockState);
            return BlockTypes.AIR_TYPE.getDefaultState();
        }
        return result;
    }

    public static String itemIdJeToBe(String jeItemId) {
        return ITEM_ID_JE_TO_BE.get(jeItemId);
    }

    // Enchantment identifiers are same in both versions

    public static String enchantmentIdBeToJe(String beEnchantmentId) {
        return beEnchantmentId;
    }

    public static String enchantmentIdJeToBe(String jeEnchantmentId) {
        return jeEnchantmentId;
    }

    public static int biomeIdJeToBe(String jeBiomeId) {
        return BIOME_ID_JE_TO_BE.get(jeBiomeId);
    }

    public static Map<String, String> getJeBlockDefaultProperties(String jeBlockIdentifier) {
        var defaultProperties = JE_BLOCK_DEFAULT_PROPERTIES.get(jeBlockIdentifier);
        if( defaultProperties == null) {
            log.warn("Failed to find default properties for {}", jeBlockIdentifier);
            return Map.of();
        }
        return defaultProperties;
    }
}
