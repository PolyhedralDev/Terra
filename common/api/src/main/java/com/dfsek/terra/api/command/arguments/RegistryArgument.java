package com.dfsek.terra.api.command.arguments;

import io.leangen.geantyref.TypeToken;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.registry.exception.NoSuchEntryException;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.util.reflection.TypeKey;

import org.incendo.cloud.component.CommandComponent;
import org.incendo.cloud.component.DefaultValue;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;


public class RegistryArgument {

    public static <T, R> Builder<T, R> builder(String name, Registry<R> registry) {
        return new Builder<>(name, registry);
    }

    public static <T, R> CommandComponent<T> of(String name, Registry<R> registry) {
        return RegistryArgument.<T, R>builder(name, registry).build();
    }

    public static <T, R> CommandComponent<T> optional(String name, Registry<R> registry) {
        return RegistryArgument.<T, R>builder(name, registry).optional().build();
    }

    public static <T, R> CommandComponent<T> optional(String name, Registry<R> registry, DefaultValue<T, R> defaultKey) {
        return RegistryArgument.<T, R>builder(name, registry).optional(defaultKey).build();
    }

    @SuppressWarnings("unchecked")
    public static <T, R> Builder<T, R> builder(String name, Function<CommandContext<T>, Registry<R>> registryFunction,
                                               TypeKey<R> registryType) {
        return new Builder<>(name, registryFunction, (TypeToken<R>) TypeToken.get(registryType.getType()));
    }

    public static <T, R> CommandComponent<T> of(String name, Function<CommandContext<T>, Registry<R>> registryFunction,
                                                  TypeKey<R> registryType) {
        return RegistryArgument.<T, R>builder(name, registryFunction, registryType).build();
    }

    public static <T, R> CommandComponent<T> optional(String name, Function<CommandContext<T>, Registry<R>> registryFunction,
                                                        TypeKey<R> registryType) {
        return RegistryArgument.builder(name, registryFunction, registryType).optional().build();
    }

    public static <T, R> CommandComponent<T> optional(String name, Function<CommandContext<T>, Registry<R>> registryFunction,
                                                        TypeKey<R> registryType, DefaultValue<T, R> defaultKey) {
        return RegistryArgument.builder(name, registryFunction, registryType).optional(defaultKey).build();
    }

    public static final class Builder<T, R> extends CommandComponent.Builder<T, R> {

        @SuppressWarnings("unchecked")
        private Builder(@NonNull String name, Registry<R> registry) {
            super();
            this.name(name);
            this.parser(ParserDescriptor.of(
                new RegistryArgumentParser<>(commandContext -> registry),
                (TypeToken<R>) TypeToken.get(registry.getType().getType())));
        }

        private Builder(@NonNull String name, Function<CommandContext<T>, Registry<R>> registryFunction, TypeToken<R> typeToken) {
            super();
            this.name(name);
            this.parser(ParserDescriptor.of(new RegistryArgumentParser<>(registryFunction), typeToken));
        }
    }


    private static final class RegistryArgumentParser<T, R> implements ArgumentParser<T, R> {
        private final Function<CommandContext<T>, Registry<R>> registryFunction;

        private RegistryArgumentParser(Function<CommandContext<T>, Registry<R>> registryFunction) {
            this.registryFunction = registryFunction;
        }

        @Override
        public @NonNull ArgumentParseResult<@NonNull R> parse(@NonNull CommandContext<@NonNull T> commandContext,
                                                              @NonNull CommandInput commandInput) {
            String input = commandInput.readString();
            String next = commandInput.peekString();
            if(next.equals(":")) {
                input += commandInput.readString();
                input += commandInput.readString();
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
        public @NonNull SuggestionProvider<T> suggestionProvider() {
            return new SuggestionProvider<>() {
                @Override
                public @NonNull CompletableFuture<? extends @NonNull Iterable<? extends @NonNull Suggestion>> suggestionsFuture(
                    @NonNull CommandContext<T> context, @NonNull CommandInput input) {

                    // TODO: Verify whether this is correct
                    return CompletableFuture.completedFuture(registryFunction.apply(context).keys().stream().map(
                        registryKey -> Suggestion.suggestion(registryKey.toString())).sorted().collect(Collectors.toList()));
                }
            };
        }
    }
}
