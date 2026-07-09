package net.claustra01.tfaura.common.block;

import net.claustra01.tfaura.common.blockentity.TFAuraAuraBloomBlockEntity;
import net.claustra01.tfaura.common.blockentity.TFAuraBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import de.ellpeck.naturesaura.blocks.tiles.ITickableBlockEntity;

public class TFAuraAuraPlantBlock extends TFAuraPlantBlock implements EntityBlock {
    private final boolean hurtsEntities;

    public TFAuraAuraPlantBlock(ExtendedProperties properties, TagKey<Block> plantableOn, VoxelShape shape, boolean hurtsEntities) {
        super(properties, plantableOn, shape);
        this.hurtsEntities = hurtsEntities;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (hurtsEntities) {
            entity.hurt(entity.damageSources().cactus(), 1.0F);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TFAuraAuraBloomBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return ITickableBlockEntity.createTickerHelper(type, TFAuraBlockEntities.AURA_BLOOM.get());
    }
}
