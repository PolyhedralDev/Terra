package com.dfsek.terra.config.lang;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.lang.Language;


public final class LangUtil {
    private static final Logger logger = LoggerFactory.getLogger(LangUtil.class);
    
    @Nullable
    private static Language LANGUAGE = null;
    
    private LangUtil() { }
    
    public static void load(String langID, Platform platform) {
        File file = new File(platform.getDataFolder(), "lang");
        try {
            File file1 = new File(file, langID + ".yml");
            logger.info(file1.getAbsolutePath());
            LANGUAGE = new LanguageImpl(file1);
            logger.info("Loaded language {}", langID);
        } catch(IOException e) {
            logger.error("Unable to load language: {}.\nDouble-check your configuration before reporting this to Terra!", langID, e);
        }
    }
    
    @NotNull
    public static Language getLanguage() {
        return Objects.requireNonNull(LANGUAGE);
    }
    
    public static void send(String messageID, CommandSender sender, String... args) {
        Objects.requireNonNull(LANGUAGE).getMessage(messageID).send(sender, args);
    }
}
