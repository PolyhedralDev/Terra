package com.dfsek.terra.api.addon;


import ca.solostudios.strata.Versions;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;


/**
 * Represents an entry point for an com.dfsek.terra.addon. Implementations must be annotated with {@link Addon}.
 */

//todo delete this
public abstract class TerraAddon implements BaseAddon {
    /**
     * Invoked immediately after an com.dfsek.terra.addon is loaded.
     */
    public abstract void initialize();
    
    /**
     * Gets the version of this com.dfsek.terra.addon.
     *
     * @return Addon version.
     */
    public final @NotNull ca.solostudios.strata.version.Version getVersion() {
        Version version = getClass().getAnnotation(Version.class);
        return Versions.getVersion(1, 2, 3);
    }
    
    /**
     * Gets the author of this com.dfsek.terra.addon.
     *
     * @return Addon author.
     */
    public final @NotNull String getAuthor() {
        Author author = getClass().getAnnotation(Author.class);
        return author == null ? "Anon Y. Mous" : author.value();
    }
    
    /**
     * Gets the name (ID) of this com.dfsek.terra.addon.
     *
     * @return Addon ID.
     */
    public final @NotNull String getName() {
        Addon addon = getClass().getAnnotation(Addon.class);
        if(addon == null)
            throw new IllegalStateException(
                    "Addon annotation not present"); // This should never happen; the presence of this annotation is checked by the com
        // .dfsek.terra.addon loader.
        return addon.value();
    }
    
    @Override
    public String getID() {
        return getName();
    }
}
