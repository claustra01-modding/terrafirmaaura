package net.claustra01.tfaura.common.fluid;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import net.claustra01.tfaura.common.item.TFAuraMetal;
import net.dries007.tfc.common.fluids.FluidHolder;
import net.dries007.tfc.common.fluids.MoltenFluid;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class TFAuraFluids {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, "tfc");
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, "tfc");

    public static final Map<TFAuraMetal, FluidHolder<BaseFlowingFluid>> METAL_FLUIDS = registerMetalFluids();

    private TFAuraFluids() {
    }

    private static Map<TFAuraMetal, FluidHolder<BaseFlowingFluid>> registerMetalFluids() {
        EnumMap<TFAuraMetal, FluidHolder<BaseFlowingFluid>> fluids = new EnumMap<>(TFAuraMetal.class);
        for (TFAuraMetal metal : TFAuraMetal.VALUES) {
            String fluidName = "metal/" + metal.getSerializedName();
            fluids.put(metal, registerMoltenMetal(fluidName, "metal/flowing_" + metal.getSerializedName(), metal));
        }
        return Collections.unmodifiableMap(fluids);
    }

    private static FluidHolder<BaseFlowingFluid> registerMoltenMetal(String fluidName, String flowingName, TFAuraMetal metal) {
        return RegistrationHelpers.registerFluid(FLUID_TYPES, FLUIDS, fluidName, fluidName, flowingName,
            properties -> {
            },
            () -> new FluidType(moltenFluidProperties().descriptionId("fluid.tfc.metal." + metal.getSerializedName()).rarity(metal.rarity())),
            MoltenFluid.Source::new,
            MoltenFluid.Flowing::new
        );
    }

    private static FluidType.Properties moltenFluidProperties() {
        return FluidType.Properties.create()
            .adjacentPathType(PathType.LAVA)
            .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
            .lightLevel(15)
            .density(3000)
            .viscosity(6000)
            .temperature(1300)
            .canConvertToSource(false)
            .canDrown(false)
            .canExtinguish(false)
            .canHydrate(false)
            .canPushEntity(false)
            .canSwim(false)
            .supportsBoating(false);
    }
}
