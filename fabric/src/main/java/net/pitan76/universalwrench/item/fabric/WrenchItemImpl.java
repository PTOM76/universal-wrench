package net.pitan76.universalwrench.item.fabric;

import appeng.block.AEBaseBlock;
import aztech.modern_industrialization.blocks.WrenchableBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.DirtPathBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.pitan76.bew76.config.BEWConfig;
import net.pitan76.enhancedquarries.block.base.BaseBlock;
import net.pitan76.itemalchemy.block.IUseableWrench;
import net.pitan76.mcpitanlib.api.util.BlockUtil;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.PlatformUtil;
import net.pitan76.speedypath.block.CustomPathBlock;
import reborncore.common.blocks.BlockMachineBase;

import static net.pitan76.universalwrench.item.WrenchItem.getModWrench;

public class WrenchItemImpl {
    public static ItemStack getModWrenchByBlock(Block block) {
        if (PlatformUtil.isModLoaded("reborncore") && block instanceof BlockMachineBase)
            return getModWrench("techreborn", "wrench");

        if (PlatformUtil.isModLoaded("itemalchemy") && block instanceof IUseableWrench)
            return getModWrench("itemalchemy", "wrench");

        if (PlatformUtil.isModLoaded("enhancedquarries") && block instanceof BaseBlock)
            return getModWrench("enhancedquarries", "wrench");

        if (PlatformUtil.isModLoaded("speedypath") && (block instanceof CustomPathBlock || block instanceof DirtPathBlock))
            return getModWrench("speedypath", "wrench_for_path");

        if (PlatformUtil.isModLoaded("ae2") && (block instanceof AEBaseBlock)) {
            return getModWrench("ae2", "certus_quartz_wrench");
        }

        return ItemStackUtil.empty();
    }

    public static ItemStack getModWrenchByBlockEntity(BlockEntity blockEntity, Block block) {
        if (blockEntity == null)
            return ItemStackUtil.empty();

        if (PlatformUtil.isModLoaded("modern_industrialization") && blockEntity instanceof WrenchableBlockEntity)
            return getModWrench("modern_industrialization", "wrench");

        if (PlatformUtil.isModLoaded("bew76")) {
            if ((BEWConfig.breakFeature || BEWConfig.rotateFeature) && !BEWConfig.blacklistBlocks.contains(BlockUtil.toCompatID(block).toString()))
                return getModWrench("bew76", "wrench");
        }

        return ItemStackUtil.empty();
    }
}
