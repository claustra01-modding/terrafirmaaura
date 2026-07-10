package net.claustra01.tfaura.common.aura;

import de.ellpeck.naturesaura.ModConfig;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import de.ellpeck.naturesaura.api.aura.chunk.IDrainSpotEffect;
import de.ellpeck.naturesaura.api.aura.type.IAuraType;
import net.dries007.tfc.common.entities.livestock.TFCAnimalProperties;
import net.dries007.tfc.common.entities.predator.Predator;
import net.dries007.tfc.common.entities.prey.WildAnimal;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;

public class TFAuraTFCAngerEffect implements IDrainSpotEffect {
    public static final ResourceLocation NAME = ResourceLocation.fromNamespaceAndPath(NaturesAuraAPI.MOD_ID, "anger");

    private int dist;

    @Override
    public ActiveType isActiveHere(Player player, LevelChunk chunk, IAuraChunk auraChunk, BlockPos spot, Integer spotAura) {
        if (!calcValues(player.level(), spot, spotAura)) {
            return ActiveType.INACTIVE;
        }

        return player.distanceToSqr(spot.getX(), spot.getY(), spot.getZ()) <= dist * dist
            ? ActiveType.ACTIVE
            : ActiveType.INACTIVE;
    }

    @Override
    public ItemStack getDisplayIcon() {
        return new ItemStack(Items.BONE);
    }

    @Override
    public void update(Level level, LevelChunk chunk, IAuraChunk auraChunk, BlockPos spot, Integer spotAura, IAuraChunk.DrainSpot drainSpot) {
        if (level.isClientSide || level.getGameTime() % 100 != 0 || !calcValues(level, spot, spotAura)) {
            return;
        }

        AABB bounds = new AABB(spot).inflate(dist);
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, bounds, TFAuraTFCAngerEffect::isAngerTarget)) {
            Player player = level.getNearestPlayer(entity, 25.0D);
            if (player == null || !EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(player)) {
                continue;
            }

            if (entity instanceof NeutralMob neutralMob) {
                neutralMob.setTarget(player);
            }

            if (isTFCAnimal(entity) && entity instanceof Mob mob) {
                angerTFCMob(mob, player);
            }
        }
    }

    @Override
    public ResourceLocation getName() {
        return NAME;
    }

    @Override
    public boolean appliesHere(LevelChunk chunk, IAuraChunk auraChunk, IAuraType type) {
        return ModConfig.instance == null || Boolean.TRUE.equals(ModConfig.instance.angerEffect.get());
    }

    private boolean calcValues(Level level, BlockPos spot, int spotAura) {
        if (spotAura >= 0) {
            return false;
        }

        int aura = IAuraChunk.getAuraInArea(level, spot, 50);
        if (aura > 0) {
            return false;
        }

        dist = Math.min(Math.abs(aura) / 50_000, 75);
        return dist >= 10;
    }

    private static boolean isAngerTarget(LivingEntity entity) {
        return entity instanceof NeutralMob || isTFCAnimal(entity);
    }

    private static boolean isTFCAnimal(LivingEntity entity) {
        return entity instanceof TFCAnimalProperties || entity instanceof WildAnimal;
    }

    private static void angerTFCMob(Mob mob, Player player) {
        mob.setTarget(player);
        mob.setLastHurtByMob(player);
        mob.setAggressive(true);

        mob.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, player);
        mob.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        mob.getBrain().setActiveActivityIfPossible(Activity.FIGHT);

        if (mob instanceof Predator predator) {
            predator.getBrain().eraseMemory(MemoryModuleType.HUNTED_RECENTLY);
            predator.getBrain().eraseMemory(MemoryModuleType.PACIFIED);
            predator.setSleeping(false);
        }
    }
}
