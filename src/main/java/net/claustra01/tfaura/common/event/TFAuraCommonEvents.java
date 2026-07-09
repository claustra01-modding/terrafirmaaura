package net.claustra01.tfaura.common.event;

import de.ellpeck.naturesaura.items.ModItems;
import net.claustra01.tfaura.TerraFirmaAura;
import net.claustra01.tfaura.common.block.TFAuraGoldenLeavesBlock;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;

@EventBusSubscriber(modid = TerraFirmaAura.MOD_ID)
public final class TFAuraCommonEvents {
    private TFAuraCommonEvents() {
    }

    @SubscribeEvent
    public static void onUseItemOnBlock(UseItemOnBlockEvent event) {
        if (event.getUsePhase() != UseItemOnBlockEvent.UsePhase.ITEM_BEFORE_BLOCK) {
            return;
        }

        ItemStack stack = event.getItemStack();
        if (!stack.is(ModItems.GOLD_FIBER) || !TFAuraGoldenLeavesBlock.isTFCConvertibleLeaf(event.getLevel().getBlockState(event.getPos()))) {
            return;
        }

        if (TFAuraGoldenLeavesBlock.convert(event.getLevel(), event.getPos())) {
            Player player = event.getPlayer();
            if (!event.getLevel().isClientSide && (player == null || !player.getAbilities().instabuild)) {
                stack.shrink(1);
            }
            event.cancelWithResult(ItemInteractionResult.SUCCESS);
        }
    }
}
