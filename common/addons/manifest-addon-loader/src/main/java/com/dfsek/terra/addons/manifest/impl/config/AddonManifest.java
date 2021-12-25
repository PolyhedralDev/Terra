/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.manifest.impl.config;

import ca.solostudios.strata.version.Version;
import ca.solostudios.strata.version.VersionRange;
import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.api.registry.key.StringIdentifiable;


@SuppressWarnings("FieldMayBeFinal")
public class AddonManifest implements ConfigTemplate, StringIdentifiable {
    @Value("schema-version")
    private int schemaVersion;
    
    @Value("id")
    private String id;
    
    @Value("version")
    private Version version;
    
    @Value("license")
    private String license;
    
    @Value("contributors")
    @Default
    private List<String> contributors = Collections.emptyList();
    
    @Value("entrypoints")
    private List<String> entryPoints;
    
    @Value("depends")
    @Default
    private Map<String, VersionRange> dependencies = Collections.emptyMap();
    
    @Value("website")
    @Default
    private WebsiteConfig website;
    
    @Override
    public String getID() {
        return id;
    }
    
    public int getSchemaVersion() {
        return schemaVersion;
    }
    
    public Version getVersion() {
        return version;
    }
    
    public List<String> getContributors() {
        return contributors;
    }
    
    public List<String> getEntryPoints() {
        return entryPoints;
    }
    
    public String getLicense() {
        return license;
    }
    
    public WebsiteConfig getWebsite() {
        return website;
    }
    
    public Map<String, VersionRange> getDependencies() {
        return dependencies;
    }
}
