package net.claustra01.tfaura.client;

import net.claustra01.tfaura.TerraFirmaAura;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = TerraFirmaAura.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class TFAuraClientGameEvents {
    private static final ResourceLocation CANOPY_DIMINISHER = ResourceLocation.fromNamespaceAndPath("naturesaura", "oak_generator");
    private static final ResourceLocation DISENTANGLER_OF_MORTALS = ResourceLocation.fromNamespaceAndPath("naturesaura", "animal_generator");

    private TFAuraClientGameEvents() {
    }

    @SubscribeEvent
    public static void addItemTooltip(ItemTooltipEvent event) {
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(event.getItemStack().getItem());
        if (CANOPY_DIMINISHER.equals(itemId) || DISENTANGLER_OF_MORTALS.equals(itemId)) {
            event.getToolTip().add(Component.literal("[Disabled]").withStyle(ChatFormatting.DARK_RED));
        }
    }
}
