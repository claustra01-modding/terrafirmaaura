package net.claustra01.tfaura;

import com.mojang.logging.LogUtils;
import net.claustra01.tfaura.common.TFAuraCreativeTabs;
import net.claustra01.tfaura.common.aura.TFAuraAuraEffects;
import net.claustra01.tfaura.common.block.TFAuraBlocks;
import net.claustra01.tfaura.common.block.TFAuraWood;
import net.claustra01.tfaura.common.blockentity.TFAuraBlockEntities;
import net.claustra01.tfaura.common.fluid.TFAuraFluids;
import net.claustra01.tfaura.common.integration.TFAuraNaturesAuraCompat;
import net.claustra01.tfaura.common.item.TFAuraItems;
import net.claustra01.tfaura.common.world.TFAuraFeatures;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(TerraFirmaAura.MOD_ID)
public final class TerraFirmaAura {
    public static final String MOD_ID = "tfaura";
    public static final String MOD_NAME = "TerraFirmaAura";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TerraFirmaAura(IEventBus modEventBus) {
        TFAuraBlocks.BLOCKS.register(modEventBus);
        TFAuraBlocks.ITEMS.register(modEventBus);
        TFAuraItems.ITEMS.register(modEventBus);
        TFAuraBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        TFAuraFluids.FLUID_TYPES.register(modEventBus);
        TFAuraFluids.FLUIDS.register(modEventBus);
        TFAuraFeatures.FEATURES.register(modEventBus);
        TFAuraCreativeTabs.TABS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerCapabilities);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        TFAuraAuraEffects.register();

        event.enqueueWork(() -> {
            for (TFAuraWood wood : TFAuraWood.VALUES) {
                BlockSetType.register(wood.getBlockSet());
                WoodType.register(wood.getVanillaWoodType());
            }

            FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;
            pot.addPlant(TFAuraBlocks.ANCIENT_SAPLING.getId(), TFAuraBlocks.POTTED_ANCIENT_SAPLING);
            TFAuraNaturesAuraCompat.register();
        });
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
            NaturesAuraAPI.AURA_CONTAINER_BLOCK_CAPABILITY,
            TFAuraBlockEntities.ANCIENT_LEAVES.get(),
            (blockEntity, side) -> blockEntity.container
        );
    }
}
