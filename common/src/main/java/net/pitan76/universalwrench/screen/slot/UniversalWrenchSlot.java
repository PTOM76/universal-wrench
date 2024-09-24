package net.pitan76.universalwrench.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.pitan76.mcpitanlib.api.gui.slot.CompatibleSlot;
import net.pitan76.universalwrench.item.WrenchItem;

public class UniversalWrenchSlot extends CompatibleSlot {

    public UniversalWrenchSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        if (!(stack.getItem() instanceof WrenchItem))
            return false;

        return super.canInsert(stack);
    }
}
