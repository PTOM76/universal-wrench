package net.pitan76.universalwrench.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.item.ItemUseEvent;
import net.pitan76.mcpitanlib.api.event.item.ItemUseOnBlockEvent;
import net.pitan76.mcpitanlib.api.event.item.ItemUseOnEntityEvent;
import net.pitan76.mcpitanlib.api.event.result.EventResult;
import net.pitan76.mcpitanlib.api.event.v0.InteractionEventRegistry;
import net.pitan76.mcpitanlib.api.event.v0.event.ClickBlockEvent;
import net.pitan76.mcpitanlib.api.item.v2.CompatItem;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.mcpitanlib.api.util.collection.ItemStackList;
import net.pitan76.mcpitanlib.midohra.item.ItemGroups;

import static net.pitan76.universalwrench.UniversalWrench._id;

public class WrenchItem extends CompatItem {
    public WrenchItem(CompatibleItemSettings settings) {
        super(settings);
        InteractionEventRegistry.registerRightClickBlock(this::onRightClickOnBlockEvent);
    }

    public WrenchItem() {
        this(CompatibleItemSettings.of(_id("wrench")).maxCount(1).addGroup(ItemGroups.TOOLS));
    }

    /**
     * Right click on block event
     * @param e Click block event
     * @return Event result
     */
    public EventResult onRightClickOnBlockEvent(ClickBlockEvent e) {
        if (!e.isExistPlayer()) return EventResult.pass();

        Player player = e.getPlayer();
        ItemStack stack = e.getStackInHand();

        if (e.isEmptyStackInHand() || !(stack.getItem() instanceof WrenchItem)) return EventResult.pass();

        BlockState state = e.getBlockState();
        Block block = e.getBlock();
        if (block == null) return EventResult.pass();

        Hand hand = e.getHand();
        World world = e.getWorld();

        ItemStackList wrenches = getWrenches(world, stack);
        for (int i = 0; i < wrenches.size(); i++) {
            ItemStack wrench = wrenches.get(i);
            if (wrench.isEmpty()) continue;

            player.setStackInHand(hand, wrench);
            CompatActionResult result = InteractUtil.useBlock(state, world, player, e.getDirection(), e.getPos());
            player.setStackInHand(hand, stack);

            if (!result.equals(CompatActionResult.PASS)) {
                wrenches.set(i, wrench);
                setWrenches(world, stack, wrenches);

                return result.toEventResult();
            }
        }

        return EventResult.pass();
    }

    /**
     * Get list of wrenches from universal wrench item stack
     * @param world World
     * @param universalWrenchStack Universal wrench item stack
     * @return List of wrenches
     */
    public static ItemStackList getWrenches(World world, ItemStack universalWrenchStack) {
        if (!(universalWrenchStack.getItem() instanceof WrenchItem) || !CustomDataUtil.hasNbt(universalWrenchStack))
            return ItemStackList.of();

        ItemStackList list = ItemStackList.ofSize(4 * 4, ItemStackUtil.empty());

        NbtCompound nbt = CustomDataUtil.getOrCreateNbt(universalWrenchStack);
        InventoryUtil.readNbt(RegistryLookupUtil.getRegistryLookup(world), nbt, list);

        return list;
    }

    /**
     * Set list of wrenches to universal wrench item stack
     * @param universalWrenchStack Universal wrench item stack
     * @param wrenches List of wrenches
     */
    public static void setWrenches(World world, ItemStack universalWrenchStack, ItemStackList wrenches) {
        if (!(universalWrenchStack.getItem() instanceof WrenchItem))
            return;

        NbtCompound nbt = CustomDataUtil.getOrCreateNbt(universalWrenchStack);
        InventoryUtil.writeNbt(RegistryLookupUtil.getRegistryLookup(world), nbt, wrenches);
    }

    @Override
    public CompatActionResult onRightClickOnBlock(ItemUseOnBlockEvent e) {
        Player player = e.getPlayer();
        Hand hand = e.getHand();
        ItemStack stack = e.getStack();
        World world = e.getWorld();

        ItemStackList wrenches = getWrenches(world, stack);

        for (int i = 0; i < wrenches.size(); i++) {
            ItemStack wrench = wrenches.get(i);
            if (wrench.isEmpty()) continue;

            player.setStackInHand(hand, wrench);
            CompatActionResult result = InteractUtil.useItemOnBlock(wrench.getItem(), e);
            player.setStackInHand(hand, stack);

            if (!result.equals(CompatActionResult.PASS)) {
                wrenches.set(i, wrench);
                setWrenches(world, stack, wrenches);

                return result;
            }
        }

        return e.pass();
    }

    @Override
    public StackActionResult onRightClick(ItemUseEvent e) {
        return super.onRightClick(e);
    }

    @Override
    public CompatActionResult onRightClickOnEntity(ItemUseOnEntityEvent e) {
        return super.onRightClickOnEntity(e);
    }
}
