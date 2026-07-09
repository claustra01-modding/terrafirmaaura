package net.claustra01.tfaura.common.aura;

import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import net.claustra01.tfaura.TerraFirmaAura;

public final class TFAuraAuraEffects {
    private TFAuraAuraEffects() {
    }

    public static void register() {
        NaturesAuraAPI.DRAIN_SPOT_EFFECTS.put(TFAuraTFCPlantBoostEffect.NAME, TFAuraTFCPlantBoostEffect::new);
        NaturesAuraAPI.DRAIN_SPOT_EFFECTS.put(TFAuraTFCPlantDecayEffect.NAME, TFAuraTFCPlantDecayEffect::new);
        NaturesAuraAPI.EFFECT_POWDERS.put(TFAuraTFCPlantBoostEffect.NAME, 0x7BE65A);
        NaturesAuraAPI.EFFECT_POWDERS.put(TFAuraTFCPlantDecayEffect.NAME, 0x72624D);

        TerraFirmaAura.LOGGER.debug("Registered TerraFirmaAura aura plant effects");
    }
}
