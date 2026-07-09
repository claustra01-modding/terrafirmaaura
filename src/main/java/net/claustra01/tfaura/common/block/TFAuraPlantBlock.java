package net.claustra01.tfaura.common.block;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.plant.TFCBushBlock;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TFAuraPlantBlock extends TFCBushBlock {
    public static final VoxelShape AURA_BLOOM_SHAPE = box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);
    public static final VoxelShape TALL_FLOWER_SHAPE = box(2.0D, 0.0D, 2.0D, 14.0D, 15.0D, 14.0D);
    public static final VoxelShape MUSHROOM_SHAPE = box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D);
    public static final VoxelShape CACTUS_SHAPE = box(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D);
    public static final VoxelShape GRASS_SHAPE = box(2.0D, 0.0D, 2.0D, 14.0D, 9.0D, 14.0D);

    private final TagKey<Block> plantableOn;
    private final VoxelShape shape;

    public TFAuraPlantBlock(ExtendedProperties properties, TagKey<Block> plantableOn, VoxelShape shape) {
        super(properties);
        this.plantableOn = plantableOn;
        this.shape = shape;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return Helpers.isBlock(state, plantableOn);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shape;
    }
}
