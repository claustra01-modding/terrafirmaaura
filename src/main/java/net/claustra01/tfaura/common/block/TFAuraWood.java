package net.claustra01.tfaura.common.block;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;
import net.claustra01.tfaura.TerraFirmaAura;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.util.calendar.ICalendar;
import net.dries007.tfc.util.registry.RegistryWood;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;

public enum TFAuraWood implements RegistryWood {
    ANCIENT(false, MapColor.TERRACOTTA_GREEN, MapColor.COLOR_GREEN, 8, 3);

    public static final TFAuraWood[] VALUES = values();

    private final String serializedName;
    private final boolean conifer;
    private final MapColor woodColor;
    private final MapColor barkColor;
    private final TreeGrower tree;
    private final int ticksToGrow;
    private final int autumnIndex;
    private final BlockSetType blockSet;
    private final WoodType woodType;

    TFAuraWood(boolean conifer, MapColor woodColor, MapColor barkColor, int daysToGrow, int autumnIndex) {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.conifer = conifer;
        this.woodColor = woodColor;
        this.barkColor = barkColor;
        this.tree = new TreeGrower(
            TerraFirmaAura.id(serializedName).toString(),
            Optional.empty(),
            Optional.of(ResourceKey.create(Registries.CONFIGURED_FEATURE, TerraFirmaAura.id("tree/" + serializedName))),
            Optional.empty()
        );
        this.ticksToGrow = daysToGrow * ICalendar.CALENDAR_TICKS_IN_DAY;
        this.autumnIndex = autumnIndex;
        this.blockSet = new BlockSetType(TerraFirmaAura.id(serializedName).toString());
        this.woodType = new WoodType(TerraFirmaAura.id(serializedName).toString(), this.blockSet);
    }

    @Override
    public String getSerializedName() {
        return serializedName;
    }

    @Override
    public MapColor woodColor() {
        return woodColor;
    }

    @Override
    public MapColor barkColor() {
        return barkColor;
    }

    @Override
    public TreeGrower tree() {
        return tree;
    }

    @Override
    public Supplier<Integer> ticksToGrow() {
        return () -> ticksToGrow;
    }

    @Override
    public int autumnIndex() {
        return autumnIndex;
    }

    @Override
    public Supplier<Block> getBlock(Wood.BlockType type) {
        return switch (type) {
            case LOG -> () -> TFAuraBlocks.ANCIENT_LOG.get();
            case STRIPPED_LOG -> () -> TFAuraBlocks.STRIPPED_ANCIENT_LOG.get();
            case WOOD -> () -> TFAuraBlocks.ANCIENT_WOOD.get();
            case STRIPPED_WOOD -> () -> TFAuraBlocks.STRIPPED_ANCIENT_WOOD.get();
            case PLANKS -> () -> TFAuraBlocks.ANCIENT_PLANKS.get();
            case STAIRS -> () -> TFAuraBlocks.ANCIENT_STAIRS.get();
            case SLAB -> () -> TFAuraBlocks.ANCIENT_SLAB.get();
            case FENCE -> () -> TFAuraBlocks.ANCIENT_FENCE.get();
            case LOG_FENCE -> () -> TFAuraBlocks.ANCIENT_LOG_FENCE.get();
            case FENCE_GATE -> () -> TFAuraBlocks.ANCIENT_FENCE_GATE.get();
            case LEAVES -> () -> TFAuraBlocks.ANCIENT_LEAVES.get();
            case SAPLING -> () -> TFAuraBlocks.ANCIENT_SAPLING.get();
            case POTTED_SAPLING -> () -> TFAuraBlocks.POTTED_ANCIENT_SAPLING.get();
            case FALLEN_LEAVES -> () -> TFAuraBlocks.FALLEN_ANCIENT_LEAVES.get();
            case TWIG -> () -> TFAuraBlocks.ANCIENT_TWIG.get();
            default -> throw new IllegalArgumentException("Unsupported TerraFirmaAura wood block type: " + type);
        };
    }

    @Override
    public BlockSetType getBlockSet() {
        return blockSet;
    }

    @Override
    public WoodType getVanillaWoodType() {
        return woodType;
    }

    @Override
    public boolean isConifer() {
        return conifer;
    }

    @Override
    public float getFlowerOffset() {
        return 0.0F;
    }
}
