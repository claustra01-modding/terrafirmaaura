package net.claustra01.tfaura.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TFAuraFallenLeavesBlock extends Block {
    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
    private static final VoxelShape[] SHAPES = {
        box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
        box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
        box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
        box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
        box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
        box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
        box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
        box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    };

    public TFAuraFallenLeavesBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(LAYERS, 1));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return context.getItemInHand().is(asItem()) && state.getValue(LAYERS) < 8 || super.canBeReplaced(state, context);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        if (state.is(this)) {
            return state.setValue(LAYERS, Math.min(8, state.getValue(LAYERS) + 1));
        }
        return defaultBlockState();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(LAYERS) - 1];
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LAYERS);
    }
}
