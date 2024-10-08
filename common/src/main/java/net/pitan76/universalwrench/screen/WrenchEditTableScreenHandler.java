package net.pitan76.universalwrench.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.gui.SimpleScreenHandler;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.universalwrench.UniversalWrench;
import net.pitan76.universalwrench.inventory.WrenchEditInventory;
import net.pitan76.universalwrench.screen.slot.UniversalWrenchSlot;
import net.pitan76.universalwrench.screen.slot.WrenchInputSlot;

import java.util.ArrayList;
import java.util.List;

public class WrenchEditTableScreenHandler extends SimpleScreenHandler {

    public PlayerInventory playerInventory;
    public WrenchEditInventory inventory;

    public WrenchEditTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(UniversalWrench.WRENCH_EDIT_TABLE_SCREEN_HANDLER.get(), syncId, playerInventory
                , new WrenchEditInventory(1 + 16));
    }

    protected WrenchEditTableScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, WrenchEditInventory inventory) {
        super(type, syncId);
        this.playerInventory = playerInventory;
        this.inventory = inventory;
        this.inventory.screenHandler = this;

        initSlots();
    }

    public void initSlots() {
        addPlayerMainInventorySlots(playerInventory, 8, 84);
        addPlayerHotbarSlots(playerInventory, 8, 142);

        callAddSlot(new UniversalWrenchSlot(inventory, 0, 44, 35));
        addWrenchInputSlots(inventory, 1, 88, 7, 18, 4, 4);
    }

    @Override
    public ItemStack quickMoveOverride(Player player, int index) {
        Slot slot = ScreenHandlerUtil.getSlot(this, index);
        if (SlotUtil.hasStack(slot)) {
            ItemStack originalStack = SlotUtil.getStack(slot);

            // Inventory
            if (index < 36) {
                if (!this.callInsertItem(originalStack, 36, 53, false)) {
                    return ItemStackUtil.empty();
                }

            } else if (!this.callInsertItem(originalStack, 0, 35, false)) {
                return ItemStackUtil.empty();
            }

            if (ItemStackUtil.isEmpty(originalStack)) {
                SlotUtil.setStack(slot, ItemStackUtil.empty());
            } else {
                SlotUtil.markDirty(slot);
            }
        }
        return ItemStackUtil.empty();
    }

    @Override
    public void close(Player player) {
        if (!inventory.getWrenchStack().isEmpty())
            player.giveStack(inventory.getWrenchStack());
        super.close(player);
    }

    protected List<Slot> addWrenchInputSlots(Inventory inventory, int firstIndex, int firstX, int firstY, int size, int maxAmountX, int maxAmountY) {
        if (size < 0) {
            size = 18;
        }

        List<Slot> slots = new ArrayList<>();

        for(int y = 0; y < maxAmountY; ++y) {
            List<Slot> xSlots = this.addWrenchInputSlotsX(inventory, firstIndex + y * maxAmountX, firstX, firstY + y * size, size, maxAmountX);
            slots.addAll(xSlots);
        }

        return slots;
    }

    protected List<Slot> addWrenchInputSlotsX(Inventory inventory, int firstIndex, int firstX, int y, int size, int amount) {
        if (size < 0) {
            size = 18;
        }

        List<Slot> slots = new ArrayList<>();

        for(int x = 0; x < amount; ++x) {
            Slot slot = this.callAddSlot(new WrenchInputSlot(inventory, firstIndex + x, firstX + x * size, y));
            slots.add(slot);
        }

        return slots;
    }
}