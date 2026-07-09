package net.claustra01.tfaura.common.block;

import java.util.function.Supplier;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.wood.TFCSaplingBlock;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;

public class TFAuraSaplingBlock extends TFCSaplingBlock {
    public TFAuraSaplingBlock(TreeGrower tree, ExtendedProperties properties, Supplier<Integer> ticksToGrow) {
        super(tree, properties, ticksToGrow, false);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return Helpers.isBlock(state, TFCTags.Blocks.TREE_GROWS_ON);
    }
}
