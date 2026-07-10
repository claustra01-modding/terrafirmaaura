package net.claustra01.tfaura.common.world;

import com.mojang.serialization.Codec;
import java.util.function.Supplier;
import net.claustra01.tfaura.common.blockentity.TFAuraAuraBloomBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
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
    private static final int MIN_TRIES = 3;
    private static final int MAX_TRIES = 8;
    private static final int XZ_SPREAD = 5;
    private static final int Y_SPREAD = 64;
    private static final int NETHER_MAX_INITIAL_Y = 128;

    private final Supplier<? extends Block> block;
    private final int chance;

    public TFAuraNetherPlantPatchFeature(Supplier<? extends Block> block, int chance) {
        super(Codec.unit(FeatureConfiguration.NONE));
        this.block = block;
        this.chance = chance;
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        if (random.nextInt(chance) != 0) {
            return false;
        }

        int baseX = origin.getX() + random.nextInt(16);
        int baseZ = origin.getZ() + random.nextInt(16);
        int tries = Mth.nextInt(random, MIN_TRIES, MAX_TRIES);
        boolean placed = false;

        for (int i = 0; i < tries; i++) {
            int x = baseX + Mth.nextInt(random, -XZ_SPREAD, XZ_SPREAD);
            int z = baseZ + Mth.nextInt(random, -XZ_SPREAD, XZ_SPREAD);
            int y = Mth.nextInt(random, 0, NETHER_MAX_INITIAL_Y);
            if (tryPlaceNear(level, new BlockPos(x, y, z))) {
                placed = true;
            }
        }
        return placed;
    }

    private boolean tryPlaceNear(WorldGenLevel level, BlockPos origin) {
        BlockPos.MutableBlockPos cursor = origin.mutable();
        for (int offset = 0; offset < Y_SPREAD; offset++) {
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
        if (!state.canSurvive(level, pos)) {
            return false;
        }
        level.setBlock(pos, state, Block.UPDATE_CLIENTS | Block.UPDATE_NEIGHBORS);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof TFAuraAuraBloomBlockEntity auraBloom) {
            auraBloom.justGenerated = true;
        }
        return true;
    }
}
