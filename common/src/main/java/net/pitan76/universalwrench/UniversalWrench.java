package net.pitan76.universalwrench;

import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandlerType;
import net.pitan76.mcpitanlib.api.CommonModInitializer;
import net.pitan76.mcpitanlib.api.gui.SimpleScreenHandlerTypeBuilder;
import net.pitan76.mcpitanlib.api.registry.result.RegistryResult;
import net.pitan76.mcpitanlib.api.registry.v2.CompatRegistryV2;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.universalwrench.item.DamageableWrenchItem;
import net.pitan76.universalwrench.item.WrenchItem;
import net.pitan76.universalwrench.screen.WrenchEditTableScreenHandler;

public class UniversalWrench extends CommonModInitializer {
    public static final String MOD_ID = "universalwrench";
    public static final String MOD_NAME = "Universal Wrench";

    public static UniversalWrench INSTANCE;
    public static CompatRegistryV2 registry;

    public static RegistryResult<ScreenHandlerType<?>> WRENCH_EDIT_TABLE_SCREEN_HANDLER;

    public static RegistryResult<Item> DAMAGEABLE_WRENCH;
    public static RegistryResult<Item> WRENCH;

    @Override
    public void init() {
        INSTANCE = this;
        registry = super.registry;

        UWConfig.init();

        WRENCH_EDIT_TABLE_SCREEN_HANDLER = registry.registerScreenHandlerType(_id("wrench_screen_handler"), () -> new SimpleScreenHandlerTypeBuilder<>(WrenchEditTableScreenHandler::new).build());

        DAMAGEABLE_WRENCH = registry.registerItem(_id("damageable_wrench"), () -> new DamageableWrenchItem(100));
        WRENCH = registry.registerItem(_id("wrench"), WrenchItem::new);

    }

    // ----
    public static CompatIdentifier _id(String path) {
        return CompatIdentifier.of(MOD_ID, path);
    }

    @Override
    public String getId() {
        return MOD_ID;
    }

    @Override
    public String getName() {
        return MOD_NAME;
    }
}