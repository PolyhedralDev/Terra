package com.dfsek.terra.forge;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("terra")
public class TerraForgePlugin {
    public Logger getLogger()
    {
        return LogManager.getLogger("terra");
    }

    public TerraForgePlugin() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void setup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onFingerprintViolation(final FMLFingerprintViolationEvent event)
    {
        getLogger().warn("Invalid fingerprint detected! The file " + event.getSource().getName()
                + " may have been tampered with. This version will NOT be supported by the author!");
        getLogger().warn("Expected " + event.getExpectedFingerprint() + " found " + event.getFingerprints().toString());
    }
}
