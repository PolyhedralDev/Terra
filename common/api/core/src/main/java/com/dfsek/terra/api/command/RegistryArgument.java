package com.dfsek.terra.api.command;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;

import com.dfsek.terra.api.registry.Registry;

import com.dfsek.terra.api.registry.exception.NoSuchEntryException;

import io.leangen.geantyref.TypeToken;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Queue;
import java.util.function.BiFunction;
import java.util.stream.Collectors;


public class RegistryArgument<T, R> extends CommandArgument<T, R> {
    
    
    @SuppressWarnings("unchecked")
    private RegistryArgument(
            final boolean required,
            final @NonNull String name,
            final Registry<R> registry,
            final @NonNull String defaultValue,
            final @Nullable BiFunction<@NonNull CommandContext<T>, @NonNull String,
                    @NonNull List<@NonNull String>> suggestionsProvider,
            final @NonNull ArgumentDescription description
                            ) {
        super(required,
              name,
              new RegistryArgumentParser<>(registry),
              defaultValue,
              (TypeToken<R>) TypeToken.get(registry.getType().getType()),
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
    
    public static final class Builder<T, R> extends CommandArgument.Builder<T, R> {
        private final Registry<R> registry;
        
        @SuppressWarnings("unchecked")
        private Builder(@NonNull String name, Registry<R> registry) {
            super((TypeToken<R>) TypeToken.get(registry.getType().getType()), name);
            this.registry = registry;
        }
        
        @Override
        public @NonNull RegistryArgument<T, R> build() {
            return new RegistryArgument<>(
                    isRequired(),
                    getName(),
                    registry,
                    getDefaultValue(),
                    getSuggestionsProvider(),
                    getDefaultDescription()
            );
        }
    }
    
    
    private static final class RegistryArgumentParser<T, R> implements ArgumentParser<T, R> {
        private final Registry<R> registry;
        
        private RegistryArgumentParser(Registry<R> registry) {
            this.registry = registry;
        }
        
        @Override
        public @NonNull ArgumentParseResult<@NonNull R> parse(@NonNull CommandContext<@NonNull T> commandContext,
                                                              @NonNull Queue<@NonNull String> inputQueue) {
            String input = inputQueue.remove();
            return registry.get(input).map(ArgumentParseResult::success).orElse(ArgumentParseResult.failure(new NoSuchEntryException("No such entry: " + input)));
        }
        
        @Override
        public @NonNull List<@NonNull String> suggestions(@NonNull CommandContext<T> commandContext, @NonNull String input) {
            return registry.keys().stream().sorted().collect(Collectors.toList());
        }
    }
}
