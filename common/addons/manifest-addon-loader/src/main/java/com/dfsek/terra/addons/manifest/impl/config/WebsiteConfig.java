package com.dfsek.terra.addons.manifest.impl.config;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;


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
