package net.pitan76.universalwrench.item;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.item.ItemUseEvent;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.DefaultItemGroups;
import net.pitan76.mcpitanlib.api.item.ExtendItem;
import net.pitan76.mcpitanlib.api.util.BlockUtil;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.ItemUtil;
import net.pitan76.universalwrench.UWConfig;
import net.pitan76.universalwrench.screen.WrenchScreenHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class WrenchItem extends ExtendItem implements NamedScreenHandlerFactory {
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

            return EventResult.interruptTrue();
        }));
    }

    public WrenchItem() {
        this(CompatibleItemSettings.of().maxCount(1).addGroup(DefaultItemGroups.TOOLS));
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

    @Override
    public Text getDisplayName() {
        return getName();
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new WrenchScreenHandler(syncId, playerInventory);
    }
}
