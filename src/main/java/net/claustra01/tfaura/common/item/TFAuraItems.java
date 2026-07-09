package net.claustra01.tfaura.common.item;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import net.claustra01.tfaura.TerraFirmaAura;
import net.dries007.tfc.util.Metal;
import net.minecraft.world.item.Item;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TFAuraItems {
    public static final String TFC_MORE_ITEMS_MOD_ID = "tfc_items";

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TerraFirmaAura.MOD_ID);

    public static final Map<TFAuraMetal, DeferredItem<Item>> METAL_INGOTS = registerMetalItems("ingot", Metal.ItemType.INGOT);
    public static final Map<TFAuraMetal, DeferredItem<Item>> METAL_DOUBLE_INGOTS = registerMetalItems("double_ingot", Metal.ItemType.DOUBLE_INGOT);
    public static final Map<TFAuraMetal, DeferredItem<Item>> METAL_SHEETS = registerMetalItems("sheet", Metal.ItemType.SHEET);
    public static final Map<TFAuraMetal, DeferredItem<Item>> METAL_DOUBLE_SHEETS = registerMetalItems("double_sheet", Metal.ItemType.DOUBLE_SHEET);
    public static final Map<TFAuraMetal, DeferredItem<Item>> METAL_RODS = registerMetalItems("rod", Metal.ItemType.ROD);
    public static final Map<TFAuraMetal, Map<TFAuraMoreItemType, DeferredItem<Item>>> MORE_METAL_ITEMS = ModList.get().isLoaded(TFC_MORE_ITEMS_MOD_ID)
        ? registerMoreMetalItems()
        : Collections.emptyMap();

    private TFAuraItems() {
    }

    private static Map<TFAuraMetal, DeferredItem<Item>> registerMetalItems(String itemTypePath, Metal.ItemType itemType) {
        EnumMap<TFAuraMetal, DeferredItem<Item>> items = new EnumMap<>(TFAuraMetal.class);
        for (TFAuraMetal metal : TFAuraMetal.VALUES) {
            items.put(metal, ITEMS.register("metal/" + itemTypePath + "/" + metal.getSerializedName(), () -> itemType.create(metal)));
        }
        return Collections.unmodifiableMap(items);
    }

    private static Map<TFAuraMetal, Map<TFAuraMoreItemType, DeferredItem<Item>>> registerMoreMetalItems() {
        EnumMap<TFAuraMetal, Map<TFAuraMoreItemType, DeferredItem<Item>>> itemsByMetal = new EnumMap<>(TFAuraMetal.class);
        for (TFAuraMetal metal : TFAuraMetal.VALUES) {
            EnumMap<TFAuraMoreItemType, DeferredItem<Item>> itemsByType = new EnumMap<>(TFAuraMoreItemType.class);
            for (TFAuraMoreItemType type : TFAuraMoreItemType.VALUES) {
                itemsByType.put(type, ITEMS.register("metal/" + type.path() + "/" + metal.getSerializedName(), () -> new Item(new Item.Properties().rarity(metal.rarity()))));
            }
            itemsByMetal.put(metal, Collections.unmodifiableMap(itemsByType));
        }
        return Collections.unmodifiableMap(itemsByMetal);
    }

    public enum TFAuraMoreItemType {
        FOIL("foil"),
        GEAR("gear"),
        HEAVY_SHEET("heavy_sheet"),
        NAIL("nail"),
        RING("ring"),
        RIVET("rivet"),
        SCREW("screw"),
        STAMEN("stamen"),
        WIRE("wire");

        public static final TFAuraMoreItemType[] VALUES = values();

        private final String path;

        TFAuraMoreItemType(String path) {
            this.path = path;
        }

        public String path() {
            return path;
        }
    }
}
