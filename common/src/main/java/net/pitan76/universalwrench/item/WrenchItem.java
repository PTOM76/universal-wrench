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
import net.pitan76.mcpitanlib.api.util.block.BlockUtil;
import net.pitan76.mcpitanlib.api.util.collection.ItemStackList;
import net.pitan76.mcpitanlib.api.util.item.ItemUtil;
import net.pitan76.mcpitanlib.midohra.item.ItemGroups;
import net.pitan76.universalwrench.UniversalWrench;
import net.pitan76.universalwrench.WrenchAction;

import java.util.*;
import java.util.function.Supplier;

import static net.pitan76.universalwrench.UniversalWrench._id;

public class WrenchItem extends CompatItem {
    public WrenchItem(CompatibleItemSettings settings) {
        super(settings);
        InteractionEventRegistry.registerRightClickBlock(this::onRightClickOnBlockEvent);
    }

    public WrenchItem() {
        this(CompatibleItemSettings.of(_id("wrench")).maxCount(1).addGroup(ItemGroups.TOOLS));
    }

    // 後に処理するためのアクションをスタックする
    public static final Map<ItemStack, List<WrenchAction>> actionStack = new HashMap<>();

    public static void pushAction(ItemStack stack, Supplier<CompatActionResult> action, int index) {
        if (stack.isEmpty() || !(stack.getItem() instanceof WrenchItem)) return;

        List<WrenchAction> actions = actionStack.computeIfAbsent(stack, k -> new ArrayList<>());
        actions.add(new WrenchAction(action, index));
    }

    /**
     * Right-click on block event
     * onRightClickOnBlockと違ってブロック側で処理されているイベントはそれが優先されるのでここで対処する
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

        String namespace = BlockUtil.toId(block).getNamespace();
        ItemStackList wrenches = getWrenchesWithSortByNamespace(world, stack, namespace);
        for (int i = 0; i < wrenches.size(); i++) {
            ItemStack wrench = wrenches.get(i);
            if (wrench.isEmpty()) continue;

            if (!ItemUtil.toId(ItemStackUtil.getItem(wrench)).getNamespace().equalsIgnoreCase(namespace)) {
                pushAction(stack, () -> {
                    player.setStackInHand(hand, wrench);
                    CompatActionResult result = InteractUtil.useBlock(state, world, player, e.getDirection(), e.getPos());
                    player.setStackInHand(hand, stack);

                    return result;
                }, i);
                continue;
            }

            player.setStackInHand(hand, wrench);
            CompatActionResult result = InteractUtil.useBlock(state, world, player, e.getDirection(), e.getPos());
            player.setStackInHand(hand, stack);

            //UniversalWrench.INSTANCE.logger.info("WrenchItem.onRightClickOnBlockE: " + result.getName() + " for " + ItemUtil.toId(wrench.getItem()).toString());

            if (!result.equals(CompatActionResult.PASS)) {
                wrenches.set(i, wrench);
                setWrenches(world, stack, wrenches);

                return result.toEventResult();
            }
        }

        return EventResult.pass();
    }

    /**
     * Sort wrenches by namespace
     * @param wrenches List of wrenches
     * @param namespace Namespace to sort by
     * @return Sorted list of wrenches
     */
    public static ItemStackList sortByNamespace(ItemStackList wrenches, String namespace) {
        if (wrenches.isEmpty() || namespace == null || namespace.isEmpty()) return wrenches;

        List<ItemStack> sorted = new ArrayList<>();
        List<ItemStack> temp = new ArrayList<>();

        for (ItemStack stack : wrenches) {
            if (ItemStackUtil.isEmpty(stack)) continue;
            if (!ItemUtil.toId(ItemStackUtil.getItem(stack)).getNamespace()
                    .equalsIgnoreCase(namespace)) {
                temp.add(stack);
                continue;
            }

            sorted.add(stack);
        }

        sorted.addAll(temp);

        return ItemStackList.of2(sorted);
    }

    /**
     * Get list of wrenches from universal wrench item stack
     * @param world World
     * @param universalWrenchStack Universal wrench item stack
     * @return List of wrenches
     */
    public static ItemStackList getWrenchesWithSortByNamespace(World world, ItemStack universalWrenchStack, String namespace) {
        ItemStackList list = getWrenches(world, universalWrenchStack);
        list = sortByNamespace(list, namespace);
        return list;
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

        String namespace = e.getBlockWrapper().getId().getNamespace();
        ItemStackList wrenches = getWrenchesWithSortByNamespace(world, stack, namespace);

        for (int i = 0; i < wrenches.size(); i++) {
            ItemStack wrench = wrenches.get(i);
            if (wrench.isEmpty()) continue;

            player.setStackInHand(hand, wrench);
            CompatActionResult result = InteractUtil.useItemOnBlock(wrench.getItem(), e);
            player.setStackInHand(hand, stack);

            //UniversalWrench.INSTANCE.logger.info("WrenchItem.onRightClickOnBlock: " + result.getName() + " for " + ItemUtil.toId(wrench.getItem()).toString());

            if (!result.equals(CompatActionResult.PASS)) {
                wrenches.set(i, wrench);
                setWrenches(world, stack, wrenches);

                return result;
            }
        }

        if (actionStack.containsKey(stack)) {
            List<WrenchAction> actions = actionStack.get(stack);
            for (WrenchAction action : actions) {
                CompatActionResult result = action.supplier.get();
                if (isSuccess(result)) {
                    int index = action.index;
                    wrenches.set(index, stack);
                    setWrenches(world, stack, wrenches);

                    return result;
                }
            }
            actionStack.remove(stack);
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

    public static boolean isSuccess(CompatActionResult result) {
        return result.equals(CompatActionResult.SUCCESS) || result.equals(CompatActionResult.SUCCESS_SERVER);
    }
}
