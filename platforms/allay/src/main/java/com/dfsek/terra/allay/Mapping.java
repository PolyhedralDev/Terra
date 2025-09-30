package com.dfsek.terra.allay;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.allaymc.api.block.type.BlockState;
import org.allaymc.api.block.type.BlockStateGetter;
import org.allaymc.api.block.type.BlockTypes;
import org.allaymc.api.item.type.ItemType;
import org.allaymc.api.item.type.ItemTypeGetter;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;


/**
 * @author daoge_cmd
 * @author IWareQ
 */
public final class Mapping {
    private static final Gson GSON = new GsonBuilder()
        .registerTypeAdapterFactory(new IgnoreFailureTypeAdapterFactory())
        .create();

    private static final Map<String, Map<String, String>> JE_BLOCK_DEFAULT_PROPERTIES = new HashMap<>();
    private static final Map<BlockState, JeBlockState> BE_BLOCK_STATE_TO_JE = new HashMap<>();
    private static final Map<Integer, BlockState> JE_BLOCK_STATE_HASH_TO_BE = new HashMap<>();
    private static final Map<String, ItemType<?>> JE_ITEM_ID_TO_BE = new HashMap<>();
    private static final Map<String, Integer> JE_BIOME_ID_TO_BE = new HashMap<>();

    private static final BlockState BE_AIR_STATE = BlockTypes.AIR.getDefaultState();

    public static void init() {
        if(!initBlockStateMapping()) error();
        if(!initJeBlockDefaultProperties()) error();
        if(!initItemMapping()) error();
        if(!initBiomeMapping()) error();
    }

    public static JeBlockState blockStateBeToJe(BlockState beBlockState) {
        return BE_BLOCK_STATE_TO_JE.get(beBlockState);
    }

    public static BlockState blockStateJeToBe(JeBlockState jeBlockState) {
        BlockState result = JE_BLOCK_STATE_HASH_TO_BE.get(jeBlockState.getHash());
        if(result == null) {
            TerraAllayPlugin.INSTANCE.getPluginLogger().warn("Failed to find be block state for {}", jeBlockState);
            return BE_AIR_STATE;
        }
        return result;
    }

    public static ItemType<?> itemIdJeToBe(String jeItemId) {
        return JE_ITEM_ID_TO_BE.get(jeItemId);
    }

    // Enchantment identifiers are same in both versions

    public static String enchantmentIdBeToJe(String beEnchantmentId) {
        return beEnchantmentId;
    }

    public static String enchantmentIdJeToBe(String jeEnchantmentId) {
        return jeEnchantmentId;
    }

    public static int biomeIdJeToBe(String jeBiomeId) {
        return JE_BIOME_ID_TO_BE.get(jeBiomeId);
    }

    public static Map<String, String> getJeBlockDefaultProperties(String jeBlockIdentifier) {
        var defaultProperties = JE_BLOCK_DEFAULT_PROPERTIES.get(jeBlockIdentifier);
        if(defaultProperties == null) {
            TerraAllayPlugin.INSTANCE.getPluginLogger().warn("Failed to find default properties for {}", jeBlockIdentifier);
            return Map.of();
        }

        return defaultProperties;
    }

    private static void error() {
        throw new RuntimeException("Mapping not initialized");
    }

    private static boolean initBiomeMapping() {
        try(InputStream stream = Mapping.class.getClassLoader().getResourceAsStream("mapping/biomes.json")) {
            if(stream == null) {
                TerraAllayPlugin.INSTANCE.getPluginLogger().error("biomes mapping not found");
                return false;
            }

            Map<String, BiomeMapping> mappings = from(stream, new TypeToken<>() {});
            mappings.forEach((javaId, mapping) -> JE_BIOME_ID_TO_BE.put(javaId, mapping.bedrockId()));
        } catch(IOException e) {
            TerraAllayPlugin.INSTANCE.getPluginLogger().error("Failed to load biomes mapping", e);
            return false;
        }
        return true;
    }

    private static boolean initItemMapping() {
        try(InputStream stream = Mapping.class.getClassLoader().getResourceAsStream("mapping/items.json")) {
            if(stream == null) {
                TerraAllayPlugin.INSTANCE.getPluginLogger().error("items mapping not found");
                return false;
            }

            Map<String, ItemMapping> mappings = from(stream, new TypeToken<>() {});
            mappings.forEach((javaId, mapping) -> {
                ItemType<?> itemType = ItemTypeGetter
                    .name(mapping.bedrockId())
                    .meta(mapping.bedrockData())
                    .itemType();
                JE_ITEM_ID_TO_BE.put(javaId, itemType);
            });
        } catch(IOException e) {
            TerraAllayPlugin.INSTANCE.getPluginLogger().error("Failed to load items mapping", e);
            return false;
        }
        return true;
    }

