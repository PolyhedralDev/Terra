package com.dfsek.terra.api.addons;


import com.dfsek.terra.api.addons.annotations.Addon;
import com.dfsek.terra.api.addons.annotations.Author;
import com.dfsek.terra.api.addons.annotations.Version;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an entry point for an addon. Implementations must be annotated with {@link Addon}.
 */
public abstract class TerraAddon {
    /**
     * Gets the version of this addon.
     *
     * @return Addon version.
     */
    public final @NotNull String getVersion() {
        Version version = getClass().getAnnotation(Version.class);
        return version == null ? "0.1.0" : version.value();
    }

    /**
     * Gets the author of this addon.
     *
     * @return Addon author.
     */
    public final @NotNull String getAuthor() {
        Author author = getClass().getAnnotation(Author.class);
        return author == null ? "Anon Y. Mous" : author.value();
    }

    /**
     * Gets the name (ID) of this addon.
     *
     * @return Addon ID.
     */
    public final @NotNull String getName() {
        Addon addon = getClass().getAnnotation(Addon.class);
        if(addon == null)
            throw new IllegalStateException("Addon annotation not present"); // This should never happen; the presence of this annotation is checked by the addon loader.
        return addon.value();
    }

    /**
     * Invoked immediately after an addon is loaded.
     */
    public abstract void initialize();
}
