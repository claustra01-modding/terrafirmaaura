package net.claustra01.tfaura.common.integration;

import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import net.dries007.tfc.common.blocks.DecorationBlockHolder;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.rock.Rock.BlockType;
import net.dries007.tfc.common.entities.TFCEntities;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public final class TFAuraNaturesAuraCompat {
    private static final int THROWN_JAVELIN_AURA = 30_000;
    private static final int GLOW_ARROW_AURA = 45_000;

    private TFAuraNaturesAuraCompat() {
    }

    public static void register() {
        registerMossGeneratorConversions();
        registerProjectileGeneratorProjectiles();
    }

    private static void registerMossGeneratorConversions() {
        for (Rock rock : Rock.VALUES) {
            registerRockMossPair(rock, BlockType.COBBLE, BlockType.MOSSY_COBBLE);
            registerRockMossPair(rock, BlockType.BRICKS, BlockType.MOSSY_BRICKS);
        }
    }

    private static void registerRockMossPair(Rock rock, BlockType cleanType, BlockType mossyType) {
        Map<BlockType, TFCBlocks.Id<Block>> blocks = TFCBlocks.ROCK_BLOCKS.get(rock);
        if (blocks != null) {
            registerMossConversion(blocks.get(cleanType), blocks.get(mossyType));
        }

        Map<BlockType, DecorationBlockHolder> decorations = TFCBlocks.ROCK_DECORATIONS.get(rock);
        if (decorations != null) {
            DecorationBlockHolder clean = decorations.get(cleanType);
            DecorationBlockHolder mossy = decorations.get(mossyType);
            if (clean != null && mossy != null) {
                registerMossConversion(clean.slab(), mossy.slab());
                registerMossConversion(clean.stair(), mossy.stair());
                registerMossConversion(clean.wall(), mossy.wall());
            }
        }
    }

    private static void registerMossConversion(TFCBlocks.Id<? extends Block> clean, TFCBlocks.Id<? extends Block> mossy) {
        if (clean == null || mossy == null) {
            return;
        }
        BlockState cleanState = clean.get().defaultBlockState();
        BlockState mossyState = mossy.get().defaultBlockState();
        if (!NaturesAuraAPI.BOTANIST_PICKAXE_CONVERSIONS.containsKey(cleanState)
            && !NaturesAuraAPI.BOTANIST_PICKAXE_CONVERSIONS.containsValue(mossyState)) {
            NaturesAuraAPI.BOTANIST_PICKAXE_CONVERSIONS.put(cleanState, mossyState);
        }
    }

    private static void registerProjectileGeneratorProjectiles() {
        NaturesAuraAPI.PROJECTILE_GENERATIONS.put(TFCEntities.THROWN_JAVELIN.get(), THROWN_JAVELIN_AURA);
        NaturesAuraAPI.PROJECTILE_GENERATIONS.put(TFCEntities.GLOW_ARROW.get(), GLOW_ARROW_AURA);
    }
}
