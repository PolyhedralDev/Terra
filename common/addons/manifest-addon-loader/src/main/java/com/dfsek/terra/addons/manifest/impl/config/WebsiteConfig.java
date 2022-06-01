/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.manifest.impl.config;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;


public class WebsiteConfig implements ObjectTemplate<WebsiteConfig> {
    @Value("issues")
    private String issues;
    
    @Value("source")
    private String source;
    
    @Value("docs")
    private String docs;
    
    public String getDocs() {
        return docs;
    }
    
    public String getIssues() {
        return issues;
    }
    
    public String getSource() {
        return source;
    }
    
    @Override
    public WebsiteConfig get() {
        return this;
    }
}
