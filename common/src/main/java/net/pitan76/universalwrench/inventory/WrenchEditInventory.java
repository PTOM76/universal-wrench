package net.pitan76.universalwrench.inventory;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.util.CustomDataUtil;
import net.pitan76.mcpitanlib.api.util.InventoryUtil;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.NbtUtil;
import net.pitan76.universalwrench.item.WrenchItem;
import net.pitan76.universalwrench.screen.WrenchEditTableScreenHandler;

import java.util.Optional;

// TODO: SimpleInventoryをMCPItanLibに実装する
public class WrenchEditInventory extends SimpleInventory {

    public WrenchEditTableScreenHandler screenHandler;

    public WrenchEditInventory(WrenchEditTableScreenHandler screenHandler) {
        this(1 + 16);
        this.screenHandler = screenHandler;
    }

    public WrenchEditInventory(int size) {
        super(size);
    }

    public Optional<WrenchEditTableScreenHandler> getScreenHandler() {
        return Optional.ofNullable(screenHandler);
    }

    public Optional<Player> getPlayer() {
        Optional<WrenchEditTableScreenHandler> optional = getScreenHandler();
        if (!optional.isPresent()) return Optional.empty();

        WrenchEditTableScreenHandler screenHandler = optional.get();
        if (screenHandler.playerInventory == null || screenHandler.playerInventory.player == null)
            return Optional.empty();

        return Optional.of(new Player(screenHandler.playerInventory.player));
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (slot == 0) {
            if (stack.isEmpty()) {
                super.setStack(slot, stack);
                setEmptyToWrenchContainer();
            }

            if (stack.getItem() instanceof WrenchItem) {
                super.setStack(slot, stack);
                updateWrenchContainerByWrenchStack();
            }
            return;
        }

        updateWrenchStack();

        super.setStack(slot, stack);
    }

    public void superSetStack(int slot, ItemStack stack) {
        super.setStack(slot, stack);
    }

    public void setEmptyToWrenchContainer() {
        for (int i = 1; i < size(); i++) {
            if (getStack(i).isEmpty()) {
                super.setStack(i, ItemStackUtil.empty());
            }
        }
    }

    public ItemStack getWrenchStack() {
        return getStack(0);
    }

    public void updateWrenchStack() {
        Optional<Player> optional = getPlayer();
        if (!optional.isPresent() || optional.get().isClient()) return;

        Player player = optional.get();

        ItemStack stack = getWrenchStack();
        if (stack.isEmpty() || !(stack.getItem() instanceof WrenchItem)) return;

        DefaultedList<ItemStack> list = DefaultedList.ofSize(4 * 4, ItemStackUtil.empty());
        for (int i = 1; i < size(); i++)
            list.set(i, getStack(i));

        //CompatRegistryLookup registryLookup = new CompatRegistryLookup(player.getWorld().getRegistryManager());
        NbtCompound nbt = NbtUtil.create();
        InventoryUtil.writeNbt(player.getWorld(), nbt, list);

        CustomDataUtil.setNbt(stack, nbt);
    }

    public void updateWrenchContainerByWrenchStack() {
        Optional<Player> optional = getPlayer();
        if (!optional.isPresent() || optional.get().isClient()) return;

        Player player = optional.get();

        ItemStack stack = getWrenchStack();
        if (stack.isEmpty() || !(stack.getItem() instanceof WrenchItem)) return;

        NbtCompound nbt = CustomDataUtil.getNbt(stack);
        if (nbt == null) return;

        DefaultedList<ItemStack> list = DefaultedList.ofSize(4 * 4, ItemStackUtil.empty());
        InventoryUtil.readNbt(player.getWorld(), nbt, list);

        for (int i = 1; i < size(); i++) {
            super.setStack(i, list.get(i));
        }
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack stack = super.removeStack(slot, amount);
        updateWrenchStack();
        return stack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return super.removeStack(slot);
    }

    @Override
    public ItemStack removeItem(Item item, int count) {
        return super.removeItem(item, count);
    }
}
