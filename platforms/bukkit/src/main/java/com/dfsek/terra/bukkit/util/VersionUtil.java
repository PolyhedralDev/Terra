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
        private final boolean airplane;
        private final boolean tuinity;
        private final boolean purpur;
        private final boolean yatopia;
        
        
        public SpigotVersionInfo() {
            logger.debug("Parsing spigot version info...");
            
            paper = PaperLib.isPaper();
            spigot = PaperLib.isSpigot();
            
            boolean isTuinity = false;
            try {
                Class.forName("com.tuinity.tuinity.config.TuinityConfig");
                isTuinity = true;
            } catch(ClassNotFoundException ignored) { }
            this.tuinity = isTuinity;
            
            boolean isAirplane = false;
            try {
                Class.forName("gg.airplane.AirplaneConfig");
                isAirplane = true;
            } catch(ClassNotFoundException ignored) { }
            this.airplane = isAirplane;
            
            boolean isPurpur = false;
            try {
                Class.forName("net.pl3x.purpur.PurpurConfig");
                isPurpur = true;
            } catch(ClassNotFoundException ignored) { }
            this.purpur = isPurpur;
            
            boolean isYatopia = false;
            try {
                Class.forName("org.yatopiamc.yatopia.server.YatopiaConfig");
                isYatopia = true;
            } catch(ClassNotFoundException ignored) { }
            this.yatopia = isYatopia;
            
            boolean isMohist = false;
            try {
                Class.forName("com.mohistmc.MohistMC");
                // it's mohist
                isMohist = true;
            } catch(ClassNotFoundException ignore) { }
            this.mohist = isMohist;
            
            logger.debug("Spigot version info parsed successfully.");
        }
        
        @Override
        public String toString() {
            if(mohist)
                return "Mohist...";
            else if(yatopia)
                return "Yaptopia";
            else if(purpur)
                return "Purpur";
            else if(tuinity)
                return "Tuinity";
            else if(airplane)
                return "Airplane";
            else if(paper)
                return "Paper";
            else if(spigot)
                return "Spigot";
            else
                return "Craftbukkit";
        }
        
        public boolean isAirplane() {
            return airplane;
        }
        
        public boolean isPaper() {
            return paper;
        }
        
        public boolean isMohist() {
            return mohist;
        }
        
        public boolean isPurpur() {
            return purpur;
        }
        
        public boolean isSpigot() {
            return spigot;
        }
        
        public boolean isTuinity() {
            return tuinity;
        }
        
        public boolean isYatopia() {
            return yatopia;
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