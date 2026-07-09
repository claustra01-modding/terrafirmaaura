package net.claustra01.tfaura.common.block;

import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.blocks.BlockAncientLeaves;
import net.dries007.tfc.common.TFCTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class TFAuraGoldenLeavesBlock extends LeavesBlock {
    public static final int HIGHEST_STAGE = 3;
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, HIGHEST_STAGE);
    private static final int PARTICLE_COLOR = 15924992;

    public TFAuraGoldenLeavesBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(STAGE, 0));
    }

    public static boolean convert(Level level, BlockPos pos) {
        BlockState oldState = level.getBlockState(pos);
        if (!isConvertibleLeaf(oldState)) {
            return false;
        }
        if (!level.isClientSide) {
            BlockState newState = TFAuraBlocks.GOLDEN_LEAVES.get().defaultBlockState()
                .setValue(DISTANCE, oldState.hasProperty(DISTANCE) ? oldState.getValue(DISTANCE) : 1)
                .setValue(PERSISTENT, oldState.hasProperty(PERSISTENT) && oldState.getValue(PERSISTENT));
            if (oldState.hasProperty(WATERLOGGED)) {
                newState = newState.setValue(WATERLOGGED, oldState.getValue(WATERLOGGED));
            }
            level.setBlockAndUpdate(pos, newState);
        }
        return true;
    }

    public static boolean isTFCConvertibleLeaf(BlockState state) {
        return isConvertibleLeaf(state)
            && !(state.getBlock() instanceof LeavesBlock)
            && (state.is(TFCTags.Blocks.SEASONAL_LEAVES) || state.is(TFCTags.Blocks.FRUIT_TREE_LEAVES) || state.is(BlockTags.LEAVES));
    }

    private static boolean isConvertibleLeaf(BlockState state) {
        Block block = state.getBlock();
        return !(block instanceof TFAuraAncientLeavesBlock)
            && !(block instanceof TFAuraGoldenLeavesBlock)
            && !(block instanceof BlockAncientLeaves)
            && !(block instanceof de.ellpeck.naturesaura.blocks.BlockGoldenLeaves)
            && (block instanceof LeavesBlock || state.is(BlockTags.LEAVES) || state.is(TFCTags.Blocks.SEASONAL_LEAVES) || state.is(TFCTags.Blocks.FRUIT_TREE_LEAVES));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(STAGE) == HIGHEST_STAGE && random.nextFloat() > 0.75F) {
            NaturesAuraAPI.instance().spawnMagicParticle(
                pos.getX() + random.nextFloat(),
                pos.getY() + random.nextFloat(),
                pos.getZ() + random.nextFloat(),
                0.0D,
                0.0D,
                0.0D,
                PARTICLE_COLOR,
                0.5F + random.nextFloat(),
                50,
                0.0F,
                false,
                true
            );
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        int stage = state.getValue(STAGE);
        if (stage < HIGHEST_STAGE) {
            level.setBlockAndUpdate(pos, state.setValue(STAGE, stage + 1));
        }
        if (stage > 1) {
            BlockPos target = pos.relative(Direction.getRandom(random));
            if (level.isLoaded(target)) {
                convert(level, target);
            }
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(STAGE);
    }
}
