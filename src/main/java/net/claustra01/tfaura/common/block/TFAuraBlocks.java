package net.claustra01.tfaura.common.block;

import java.util.function.Supplier;
import net.claustra01.tfaura.TerraFirmaAura;
import net.claustra01.tfaura.common.blockentity.TFAuraBlockEntities;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TFAuraBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TerraFirmaAura.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TerraFirmaAura.MOD_ID);
    public static final TagKey<Block> CRIMSON_AURA_MUSHROOM_PLANTABLE_ON = BlockTags.create(TerraFirmaAura.id("crimson_aura_mushroom_plantable_on"));
    public static final TagKey<Block> WARPED_AURA_MUSHROOM_PLANTABLE_ON = BlockTags.create(TerraFirmaAura.id("warped_aura_mushroom_plantable_on"));

    public static final DeferredBlock<TFAuraPlantBlock> AURA_BLOOM = register("plant/aura_bloom",
        () -> new TFAuraAuraPlantBlock(plantProperties(MapColor.PLANT).lightLevel(state -> 7), TFCTags.Blocks.BUSH_PLANTABLE_ON, TFAuraPlantBlock.AURA_BLOOM_SHAPE, false));
    public static final DeferredBlock<TFAuraPlantBlock> AURA_CACTUS = register("plant/aura_cactus",
        () -> new TFAuraAuraPlantBlock(plantProperties(MapColor.PLANT), TFCTags.Blocks.DRY_PLANT_PLANTABLE_ON, TFAuraPlantBlock.CACTUS_SHAPE, true));
    public static final DeferredBlock<TFAuraPlantBlock> AURA_MUSHROOM = register("plant/aura_mushroom",
        () -> new TFAuraAuraPlantBlock(plantProperties(MapColor.PLANT).lightLevel(state -> 3), TFCTags.Blocks.BUSH_PLANTABLE_ON, TFAuraPlantBlock.MUSHROOM_SHAPE, false));
    public static final DeferredBlock<TFAuraPlantBlock> CRIMSON_AURA_MUSHROOM = register("plant/crimson_aura_mushroom",
        () -> new TFAuraAuraPlantBlock(plantProperties(MapColor.CRIMSON_STEM).lightLevel(state -> 3), CRIMSON_AURA_MUSHROOM_PLANTABLE_ON, TFAuraPlantBlock.MUSHROOM_SHAPE, false));
    public static final DeferredBlock<TFAuraPlantBlock> WARPED_AURA_MUSHROOM = register("plant/warped_aura_mushroom",
        () -> new TFAuraAuraPlantBlock(plantProperties(MapColor.WARPED_STEM).lightLevel(state -> 3), WARPED_AURA_MUSHROOM_PLANTABLE_ON, TFAuraPlantBlock.MUSHROOM_SHAPE, false));
    public static final DeferredBlock<TFAuraBrilliantGrassBlock> BRILLIANT_GRASS = registerNoItem("plant/brilliant_grass",
        () -> new TFAuraBrilliantGrassBlock(plantProperties(MapColor.GOLD).lightLevel(state -> 5).randomTicks()));

    public static final DeferredBlock<Block> STRIPPED_ANCIENT_LOG = registerWood("wood/stripped_log/ancient", Wood.BlockType.STRIPPED_LOG,
        Wood.BlockType.STRIPPED_LOG.create(TFAuraWood.ANCIENT));
    public static final DeferredBlock<Block> STRIPPED_ANCIENT_WOOD = registerWood("wood/stripped_wood/ancient", Wood.BlockType.STRIPPED_WOOD,
        Wood.BlockType.STRIPPED_WOOD.create(TFAuraWood.ANCIENT));
    public static final DeferredBlock<Block> ANCIENT_LOG = registerWood("wood/log/ancient", Wood.BlockType.LOG,
        Wood.BlockType.LOG.create(TFAuraWood.ANCIENT));
    public static final DeferredBlock<Block> ANCIENT_WOOD = registerWood("wood/wood/ancient", Wood.BlockType.WOOD,
        Wood.BlockType.WOOD.create(TFAuraWood.ANCIENT));
    public static final DeferredBlock<Block> ANCIENT_PLANKS = registerWood("wood/planks/ancient", Wood.BlockType.PLANKS,
        Wood.BlockType.PLANKS.create(TFAuraWood.ANCIENT));
    public static final DeferredBlock<Block> ANCIENT_STAIRS = registerWood("wood/stairs/ancient", Wood.BlockType.STAIRS,
        Wood.BlockType.STAIRS.create(TFAuraWood.ANCIENT));
    public static final DeferredBlock<Block> ANCIENT_SLAB = registerWood("wood/slab/ancient", Wood.BlockType.SLAB,
        Wood.BlockType.SLAB.create(TFAuraWood.ANCIENT));
    public static final DeferredBlock<Block> ANCIENT_FENCE = registerWood("wood/fence/ancient", Wood.BlockType.FENCE,
        Wood.BlockType.FENCE.create(TFAuraWood.ANCIENT));
    public static final DeferredBlock<Block> ANCIENT_LOG_FENCE = registerWood("wood/log_fence/ancient", Wood.BlockType.LOG_FENCE,
        Wood.BlockType.LOG_FENCE.create(TFAuraWood.ANCIENT));
    public static final DeferredBlock<Block> ANCIENT_FENCE_GATE = registerWood("wood/fence_gate/ancient", Wood.BlockType.FENCE_GATE,
        Wood.BlockType.FENCE_GATE.create(TFAuraWood.ANCIENT));

    public static final DeferredBlock<TFAuraAncientLeavesBlock> ANCIENT_LEAVES = registerWood("wood/leaves/ancient", Wood.BlockType.LEAVES,
        () -> new TFAuraAncientLeavesBlock(leavesProperties(MapColor.PLANT, 0.5F), TFAuraWood.ANCIENT));
    public static final DeferredBlock<TFAuraGoldenLeavesBlock> GOLDEN_LEAVES = registerWood("wood/leaves/golden", Wood.BlockType.LEAVES,
        () -> new TFAuraGoldenLeavesBlock(leavesProperties(MapColor.GOLD, 0.2F), TFAuraWood.ANCIENT));
    public static final DeferredBlock<TFAuraDecayedLeavesBlock> DECAYED_LEAVES = registerWood("wood/leaves/decayed", Wood.BlockType.LEAVES,
        () -> new TFAuraDecayedLeavesBlock(leavesProperties(MapColor.COLOR_GRAY, 0.2F), TFAuraWood.ANCIENT));

    public static final DeferredBlock<TFAuraSaplingBlock> ANCIENT_SAPLING = registerWood("wood/sapling/ancient", Wood.BlockType.SAPLING,
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

    private static ExtendedProperties leavesProperties(MapColor color, float strength) {
        return ExtendedProperties.of()
            .mapColor(color)
            .strength(strength)
            .sound(SoundType.GRASS)
            .defaultInstrument()
            .randomTicks()
            .noOcclusion()
            .isViewBlocking(TFCBlocks::never)
            .flammableLikeLeaves();
    }

    private static <T extends Block> DeferredBlock<T> register(String name, Supplier<T> blockSupplier) {
        DeferredBlock<T> block = BLOCKS.register(name, blockSupplier);
        ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

    private static <T extends Block> DeferredBlock<T> registerWood(String name, Wood.BlockType type, Supplier<T> blockSupplier) {
        DeferredBlock<T> block = BLOCKS.register(name, blockSupplier);
        ITEMS.register(name, () -> type.createBlockItem(TFAuraWood.ANCIENT, new Item.Properties()).apply(block.get()));
        return block;
    }

    private static <T extends Block> DeferredBlock<T> registerNoItem(String name, Supplier<T> blockSupplier) {
        return BLOCKS.register(name, blockSupplier);
    }
}
