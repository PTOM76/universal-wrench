package net.pitan76.universalwrench.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.pitan76.universalwrench.client.UniversalWrenchClient;

public class UniversalWrenchClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        UniversalWrenchClient.init();
    }
}