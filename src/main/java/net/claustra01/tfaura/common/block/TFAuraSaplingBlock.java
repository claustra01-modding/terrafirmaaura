package net.claustra01.tfaura.common.block;

import java.util.function.Supplier;
import net.claustra01.tfaura.common.blockentity.TFAuraTickCounterBlockEntity;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.wood.TFCSaplingBlock;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;

public class TFAuraSaplingBlock extends TFCSaplingBlock {
    public TFAuraSaplingBlock(TreeGrower tree, ExtendedProperties properties, Supplier<Integer> ticksToGrow) {
        super(tree, properties, ticksToGrow, false);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        TFAuraTickCounterBlockEntity.reset(level, pos);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return Helpers.isBlock(state, TFCTags.Blocks.TREE_GROWS_ON);
    }
}
