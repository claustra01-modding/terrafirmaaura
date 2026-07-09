package net.claustra01.tfaura.client;

import java.util.stream.Stream;
import net.claustra01.tfaura.TerraFirmaAura;
import net.claustra01.tfaura.common.block.TFAuraBlocks;
import net.claustra01.tfaura.common.block.TFAuraWood;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = TerraFirmaAura.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class TFAuraClientEvents {
    private TFAuraClientEvents() {
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            for (TFAuraWood wood : TFAuraWood.VALUES) {
                Sheets.addWoodType(wood.getVanillaWoodType());
            }
        });

        RenderType cutout = RenderType.cutout();
        RenderType cutoutMipped = RenderType.cutoutMipped();
        RenderType solid = RenderType.solid();

        Stream.of(
            TFAuraBlocks.AURA_BLOOM,
            TFAuraBlocks.AURA_CACTUS,
            TFAuraBlocks.AURA_MUSHROOM,
            TFAuraBlocks.CRIMSON_AURA_MUSHROOM,
            TFAuraBlocks.WARPED_AURA_MUSHROOM,
            TFAuraBlocks.BRILLIANT_GRASS,
            TFAuraBlocks.ANCIENT_SAPLING,
            TFAuraBlocks.POTTED_ANCIENT_SAPLING,
            TFAuraBlocks.ANCIENT_TWIG
        ).forEach(block -> ItemBlockRenderTypes.setRenderLayer(block.get(), cutout));

        Stream.of(TFAuraBlocks.ANCIENT_LEAVES, TFAuraBlocks.GOLDEN_LEAVES, TFAuraBlocks.FALLEN_ANCIENT_LEAVES)
            .forEach(block -> ItemBlockRenderTypes.setRenderLayer(block.get(), layer -> Minecraft.useFancyGraphics() ? layer == cutoutMipped : layer == solid));
    }
}
