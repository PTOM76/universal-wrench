package net.pitan76.universalwrench.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.eventbus.api.IEventBus;
import net.pitan76.universalwrench.UniversalWrenchMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(UniversalWrenchMod.MOD_ID)
public class UniversalWrenchForge {
    public UniversalWrenchForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        EventBuses.registerModEventBus(UniversalWrenchMod.MOD_ID, bus);
        new UniversalWrenchMod();
    }
}