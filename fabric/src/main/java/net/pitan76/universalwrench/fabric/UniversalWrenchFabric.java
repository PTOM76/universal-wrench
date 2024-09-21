package net.pitan76.universalwrench.fabric;

import net.pitan76.universalwrench.UniversalWrenchMod;
import net.fabricmc.api.ModInitializer;

public class UniversalWrenchFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        new UniversalWrenchMod();
    }
}