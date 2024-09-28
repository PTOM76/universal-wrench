package net.pitan76.universalwrench.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.result.EventResult;
import net.pitan76.mcpitanlib.api.event.v0.InteractionEventRegistry;
import net.pitan76.mcpitanlib.api.event.v0.event.ClickBlockEvent;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.DefaultItemGroups;
import net.pitan76.mcpitanlib.api.item.ExtendItem;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.mcpitanlib.api.util.collection.ItemStackList;

import java.util.ArrayList;
import java.util.List;

public class WrenchItem extends ExtendItem {
    public WrenchItem(CompatibleItemSettings settings) {
        super(settings);
        InteractionEventRegistry.registerRightClickBlock(this::onRightClickOnBlockEvent);
    }

    public WrenchItem() {
        this(CompatibleItemSettings.of().maxCount(1)
                .addGroup(DefaultItemGroups.TOOLS, CompatIdentifier.of("universalwrench", "wrench")));
    }

    /**
     * Right click on block event
     * @param e Click block event
     * @return Event result
     */
    public EventResult onRightClickOnBlockEvent(ClickBlockEvent e) {
        if (!e.isExistPlayer())
            return EventResult.pass();

        Player player = e.getPlayer();
        ItemStack stack = e.getStackInHand();

        if (e.isEmptyStackInHand() || !(stack.getItem() instanceof WrenchItem))
            return EventResult.pass();

        BlockState state = e.getBlockState();
        Block block = e.getBlock();
        if (block == null) return EventResult.pass();

        Hand hand = e.getHand();

        List<ItemStack> wrenches = getWrenches(player.getWorld(), stack);
        for (ItemStack wrench : wrenches) {
            player.setStackInHand(hand, wrench);
            ActionResult result = BlockStateUtil.onUse(state, player.getWorld(), player, e.getDirection(), e.getPos());
            player.setStackInHand(hand, stack);

            if (result != ActionResult.PASS)
                return EventResult.success();
        }

        return EventResult.pass();
    }

    /**
     * Get list of wrenches from universal wrench item stack
     * @param world World
     * @param universalWrenchStack Universal wrench item stack
     * @return List of wrenches
     */
    public static List<ItemStack> getWrenches(World world, ItemStack universalWrenchStack) {
        List<ItemStack> list = new ArrayList<>();

        if (!(universalWrenchStack.getItem() instanceof WrenchItem) || !CustomDataUtil.hasNbt(universalWrenchStack)) return list;

        NbtCompound nbt = CustomDataUtil.getNbt(universalWrenchStack);

        list = ItemStackList.ofSize(4 * 4);
        InventoryUtil.readNbt(RegistryLookupUtil.getRegistryLookup(world), nbt, (ItemStackList) list);

        return list;
    }
}
