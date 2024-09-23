package net.pitan76.universalwrench.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.collection.DefaultedList;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.gui.SimpleScreenHandler;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.universalwrench.item.WrenchItem;

import java.util.Optional;

public class WrenchScreenHandler extends SimpleScreenHandler {

    public PlayerInventory playerInventory;
    public Inventory inventory;

    public ItemStack stack = ItemStackUtil.empty();

    public WrenchScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(ScreenHandlers.WRENCH_SCREEN_HANDLER.getOrNull(), syncId, playerInventory, InventoryUtil.createSimpleInventory(3 * 9));

        if (playerInventory.player == null) return;
        Player player = new Player(playerInventory.player);

        Optional<ItemStack> optional = player.getCurrentHandItem();

        if (!optional.isPresent()) return;

        ItemStack stack = optional.get();
        if (stack.isEmpty() || !(stack.getItem() instanceof WrenchItem)) return;
        this.stack = stack;
        if (!CustomDataUtil.hasNbt(stack))
            return;

        loadInventoryFromNbt(CustomDataUtil.getNbt(stack), player);
    }

    public void loadInventoryFromNbt(NbtCompound nbt, Player player) {
        // TODO: Generate CompatRegistryLookup from World
        //CompatRegistryLookup registryLookup = new CompatRegistryLookup(player.getWorld().getRegistryManager());

        DefaultedList<ItemStack> list = DefaultedList.ofSize(3 * 9, ItemStackUtil.empty());
        InventoryUtil.readNbt(player.getWorld(), nbt, list);

        // TODO: InventoryUtil.copy(list, inventory); をつくれや
        //inventory = IInventory.of(list);

        // TODO: クソ処理なので InventoryUtil.copy 完成後に修正する
        for (int i = 0; i < list.size(); i++) {
            inventory.setStack(i, list.get(i));
        }
    }

    protected WrenchScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(type, syncId);
        this.playerInventory = playerInventory;
        this.inventory = inventory;

        initSlots();
    }

    public void initSlots() {
        addPlayerMainInventorySlots(playerInventory, 8, 84);
        addPlayerHotbarSlots(playerInventory, 8, 142);

        addSlots(inventory, 0, 8, 18, 18, 9, 3);
    }

    @Override
    public void close(Player player) {
        if (player.isClient()) {
            super.close(player);
            return;
        }

        DefaultedList<ItemStack> list = DefaultedList.ofSize(3 * 9, ItemStackUtil.empty());
        for (int i = 0; i < inventory.size(); i++)
            list.set(i, inventory.getStack(i));

        //CompatRegistryLookup registryLookup = new CompatRegistryLookup(player.getWorld().getRegistryManager());
        NbtCompound nbt = NbtUtil.create();
        InventoryUtil.writeNbt(player.getWorld(), nbt, list);

        CustomDataUtil.setNbt(stack, nbt);

        super.close(player);
    }
}
