package net.claustra01.tfaura.common.aura;

import de.ellpeck.naturesaura.ModConfig;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import de.ellpeck.naturesaura.api.aura.chunk.IDrainSpotEffect;
import de.ellpeck.naturesaura.api.aura.type.IAuraType;
import java.util.List;
import net.dries007.tfc.common.entities.livestock.Age;
import net.dries007.tfc.common.entities.livestock.TFCAnimalProperties;
import net.dries007.tfc.common.entities.prey.WildAnimal;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.tuple.Pair;

public class TFAuraTFCAnimalGrowthEffect implements IDrainSpotEffect {
    public static final ResourceLocation NAME = ResourceLocation.fromNamespaceAndPath(NaturesAuraAPI.MOD_ID, "animal");
    private static final int DEFAULT_AURA = IAuraChunk.DEFAULT_AURA;
    private static final int POSITIVE_THRESHOLD = DEFAULT_AURA * 3 / 2;
    private static final int NEGATIVE_THRESHOLD = DEFAULT_AURA / 4;
    private static final long TFC_DAY_TICKS = 28_800L;

    private int amount;
    private int dist;
    private int auraDeltaInArea;
    private boolean positive;

    @Override
    public ActiveType isActiveHere(Player player, LevelChunk chunk, IAuraChunk auraChunk, BlockPos spot, Integer spotAura) {
        Level level = player.level();
        if (calcPositiveValues(level, spot, spotAura)) {
            if (player.distanceToSqr(spot.getX(), spot.getY(), spot.getZ()) > dist * dist) {
                return ActiveType.INACTIVE;
            }
            return NaturesAuraAPI.instance().isEffectPowderActive(level, player.blockPosition(), NAME)
                ? ActiveType.ACTIVE
                : ActiveType.INHIBITED;
        }

        if (calcNegativeValues(level, spot, spotAura)) {
            return player.distanceToSqr(spot.getX(), spot.getY(), spot.getZ()) <= dist * dist
                ? ActiveType.ACTIVE
                : ActiveType.INACTIVE;
        }

        return ActiveType.INACTIVE;
    }

    @Override
    public ItemStack getDisplayIcon() {
        return new ItemStack(Items.WHEAT);
    }

