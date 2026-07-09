package net.claustra01.tfaura.common.blockentity;

import de.ellpeck.naturesaura.blocks.tiles.BlockEntityImpl;
import de.ellpeck.naturesaura.blocks.tiles.ITickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class TFAuraAuraBloomBlockEntity extends BlockEntityImpl implements ITickableBlockEntity {
    public boolean justGenerated;

    public TFAuraAuraBloomBlockEntity(BlockPos pos, BlockState state) {
        super(TFAuraBlockEntities.AURA_BLOOM.get(), pos, state);
    }

    @Override
    public void tick() {
        if (level == null || level.isClientSide || !justGenerated) {
            return;
        }
        generateAura(150000);
        justGenerated = false;
        setChanged();
    }

    @Override
    public void writeNBT(CompoundTag tag, SaveType type, HolderLookup.Provider registries) {
        super.writeNBT(tag, type, registries);
        if (type == SaveType.TILE) {
            tag.putBoolean("just_generated", justGenerated);
        }
    }

    @Override
    public void readNBT(CompoundTag tag, SaveType type, HolderLookup.Provider registries) {
        super.readNBT(tag, type, registries);
        if (type == SaveType.TILE) {
            justGenerated = tag.getBoolean("just_generated");
        }
    }
}
