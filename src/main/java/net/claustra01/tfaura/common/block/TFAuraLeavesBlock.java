package net.claustra01.tfaura.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class TFAuraLeavesBlock extends LeavesBlock {
    private static final int DECAY_DISTANCE = 7;

    public TFAuraLeavesBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        level.scheduleTick(pos, this, 1);
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        decayIfUnsupported(state, level, pos, random);
    }

    protected boolean decayIfUnsupported(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int distance = updateDistance(level, pos);
        if (distance >= DECAY_DISTANCE && !state.getValue(PERSISTENT)) {
            level.removeBlock(pos, false);
            return true;
        }
        if (distance != state.getValue(DISTANCE)) {
            level.setBlock(pos, state.setValue(DISTANCE, distance), Block.UPDATE_CLIENTS | Block.UPDATE_NEIGHBORS);
        }
        return false;
    }

    private int updateDistance(LevelAccessor level, BlockPos pos) {
        int distance = DECAY_DISTANCE;
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (Direction direction : Direction.values()) {
            cursor.set(pos).move(direction);
            distance = Math.min(distance, getDistance(level.getBlockState(cursor)) + 1);
            if (distance == 1) {
                break;
            }
        }
        return distance;
    }

    private int getDistance(BlockState state) {
        if (state.is(BlockTags.LOGS)) {
            return 0;
        }
        if (state.is(BlockTags.LEAVES)) {
            IntegerProperty property = distanceProperty(state);
            if (property != null) {
                return Math.min(DECAY_DISTANCE, state.getValue(property));
            }
        }
        return DECAY_DISTANCE;
    }

    private static IntegerProperty distanceProperty(BlockState state) {
        if (state.hasProperty(DISTANCE)) {
            return DISTANCE;
        }
        for (Property<?> property : state.getProperties()) {
            if (property instanceof IntegerProperty integerProperty && "distance".equals(property.getName())) {
                return integerProperty;
            }
        }
        return null;
    }
}
