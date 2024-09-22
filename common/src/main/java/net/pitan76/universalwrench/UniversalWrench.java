package net.pitan76.universalwrench;

import net.pitan76.mcpitanlib.api.CommonModInitializer;
import net.pitan76.mcpitanlib.api.registry.v2.CompatRegistryV2;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.universalwrench.item.DamageableWrenchItem;
import net.pitan76.universalwrench.item.WrenchItem;

public class UniversalWrench extends CommonModInitializer {
    public static final String MOD_ID = "universalwrench";
    public static final String MOD_NAME = "Universal Wrench";

    public static UniversalWrench INSTANCE;
    public static CompatRegistryV2 registry;

    @Override
    public void init() {
        INSTANCE = this;
        registry = super.registry;

        UWConfig.init();

        registry.registerItem(_id("damageable_wrench"), () -> new DamageableWrenchItem(100));
        registry.registerItem(_id("wrench"), WrenchItem::new);
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