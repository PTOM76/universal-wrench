package net.pitan76.universalwrench.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.item.ItemUseEvent;
import net.pitan76.mcpitanlib.api.event.result.EventResult;
import net.pitan76.mcpitanlib.api.event.v0.InteractionEventRegistry;
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

        InteractionEventRegistry.registerRightClickBlock(e -> {
            if (!e.isExistPlayer()) return EventResult.pass();

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
                ActionResult result = BlockStateUtil.onUse(state, player.getWorld(), player, e.direction, e.getPos());
                player.setStackInHand(hand, stack);

                if (result != ActionResult.PASS)
                    return EventResult.success();
            }

            return EventResult.success();
        });
    }

    public WrenchItem() {
        this(CompatibleItemSettings.of().maxCount(1).addGroup(DefaultItemGroups.TOOLS));
    }

    public static List<ItemStack> getWrenches(World world, ItemStack universalWrenchStack) {
        List<ItemStack> list = new ArrayList<>();

        if (!(universalWrenchStack.getItem() instanceof WrenchItem) || !CustomDataUtil.hasNbt(universalWrenchStack)) return list;

        NbtCompound nbt = CustomDataUtil.getNbt(universalWrenchStack);

        list = ItemStackList.ofSize(4 * 4);
        InventoryUtil.readNbt(RegistryLookupUtil.getRegistryLookup(world), nbt, (ItemStackList) list);

        return list;
    }

    @Override
    public TypedActionResult<ItemStack> onRightClick(ItemUseEvent e) {
        return super.onRightClick(e);
    }

    public static ItemStack getModWrench(String namespace, String path) {
        if (!ItemUtil.isExist(CompatIdentifier.of(namespace, path)))
            return ItemStackUtil.empty();

        return ItemStackUtil.create(ItemUtil.fromId(CompatIdentifier.of(namespace, path)));
    }
}
