package com.dfsek.terra.config.pack;

import ca.solostudios.strata.version.Version;
import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import java.util.Map;


public class MetaPackTemplate implements ConfigTemplate {
    @Value("id")
    private String id;

    @Value("author")
    @Default
    private String author = "Anon Y. Mous";

    @Value("version")
    private Version version;

    @Value("packs")
    private Map<String, String> packs;

    public String getID() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public Version getVersion() {
        return version;
    }

    public Map<String, String> getPacks() {
        return packs;
    }
}
