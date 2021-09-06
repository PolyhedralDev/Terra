package com.dfsek.terra.api.cloud;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.arguments.standard.EnumArgument;

import cloud.commandframework.context.CommandContext;

import cloud.commandframework.exceptions.parsing.NoInputProvidedException;

import com.dfsek.terra.api.registry.Registry;

import io.leangen.geantyref.TypeToken;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.BiFunction;


/**
 * An argument which recieves values from a registry.
 *
 * @param <T>
 */
public class RegistryArgument<C, T> extends CommandArgument<C, T> {
    
    public RegistryArgument(Registry<T> registry,
                            Class<T> registryClass,
                            boolean required,
                            @NonNull String name,
                            @NonNull String defaultValue,
                            @Nullable BiFunction<CommandContext<C>, String, List<String>> suggestionsProvider,
                            @NonNull ArgumentDescription defaultDescription) {
        super(required, name, new RegistryParser<>(registry), defaultValue, registryClass, suggestionsProvider, defaultDescription);
    }
    
    public static <C1, T1> Builder<C1, T1> newBuilder(Registry<T1> registry, Class<T1> clazz, String name) {
        return new Builder<>(clazz, name, registry);
    }
    
    public static <C1, T1> RegistryArgument<C1, T1> of(Registry<T1> registry, Class<T1> registryClass, String name) {
        return RegistryArgument.<C1, T1>newBuilder(registry, registryClass, name).build();
    }
    
    public static final class Builder<C, R> extends CommandArgument.Builder<C, R> {
        
        private final Registry<R> registry;
        
        private final Class<R> registryType;
        
        
        private Builder(@NonNull Class<R> valueType, @NonNull String name, Registry<R> registry) {
            super(valueType, name);
            this.registry = registry;
            this.registryType = valueType;
        }
        
        @Override
        public @NonNull RegistryArgument<@NonNull C, @NonNull R> build() {
            return new RegistryArgument<>(registry, registryType,
                                          this.isRequired(),
                                          this.getName(),
                                          this.getDefaultValue(),
                                          this.getSuggestionsProvider(),
                                          this.getDefaultDescription()
            );
        }
    }
    
    
    public static final class RegistryParser<C, E> implements ArgumentParser<C, E> {
        
        private final Registry<E> registry;
        
        public RegistryParser(Registry<E> registry) {
            this.registry = registry;
        }
        
        
        @Override
        public @NonNull ArgumentParseResult<E> parse(
                final @NonNull CommandContext<C> commandContext,
                final @NonNull Queue<@NonNull String> inputQueue
                                                    ) {
            final String input = inputQueue.peek();
            if(input == null) {
                return ArgumentParseResult.failure(new NoInputProvidedException(
                        EnumArgument.EnumParser.class,
                        commandContext
                ));
            }
            
            if(registry.contains(input)) return ArgumentParseResult.success(registry.get(input));
            
            return ArgumentParseResult.failure(new IllegalArgumentException(input));
        }
        
        @Override
        public @NonNull List<@NonNull String> suggestions(
                @NotNull CommandContext<C> commandContext,
                final @NonNull String input) {
            return new ArrayList<>(registry.keys());
        }
        
        @Override
        public boolean isContextFree() {
            return true;
        }
        
    }
}
