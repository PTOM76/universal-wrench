package net.pitan76.universalwrench.client;

import net.pitan76.mcpitanlib.api.client.registry.CompatRegistryClient;
import net.pitan76.universalwrench.screen.ScreenHandlers;

public class UniversalWrenchClient {
    public static void init() {
        CompatRegistryClient.registerScreen(ScreenHandlers.WRENCH_SCREEN_HANDLER.getOrNull(), WrenchScreen::new);
    }
}
