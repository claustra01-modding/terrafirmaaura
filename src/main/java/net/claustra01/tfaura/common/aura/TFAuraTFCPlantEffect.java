package net.claustra01.tfaura.common.aura;

import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import de.ellpeck.naturesaura.api.aura.chunk.IDrainSpotEffect;
import java.util.Set;
import net.claustra01.tfaura.TerraFirmaAura;
import net.claustra01.tfaura.common.block.TFAuraDecayedLeavesBlock;
import net.claustra01.tfaura.common.block.TFAuraGoldenLeavesBlock;
import net.dries007.tfc.common.TFCTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import org.apache.commons.lang3.tuple.Pair;

abstract class TFAuraTFCPlantEffect implements IDrainSpotEffect {
    protected static final ResourceLocation NATURES_AURA_PLANT_BOOST = ResourceLocation.fromNamespaceAndPath(NaturesAuraAPI.MOD_ID, "plant_boost");
    protected static final ResourceLocation NATURES_AURA_GRASS_DIE = ResourceLocation.fromNamespaceAndPath(NaturesAuraAPI.MOD_ID, "grass_die");
    protected static final int DEFAULT_AURA = IAuraChunk.DEFAULT_AURA;
    protected static final int NEUTRAL_AURA_DELTA = 0;
    protected static final int LOW_AURA_DELTA_THRESHOLD = DEFAULT_AURA / 4;
    protected static final int MEDIUM_AURA_DELTA_THRESHOLD = DEFAULT_AURA * 3 / 5;
    protected static final int HIGH_AURA_DELTA_THRESHOLD = DEFAULT_AURA;
    protected static final TagKey<Block> AURA_NATURE_EFFECTS = BlockTags.create(TerraFirmaAura.id("aura_nature_effects"));
    private static final Set<String> PLANT_EFFECT_NAMESPACES = Set.of("tfc", TerraFirmaAura.MOD_ID, "beneath", "arborfirmacraft", "arbor_firmacraft", "afc");

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
        if (auraDeltaInArea < LOW_AURA_DELTA_THRESHOLD) {
            return false;
        }

        int spots = Math.max(1, aura.getRight());
        amount = Math.min(160, Mth.ceil(Math.abs(auraDeltaInArea) / 80_000.0F / spots));
        if (amount <= 0) {
            return false;
        }

        dist = Mth.clamp(Math.abs(auraDeltaInArea) / 100_000, 4, 45);
        return true;
    }

    protected boolean calcNegativeValues(Level level, BlockPos spot, Integer spotAura) {
        if (spotAura >= NEUTRAL_AURA_DELTA) {
            return false;
        }

        Pair<Integer, Integer> aura = IAuraChunk.getAuraAndSpotAmountInArea(level, spot, 50);
        auraDeltaInArea = aura.getLeft();
        if (Math.abs(auraDeltaInArea) < LOW_AURA_DELTA_THRESHOLD) {
            return false;
        }

        int spots = Math.max(1, aura.getRight());
        amount = Math.min(400, Mth.ceil(Math.abs(auraDeltaInArea) / 90_000.0F / spots));
        if (amount <= 0) {
            return false;
        }

        dist = Mth.clamp(Math.abs(auraDeltaInArea) / 70_000, 4, 85);
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

    protected int effectTier() {
        int delta = Math.abs(auraDeltaInArea);
        if (delta >= HIGH_AURA_DELTA_THRESHOLD) {
            return 3;
        }
        if (delta >= MEDIUM_AURA_DELTA_THRESHOLD) {
            return 2;
        }
        if (delta >= LOW_AURA_DELTA_THRESHOLD) {
            return 1;
        }
        return 0;
    }

    protected float effectIntensity() {
        return Mth.clamp(Math.abs(auraDeltaInArea) / (float) DEFAULT_AURA, 0.25F, 2.0F);
    }

    protected int stagedRandomTickAttempts() {
        return switch (effectTier()) {
            case 1 -> 1;
            case 2 -> 3;
            case 3 -> 6 + Mth.floor(effectIntensity() * 2.0F);
            default -> 0;
        };
    }

    protected boolean isSupportedNatureBlock(BlockState state) {
        Block block = state.getBlock();
        if (block instanceof TFAuraGoldenLeavesBlock || block instanceof TFAuraDecayedLeavesBlock) {
            return false;
        }

        String namespace = BuiltInRegistries.BLOCK.getKey(block).getNamespace();
        return PLANT_EFFECT_NAMESPACES.contains(namespace)
            && (state.is(AURA_NATURE_EFFECTS)
                || state.is(BlockTags.LEAVES)
                || state.is(BlockTags.SAPLINGS)
                || state.is(BlockTags.FLOWERS)
                || state.is(BlockTags.SMALL_FLOWERS)
                || state.is(TFCTags.Blocks.NATURAL_REGROWING_PLANTS)
                || state.is(TFCTags.Blocks.LIVING_SPREADING_BUSHES)
                || state.is(TFCTags.Blocks.SPREADING_BUSHES)
                || state.is(TFCTags.Blocks.THORNY_BUSHES)
                || state.is(TFCTags.Blocks.FRUIT_TREE_SAPLING)
                || state.is(TFCTags.Blocks.BAMBOO_SAPLING)
                || state.is(TFCTags.Blocks.GRASS)
                || state.is(TFCTags.Blocks.SEASONAL_LEAVES)
                || state.is(TFCTags.Blocks.FRUIT_TREE_LEAVES));
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
