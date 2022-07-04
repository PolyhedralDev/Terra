package com.dfsek.terra.bukkit.util;

import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class VersionUtil {
    public static final SpigotVersionInfo SPIGOT_VERSION_INFO;
    public static final MinecraftVersionInfo MINECRAFT_VERSION_INFO;
    
    private static final Logger logger = LoggerFactory.getLogger(VersionUtil.class);
    
    static {
        SPIGOT_VERSION_INFO = new SpigotVersionInfo();
        
        MinecraftVersionInfo mcVersionInfo;
        try {
            mcVersionInfo = new MinecraftVersionInfo();
        } catch(Throwable t) {
            logger.error("Error while parsing minecraft version info. Continuing launch, but setting all versions to -1.");
            mcVersionInfo = new MinecraftVersionInfo(-1, -1, -1);
        }
        MINECRAFT_VERSION_INFO = mcVersionInfo;
    }
    
    public static MinecraftVersionInfo getMinecraftVersionInfo() {
        return MINECRAFT_VERSION_INFO;
    }
    
    public static SpigotVersionInfo getSpigotVersionInfo() {
        return SPIGOT_VERSION_INFO;
    }
    
    public static final class SpigotVersionInfo {
        private final boolean spigot;
        private final boolean paper;
        private final boolean mohist;
        
        
        public SpigotVersionInfo() {
            logger.debug("Parsing spigot version info...");
            
            paper = PaperLib.isPaper();
            spigot = PaperLib.isSpigot();
            
            
            boolean isMohist = false;
            try {
                Class.forName("com.mohistmc.MohistMC");
                // it's mohist
                isMohist = true;
            } catch(ClassNotFoundException ignore) { }
            this.mohist = isMohist;
            
            logger.debug("Spigot version info parsed successfully.");
        }
        
        public boolean isPaper() {
            return paper;
        }
        
        public boolean isMohist() {
            return mohist;
        }
        
        public boolean isSpigot() {
            return spigot;
        }
    }
    
    
    public static final class MinecraftVersionInfo {
        private static final Logger logger = LoggerFactory.getLogger(MinecraftVersionInfo.class);
        
        private static final Pattern VERSION_PATTERN = Pattern.compile("v?(\\d+)_(\\d+)_R(\\d+)");
        private final int major;
        private final int minor;
        private final int patch;
        
        private MinecraftVersionInfo() {
            this(Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3]);
        }
        
        private MinecraftVersionInfo(int major, int minor, int patch) {
            this.major = major;
            this.minor = minor;
            this.patch = patch;
        }
        
        private MinecraftVersionInfo(String versionString) {
            Matcher versionMatcher = VERSION_PATTERN.matcher(versionString);
            if(versionMatcher.find()) {
                major = Integer.parseInt(versionMatcher.group(1));
                minor = Integer.parseInt(versionMatcher.group(2));
                patch = Integer.parseInt(versionMatcher.group(3));
            } else {
                logger.warn("Error while parsing minecraft version info. Continuing launch, but setting all versions to -1.");
                
                major = -1;
                minor = -1;
                patch = -1;
            }
        }
        
        @Override
        public String toString() {
            if(major == -1 && minor == -1 && patch == -1)
                return "Unknown";
            
            return String.format("v%d.%d.%d", major, minor, patch);
        }
        
        public int getMajor() {
            return major;
        }
        
        public int getMinor() {
            return minor;
        }
        
        public int getPatch() {
            return patch;
        }
    }
}