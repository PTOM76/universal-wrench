package net.pitan76.universalwrench.item;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.item.ItemUseEvent;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.DefaultItemGroups;
import net.pitan76.mcpitanlib.api.item.ExtendItem;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.universalwrench.UWConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WrenchItem extends ExtendItem {
    public WrenchItem(CompatibleItemSettings settings) {
        super(settings);
        InteractionEvent.RIGHT_CLICK_BLOCK.register(((p, hand, pos, dir) -> {
            Player player = new Player(p);
            if (player.getEntity() == null) return EventResult.pass();
            ItemStack stack = player.getStackInHand(hand);

            if (stack.isEmpty() || !(stack.getItem() instanceof WrenchItem))
                return EventResult.pass();

            BlockState state = player.getWorld().getBlockState(pos);
            Block block = state.getBlock();
            if (block == null) return EventResult.pass();


            // new
            List<ItemStack> wrenches = getWrenches(player.getWorld(), stack);
            for (ItemStack wrench : wrenches) {
                player.getEntity().setStackInHand(hand, wrench);
                ActionResult result = state.onUse(player.getWorld(), player.getEntity(), hand, new BlockHitResult(p.getPos(), dir, pos, false));
                player.getEntity().setStackInHand(hand, stack);

                if (result != ActionResult.PASS)
                    return EventResult.interruptTrue();
            }






            // 廃止
            /*
            ItemStack tmpStack = ItemStackUtil.empty();

            // Config
            String namespace = BlockUtil.toCompatID(block).getNamespace();
            for (String wrench : UWConfig.wrenches) {
                CompatIdentifier id = CompatIdentifier.of(wrench);

                if (namespace.equals(id.getNamespace()))
                    tmpStack = getModWrench(id.getNamespace(), id.getPath());
            }

            // Default
            if (tmpStack.isEmpty()) tmpStack = getModWrenchByBlock(block);
            if (tmpStack.isEmpty()) tmpStack = getModWrenchByBlockEntity(player.getWorld().getBlockEntity(pos), block);

            if (tmpStack.isEmpty()) return EventResult.pass();

            player.getEntity().setStackInHand(hand, tmpStack);

            state.onUse(player.getWorld(), player.getEntity(), hand, new BlockHitResult(p.getPos(), dir, pos, false));

            player.getEntity().setStackInHand(hand, stack);

            if (stack.getItem() instanceof DamageableWrenchItem) {
                Optional<ServerPlayerEntity> optional = player.getServerPlayer();
                optional.ifPresent(serverPlayerEntity -> ItemStackUtil.damage(stack, 1, serverPlayerEntity));
            }


             */
            return EventResult.interruptTrue();
        }));
    }

    public WrenchItem() {
        this(CompatibleItemSettings.of().maxCount(1).addGroup(DefaultItemGroups.TOOLS));
    }

    public static List<ItemStack> getWrenches(World world, ItemStack universalWrenchStack) {
        List<ItemStack> list = new ArrayList<>();

        if (!(universalWrenchStack.getItem() instanceof WrenchItem) || !CustomDataUtil.hasNbt(universalWrenchStack)) return list;

        NbtCompound nbt = CustomDataUtil.getNbt(universalWrenchStack);

        list = DefaultedList.ofSize(4 * 4, ItemStackUtil.empty());
        InventoryUtil.readNbt(world, nbt, (DefaultedList<ItemStack>) list);

        return list;
    }

    @Override
    public TypedActionResult<ItemStack> onRightClick(ItemUseEvent e) {
        return super.onRightClick(e);
    }

    @ExpectPlatform
    public static ItemStack getModWrenchByBlock(Block block) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static ItemStack getModWrenchByBlockEntity(BlockEntity blockEntity, Block block) {
        throw new AssertionError();
    }

    public static ItemStack getModWrench(String namespace, String path) {
        if (!ItemUtil.isExist(CompatIdentifier.of(namespace, path)))
            return ItemStackUtil.empty();

        return ItemStackUtil.create(ItemUtil.fromId(CompatIdentifier.of(namespace, path)));
    }
}
