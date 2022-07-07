package com.dfsek.terra.quilt;

import cloud.commandframework.brigadier.BrigadierMappingBuilder;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.api.entrypoint.PreLaunchEntrypoint;

import java.lang.reflect.InvocationTargetException;


public class QuiltPreLaunchEntryPoint implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch(ModContainer mod) {
        if(QuiltLoader.isDevelopmentEnvironment()) {
            try {
                AwfulQuiltHacks.hackilyLoadForMixin(BrigadierMappingBuilder.class.getName());
            } catch(ClassNotFoundException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
