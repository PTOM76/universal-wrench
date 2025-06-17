package net.pitan76.universalwrench.block;

import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.pitan76.mcpitanlib.api.block.v2.CompatBlock;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.event.block.BlockUseEvent;
import net.pitan76.mcpitanlib.api.event.block.StateReplacedEvent;
import net.pitan76.mcpitanlib.api.event.container.factory.DisplayNameArgs;
import net.pitan76.mcpitanlib.api.gui.args.CreateMenuEvent;
import net.pitan76.mcpitanlib.api.gui.v2.SimpleScreenHandlerFactory;
import net.pitan76.mcpitanlib.api.util.CompatActionResult;
import net.pitan76.mcpitanlib.api.util.InventoryUtil;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.TextUtil;
import net.pitan76.mcpitanlib.core.serialization.CompatMapCodec;
import net.pitan76.mcpitanlib.core.serialization.codecs.CompatBlockMapCodecUtil;
import net.pitan76.universalwrench.screen.WrenchEditTableScreenHandler;

public class WrenchEditTable extends CompatBlock implements SimpleScreenHandlerFactory {

    public static final CompatMapCodec<WrenchEditTable> CODEC = CompatBlockMapCodecUtil.createCodec(WrenchEditTable::new);

    @Override
    public CompatMapCodec<? extends WrenchEditTable> getCompatCodec() {
        return CODEC;
    }

    public WrenchEditTable(CompatibleBlockSettings settings) {
        super(settings);
    }

    @Override
    public CompatActionResult onRightClick(BlockUseEvent e) {
        e.player.openMenu(this);
        return e.success();
    }

    @Override
    public void onStateReplaced(StateReplacedEvent e) {
        if (!e.isSameState() && e.hasInventory()) {
            Inventory inv = (Inventory) e.getBlockEntity();
            int size = InventoryUtil.getSize(inv);
            for (int i = 1; i < size; i++) {
                InventoryUtil.setStack(inv, i, ItemStackUtil.empty());
            }

            e.spawnDropsInContainer();
        }
        super.onStateReplaced(e);
    }

    @Override
    public Text getDisplayName(DisplayNameArgs args) {
        return TextUtil.translatable("container.universalwrench.wrench_edit_table");
    }

    @Override
    public ScreenHandler createMenu(CreateMenuEvent e) {
        return new WrenchEditTableScreenHandler(e.syncId, e.playerInventory);
    }
}
