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

import java.util.Set;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = TerraFirmaAura.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class TFAuraClientGameEvents {
    private static final Set<ResourceLocation> DISABLED_ITEMS = Set.of(
        ResourceLocation.fromNamespaceAndPath("naturesaura", "oak_generator"),
        ResourceLocation.fromNamespaceAndPath("naturesaura", "animal_generator"),
        ResourceLocation.fromNamespaceAndPath("naturesaura", "animal_spawner"),
        ResourceLocation.fromNamespaceAndPath("naturesaura", "furnace_heater"),
        ResourceLocation.fromNamespaceAndPath("naturesaura", "blast_furnace_booster"),
        ResourceLocation.fromNamespaceAndPath("naturesaura", "snow_creator"),
        ResourceLocation.fromNamespaceAndPath("naturesaura", "weather_changer")
    );

    private TFAuraClientGameEvents() {
    }

    @SubscribeEvent
    public static void addItemTooltip(ItemTooltipEvent event) {
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(event.getItemStack().getItem());
        if (DISABLED_ITEMS.contains(itemId)) {
            event.getToolTip().add(Component.literal("[Disabled]").withStyle(ChatFormatting.DARK_RED));
        }
    }
}
