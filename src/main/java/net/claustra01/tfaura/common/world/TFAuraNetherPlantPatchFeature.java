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
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class TFAuraNetherPlantPatchFeature extends Feature<NoneFeatureConfiguration> {
    private final Supplier<? extends Block> block;
    private final int tries;
    private final int xzSpread;
    private final int ySpread;

    public TFAuraNetherPlantPatchFeature(Supplier<? extends Block> block, int tries, int xzSpread, int ySpread) {
        super(Codec.unit(FeatureConfiguration.NONE));
        this.block = block;
        this.tries = tries;
        this.xzSpread = xzSpread;
        this.ySpread = ySpread;
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
            int y = randomY(level, origin, random);
            if (tryPlaceNear(level, new BlockPos(x, y, z))) {
                placed = true;
            }
        }
        return placed;
    }

    private int randomY(WorldGenLevel level, BlockPos origin, RandomSource random) {
        int min = level.getMinBuildHeight() + 2;
        int max = level.getMaxBuildHeight() - 2;
        int originY = Math.max(min, Math.min(max, origin.getY()));
        int low = Math.max(min, originY - ySpread);
        int high = Math.min(max, originY + ySpread);
        if (high <= low) {
            return min + random.nextInt(Math.max(1, max - min));
        }
        return low + random.nextInt(high - low + 1);
    }

    private boolean tryPlaceNear(WorldGenLevel level, BlockPos origin) {
        BlockPos.MutableBlockPos cursor = origin.mutable();
        for (int offset = 0; offset <= ySpread; offset++) {
            if (tryPlace(level, cursor.set(origin.getX(), origin.getY() - offset, origin.getZ()))) {
                return true;
            }
            if (offset > 0 && tryPlace(level, cursor.set(origin.getX(), origin.getY() + offset, origin.getZ()))) {
                return true;
            }
        }
        return false;
    }

    private boolean tryPlace(WorldGenLevel level, BlockPos pos) {
        if (level.isOutsideBuildHeight(pos)) {
            return false;
        }
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
