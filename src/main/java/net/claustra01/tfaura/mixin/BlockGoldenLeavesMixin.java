package net.claustra01.tfaura.mixin;

import de.ellpeck.naturesaura.blocks.BlockGoldenLeaves;
import net.claustra01.tfaura.common.block.TFAuraGoldenLeaves;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockGoldenLeaves.class)
public abstract class BlockGoldenLeavesMixin {
    @Inject(method = "randomTick", at = @At("TAIL"))
    private void tfaura$propagateToTFCLeaves(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        TFAuraGoldenLeaves.propagateFromGoldenLeaves(state, level, pos, random);
    }
}
