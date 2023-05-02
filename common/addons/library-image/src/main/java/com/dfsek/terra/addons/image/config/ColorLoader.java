package com.dfsek.terra.addons.image.config;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;

import com.dfsek.terra.addons.image.config.ColorLoader.ColorString;
import com.dfsek.terra.addons.image.util.ColorUtil;


public class ColorLoader implements TypeLoader<ColorString> {
    
    @Override
    public ColorString load(@NotNull AnnotatedType annotatedType, @NotNull Object o, @NotNull ConfigLoader configLoader,
                            DepthTracker depthTracker) throws LoadException {
        return new ColorString((String) o);
    }
    
    public static class ColorString {
        
        private final int argb;
        
        public ColorString(String string) throws IllegalArgumentException {
            this.argb = parse(string);
        }
        
        public int getColor() {
            return argb;
        }
        
        private static int parse(String string) throws IllegalArgumentException {
            if (string.length() == 0)
                throw new IllegalArgumentException("Empty string cannot be parsed as a valid color");
            
            String[] split = string.split(",");
            
            if (split.length == 1)
                return parseHex(string);
            else if (split.length == 3)
                return parseChannels("255", split[0], split[1], split[2]);
            else if (split.length == 4)
                return parseChannels(split[0], split[1], split[2], split[3]);
            else
                throw new IllegalArgumentException("Invalid channels provided, required format RED,GREEN,BLUE or ALPHA,RED,GREEN,BLUE");
        }
        
        private static int parseHex(String hex) throws IllegalArgumentException {
            if (hex.startsWith("#"))
                hex = hex.substring(1);
            
            int alpha = 255;
            int red = 0;
            int green = 0;
            int blue = 0;
            
            try {
                if(hex.length() == 8) {
                    alpha = Integer.parseInt(hex.substring(0, 2), 16);
                    hex = hex.substring(2);
                }
                
                if(hex.length() != 6)
                    throw new IllegalArgumentException("Invalid color channels, required format AARRGGBB or RRGGBB");
                
                red = Integer.parseInt(hex.substring(0, 2), 16);
                green = Integer.parseInt(hex.substring(2, 4), 16);
                blue = Integer.parseInt(hex.substring(4, 6), 16);
                
                return ColorUtil.rgbValidated(alpha, red, green, blue);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Failed to parse hex color", e);
            }
        }
        
        private static int parseChannels(String alpha, String red, String green, String blue) throws IllegalArgumentException {
            try {
                int a = Integer.decode(alpha);
                int r = Integer.decode(red);
                int g = Integer.decode(green);
                int b = Integer.decode(blue);
                
                return ColorUtil.rgbValidated(a, r, g, b);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid channel value", e);
            }
        }
    }
}
