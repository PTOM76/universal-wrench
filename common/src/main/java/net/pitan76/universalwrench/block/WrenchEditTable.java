package net.pitan76.universalwrench.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.pitan76.mcpitanlib.api.block.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.block.ExtendBlock;
import net.pitan76.mcpitanlib.api.event.block.BlockUseEvent;
import net.pitan76.mcpitanlib.api.event.block.ItemScattererUtil;
import net.pitan76.mcpitanlib.api.event.block.StateReplacedEvent;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.TextUtil;
import net.pitan76.universalwrench.screen.WrenchEditTableScreenHandler;

// TODO: NamedScreenHandlerFactoryもMCPitanLibに実装する
public class WrenchEditTable extends ExtendBlock implements NamedScreenHandlerFactory {
    public WrenchEditTable(CompatibleBlockSettings settings) {
        super(settings);
    }

    @Override
    public ActionResult onRightClick(BlockUseEvent e) {
        if (e.isClient())
            return e.success();

        e.player.openMenu(this);

        return e.success();
    }

    @Override
    public Text getDisplayName() {
        return TextUtil.translatable("container.universalwrench.wrench_edit_table");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new WrenchEditTableScreenHandler(syncId, playerInventory);
    }

    @Override
    public void onStateReplaced(StateReplacedEvent e) {
        if (!e.isSameState() && e.hasInventory()) {
            Inventory inventory = (Inventory) e.getBlockEntity();
            for (int i = 1; i < inventory.size(); i++) {
                inventory.setStack(i, ItemStackUtil.empty());
            }

            ItemScattererUtil.spawn(e.getWorld(), e.getPos(), e.getBlockEntity());
            e.updateComparators();
        }
        super.onStateReplaced(e);
    }
}
