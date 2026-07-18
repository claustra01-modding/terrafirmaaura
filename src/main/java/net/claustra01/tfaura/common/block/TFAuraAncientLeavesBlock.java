package net.claustra01.tfaura.common.block;

import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.aura.container.IAuraContainer;
import net.claustra01.tfaura.common.blockentity.TFAuraAncientLeavesBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.util.registry.RegistryWood;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TFAuraAncientLeavesBlock extends TFAuraLeavesBlock implements EntityBlock {
    private static final int PARTICLE_COLOR = 13387648;

    public TFAuraAncientLeavesBlock(ExtendedProperties properties, RegistryWood wood) {
        super(properties, wood);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TFAuraAncientLeavesBlockEntity(pos, state);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (random.nextFloat() <= 0.95F || level.getBlockState(pos.below()).isCollisionShapeFullBlock(level, pos)) {
            return;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof TFAuraAncientLeavesBlockEntity)) {
            return;
        }

        IAuraContainer container = level.getCapability(
            NaturesAuraAPI.AURA_CONTAINER_BLOCK_CAPABILITY,
            blockEntity.getBlockPos(),
            blockEntity.getBlockState(),
            blockEntity,
            null
        );
        if (container != null && container.getStoredAura() > 0) {
            NaturesAuraAPI.instance().spawnMagicParticle(
                pos.getX() + random.nextDouble(),
                pos.getY(),
                pos.getZ() + random.nextDouble(),
                0.0D,
                0.0D,
                0.0D,
                PARTICLE_COLOR,
                random.nextFloat() * 2.0F + 0.5F,
                random.nextInt(50) + 75,
                random.nextFloat() * 0.02F + 0.002F,
                true,
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
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof TFAuraAncientLeavesBlockEntity ancientLeaves)) {
            return;
        }
        IAuraContainer container = level.getCapability(
            NaturesAuraAPI.AURA_CONTAINER_BLOCK_CAPABILITY,
            ancientLeaves.getBlockPos(),
            ancientLeaves.getBlockState(),
            ancientLeaves,
            null
        );
        if (container != null && container.getStoredAura() <= 0) {
            level.setBlockAndUpdate(pos, copyLeafProperties(TFAuraBlocks.DECAYED_LEAVES.get().defaultBlockState(), state));
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }
}
