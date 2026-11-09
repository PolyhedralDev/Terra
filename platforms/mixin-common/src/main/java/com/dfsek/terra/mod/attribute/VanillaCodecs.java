package com.dfsek.terra.mod.attribute;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.RegistryOps;

import java.util.Optional;

import com.dfsek.terra.api.world.attribute.DecodeException;


public final class VanillaCodecs {
    private static final String CAUGHT = "Caught exception decoding ";

    private VanillaCodecs() { }

    public static <V> Object encode(Codec<V> codec, V value, RegistryOps<Object> ops) {
        return codec.encodeStart(ops, value).getOrThrow(IllegalStateException::new);
    }

    public static <V> V parse(Codec<V> codec, Object raw, RegistryOps<Object> ops) throws DecodeException {
        DataResult<V> result = codec.parse(ops, raw);
        Optional<DataResult.Error<V>> error = result.error();
        if(error.isPresent()) {
            String message = error.get().message();
            // ExtraCodecs.catchDecoderException's form; only the outermost one names raw itself.
            String caught = CAUGHT + raw + ": ";
            throw new DecodeException(lowerFirst(message.startsWith(caught) ? message.substring(caught.length()) : message));
        }
        return result.getOrThrow();
    }

    private static String lowerFirst(String reason) {
        if(reason.isEmpty()) return reason;
        return Character.toLowerCase(reason.charAt(0)) + reason.substring(1);
    }
}
