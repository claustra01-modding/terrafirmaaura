package net.claustra01.tfaura.common.aura;

import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import de.ellpeck.naturesaura.api.aura.chunk.IDrainSpotEffect;
import net.claustra01.tfaura.TerraFirmaAura;
import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blockentities.TickCounterBlockEntity;
import net.dries007.tfc.common.blockentities.TickingPlantBlockEntity;
import net.dries007.tfc.common.blocks.crop.CropBlock;
import net.dries007.tfc.common.blocks.plant.TFCBambooSaplingBlock;
import net.dries007.tfc.common.blocks.plant.fruit.FruitTreeSaplingBlock;
import net.dries007.tfc.common.blocks.soil.IGrassBlock;
import net.dries007.tfc.common.blocks.wood.TFCSaplingBlock;
import net.dries007.tfc.util.calendar.Calendars;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

public class TFAuraTFCPlantDecayEffect extends TFAuraTFCPlantEffect {
    public static final ResourceLocation NAME = TerraFirmaAura.id("tfc_plant_decay");

    @Override
    public IDrainSpotEffect.ActiveType isActiveHere(net.minecraft.world.entity.player.Player player, LevelChunk chunk, IAuraChunk auraChunk, BlockPos spot, Integer spotAura) {
        return calcNegativeValues(player.level(), spot, spotAura)
            ? activeType(player, spot, NAME, NATURES_AURA_GRASS_DIE)
            : IDrainSpotEffect.ActiveType.INACTIVE;
    }

    @Override
    public ItemStack getDisplayIcon() {
        return new ItemStack(Items.DEAD_BUSH);
    }

    @Override
    public void update(Level level, LevelChunk chunk, IAuraChunk auraChunk, BlockPos spot, Integer spotAura, IAuraChunk.DrainSpot drainSpot) {
        if (!calcNegativeValues(level, spot, spotAura)) {
            return;
        }

        RandomSource random = level.random;
        int attempts = randomAttempts(level);
        while (attempts-- >= 0) {
            BlockPos target = findNearbyTarget(level, spot, random, this::isDecayTarget);
            if (target != null) {
                decay(level, target, random);
            }
        }
    }

    @Override
    public ResourceLocation getName() {
        return NAME;
    }

    private boolean isDecayTarget(BlockState state, BlockPos pos) {
        Block block = state.getBlock();
        return block instanceof TFCSaplingBlock
            || block instanceof FruitTreeSaplingBlock
            || block instanceof TFCBambooSaplingBlock
            || block instanceof CropBlock
            || block instanceof IGrassBlock;
    }

    private void decay(Level level, BlockPos pos, RandomSource random) {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof IGrassBlock grass) {
            level.setBlockAndUpdate(pos, grass.getDirt());
            return;
        }

        if (block instanceof CropBlock crop && level.getBlockEntity(pos) instanceof CropBlockEntity cropEntity) {
            long now = Calendars.get(level).getTicks();
            cropEntity.setLastGrowthTick(now);
            cropEntity.setGrowth(Math.max(0.0F, cropEntity.getGrowth() - 0.05F));

            float chance = disappearanceChance();
            if (random.nextFloat() < chance) {
                crop.die(level, pos, state, cropEntity.getGrowth() >= 1.0F);
            }
            return;
        }

        if (block instanceof TFCSaplingBlock) {
            if (level.getBlockEntity(pos) instanceof TickCounterBlockEntity counter) {
                counter.resetCounter();
            }
            if (random.nextFloat() < disappearanceChance()) {
                level.destroyBlock(pos, false);
            }
            return;
        }

        if (block instanceof FruitTreeSaplingBlock) {
            TickingPlantBlockEntity.reset(level, pos);
            if (random.nextFloat() < disappearanceChance()) {
                level.destroyBlock(pos, false);
            }
            return;
        }

        if (block instanceof TFCBambooSaplingBlock && random.nextFloat() < disappearanceChance()) {
            level.destroyBlock(pos, false);
        }
    }

    private float disappearanceChance() {
        return Mth.clamp(Math.abs(auraDeltaInArea) / 2_000_000.0F, 0.12F, 0.85F);
    }
}