    @Override
    public void update(Level level, LevelChunk chunk, IAuraChunk auraChunk, BlockPos spot, Integer spotAura, IAuraChunk.DrainSpot drainSpot) {
        if (level.isClientSide) {
            return;
        }

        boolean active = calcPositiveValues(level, spot, spotAura) || calcNegativeValues(level, spot, spotAura);
        if (!active) {
            return;
        }

        if (positive && !NaturesAuraAPI.instance().isEffectPowderActive(level, spot, NAME)) {
            return;
        }

        AABB bounds = new AABB(spot).inflate(dist);
        List<LivingEntity> animals = level.getEntitiesOfClass(LivingEntity.class, bounds, TFAuraTFCAnimalGrowthEffect::isTFCAnimal);
        if (animals.isEmpty()) {
            return;
        }

        int attempts = Mth.clamp(amount, 1, animals.size() * 2);
        while (attempts-- > 0) {
            LivingEntity animal = animals.get(level.random.nextInt(animals.size()));
            boolean changed = positive ? accelerateGrowth(animal) : inhibitGrowth(animal);
            if (changed && positive) {
                drainAura(level, animal.blockPosition(), spot, positiveAuraCost());
            }
        }
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

    private boolean calcPositiveValues(Level level, BlockPos spot, int spotAura) {
        if (spotAura <= 0) {
            return false;
        }

        Pair<Integer, Integer> aura = IAuraChunk.getAuraAndSpotAmountInArea(level, spot, 30);
        auraDeltaInArea = aura.getLeft();
        if (auraDeltaInArea < POSITIVE_THRESHOLD) {
            return false;
        }

        int spots = Math.max(1, aura.getRight());
        amount = Math.min(50, Mth.ceil(Math.abs(auraDeltaInArea) / 500_000.0F / spots));
        dist = Mth.clamp(Math.abs(auraDeltaInArea) / 150_000, 5, 35);
        positive = true;
        return amount > 0;
    }

    private boolean calcNegativeValues(Level level, BlockPos spot, int spotAura) {
        if (spotAura >= 0) {
            return false;
        }

        Pair<Integer, Integer> aura = IAuraChunk.getAuraAndSpotAmountInArea(level, spot, 50);
        auraDeltaInArea = aura.getLeft();
        if (Math.abs(auraDeltaInArea) < NEGATIVE_THRESHOLD) {
            return false;
        }

        int spots = Math.max(1, aura.getRight());
        amount = Math.min(80, Mth.ceil(Math.abs(auraDeltaInArea) / 200_000.0F / spots));
        dist = Mth.clamp(Math.abs(auraDeltaInArea) / 120_000, 5, 45);
        positive = false;
        return amount > 0;
    }

    private static boolean isTFCAnimal(LivingEntity entity) {
        return entity instanceof TFCAnimalProperties || entity instanceof WildAnimal;
    }

    private boolean accelerateGrowth(LivingEntity entity) {
        if (entity instanceof TFCAnimalProperties animal && animal.getAgeType() == Age.CHILD) {
            Age beforeAge = animal.getAgeType();
            long beforeBirthTick = animal.getBirthTick();
            animal.setBirthTick(beforeBirthTick - growthShiftTicks());
            refreshAnimalAge(animal, beforeAge);
            return animal.getBirthTick() != beforeBirthTick;
        }

        if (entity instanceof WildAnimal wildAnimal && wildAnimal.isBaby() && entity.getRandom().nextFloat() < wildGrowthChance()) {
            wildAnimal.setBaby(false);
            return true;
        }

        return false;
    }

    private boolean inhibitGrowth(LivingEntity entity) {
        if (!(entity instanceof TFCAnimalProperties animal) || animal.getAgeType() != Age.CHILD) {
            return false;
        }

        long beforeBirthTick = animal.getBirthTick();
        long maxFutureBirthTick = animal.calendar().getTicks() + TFC_DAY_TICKS * 3L;
        animal.setBirthTick(Math.min(maxFutureBirthTick, beforeBirthTick + inhibitionShiftTicks()));
        return animal.getBirthTick() != beforeBirthTick;
    }

    private void refreshAnimalAge(TFCAnimalProperties animal, Age beforeAge) {
        Age afterAge = animal.getAgeType();
        if (afterAge != beforeAge) {
            animal.setLastAge(afterAge);
            animal.getEntity().refreshDimensions();
        }
    }

    private void drainAura(Level level, BlockPos target, BlockPos spot, int amount) {
        BlockPos drainSpot = IAuraChunk.getHighestSpot(level, target, 25, spot);
        IAuraChunk.getAuraChunk(level, drainSpot).drainAura(drainSpot, amount);
    }

    private int effectTier() {
        int delta = Math.abs(auraDeltaInArea);
        if (delta >= DEFAULT_AURA) {
            return 3;
        }
        if (delta >= DEFAULT_AURA * 3 / 5) {
            return 2;
        }
        if (delta >= DEFAULT_AURA / 4) {
            return 1;
        }
        return 0;
    }

    private float intensity() {
        return Mth.clamp(Math.abs(auraDeltaInArea) / (float) DEFAULT_AURA, 0.25F, 2.0F);
    }

    private long growthShiftTicks() {
        return Mth.clamp((long) (TFC_DAY_TICKS * 0.5F * intensity()), 3_600L, TFC_DAY_TICKS * 2L);
    }

    private long inhibitionShiftTicks() {
        return Mth.clamp((long) (TFC_DAY_TICKS * 0.25F * intensity()), 1_800L, TFC_DAY_TICKS);
    }

    private float wildGrowthChance() {
        return switch (effectTier()) {
            case 1 -> 0.12F;
            case 2 -> 0.25F;
            case 3 -> Mth.clamp(0.35F * intensity(), 0.35F, 0.75F);
            default -> 0.0F;
        };
    }

    private int positiveAuraCost() {
        return switch (effectTier()) {
            case 1 -> 2_500;
            case 2 -> 4_000;
            case 3 -> 5_500;
            default -> 2_000;
        };
    }
}
