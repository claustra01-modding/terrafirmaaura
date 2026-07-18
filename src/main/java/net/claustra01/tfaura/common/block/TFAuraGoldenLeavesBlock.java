package net.claustra01.tfaura.common.block;

import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.blocks.BlockAncientLeaves;
import de.ellpeck.naturesaura.blocks.BlockGoldenLeaves;
import java.util.Set;
import net.claustra01.tfaura.TerraFirmaAura;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.util.registry.RegistryWood;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class TFAuraGoldenLeavesBlock extends TFAuraLeavesBlock {
    public static final int HIGHEST_STAGE = 3;
    public static final int COLOR = 15924992;
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, HIGHEST_STAGE);
    public static final TagKey<Block> CONVERTIBLE_LEAVES = BlockTags.create(TerraFirmaAura.id("golden_leaves_convertible"));
    private static final Set<String> CONVERTIBLE_NAMESPACES = Set.of("tfc", TerraFirmaAura.MOD_ID, "beneath", "arborfirmacraft", "arbor_firmacraft", "afc");

    public TFAuraGoldenLeavesBlock(ExtendedProperties properties, RegistryWood wood) {
        super(properties, wood);
        registerDefaultState(defaultBlockState().setValue(STAGE, 0));
    }

    public static boolean convert(Level level, BlockPos pos) {
        BlockState oldState = level.getBlockState(pos);
        if (!isConvertibleLeaf(oldState)) {
            return false;
        }

        if (!level.isClientSide) {
            BlockState newState = copyLeafProperties(TFAuraBlocks.GOLDEN_LEAVES.get().defaultBlockState(), oldState);
            level.setBlockAndUpdate(pos, newState);
        }
        return true;
    }

    public static boolean isConvertibleLeaf(BlockState state) {
        Block block = state.getBlock();
        if (block instanceof TFAuraGoldenLeavesBlock || block instanceof BlockGoldenLeaves || block instanceof BlockAncientLeaves) {
            return false;
        }
        if (state.is(CONVERTIBLE_LEAVES) || state.is(TFCTags.Blocks.SEASONAL_LEAVES) || state.is(TFCTags.Blocks.FRUIT_TREE_LEAVES)) {
            return true;
        }

        String namespace = BuiltInRegistries.BLOCK.getKey(block).getNamespace();
        return CONVERTIBLE_NAMESPACES.contains(namespace)
            && (state.is(BlockTags.LEAVES) || state.is(TFCTags.Blocks.SEASONAL_LEAVES) || state.is(TFCTags.Blocks.FRUIT_TREE_LEAVES));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (state.getValue(STAGE) == HIGHEST_STAGE && random.nextFloat() >= 0.75F) {
            NaturesAuraAPI.instance().spawnMagicParticle(
                pos.getX() + random.nextFloat(),
                pos.getY() + random.nextFloat(),
                pos.getZ() + random.nextFloat(),
                0.0D,
                0.0D,
                0.0D,
                COLOR,
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
        if (!level.getBlockState(pos).is(this)) {
            return;
        }
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
