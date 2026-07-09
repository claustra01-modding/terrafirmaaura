package net.claustra01.tfaura.common.block;

import de.ellpeck.naturesaura.blocks.BlockAncientLeaves;
import de.ellpeck.naturesaura.blocks.BlockGoldenLeaves;
import de.ellpeck.naturesaura.blocks.ModBlocks;
import net.dries007.tfc.common.TFCTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class TFAuraGoldenLeaves {
    private TFAuraGoldenLeaves() {
    }

    public static boolean convert(Level level, BlockPos pos) {
        BlockState oldState = level.getBlockState(pos);
        if (!isTFCConvertibleLeaf(oldState)) {
            return false;
        }

        if (!level.isClientSide) {
            BlockState newState = ModBlocks.GOLDEN_LEAVES.defaultBlockState()
                .setValue(LeavesBlock.DISTANCE, oldState.hasProperty(LeavesBlock.DISTANCE) ? oldState.getValue(LeavesBlock.DISTANCE) : 1)
                .setValue(LeavesBlock.PERSISTENT, oldState.hasProperty(LeavesBlock.PERSISTENT) && oldState.getValue(LeavesBlock.PERSISTENT));
            if (oldState.hasProperty(LeavesBlock.WATERLOGGED)) {
                newState = newState.setValue(LeavesBlock.WATERLOGGED, oldState.getValue(LeavesBlock.WATERLOGGED));
            }
            level.setBlockAndUpdate(pos, newState);
        }
        return true;
    }

    public static boolean isTFCConvertibleLeaf(BlockState state) {
        Block block = state.getBlock();
        return !(block instanceof TFAuraAncientLeavesBlock)
            && !(block instanceof BlockAncientLeaves)
            && !(block instanceof BlockGoldenLeaves)
            && (state.is(TFCTags.Blocks.SEASONAL_LEAVES)
                || state.is(TFCTags.Blocks.FRUIT_TREE_LEAVES)
                || (state.is(BlockTags.LEAVES) && "tfc".equals(BuiltInRegistries.BLOCK.getKey(block).getNamespace())));
    }

    public static void propagateFromGoldenLeaves(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.is(ModBlocks.GOLDEN_LEAVES) || state.getValue(BlockGoldenLeaves.STAGE) <= 1) {
            return;
        }

        BlockPos target = pos.relative(Direction.getRandom(random));
        if (level.isLoaded(target)) {
            convert(level, target);
        }
    }
}
