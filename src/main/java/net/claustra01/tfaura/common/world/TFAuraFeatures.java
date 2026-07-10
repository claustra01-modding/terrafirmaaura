package net.claustra01.tfaura.common.world;

import net.claustra01.tfaura.TerraFirmaAura;
import net.claustra01.tfaura.common.block.TFAuraBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TFAuraFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, TerraFirmaAura.MOD_ID);

    public static final DeferredHolder<Feature<?>, TFAuraPlantPatchFeature> AURA_BLOOM = FEATURES.register("aura_bloom",
        () -> new TFAuraPlantPatchFeature(TFAuraBlocks.AURA_BLOOM, 4, 6));
    public static final DeferredHolder<Feature<?>, TFAuraPlantPatchFeature> AURA_CACTUS = FEATURES.register("aura_cactus",
        () -> new TFAuraPlantPatchFeature(TFAuraBlocks.AURA_CACTUS, 3, 6));
    public static final DeferredHolder<Feature<?>, TFAuraPlantPatchFeature> AURA_MUSHROOM = FEATURES.register("aura_mushroom",
        () -> new TFAuraPlantPatchFeature(TFAuraBlocks.AURA_MUSHROOM, 5, 8));
    public static final DeferredHolder<Feature<?>, TFAuraNetherPlantPatchFeature> CRIMSON_AURA_MUSHROOM = FEATURES.register("crimson_aura_mushroom",
        () -> new TFAuraNetherPlantPatchFeature(TFAuraBlocks.CRIMSON_AURA_MUSHROOM, 10));
    public static final DeferredHolder<Feature<?>, TFAuraNetherPlantPatchFeature> WARPED_AURA_MUSHROOM = FEATURES.register("warped_aura_mushroom",
        () -> new TFAuraNetherPlantPatchFeature(TFAuraBlocks.WARPED_AURA_MUSHROOM, 10));
    public static final DeferredHolder<Feature<?>, TFAuraPlantPatchFeature> BRILLIANT_GRASS = FEATURES.register("brilliant_grass",
        () -> new TFAuraPlantPatchFeature(TFAuraBlocks.BRILLIANT_GRASS, 6, 8));

    private TFAuraFeatures() {
    }
}
