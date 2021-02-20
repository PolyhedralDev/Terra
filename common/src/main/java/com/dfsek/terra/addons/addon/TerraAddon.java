package com.dfsek.terra.addons.addon;


import com.dfsek.terra.addons.annotations.Addon;
import com.dfsek.terra.addons.annotations.Author;
import com.dfsek.terra.addons.annotations.Version;
import org.jetbrains.annotations.NotNull;

public abstract class TerraAddon {
    public final @NotNull String getVersion() {
        Version version = getClass().getAnnotation(Version.class);
        return version == null ? "0.1.0" : version.value();
    }

    public final @NotNull String getAuthor() {
        Author author = getClass().getAnnotation(Author.class);
        return author == null ? "Anon Y. Mous" : author.value();
    }

    public final @NotNull String getName() {
        Addon addon = getClass().getAnnotation(Addon.class);
        if(addon == null) throw new IllegalStateException("Addon annotation not present"); // This should never happen; the presence of this annotation is checked by the addon loader.
        return addon.value();
    }

    /**
     * Invoked immediately after an addon is loaded.
     */
    public abstract void initialize();
}
