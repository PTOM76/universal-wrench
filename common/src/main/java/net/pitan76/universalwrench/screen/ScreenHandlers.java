package net.pitan76.universalwrench.screen;

import net.minecraft.screen.ScreenHandlerType;
import net.pitan76.mcpitanlib.api.gui.SimpleScreenHandlerTypeBuilder;
import net.pitan76.mcpitanlib.api.registry.result.RegistryResult;

import static net.pitan76.universalwrench.UniversalWrench._id;
import static net.pitan76.universalwrench.UniversalWrench.registry;

public class ScreenHandlers {
    public static RegistryResult<ScreenHandlerType<?>> WRENCH_SCREEN_HANDLER;

    public static void init() {
        WRENCH_SCREEN_HANDLER = registry.registerScreenHandlerType(_id("wrench_screen_handler"), () -> new SimpleScreenHandlerTypeBuilder<>(WrenchScreenHandler::new).build());
    }
}
