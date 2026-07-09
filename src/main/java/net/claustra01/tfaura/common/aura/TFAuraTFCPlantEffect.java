package net.claustra01.tfaura.common.aura;

import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import de.ellpeck.naturesaura.api.aura.chunk.IDrainSpotEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import org.apache.commons.lang3.tuple.Pair;

abstract class TFAuraTFCPlantEffect implements IDrainSpotEffect {
    protected static final ResourceLocation NATURES_AURA_PLANT_BOOST = ResourceLocation.fromNamespaceAndPath(NaturesAuraAPI.MOD_ID, "plant_boost");
    protected static final ResourceLocation NATURES_AURA_GRASS_DIE = ResourceLocation.fromNamespaceAndPath(NaturesAuraAPI.MOD_ID, "grass_die");
    protected static final int DEFAULT_AURA = IAuraChunk.DEFAULT_AURA;
    protected static final int NEUTRAL_AURA_DELTA = 0;
    protected static final int PLANT_BOOST_AURA_DELTA_THRESHOLD = DEFAULT_AURA + DEFAULT_AURA / 2;

    protected int amount;
    protected int dist;
    protected int auraDeltaInArea;

    protected boolean calcPositiveValues(Level level, BlockPos spot, Integer spotAura) {
        if (spotAura <= NEUTRAL_AURA_DELTA) {
            return false;
        }

        // Nature's Aura drain spot effects work with deltas from DEFAULT_AURA.
        // A delta of 0 is neutral/default aura; positive values are surplus.
        Pair<Integer, Integer> aura = IAuraChunk.getAuraAndSpotAmountInArea(level, spot, 30);
        auraDeltaInArea = aura.getLeft();
        if (auraDeltaInArea < PLANT_BOOST_AURA_DELTA_THRESHOLD) {
            return false;
        }

        int spots = Math.max(1, aura.getRight());
        amount = Math.min(160, Mth.ceil(Math.abs(auraDeltaInArea) / 25_000.0F / spots));
        if (amount <= 1) {
            return false;
        }

        dist = Mth.clamp(Math.abs(auraDeltaInArea) / 100_000, 6, 45);
        return true;
    }

    protected boolean calcNegativeValues(Level level, BlockPos spot, Integer spotAura) {
        if (spotAura >= NEUTRAL_AURA_DELTA) {
            return false;
        }

        Pair<Integer, Integer> aura = IAuraChunk.getAuraAndSpotAmountInArea(level, spot, 50);
        auraDeltaInArea = aura.getLeft();
        if (auraDeltaInArea >= NEUTRAL_AURA_DELTA) {
            return false;
        }

        int spots = Math.max(1, aura.getRight());
        amount = Math.min(400, Mth.ceil(Math.abs(auraDeltaInArea) / 75_000.0F / spots));
        if (amount <= 1) {
            return false;
        }

        dist = Mth.clamp(Math.abs(auraDeltaInArea) / 50_000, 6, 85);
        return true;
    }

    protected int randomAttempts(Level level) {
        int half = Math.max(1, amount / 2);
        return half + level.random.nextInt(half);
    }

    protected BlockPos findNearbyTarget(Level level, BlockPos spot, RandomSource random, TargetPredicate predicate) {
        int x = spot.getX() + random.nextInt(dist * 2 + 1) - dist;
        int z = spot.getZ() + random.nextInt(dist * 2 + 1) - dist;
        int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int y = surfaceY + 2; y >= surfaceY - 8; y--) {
            mutable.set(x, y, z);
            if (mutable.distSqr(spot) > dist * dist || !level.isLoaded(mutable)) {
                continue;
            }

            BlockState state = level.getBlockState(mutable);
            if (predicate.test(state, mutable)) {
                return mutable.immutable();
            }
        }

        return null;
    }

    protected void drainAura(Level level, BlockPos target, BlockPos spot, int amount) {
        BlockPos drainSpot = IAuraChunk.getHighestSpot(level, target, 25, spot);
        IAuraChunk.getAuraChunk(level, drainSpot).drainAura(drainSpot, amount);
    }

    protected IDrainSpotEffect.ActiveType activeType(Player player, BlockPos spot, ResourceLocation ownName, ResourceLocation parentName) {
        if (player.distanceToSqr(spot.getX(), spot.getY(), spot.getZ()) > dist * dist) {
            return IDrainSpotEffect.ActiveType.INACTIVE;
        }

        Level level = player.level();
        BlockPos playerPos = player.blockPosition();
        if (NaturesAuraAPI.instance().isEffectPowderActive(level, playerPos, ownName)
            || NaturesAuraAPI.instance().isEffectPowderActive(level, playerPos, parentName)) {
            return IDrainSpotEffect.ActiveType.INHIBITED;
        }

        return IDrainSpotEffect.ActiveType.ACTIVE;
    }

    @FunctionalInterface
    protected interface TargetPredicate {
        boolean test(BlockState state, BlockPos pos);
    }

    @Override
    public boolean appliesHere(LevelChunk chunk, IAuraChunk auraChunk, de.ellpeck.naturesaura.api.aura.type.IAuraType type) {
        return type.isSimilar(NaturesAuraAPI.TYPE_OVERWORLD);
    }
}
