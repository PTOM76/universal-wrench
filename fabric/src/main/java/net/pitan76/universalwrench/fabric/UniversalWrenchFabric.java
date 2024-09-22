package net.pitan76.universalwrench.fabric;

import net.pitan76.universalwrench.UniversalWrench;
import net.fabricmc.api.ModInitializer;

public class UniversalWrenchFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        new UniversalWrench();
    }
}