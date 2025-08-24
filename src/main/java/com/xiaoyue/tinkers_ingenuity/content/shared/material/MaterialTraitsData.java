package com.xiaoyue.tinkers_ingenuity.content.shared.material;

import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;

import java.util.HashMap;
import java.util.Map;

public record MaterialTraitsData(Map<MaterialStatsId, ModifierEntry[]> otherTraits, ModifierEntry[] defaultTraits) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Map<MaterialStatsId, ModifierEntry[]> traits = new HashMap<>();
        private ModifierEntry[] defaultTraits;

        public Builder addDefault(ModifierEntry... traits) {
            this.defaultTraits = traits;
            return this;
        }

        public Builder addMelee(ModifierEntry... traits) {
            this.traits.put(MaterialRegistry.MELEE_HARVEST, traits);
            return this;
        }

        public Builder addRanged(ModifierEntry... traits) {
            this.traits.put(MaterialRegistry.RANGED, traits);
            return this;
        }

        public Builder addArmor(ModifierEntry... traits) {
            this.traits.put(MaterialRegistry.ARMOR, traits);
            return this;
        }

        public MaterialTraitsData build() {
            return new MaterialTraitsData(this.traits, this.defaultTraits);
        }
    }
}