    private static boolean initBlockStateMapping() {
        try(InputStream stream = Mapping.class.getClassLoader().getResourceAsStream("mapping/blocks.json")) {
            if(stream == null) {
                TerraAllayPlugin.INSTANCE.getPluginLogger().error("blocks mapping not found");
                return false;
            }

            Map<String, List<BlockMapping>> root = from(stream, new TypeToken<>() {});
            List<BlockMapping> mappings = root.get("mappings");
            mappings.forEach(mapping -> {
                JeBlockState jeState = createJeBlockState(mapping.javaState());
                BlockState beState = createBeBlockState(mapping.bedrockState());
                BE_BLOCK_STATE_TO_JE.put(beState, jeState);
                JE_BLOCK_STATE_HASH_TO_BE.put(jeState.getHash(), beState);
            });
        } catch(IOException e) {
            TerraAllayPlugin.INSTANCE.getPluginLogger().error("Failed to load blocks mapping", e);
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private static boolean initJeBlockDefaultProperties() {
        try(InputStream stream = Mapping.class.getClassLoader().getResourceAsStream("je_blocks.json")) {
            if(stream == null) {
                TerraAllayPlugin.INSTANCE.getPluginLogger().error("je_block_default_states.json not found");
                return false;
            }

            Map<String, List<Map<String, ?>>> data = from(stream, new TypeToken<>() {});
            for(var entry : data.entrySet()) {
                JE_BLOCK_DEFAULT_PROPERTIES.put(
                    "minecraft:" + entry.getKey(),
                    (Map<String, String>) entry.getValue().get(1)
                );
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private static JeBlockState createJeBlockState(BlockMapping.JavaState state) {
        Map<String, String> properties = state.properties() == null ? Map.of() : state.properties();
        return JeBlockState.create(state.name(), new TreeMap<>(properties));
    }

    private static BlockState createBeBlockState(BlockMapping.BedrockState state) {
        BlockStateGetter.Getter getter = BlockStateGetter.name("minecraft:" + state.bedrockId());
        if(state.state() != null) {
            convertValueType(state.state()).forEach(getter::property);
        }
        return getter.blockState();
    }

    private static Map<String, Object> convertValueType(Map<String, Object> data) {
        Map<String, Object> result = new TreeMap<>();
        for(Entry<String, Object> entry : data.entrySet()) {
            if(entry.getValue() instanceof Number number) {
                // Convert double to int because the number in json is double
                result.put(entry.getKey(), number.intValue());
            } else {
                result.put(entry.getKey(), entry.getValue());
            }
        }

        return result;
    }

    public static <V> V from(InputStream inputStream, TypeToken<V> typeToken) {
        JsonReader reader = new JsonReader(new InputStreamReader(Objects.requireNonNull(inputStream)));
        return GSON.fromJson(reader, typeToken.getType());
    }

    public record BiomeMapping(
        @SerializedName("bedrock_id")
        int bedrockId
    ) {
    }


    public record ItemMapping(
        @SerializedName("bedrock_identifier")
        String bedrockId,
        @SerializedName("bedrock_data")
        int bedrockData
    ) {
    }


    public record BlockMapping(
        @SerializedName("java_state")
        BlockMapping.JavaState javaState,
        @SerializedName("bedrock_state")
        BlockMapping.BedrockState bedrockState
    ) {
        public record JavaState(
            @SerializedName("Name")
            String name,
            @Nullable
            @SerializedName("Properties")
            Map<String, String> properties
        ) {
        }


        public record BedrockState(
            @SerializedName("bedrock_identifier")
            String bedrockId,
            @Nullable
            Map<String, Object> state
        ) {
        }
    }

    // see https://stackoverflow.com/questions/59655279/is-there-an-easy-way-to-make-gson-skip-a-field-if-theres-an-error-deserializing
    public static class IgnoreFailureTypeAdapterFactory implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            TypeAdapter<T> delegate = gson.getDelegateAdapter(this, typeToken);
            return new TypeAdapter<>() {
                @Override
                public void write(JsonWriter writer, T value) throws IOException {
                    delegate.write(writer, value);
                }

                @Override
                public T read(JsonReader reader) throws IOException {
                    try {
                        return delegate.read(reader);
                    } catch(Exception e) {
                        reader.skipValue();
                        return null;
                    }
                }
            };
        }
    }
}
