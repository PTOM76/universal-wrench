package net.pitan76.universalwrench.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.pitan76.mcpitanlib.api.gui.slot.CompatibleSlot;
import net.pitan76.universalwrench.item.WrenchItem;

public class WrenchInputSlot extends CompatibleSlot {

    public WrenchInputSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        if (!(inventory.getStack(0).getItem() instanceof WrenchItem))
            return false;

        return super.canInsert(stack);
    }
}
