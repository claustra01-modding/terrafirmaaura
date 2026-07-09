package net.claustra01.tfaura.client;

import de.ellpeck.naturesaura.Helper;
import java.util.stream.Stream;
import net.claustra01.tfaura.TerraFirmaAura;
import net.claustra01.tfaura.common.block.TFAuraBlocks;
import net.claustra01.tfaura.common.block.TFAuraGoldenLeavesBlock;
import net.claustra01.tfaura.common.block.TFAuraWood;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.core.BlockPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

@EventBusSubscriber(modid = TerraFirmaAura.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class TFAuraClientEvents {
    private static final int ANCIENT_LEAVES_COLOR = 15031191;

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
            TFAuraBlocks.POTTED_ANCIENT_SAPLING
        ).forEach(block -> ItemBlockRenderTypes.setRenderLayer(block.get(), cutout));

        Stream.of(TFAuraBlocks.ANCIENT_LEAVES, TFAuraBlocks.GOLDEN_LEAVES)
            .forEach(block -> ItemBlockRenderTypes.setRenderLayer(block.get(), layer -> Minecraft.useFancyGraphics() ? layer == cutoutMipped : layer == solid));
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> ANCIENT_LEAVES_COLOR, TFAuraBlocks.ANCIENT_LEAVES.get());
        event.register(TFAuraClientEvents::goldenLeavesColor, TFAuraBlocks.GOLDEN_LEAVES.get());
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> ANCIENT_LEAVES_COLOR, TFAuraBlocks.ANCIENT_LEAVES.get());
        event.register((stack, tintIndex) -> TFAuraGoldenLeavesBlock.COLOR, TFAuraBlocks.GOLDEN_LEAVES.get());
    }

    private static int goldenLeavesColor(BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex) {
        if (state == null || level == null || pos == null) {
            return TFAuraGoldenLeavesBlock.COLOR;
        }
        return Helper.blendColors(
            TFAuraGoldenLeavesBlock.COLOR,
            BiomeColors.getAverageFoliageColor(level, pos),
            state.getValue(TFAuraGoldenLeavesBlock.STAGE) / (float) TFAuraGoldenLeavesBlock.HIGHEST_STAGE
        );
    }
}
