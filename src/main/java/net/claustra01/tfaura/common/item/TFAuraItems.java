package net.claustra01.tfaura.common.item;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import net.claustra01.tfaura.TerraFirmaAura;
import net.dries007.tfc.util.Metal;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TFAuraItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TerraFirmaAura.MOD_ID);

    public static final DeferredItem<Item> ANCIENT_LUMBER = ITEMS.register("wood/lumber/ancient", () -> new Item(new Item.Properties()));

    public static final Map<TFAuraMetal, DeferredItem<Item>> METAL_INGOTS = registerMetalItems("ingot", Metal.ItemType.INGOT);
    public static final Map<TFAuraMetal, DeferredItem<Item>> METAL_DOUBLE_INGOTS = registerMetalItems("double_ingot", Metal.ItemType.DOUBLE_INGOT);
    public static final Map<TFAuraMetal, DeferredItem<Item>> METAL_SHEETS = registerMetalItems("sheet", Metal.ItemType.SHEET);
    public static final Map<TFAuraMetal, DeferredItem<Item>> METAL_DOUBLE_SHEETS = registerMetalItems("double_sheet", Metal.ItemType.DOUBLE_SHEET);
    public static final Map<TFAuraMetal, DeferredItem<Item>> METAL_RODS = registerMetalItems("rod", Metal.ItemType.ROD);

    private TFAuraItems() {
    }

    private static Map<TFAuraMetal, DeferredItem<Item>> registerMetalItems(String itemTypePath, Metal.ItemType itemType) {
        EnumMap<TFAuraMetal, DeferredItem<Item>> items = new EnumMap<>(TFAuraMetal.class);
        for (TFAuraMetal metal : TFAuraMetal.VALUES) {
            items.put(metal, ITEMS.register("metal/" + itemTypePath + "/" + metal.getSerializedName(), () -> itemType.create(metal)));
        }
        return Collections.unmodifiableMap(items);
    }

}
