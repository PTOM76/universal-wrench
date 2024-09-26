package net.pitan76.universalwrench.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.collection.DefaultedList;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.gui.SimpleScreenHandler;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.universalwrench.UniversalWrench;
import net.pitan76.universalwrench.inventory.WrenchEditInventory;
import net.pitan76.universalwrench.screen.slot.UniversalWrenchSlot;

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
        // TODO: Generate CompatRegistryLookup from World
        //CompatRegistryLookup registryLookup = new CompatRegistryLookup(player.getWorld().getRegistryManager());

        // TODO: DefaultedListも改善するべき
        DefaultedList<ItemStack> list = DefaultedList.ofSize(16, ItemStackUtil.empty());
        InventoryUtil.readNbt(player.getWorld(), nbt, list);

        // TODO: InventoryUtil.copy(list, inventory); をつくれや
        //inventory = IInventory.of(list);

        // TODO: クソ処理なので InventoryUtil.copy 完成後に修正する
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
        addSlots(inventory, 1, 88, 7, 18, 4, 4);
    }

    @Override
    public void close(Player player) {
        super.close(player);
    }
}
