package com.dfsek.terra.api.command.arguments;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
import io.leangen.geantyref.TypeToken;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.registry.exception.NoSuchEntryException;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.util.reflection.TypeKey;


public class RegistryArgument<T, R> extends CommandArgument<T, R> {
    private RegistryArgument(
            boolean required,
            @NonNull String name,
            Function<CommandContext<T>, Registry<R>> registryFunction,
            TypeToken<R> typeToken,
            @NonNull String defaultValue,
            @Nullable BiFunction<CommandContext<T>, String, List<String>> suggestionsProvider,
            @NonNull ArgumentDescription description
                            ) {
        super(required,
              name,
              new RegistryArgumentParser<>(registryFunction),
              defaultValue,
              typeToken,
              suggestionsProvider,
              description);
    }
    
    public static <T, R> Builder<T, R> builder(String name, Registry<R> registry) {
        return new Builder<>(name, registry);
    }
    
    public static <T, R> CommandArgument<T, R> of(String name, Registry<R> registry) {
        return RegistryArgument.<T, R>builder(name, registry).build();
    }
    
    public static <T, R> CommandArgument<T, R> optional(String name, Registry<R> registry) {
        return RegistryArgument.<T, R>builder(name, registry).asOptional().build();
    }
    
    public static <T, R> CommandArgument<T, R> optional(String name, Registry<R> registry, String defaultKey) {
        return RegistryArgument.<T, R>builder(name, registry).asOptionalWithDefault(defaultKey).build();
    }
    
    @SuppressWarnings("unchecked")
    public static <T, R> Builder<T, R> builder(String name, Function<CommandContext<T>, Registry<R>> registryFunction,
                                               TypeKey<R> registryType) {
        return new Builder<>(name, registryFunction, (TypeToken<R>) TypeToken.get(registryType.getType()));
    }
    
    public static <T, R> CommandArgument<T, R> of(String name, Function<CommandContext<T>, Registry<R>> registryFunction,
                                                  TypeKey<R> registryType) {
        return RegistryArgument.<T, R>builder(name, registryFunction, registryType).build();
    }
    
    public static <T, R> CommandArgument<T, R> optional(String name, Function<CommandContext<T>, Registry<R>> registryFunction,
                                                        TypeKey<R> registryType) {
        return RegistryArgument.builder(name, registryFunction, registryType).asOptional().build();
    }
    
    public static <T, R> CommandArgument<T, R> optional(String name, Function<CommandContext<T>, Registry<R>> registryFunction,
                                                        TypeKey<R> registryType, String defaultKey) {
        return RegistryArgument.builder(name, registryFunction, registryType).asOptionalWithDefault(defaultKey).build();
    }
    
    public static final class Builder<T, R> extends CommandArgument.Builder<T, R> {
        private final Function<CommandContext<T>, Registry<R>> registryFunction;
        private final TypeToken<R> typeToken;
        
        @SuppressWarnings("unchecked")
        private Builder(@NonNull String name, Registry<R> registry) {
            super((TypeToken<R>) TypeToken.get(registry.getType().getType()), name);
            this.registryFunction = commandContext -> registry;
            this.typeToken = (TypeToken<R>) TypeToken.get(registry.getType().getType());
        }
        
        private Builder(@NonNull String name, Function<CommandContext<T>, Registry<R>> registryFunction, TypeToken<R> typeToken) {
            super(typeToken, name);
            this.typeToken = typeToken;
            this.registryFunction = registryFunction;
        }
        
        @Override
        public @NonNull RegistryArgument<T, R> build() {
            return new RegistryArgument<>(
                    isRequired(),
                    getName(),
                    registryFunction,
                    typeToken,
                    getDefaultValue(),
                    getSuggestionsProvider(),
                    getDefaultDescription()
            );
        }
    }
    
    
    private static final class RegistryArgumentParser<T, R> implements ArgumentParser<T, R> {
        private final Function<CommandContext<T>, Registry<R>> registryFunction;
        
        private RegistryArgumentParser(Function<CommandContext<T>, Registry<R>> registryFunction) {
            this.registryFunction = registryFunction;
        }
        
        @Override
        public @NonNull ArgumentParseResult<@NonNull R> parse(@NonNull CommandContext<@NonNull T> commandContext,
                                                              @NonNull Queue<@NonNull String> inputQueue) {
            String input = inputQueue.remove();
            String next = inputQueue.peek();
            if(next != null && next.equals(":")) {
                input += inputQueue.remove();
                input += inputQueue.remove();
            }
            
            Registry<R> registry = registryFunction.apply(commandContext);
            
            Optional<R> result;
            try {
                result = registry.get(RegistryKey.parse(input));
            } catch(IllegalArgumentException e) {
                try {
                    result = registry.getByID(input);
                } catch(IllegalArgumentException e1) {
                    return ArgumentParseResult.failure(e1);
                }
            }
            
            return result
                    .map(ArgumentParseResult::success)
                    .orElse(ArgumentParseResult.failure(new NoSuchEntryException("No such entry: " + input)));
        }
        
        @Override
        public @NonNull List<@NonNull String> suggestions(@NonNull CommandContext<T> commandContext, @NonNull String input) {
            return registryFunction.apply(commandContext).keys().stream().map(RegistryKey::toString).sorted().collect(Collectors.toList());
        }
    }
}
