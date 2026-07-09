package net.claustra01.tfaura.common.aura;

import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import de.ellpeck.naturesaura.api.aura.chunk.IDrainSpotEffect;
import net.claustra01.tfaura.TerraFirmaAura;
import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blockentities.TickCounterBlockEntity;
import net.dries007.tfc.common.blockentities.TickingPlantBlockEntity;
import net.dries007.tfc.common.blocks.crop.CropBlock;
import net.dries007.tfc.common.blocks.crop.DefaultCropBlock;
import net.dries007.tfc.common.blocks.plant.TFCBambooSaplingBlock;
import net.dries007.tfc.common.blocks.plant.fruit.FruitTreeSaplingBlock;
import net.dries007.tfc.common.blocks.soil.IDirtBlock;
import net.dries007.tfc.common.blocks.soil.IGrassBlock;
import net.dries007.tfc.common.blocks.wood.TFCSaplingBlock;
import net.dries007.tfc.util.calendar.Calendars;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

public class TFAuraTFCPlantBoostEffect extends TFAuraTFCPlantEffect {
    public static final ResourceLocation NAME = TerraFirmaAura.id("tfc_plant_boost");

    @Override
    public IDrainSpotEffect.ActiveType isActiveHere(net.minecraft.world.entity.player.Player player, LevelChunk chunk, IAuraChunk auraChunk, BlockPos spot, Integer spotAura) {
        return calcPositiveValues(player.level(), spot, spotAura)
            ? activeType(player, spot, NAME, NATURES_AURA_PLANT_BOOST)
            : IDrainSpotEffect.ActiveType.INACTIVE;
    }

    @Override
    public ItemStack getDisplayIcon() {
        return new ItemStack(Items.WHEAT_SEEDS);
    }

    @Override
    public void update(Level level, LevelChunk chunk, IAuraChunk auraChunk, BlockPos spot, Integer spotAura, IAuraChunk.DrainSpot drainSpot) {
        if (!(level instanceof ServerLevel serverLevel) || !calcPositiveValues(level, spot, spotAura)) {
            return;
        }

        RandomSource random = level.random;
        int attempts = randomAttempts(level);
        while (attempts-- >= 0) {
            BlockPos target = findNearbyTarget(level, spot, random, (state, pos) -> isBoostTarget(level, state, pos));
            if (target != null && boost(serverLevel, target, spot, random)) {
                drainAura(level, target, spot, 4_500);
            }
        }
    }

    @Override
    public ResourceLocation getName() {
        return NAME;
    }

    private boolean isBoostTarget(Level level, BlockState state, BlockPos pos) {
        Block block = state.getBlock();
        if (block instanceof TFCSaplingBlock || block instanceof FruitTreeSaplingBlock || block instanceof TFCBambooSaplingBlock) {
            return true;
        }
        if (block instanceof CropBlock && level.getBlockEntity(pos) instanceof CropBlockEntity) {
            return true;
        }
        if (block instanceof IGrassBlock) {
            return true;
        }
        return block instanceof IDirtBlock && canBecomeGrass(level, state, pos);
    }

    private boolean boost(ServerLevel level, BlockPos pos, BlockPos spot, RandomSource random) {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof TFCSaplingBlock) {
            TickCounterBlockEntity.addTicks(level, pos, saplingBoostTicks());
            return runRandomTicks(level, pos, state, random, saplingRandomTickAttempts());
        }

        if (block instanceof FruitTreeSaplingBlock) {
            TickingPlantBlockEntity.addTicks(level, pos, saplingBoostTicks());
            return runRandomTicks(level, pos, state, random, 2);
        }

        if (block instanceof TFCBambooSaplingBlock) {
            return runRandomTicks(level, pos, state, random, 3);
        }

        if (block instanceof CropBlock crop && level.getBlockEntity(pos) instanceof CropBlockEntity cropEntity) {
            return boostCrop(level, pos, state, crop, cropEntity);
        }

        if (block instanceof IGrassBlock) {
            return runRandomTicks(level, pos, state, random, 6);
        }

        if (block instanceof IDirtBlock dirt && canBecomeGrass(level, state, pos)) {
            return level.setBlockAndUpdate(pos, dirt.getGrass());
        }

        return false;
    }

    private boolean boostCrop(ServerLevel level, BlockPos pos, BlockState state, CropBlock crop, CropBlockEntity cropEntity) {
        float beforeGrowth = cropEntity.getGrowth();
        BlockState beforeState = level.getBlockState(pos);
        long now = Calendars.get(level).getTicks();
        cropEntity.setLastGrowthTick(Math.min(cropEntity.getLastGrowthTick(), now - cropBoostTicks()));
        crop.growthTick(level, pos, state, cropEntity);

        BlockState afterState = level.getBlockState(pos);
        if (cropEntity.getGrowth() > beforeGrowth || !afterState.equals(beforeState)) {
            return true;
        }

        if (crop instanceof DefaultCropBlock && crop.getGrowthLimit(level, pos, state) > 0.0F && cropEntity.getGrowth() < 1.0F) {
            float boostedGrowth = Math.min(1.0F, cropEntity.getGrowth() + directCropBoost());
            cropEntity.setGrowth(boostedGrowth);
            cropEntity.setYield(Math.max(cropEntity.getYield(), boostedGrowth * 0.5F));
            int age = boostedGrowth >= 1.0F ? crop.getMaxAge() : Mth.floor(boostedGrowth * crop.getMaxAge());
            level.setBlockAndUpdate(pos, state.setValue(crop.getAgeProperty(), Mth.clamp(age, 0, crop.getMaxAge())));
            return true;
        }

        return false;
    }

    private boolean runRandomTicks(ServerLevel level, BlockPos pos, BlockState state, RandomSource random, int attempts) {
        BlockState before = state;
        for (int i = 0; i < attempts && level.getBlockState(pos).getBlock() == before.getBlock(); i++) {
            level.getBlockState(pos).randomTick(level, pos, random);
        }
        return !level.getBlockState(pos).equals(before);
    }

    private boolean canBecomeGrass(Level level, BlockState state, BlockPos pos) {
        if (!(state.getBlock() instanceof IDirtBlock dirt)) {
            return false;
        }

        BlockState grassState = dirt.getGrass();
        if (!(grassState.getBlock() instanceof IGrassBlock grass) || !grass.canPropagate(grassState, level, pos)) {
            return false;
        }

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (level.getBlockState(pos.relative(direction)).getBlock() instanceof IGrassBlock) {
                return true;
            }
        }

        return level.getBlockState(pos.above()).getBlock() instanceof IGrassBlock;
    }

    private long saplingBoostTicks() {
        return Mth.clamp(Math.abs(auraDeltaInArea) / 12L, 48_000L, 240_000L);
    }

    private long cropBoostTicks() {
        return Mth.clamp(Math.abs(auraDeltaInArea) / 8L, 48_000L, 336_000L);
    }

    private int saplingRandomTickAttempts() {
        return Mth.clamp((int) (saplingBoostTicks() / 24_000L), 3, 12);
    }

    private float directCropBoost() {
        return Mth.clamp(Math.abs(auraDeltaInArea) / 4_000_000.0F, 0.08F, 0.35F);
    }
}
