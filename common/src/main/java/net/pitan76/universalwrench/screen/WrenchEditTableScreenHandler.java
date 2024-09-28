package net.pitan76.universalwrench.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
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

    public ItemStack stack = ItemStackUtil.empty();

    public WrenchEditTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(UniversalWrench.WRENCH_EDIT_TABLE_SCREEN_HANDLER.getOrNull(), syncId, playerInventory, new WrenchEditInventory(1 + 16));

        //if (playerInventory.player == null) return;
        //Player player = new Player(playerInventory.player);

        //Optional<ItemStack> optional = player.getCurrentHandItem();

        //if (!optional.isPresent()) return;

        //ItemStack stack = optional.get();
        //if (stack.isEmpty() || !(stack.getItem() instanceof WrenchItem)) return;
        //this.stack = stack;
        //if (!CustomDataUtil.hasNbt(stack))
            return;

        //loadInventoryFromNbt(CustomDataUtil.getNbt(stack), player);
    }

    public void loadInventoryFromNbt(NbtCompound nbt, Player player) {
        DefaultedList<ItemStack> list = DefaultedList.ofSize(16, ItemStackUtil.empty());
        InventoryUtil.readNbt(RegistryLookupUtil.getRegistryLookup(player.getWorld()), nbt, list);

        for (int i = 1; i < list.size(); i++) {
            inventory.superSetStack(i, list.get(i));
        }
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