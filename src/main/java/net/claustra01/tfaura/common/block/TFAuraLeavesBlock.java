package net.claustra01.tfaura.common.block;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.wood.TFCLeavesBlock;
import net.dries007.tfc.util.registry.RegistryWood;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class TFAuraLeavesBlock extends TFCLeavesBlock {
    public TFAuraLeavesBlock(ExtendedProperties properties, RegistryWood wood) {
        super(properties, wood, null, null);
    }

    protected static BlockState copyLeafProperties(BlockState target, BlockState source) {
        if (source.hasProperty(PERSISTENT)) {
            target = target.setValue(PERSISTENT, source.getValue(PERSISTENT));
        }
        IntegerProperty distance = distanceProperty(source);
        if (distance != null) {
            target = target.setValue(DISTANCE, Math.clamp(source.getValue(distance), 1, MAX_DECAY_DISTANCE));
        }
        return target.setValue(FLUID, FLUID.keyForOrEmpty(source.getFluidState().getType()));
    }

    private static IntegerProperty distanceProperty(BlockState state) {
        if (state.hasProperty(DISTANCE)) {
            return DISTANCE;
        }
        for (Property<?> property : state.getProperties()) {
            if (property instanceof IntegerProperty integerProperty && "distance".equals(property.getName())) {
                return integerProperty;
            }
        }
        return null;
    }
}
