package net.pitan76.universalwrench.client;

import net.pitan76.mcpitanlib.api.client.registry.CompatRegistryClient;
import net.pitan76.universalwrench.UniversalWrench;

public class UniversalWrenchClient {
    public static void init() {
        CompatRegistryClient.registerScreen(UniversalWrench.WRENCH_EDIT_TABLE_SCREEN_HANDLER.getOrNull(), WrenchScreen::new);
    }
}
