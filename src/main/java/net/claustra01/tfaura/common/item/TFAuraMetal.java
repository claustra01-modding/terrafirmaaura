package net.claustra01.tfaura.common.item;

import net.dries007.tfc.common.LevelTier;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.registry.RegistryMetal;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;

public enum TFAuraMetal implements RegistryMetal {
    INFUSED_IRON("infused_iron", Rarity.UNCOMMON, 3, 1535.0F),
    TAINTED_GOLD("tainted_gold", Rarity.UNCOMMON, 1, 1060.0F),
    SKY("sky", Rarity.RARE, 5, 1540.0F),
    DEPTH("depth", Rarity.EPIC, 6, 1600.0F);

    public static final TFAuraMetal[] VALUES = values();

    private final String serializedName;
    private final Rarity rarity;
    private final int anvilTier;
    private final float meltingTemperature;

    TFAuraMetal(String serializedName, Rarity rarity, int anvilTier, float meltingTemperature) {
        this.serializedName = serializedName;
        this.rarity = rarity;
        this.anvilTier = anvilTier;
        this.meltingTemperature = meltingTemperature;
    }

    @Override
    public String getSerializedName() {
        return serializedName;
    }

    public int anvilTier() {
        return anvilTier;
    }

    public float meltingTemperature() {
        return meltingTemperature;
    }

    @Override
    public LevelTier toolTier() {
        throw unsupported("toolTier");
    }

    @Override
    public Holder<ArmorMaterial> armorMaterial() {
        throw unsupported("armorMaterial");
    }

    @Override
    public int armorDurability(ArmorItem.Type type) {
        throw unsupported("armorDurability");
    }

    @Override
    public Block getBlock(Metal.BlockType type) {
        throw unsupported("getBlock(" + type.name() + ")");
    }

    @Override
    public MapColor mapColor() {
        return MapColor.METAL;
    }

    @Override
    public Rarity rarity() {
        return rarity;
    }

    @Override
    public float weatheringResistance() {
        return -1.0F;
    }

    private UnsupportedOperationException unsupported(String method) {
        return new UnsupportedOperationException(method + " is not used by TerraFirmaAura metal item forms: " + serializedName);
    }
}
