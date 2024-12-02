package net.pitan76.universalwrench;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandlerType;
import net.pitan76.mcpitanlib.api.CommonModInitializer;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.block.CompatibleMaterial;
import net.pitan76.mcpitanlib.api.gui.SimpleScreenHandlerTypeBuilder;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.registry.result.RegistryResult;
import net.pitan76.mcpitanlib.api.registry.v2.CompatRegistryV2;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.api.util.item.ItemUtil;
import net.pitan76.mcpitanlib.midohra.item.ItemGroups;
import net.pitan76.universalwrench.block.WrenchEditTable;
import net.pitan76.universalwrench.item.WrenchItem;
import net.pitan76.universalwrench.screen.WrenchEditTableScreenHandler;

import java.util.function.Supplier;

public class UniversalWrench extends CommonModInitializer {
    public static final String MOD_ID = "universalwrench";
    public static final String MOD_NAME = "Universal Wrench";

    public static UniversalWrench INSTANCE;
    public static CompatRegistryV2 registry;

    public static Supplier<ScreenHandlerType<WrenchEditTableScreenHandler>> WRENCH_EDIT_TABLE_SCREEN_HANDLER;

    public static RegistryResult<Block> WRENCH_EDIT_TABLE_BLOCK;
    
    public static RegistryResult<Item> WRENCH;
    public static RegistryResult<Item> WRENCH_EDIT_TABLE_ITEM;

    @Override
    public void init() {
        INSTANCE = this;
        registry = super.registry;

        UWConfig.init();

        WRENCH_EDIT_TABLE_SCREEN_HANDLER = registry.registerScreenHandlerTypeSavingGenerics(_id("wrench_screen_handler"), () -> new SimpleScreenHandlerTypeBuilder<>(WrenchEditTableScreenHandler::new).build());

        WRENCH_EDIT_TABLE_BLOCK = registry.registerBlock(_id("wrench_edit_table"), () -> new WrenchEditTable(CompatibleBlockSettings.of(_id("wrench_edit_table"), CompatibleMaterial.METAL).strength(1.5f, 3.0f)));
        WRENCH_EDIT_TABLE_ITEM = registry.registerItem(_id("wrench_edit_table"), () -> ItemUtil.create(WRENCH_EDIT_TABLE_BLOCK.getOrNull(), CompatibleItemSettings.of(_id("wrench_edit_table")).addGroup(ItemGroups.FUNCTIONAL)));
        
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