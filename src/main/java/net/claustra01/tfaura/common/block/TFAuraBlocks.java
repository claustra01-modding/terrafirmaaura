package net.claustra01.tfaura.common.block;

import java.util.function.Supplier;
import net.claustra01.tfaura.TerraFirmaAura;
import net.claustra01.tfaura.common.blockentity.TFAuraBlockEntities;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.wood.LogBlock;
import net.dries007.tfc.common.blocks.wood.TFCFenceBlock;
import net.dries007.tfc.common.blocks.wood.TFCFenceGateBlock;
import net.dries007.tfc.common.blocks.wood.TFCSlabBlock;
import net.dries007.tfc.common.blocks.wood.TFCStairBlock;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TFAuraBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TerraFirmaAura.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TerraFirmaAura.MOD_ID);

    public static final DeferredBlock<TFAuraPlantBlock> AURA_BLOOM = register("plant/aura_bloom",
        () -> new TFAuraAuraPlantBlock(plantProperties(MapColor.PLANT).lightLevel(state -> 7), TFCTags.Blocks.BUSH_PLANTABLE_ON, TFAuraPlantBlock.AURA_BLOOM_SHAPE, false));
    public static final DeferredBlock<TFAuraPlantBlock> AURA_CACTUS = register("plant/aura_cactus",
        () -> new TFAuraAuraPlantBlock(plantProperties(MapColor.PLANT), TFCTags.Blocks.DRY_PLANT_PLANTABLE_ON, TFAuraPlantBlock.CACTUS_SHAPE, true));
    public static final DeferredBlock<TFAuraPlantBlock> AURA_MUSHROOM = register("plant/aura_mushroom",
        () -> new TFAuraAuraPlantBlock(plantProperties(MapColor.PLANT).lightLevel(state -> 3), TFCTags.Blocks.BUSH_PLANTABLE_ON, TFAuraPlantBlock.MUSHROOM_SHAPE, false));
    public static final DeferredBlock<TFAuraPlantBlock> CRIMSON_AURA_MUSHROOM = register("plant/crimson_aura_mushroom",
        () -> new TFAuraAuraPlantBlock(plantProperties(MapColor.CRIMSON_STEM).lightLevel(state -> 3), TFCTags.Blocks.BUSH_PLANTABLE_ON, TFAuraPlantBlock.MUSHROOM_SHAPE, false));
    public static final DeferredBlock<TFAuraPlantBlock> WARPED_AURA_MUSHROOM = register("plant/warped_aura_mushroom",
        () -> new TFAuraAuraPlantBlock(plantProperties(MapColor.WARPED_STEM).lightLevel(state -> 3), TFCTags.Blocks.BUSH_PLANTABLE_ON, TFAuraPlantBlock.MUSHROOM_SHAPE, false));
    public static final DeferredBlock<TFAuraPlantBlock> BRILLIANT_GRASS = registerNoItem("plant/brilliant_grass",
        () -> new TFAuraPlantBlock(plantProperties(MapColor.GOLD).lightLevel(state -> 5), TFCTags.Blocks.GRASS_PLANTABLE_ON, TFAuraPlantBlock.GRASS_SHAPE));

    public static final DeferredBlock<Block> STRIPPED_ANCIENT_LOG = register("wood/stripped_log/ancient",
        Wood.BlockType.STRIPPED_LOG.create(TFAuraWood.ANCIENT));
    public static final DeferredBlock<Block> STRIPPED_ANCIENT_WOOD = register("wood/stripped_wood/ancient",
        Wood.BlockType.STRIPPED_WOOD.create(TFAuraWood.ANCIENT));
    public static final DeferredBlock<LogBlock> ANCIENT_LOG = register("wood/log/ancient",
        () -> new LogBlock(logProperties(TFAuraWood.ANCIENT), () -> STRIPPED_ANCIENT_LOG.get()));
    public static final DeferredBlock<LogBlock> ANCIENT_WOOD = register("wood/wood/ancient",
        () -> new LogBlock(logProperties(TFAuraWood.ANCIENT), () -> STRIPPED_ANCIENT_WOOD.get()));
    public static final DeferredBlock<Block> ANCIENT_PLANKS = register("wood/planks/ancient",
        () -> new Block(woodProperties(TFAuraWood.ANCIENT).strength(2.0F, 3.0F).properties()));
    public static final DeferredBlock<TFCStairBlock> ANCIENT_STAIRS = register("wood/stairs/ancient",
        () -> new TFCStairBlock(() -> ANCIENT_PLANKS.get().defaultBlockState(), woodProperties(TFAuraWood.ANCIENT).strength(2.0F, 3.0F).flammableLikePlanks()));
    public static final DeferredBlock<TFCSlabBlock> ANCIENT_SLAB = register("wood/slab/ancient",
        () -> new TFCSlabBlock(woodProperties(TFAuraWood.ANCIENT).strength(2.0F, 3.0F).flammableLikePlanks()));
    public static final DeferredBlock<TFCFenceBlock> ANCIENT_FENCE = register("wood/fence/ancient",
        () -> new TFCFenceBlock(woodProperties(TFAuraWood.ANCIENT).strength(2.0F, 3.0F).flammableLikePlanks()));
    public static final DeferredBlock<Block> ANCIENT_LOG_FENCE = register("wood/log_fence/ancient",
        Wood.BlockType.LOG_FENCE.create(TFAuraWood.ANCIENT));
    public static final DeferredBlock<TFCFenceGateBlock> ANCIENT_FENCE_GATE = register("wood/fence_gate/ancient",
        () -> new TFCFenceGateBlock(woodProperties(TFAuraWood.ANCIENT).strength(2.0F, 3.0F).flammableLikePlanks()));

    public static final DeferredBlock<TFAuraAncientLeavesBlock> ANCIENT_LEAVES = register("wood/leaves/ancient",
        () -> new TFAuraAncientLeavesBlock(leavesProperties().properties()));
    public static final DeferredBlock<TFAuraGoldenLeavesBlock> GOLDEN_LEAVES = register("wood/leaves/golden",
        () -> new TFAuraGoldenLeavesBlock(leavesProperties().mapColor(MapColor.GOLD).strength(0.2F).properties()));
    public static final DeferredBlock<TFAuraDecayedLeavesBlock> DECAYED_LEAVES = register("wood/leaves/decayed",
        () -> new TFAuraDecayedLeavesBlock(leavesProperties().mapColor(MapColor.COLOR_GRAY).strength(0.2F).properties()));

    public static final DeferredBlock<TFAuraSaplingBlock> ANCIENT_SAPLING = register("wood/sapling/ancient",
        () -> new TFAuraSaplingBlock(TFAuraWood.ANCIENT.tree(), ExtendedProperties.of(MapColor.PLANT).noCollission().randomTicks().strength(0.0F).sound(SoundType.GRASS).flammableLikeLeaves().blockEntity(TFAuraBlockEntities.TICK_COUNTER), TFAuraWood.ANCIENT.ticksToGrow()));
    public static final DeferredBlock<FlowerPotBlock> POTTED_ANCIENT_SAPLING = registerNoItem("wood/potted_sapling/ancient",
        () -> new FlowerPotBlock(ANCIENT_SAPLING.get(), ExtendedProperties.of(Blocks.FLOWER_POT).noOcclusion().properties()));

    private TFAuraBlocks() {
    }

    private static ExtendedProperties plantProperties(MapColor color) {
        return ExtendedProperties.of(color)
            .sound(SoundType.GRASS)
            .instabreak()
            .speedFactor(0.8F)
            .noCollission()
            .noOcclusion();
    }

    private static ExtendedProperties woodProperties(TFAuraWood wood) {
        return ExtendedProperties.of(wood.woodColor())
            .sound(SoundType.WOOD)
            .instrument(NoteBlockInstrument.BASS);
    }

    private static ExtendedProperties logProperties(TFAuraWood wood) {
        return woodProperties(wood)
            .strength(8.0F)
            .requiresCorrectToolForDrops()
            .flammableLikeLogs();
    }

    private static ExtendedProperties leavesProperties() {
        return ExtendedProperties.of(MapColor.PLANT)
            .strength(0.5F)
            .sound(SoundType.GRASS)
            .defaultInstrument()
            .randomTicks()
            .noCollission()
            .noOcclusion()
            .isViewBlocking(TFCBlocks::never)
            .isSuffocating(TFCBlocks::never)
            .flammableLikeLeaves();
    }

    private static <T extends Block> DeferredBlock<T> register(String name, Supplier<T> blockSupplier) {
        DeferredBlock<T> block = BLOCKS.register(name, blockSupplier);
        ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

    private static <T extends Block> DeferredBlock<T> registerNoItem(String name, Supplier<T> blockSupplier) {
        return BLOCKS.register(name, blockSupplier);
    }
}
