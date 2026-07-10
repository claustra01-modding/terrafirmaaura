package net.claustra01.tfaura.common.blockentity;

import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.aura.container.NaturalAuraContainer;
import de.ellpeck.naturesaura.blocks.tiles.BlockEntityImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class TFAuraAncientLeavesBlockEntity extends BlockEntityImpl {
    private static final int AURA_COLOR = 13522057;

    public final NaturalAuraContainer container = new NaturalAuraContainer(NaturesAuraAPI.TYPE_OVERWORLD, 2000, 500) {
        @Override
        public int getAuraColor() {
            return AURA_COLOR;
        }

        @Override
        public int drainAura(int amount, boolean simulate) {
            int drained = super.drainAura(amount, simulate);
            if (drained > 0 && !simulate) {
                TFAuraAncientLeavesBlockEntity.this.sendToClients();
            }
            return drained;
        }
    };

    public TFAuraAncientLeavesBlockEntity(BlockPos pos, BlockState state) {
        super(TFAuraBlockEntities.ANCIENT_LEAVES.get(), pos, state);
    }

    @Override
    public void writeNBT(CompoundTag tag, SaveType type, HolderLookup.Provider registries) {
        super.writeNBT(tag, type, registries);
        if (type != SaveType.BLOCK) {
            container.writeNBT(tag);
        }
    }

    @Override
    public void readNBT(CompoundTag tag, SaveType type, HolderLookup.Provider registries) {
        super.readNBT(tag, type, registries);
        if (type != SaveType.BLOCK) {
            container.readNBT(tag);
        }
    }
}
