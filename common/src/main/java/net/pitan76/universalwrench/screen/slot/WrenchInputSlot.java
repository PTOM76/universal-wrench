package net.pitan76.universalwrench.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.pitan76.mcpitanlib.api.gui.slot.CompatibleSlot;
import net.pitan76.mcpitanlib.api.util.ItemUtil;
import net.pitan76.universalwrench.UWConfig;
import net.pitan76.universalwrench.item.WrenchItem;

public class WrenchInputSlot extends CompatibleSlot {

    public WrenchInputSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        if (!(inventory.getStack(0).getItem() instanceof WrenchItem))
            return false;

        Item item = stack.getItem();

        // BlockItem is not allowed
        if (UWConfig.denyBlockItem && item instanceof BlockItem)
            return false;

        // WhiteListed items are only allowed, if not empty whiteListedItems
        if (!UWConfig.whiteListedItems.isEmpty() && !UWConfig.whiteListedItems.contains(ItemUtil.toCompatID(item).toString()))
            return false;

        // Blacklisted namespaces are not allowed
        if (UWConfig.blackNamespaces.contains(ItemUtil.toCompatID(item).getNamespace()))
            return false;

        // Blacklisted items are not allowed
        if (UWConfig.blackListedItems.contains(ItemUtil.toCompatID(item).toString()))
            return false;

        return super.canInsert(stack);
    }
}
