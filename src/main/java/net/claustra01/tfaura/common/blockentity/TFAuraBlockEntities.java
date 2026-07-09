package net.claustra01.tfaura.common.blockentity;

import net.claustra01.tfaura.TerraFirmaAura;
import net.claustra01.tfaura.common.block.TFAuraBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TFAuraBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TerraFirmaAura.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TFAuraAuraBloomBlockEntity>> AURA_BLOOM = BLOCK_ENTITIES.register("aura_bloom",
        () -> BlockEntityType.Builder.of(TFAuraAuraBloomBlockEntity::new,
            TFAuraBlocks.AURA_BLOOM.get(),
            TFAuraBlocks.AURA_CACTUS.get(),
            TFAuraBlocks.AURA_MUSHROOM.get(),
            TFAuraBlocks.CRIMSON_AURA_MUSHROOM.get(),
            TFAuraBlocks.WARPED_AURA_MUSHROOM.get()
        ).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TFAuraAncientLeavesBlockEntity>> ANCIENT_LEAVES = BLOCK_ENTITIES.register("ancient_leaves",
        () -> BlockEntityType.Builder.of(TFAuraAncientLeavesBlockEntity::new, TFAuraBlocks.ANCIENT_LEAVES.get()).build(null));

    private TFAuraBlockEntities() {
    }
}
