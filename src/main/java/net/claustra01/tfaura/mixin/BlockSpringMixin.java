package net.claustra01.tfaura.mixin;

import de.ellpeck.naturesaura.blocks.BlockSpring;
import net.dries007.tfc.common.component.TFCComponents;
import net.dries007.tfc.common.component.fluid.FluidComponent;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockSpring.class, remap = false)
public abstract class BlockSpringMixin {
    @Inject(method = "pickupBlock", at = @At("RETURN"), cancellable = true)
    private void tfaura$pickupFreshWater(Player player, LevelAccessor level, BlockPos pos, BlockState state, CallbackInfoReturnable<ItemStack> cir) {
        if (cir.getReturnValue().is(Items.WATER_BUCKET)) {
            ItemStack bucket = new ItemStack(TFCItems.WOODEN_BUCKET.asItem());
            bucket.set(TFCComponents.FLUID.get(), FluidComponent.of(TFCFluids.RIVER_WATER.get(), FluidHelpers.BUCKET_VOLUME));
            cir.setReturnValue(bucket);
        }
    }
}
