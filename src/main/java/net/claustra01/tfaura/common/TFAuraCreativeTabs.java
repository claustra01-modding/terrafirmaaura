package net.claustra01.tfaura.common;

import net.claustra01.tfaura.TerraFirmaAura;
import net.claustra01.tfaura.common.block.TFAuraBlocks;
import net.claustra01.tfaura.common.item.TFAuraItems;
import net.claustra01.tfaura.common.item.TFAuraItems.TFAuraMoreItemType;
import net.claustra01.tfaura.common.item.TFAuraMetal;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TFAuraCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TerraFirmaAura.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = TABS.register("main", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.tfaura"))
        .withTabsBefore(CreativeModeTabs.NATURAL_BLOCKS)
        .icon(() -> new ItemStack(TFAuraBlocks.AURA_BLOOM.get()))
        .displayItems((parameters, output) -> {
            output.accept(TFAuraBlocks.AURA_BLOOM.get());
            output.accept(TFAuraBlocks.AURA_CACTUS.get());
            output.accept(TFAuraBlocks.AURA_MUSHROOM.get());
            output.accept(TFAuraBlocks.CRIMSON_AURA_MUSHROOM.get());
            output.accept(TFAuraBlocks.WARPED_AURA_MUSHROOM.get());
            output.accept(TFAuraBlocks.BRILLIANT_GRASS.get());
            output.accept(TFAuraBlocks.ANCIENT_SAPLING.get());
            output.accept(TFAuraBlocks.ANCIENT_LOG.get());
            output.accept(TFAuraBlocks.ANCIENT_WOOD.get());
            output.accept(TFAuraBlocks.ANCIENT_PLANKS.get());
            output.accept(TFAuraBlocks.ANCIENT_STAIRS.get());
            output.accept(TFAuraBlocks.ANCIENT_SLAB.get());
            output.accept(TFAuraBlocks.ANCIENT_FENCE.get());
            output.accept(TFAuraBlocks.ANCIENT_FENCE_GATE.get());
            output.accept(TFAuraBlocks.ANCIENT_LEAVES.get());
            output.accept(TFAuraBlocks.GOLDEN_LEAVES.get());
            output.accept(TFAuraBlocks.FALLEN_ANCIENT_LEAVES.get());
            output.accept(TFAuraBlocks.ANCIENT_TWIG.get());
            for (TFAuraMetal metal : TFAuraMetal.VALUES) {
                output.accept(TFAuraItems.METAL_INGOTS.get(metal).get());
                output.accept(TFAuraItems.METAL_DOUBLE_INGOTS.get(metal).get());
                output.accept(TFAuraItems.METAL_SHEETS.get(metal).get());
                output.accept(TFAuraItems.METAL_DOUBLE_SHEETS.get(metal).get());
                output.accept(TFAuraItems.METAL_RODS.get(metal).get());

                if (TFAuraItems.MORE_METAL_ITEMS.containsKey(metal)) {
                    for (TFAuraMoreItemType type : TFAuraMoreItemType.VALUES) {
                        output.accept(TFAuraItems.MORE_METAL_ITEMS.get(metal).get(type).get());
                    }
                }
            }
        })
        .build());

    private TFAuraCreativeTabs() {
    }
}
