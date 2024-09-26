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

// TODO: SimpleInventoryをMCPItanLibに実装、setStackなどの関数も用意するべき
public class WrenchEditInventory extends SimpleInventory {

    public WrenchEditTableScreenHandler screenHandler;

    public WrenchEditInventory() {
        this(1 + 16);
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
        // TODO: ScreenHandlerのPlayerInventoryからわざわざplayerを取り出すのはダメ、getPlayer()をprepare
        if (screenHandler.playerInventory == null || screenHandler.playerInventory.player == null)
            return Optional.empty();

        return Optional.of(new Player(screenHandler.playerInventory.player));
    }

    /**
     * If set stack, and slot equals 0 then load or write nbt of universal wrench stack
     * @param slot placed slot index
     * @param stack placed stack
     */
    @Override
    public void setStack(int slot, ItemStack stack) {
        super.setStack(slot, stack);
        if (slot == 0) {
            if (stack.isEmpty()) {
                clearWrenchContainer();
            }

            if (stack.getItem() instanceof WrenchItem) {
                updateWrenchContainerByWrenchStack();
            }
            return;
        }

        // write nbt to stack
        updateWrenchStack();
    }

    /**
     * super method of setStack(slot, stack)
     */
    public void superSetStack(int slot, ItemStack stack) {
        super.setStack(slot, stack);
    }

    /**
     * Clear wrench container (slots of index 1-16)
     */
    public void clearWrenchContainer() {
        for (int i = 1; i < size(); i++) {
            super.setStack(i, ItemStackUtil.empty());

        }
    }

    /**
     * Get universal wrench stack (slot of index 0)
     * @return universal wrench stack
     */
    public ItemStack getWrenchStack() {
        return super.getStack(0);
    }

    /**
     * Set nbt of the wrench to listed item stacks in wrench container (slots of index 1-16)
     */
    public void updateWrenchStack() {
        Optional<Player> optional = getPlayer();
        if (!optional.isPresent() || optional.get().isClient()) return;

        Player player = optional.get();

        ItemStack stack = getWrenchStack();
        if (stack.isEmpty() || !(stack.getItem() instanceof WrenchItem)) return;

        DefaultedList<ItemStack> list = DefaultedList.ofSize(4 * 4, ItemStackUtil.empty());
        for (int i = 1; i < size(); i++)
            list.set(i - 1, getStack(i));

        //CompatRegistryLookup registryLookup = new CompatRegistryLookup(player.getWorld().getRegistryManager());
        NbtCompound nbt = NbtUtil.create();
        InventoryUtil.writeNbt(player.getWorld(), nbt, list);

        CustomDataUtil.setNbt(stack, nbt);
    }

    /**
     * Load item stacks from nbt of the universal wrench stack
     * Set slots of index 1-16 to loaded stacks from nbt of the universal wrench stack
     */
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
            super.setStack(i, list.get(i - 1));
        }
    }

    /**
     * If remove stack, write nbt to the universal wrench stack
     */
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
