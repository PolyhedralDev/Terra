package org.allaymc.terra.allay;

import com.google.gson.reflect.TypeToken;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.extern.slf4j.Slf4j;
import org.allaymc.api.block.type.BlockState;
import org.allaymc.api.block.type.BlockStateSafeGetter;
import org.allaymc.api.block.type.BlockTypes;
import org.allaymc.api.item.type.ItemType;
import org.allaymc.api.item.type.ItemTypeSafeGetter;
import org.allaymc.api.registry.Registries;
import org.allaymc.api.utils.HashUtils;
import org.allaymc.api.utils.JSONUtils;
import org.allaymc.updater.block.BlockStateUpdaters;
import org.allaymc.updater.item.ItemStateUpdaters;
import org.cloudburstmc.nbt.NbtMap;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * @author daoge_cmd
 */
@Slf4j
public final class Mapping {
    private static final Map<String, Map<String, String>> JE_BLOCK_DEFAULT_PROPERTIES = new Object2ObjectOpenHashMap<>();
    private static final Map<BlockState, JeBlockState> BLOCK_STATE_BE_TO_JE = new Object2ObjectOpenHashMap<>();
    private static final Map<Integer, BlockState> BLOCK_STATE_JE_HASH_TO_BE = new Int2ObjectOpenHashMap<>();
    private static final Map<String, ItemType<?>> ITEM_ID_JE_TO_BE = new Object2ObjectOpenHashMap<>();
    private static final Map<String, Integer> BIOME_ID_JE_TO_BE = new Object2IntOpenHashMap<>();
    private static final BlockState BE_AIR_STATE = BlockTypes.AIR.getDefaultState();

    public static void init() {
        if(!initBlockStateMapping()) error();
        if(!initJeBlockDefaultProperties()) error();
        if(!initItemMapping()) error();
        if(!initBiomeMapping()) error();
    }

    private static void error() {
        throw new RuntimeException("Mapping not initialized");
    }

    private static boolean initBiomeMapping() {
        try (var stream = Mapping.class.getClassLoader().getResourceAsStream("mapping/biomes_JE_1_20_4_TO_BE_1_20_0.json")) {
            if  (stream == null) {
                log.error("biomes mapping not found");
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
        try (var stream = Mapping.class.getClassLoader().getResourceAsStream("mapping/items_JE_1_20_4_TO_BE_1_20_0.json")) {
            if  (stream == null) {
                log.error("items mapping not found");
                return false;
            }
            var mappings = JSONUtils.from(stream, new TypeToken<Map<String, Map<String, Object>>>(){}).entrySet();
            for(var mapping : mappings) {
                var item = ItemTypeSafeGetter
                    .name((String) mapping.getValue().get("bedrock_identifier"))
                    // NOTICE: Should be cast to double
                    .meta(((Double) mapping.getValue().get("bedrock_data")).intValue())
                    .itemType();
                ITEM_ID_JE_TO_BE.put(mapping.getKey(), item);
            }
        } catch(IOException e) {
            log.error("Failed to load mapping", e);
        }
        return true;
    }

    private static boolean initBlockStateMapping() {
        try (var stream = Mapping.class.getClassLoader().getResourceAsStream("mapping/blocks_JE_1_20_4_TO_BE_1_20_0.json")) {
            if (stream == null) {
                log.error("blocks mapping not found");
                return false;
            }
            // noinspection unchecked
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
        try (var stream = Mapping.class.getClassLoader().getResourceAsStream("je_block_default_states_1_20_4.json")) {
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
        var getter = BlockStateSafeGetter
            .name("minecraft:" + data.get("bedrock_identifier"));
        if (data.containsKey("state")) {
            // noinspection unchecked
            convertValueType((Map<String, Object>) data.get("state")).forEach(getter::property);
        }
        return getter.blockState();
    }

    private static Map<String, Object> convertValueType(Map<String, Object> data) {
        var result = new TreeMap<String, Object>();
        for (var entry : data.entrySet()) {
            if (entry.getValue() instanceof Number number) {
                // Convert double to int because the number in json is double
                result.put(entry.getKey(), number.intValue());
            } else {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    private static JeBlockState createJeBlockState(Map<String, Object> data) {
        var identifier = (String) data.get("Name");
        // noinspection unchecked
        return JeBlockState.create(identifier, new TreeMap<>((Map<String, String>) data.getOrDefault("Properties", Map.of())));
    }

    public static JeBlockState blockStateBeToJe(BlockState beBlockState) {
        return BLOCK_STATE_BE_TO_JE.get(beBlockState);
    }

    public static BlockState blockStateJeToBe(JeBlockState jeBlockState) {
        var result = BLOCK_STATE_JE_HASH_TO_BE.get(jeBlockState.getHash());
        if(result == null) {
            log.warn("Failed to find be block state for {}", jeBlockState);
            return BlockTypes.AIR.getDefaultState();
        }
        return result;
    }

    public static ItemType<?> itemIdJeToBe(String jeItemId) {
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
