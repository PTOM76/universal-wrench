package net.pitan76.universalwrench.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.pitan76.universalwrench.UniversalWrenchMod;

@Mod(UniversalWrenchMod.MOD_ID)
public class UniversalWrenchNeoForge {
    public UniversalWrenchNeoForge(ModContainer modContainer) {
        IEventBus bus = modContainer.getEventBus();

        new UniversalWrenchMod();
    }
}