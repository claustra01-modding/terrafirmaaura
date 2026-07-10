package net.claustra01.tfaura.common.aura;

import de.ellpeck.naturesaura.ModConfig;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import de.ellpeck.naturesaura.api.aura.chunk.IDrainSpotEffect;
import de.ellpeck.naturesaura.api.aura.type.IAuraType;
import java.util.List;
import net.dries007.tfc.common.entities.livestock.MammalProperties;
import net.dries007.tfc.common.entities.livestock.TFCAnimalProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.tuple.Pair;

public class TFAuraTFCAnimalBreedingEffect implements IDrainSpotEffect {
    public static final ResourceLocation NAME = ResourceLocation.fromNamespaceAndPath(NaturesAuraAPI.MOD_ID, "animal");
    private static final int REQUIRED_AURA_DELTA = IAuraChunk.DEFAULT_AURA * 3 / 2;
    private static final int BREEDING_AURA_COST = 3_500;
    private static final double MAX_PAIR_DISTANCE_SQR = 25.0D;

    private int chance;
    private int dist;

    @Override
    public ActiveType isActiveHere(Player player, LevelChunk chunk, IAuraChunk auraChunk, BlockPos spot, Integer spotAura) {
        if (!calcValues(player.level(), spot, spotAura)) {
            return ActiveType.INACTIVE;
        }

        if (player.distanceToSqr(spot.getX(), spot.getY(), spot.getZ()) > dist * dist) {
            return ActiveType.INACTIVE;
        }

        return NaturesAuraAPI.instance().isEffectPowderActive(player.level(), player.blockPosition(), NAME)
            ? ActiveType.ACTIVE
            : ActiveType.INHIBITED;
    }

    @Override
    public ItemStack getDisplayIcon() {
        return new ItemStack(Items.WHEAT);
    }

    @Override
    public void update(Level level, LevelChunk chunk, IAuraChunk auraChunk, BlockPos spot, Integer spotAura, IAuraChunk.DrainSpot drainSpot) {
        if (!(level instanceof ServerLevel serverLevel) || level.getGameTime() % 200 != 0 || !calcValues(level, spot, spotAura)) {
            return;
        }

        AABB bounds = new AABB(spot).inflate(dist);
        List<Animal> animals = level.getEntitiesOfClass(Animal.class, bounds, TFAuraTFCAnimalBreedingEffect::isTFCBreedCandidate);
        if (animals.size() < 2 || animals.size() >= maxAnimalsAroundPowder()) {
            return;
        }

        if (level.random.nextInt(20) > chance) {
            return;
        }

        tryBreed(serverLevel, animals, spot);
    }

    @Override
    public ResourceLocation getName() {
        return NAME;
    }

    @Override
    public boolean appliesHere(LevelChunk chunk, IAuraChunk auraChunk, IAuraType type) {
        return (ModConfig.instance == null || Boolean.TRUE.equals(ModConfig.instance.animalEffect.get()))
            && type.isSimilar(NaturesAuraAPI.TYPE_OVERWORLD);
    }

    private boolean calcValues(Level level, BlockPos spot, int spotAura) {
        if (spotAura <= 0) {
            return false;
        }

        Pair<Integer, Integer> aura = IAuraChunk.getAuraAndSpotAmountInArea(level, spot, 30);
        int auraDeltaInArea = aura.getLeft();
        if (auraDeltaInArea < REQUIRED_AURA_DELTA) {
            return false;
        }

        int spots = Math.max(1, aura.getRight());
        chance = Math.min(50, Mth.ceil(Math.abs(auraDeltaInArea) / 500_000.0F / spots));
        dist = Mth.clamp(Math.abs(auraDeltaInArea) / 150_000, 5, 35);
        return chance > 0;
    }

    private static boolean isTFCBreedCandidate(Animal animal) {
        return animal instanceof TFCAnimalProperties;
    }

    private boolean tryBreed(ServerLevel level, List<Animal> animals, BlockPos spot) {
        int start = level.random.nextInt(animals.size());
        for (int i = 0; i < animals.size(); i++) {
            Animal animal = animals.get((start + i) % animals.size());
            if (!(animal instanceof TFCAnimalProperties properties) || !properties.isReadyToMate()) {
                continue;
            }

            Animal partner = findNearestPartner(animal, properties, animals);
            if (partner == null || !isPowderActiveNearPair(level, animal, partner)) {
                continue;
            }

            Animal female = properties.isFemale() ? animal : partner;
            Animal male = properties.isMale() ? animal : partner;
            if (fertilize(level, female, male, spot)) {
                return true;
            }
        }

        return false;
    }

    private Animal findNearestPartner(Animal animal, TFCAnimalProperties properties, List<Animal> animals) {
        Animal nearest = null;
        double nearestDistance = Double.MAX_VALUE;

        for (Animal candidate : animals) {
            if (candidate == animal || !(candidate instanceof TFCAnimalProperties candidateProperties)) {
                continue;
            }
            if (properties.isMale() == candidateProperties.isMale() || !animal.canMate(candidate)) {
                continue;
            }

            double distance = animal.distanceToSqr(candidate);
            if (distance <= MAX_PAIR_DISTANCE_SQR && distance < nearestDistance) {
                nearest = candidate;
                nearestDistance = distance;
            }
        }

        return nearest;
    }

    private boolean fertilize(ServerLevel level, Animal female, Animal male, BlockPos spot) {
        if (!(female instanceof AgeableMob femaleAgeable)
            || !(male instanceof AgeableMob maleAgeable)
            || !(female instanceof TFCAnimalProperties femaleProperties)
            || !(male instanceof TFCAnimalProperties maleProperties)
            || !femaleProperties.isFemale()) {
            return false;
        }

        boolean wasFertilized = femaleProperties.isFertilized();
        long previousPregnantTime = femaleProperties instanceof MammalProperties mammal ? mammal.getPregnantTime() : -1L;

        femaleAgeable.getBreedOffspring(level, maleAgeable);
        maleProperties.setLastMatedNow();

        boolean fertilized = !wasFertilized && femaleProperties.isFertilized();
        boolean pregnancyStarted = femaleProperties instanceof MammalProperties mammal
            && mammal.getPregnantTime() > 0
            && mammal.getPregnantTime() != previousPregnantTime;
        if (!fertilized && !pregnancyStarted) {
            return false;
        }

        spawnHearts(level, female, male);
        drainAura(level, female.blockPosition(), spot, BREEDING_AURA_COST);
        return true;
    }

    private boolean isPowderActiveNearPair(Level level, Animal first, Animal second) {
        return NaturesAuraAPI.instance().isEffectPowderActive(level, first.blockPosition(), NAME)
            || NaturesAuraAPI.instance().isEffectPowderActive(level, second.blockPosition(), NAME);
    }

    private void drainAura(Level level, BlockPos target, BlockPos spot, int amount) {
        BlockPos drainSpot = IAuraChunk.getHighestSpot(level, target, 35, spot);
        IAuraChunk.getAuraChunk(level, drainSpot).drainAura(drainSpot, amount);
    }

    private int maxAnimalsAroundPowder() {
        return ModConfig.instance == null ? 20 : ModConfig.instance.maxAnimalsAroundPowder.get();
    }

    private void spawnHearts(ServerLevel level, Animal first, Animal second) {
        double x = (first.getX() + second.getX()) * 0.5D;
        double y = Math.max(first.getEyeY(), second.getEyeY());
        double z = (first.getZ() + second.getZ()) * 0.5D;
        level.sendParticles(ParticleTypes.HEART, x, y, z, 7, 0.6D, 0.4D, 0.6D, 0.02D);
    }
}
