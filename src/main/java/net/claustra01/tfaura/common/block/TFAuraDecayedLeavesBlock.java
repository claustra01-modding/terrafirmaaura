package net.claustra01.tfaura.common.block;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.util.registry.RegistryWood;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class TFAuraDecayedLeavesBlock extends TFAuraLeavesBlock {
    public TFAuraDecayedLeavesBlock(ExtendedProperties properties, RegistryWood wood) {
        super(properties, wood);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
    }
}
