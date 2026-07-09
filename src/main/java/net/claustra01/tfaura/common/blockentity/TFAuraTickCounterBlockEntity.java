package net.claustra01.tfaura.common.blockentity;

import net.dries007.tfc.common.blockentities.TickCounterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TFAuraTickCounterBlockEntity extends TickCounterBlockEntity {
    public TFAuraTickCounterBlockEntity(BlockPos pos, BlockState state) {
        super(TFAuraBlockEntities.TICK_COUNTER.get(), pos, state);
    }

    public static void reset(Level level, BlockPos pos) {
        level.getBlockEntity(pos, TFAuraBlockEntities.TICK_COUNTER.get()).ifPresent(TickCounterBlockEntity::resetCounter);
    }

    public static void addTicks(Level level, BlockPos pos, long ticks) {
        level.getBlockEntity(pos, TFAuraBlockEntities.TICK_COUNTER.get()).ifPresent(counter -> counter.increaseCounter(ticks));
    }
}
