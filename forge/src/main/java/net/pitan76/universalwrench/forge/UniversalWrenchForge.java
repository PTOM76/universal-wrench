package net.pitan76.universalwrench.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.eventbus.api.IEventBus;
import net.pitan76.mcpitanlib.api.util.PlatformUtil;
import net.pitan76.universalwrench.UniversalWrench;
import net.pitan76.universalwrench.client.UniversalWrenchClient;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(UniversalWrench.MOD_ID)
public class UniversalWrenchForge {
    public UniversalWrenchForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        EventBuses.registerModEventBus(UniversalWrench.MOD_ID, bus);
        new UniversalWrench();

        if (PlatformUtil.isClient())
            UniversalWrenchClient.init();
    }
}