package net.pitan76.universalwrench.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.pitan76.mcpitanlib.api.block.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.block.ExtendBlock;
import net.pitan76.mcpitanlib.api.event.block.BlockUseEvent;
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
        return TextUtil.translatable("container.universalwrench.wrench_edit_table.json");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new WrenchEditTableScreenHandler(syncId, playerInventory);
    }
}
