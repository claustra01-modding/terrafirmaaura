package net.claustra01.tfaura.common.world;

import com.mojang.serialization.Codec;
import java.util.function.Supplier;
import net.claustra01.tfaura.common.blockentity.TFAuraAuraBloomBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class TFAuraPlantPatchFeature extends Feature<NoneFeatureConfiguration> {
    private final Supplier<? extends Block> block;
    private final int tries;
    private final int xzSpread;

    public TFAuraPlantPatchFeature(Supplier<? extends Block> block, int tries, int xzSpread) {
        super(Codec.unit(FeatureConfiguration.NONE));
        this.block = block;
        this.tries = tries;
        this.xzSpread = xzSpread;
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        boolean placed = false;

        for (int i = 0; i < tries; i++) {
            int x = origin.getX() + random.nextInt(xzSpread * 2 + 1) - xzSpread;
            int z = origin.getZ() + random.nextInt(xzSpread * 2 + 1) - xzSpread;
            BlockPos pos = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, new BlockPos(x, origin.getY(), z));
            if (tryPlace(level, pos)) {
                placed = true;
            }
        }
        return placed;
    }

    private boolean tryPlace(WorldGenLevel level, BlockPos pos) {
        BlockState state = block.get().defaultBlockState();
        if (!level.isEmptyBlock(pos) || !state.canSurvive(level, pos)) {
            return false;
        }
        level.setBlock(pos, state, Block.UPDATE_CLIENTS);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof TFAuraAuraBloomBlockEntity auraBloom) {
            auraBloom.justGenerated = true;
        }
        return true;
    }
}
