package net.claustra01.tfaura.mixin;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "de.ellpeck.naturesaura.blocks.tiles.BlockEntitySpring$InfiniteTank", remap = false)
public abstract class BlockEntitySpringInfiniteTankMixin {
    @Inject(method = "getFluid()Lnet/neoforged/neoforge/fluids/FluidStack;", at = @At("HEAD"), cancellable = true)
    private void tfaura$getFluid(CallbackInfoReturnable<FluidStack> cir) {
        cir.setReturnValue(tfaura$freshWater(1000));
    }

    @Inject(method = "isFluidValid(Lnet/neoforged/neoforge/fluids/FluidStack;)Z", at = @At("HEAD"), cancellable = true)
    private void tfaura$isFluidValid(FluidStack stack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(stack != null && !stack.isEmpty() && stack.getFluid().defaultFluidState().is(TFCTags.Fluids.ANY_FRESH_WATER));
    }

    @Inject(
        method = "drain(ILnet/neoforged/neoforge/fluids/capability/IFluidHandler$FluidAction;)Lnet/neoforged/neoforge/fluids/FluidStack;",
        at = @At("RETURN"),
        cancellable = true
    )
    private void tfaura$drainFreshWater(int maxDrain, IFluidHandler.FluidAction action, CallbackInfoReturnable<FluidStack> cir) {
        FluidStack drained = cir.getReturnValue();
        if (!drained.isEmpty()) {
            cir.setReturnValue(tfaura$freshWater(drained.getAmount()));
        }
    }

    private static FluidStack tfaura$freshWater(int amount) {
        return new FluidStack(TFCFluids.RIVER_WATER.get(), amount);
    }
}
