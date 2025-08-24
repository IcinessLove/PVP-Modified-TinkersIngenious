package com.xiaoyue.tinkers_ingenuity.data.material;

import com.xiaoyue.tinkers_ingenuity.content.shared.material.MaterialTraitsData;
import com.xiaoyue.tinkers_ingenuity.data.modifier.TIModifierData;
import com.xiaoyue.tinkers_ingenuity.register.TIMaterialStats;
import net.minecraft.data.PackOutput;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialTraitDataProvider;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.tools.data.material.MaterialIds;

public class TIMaterialTraitGen extends AbstractMaterialTraitDataProvider {
    public TIMaterialTraitGen(PackOutput packOutput, AbstractMaterialDataProvider materials) {
        super(packOutput, materials);
    }

    @Override
    protected void addMaterialTraits() {
        for(TIMaterials mate : TIMaterials.values()) {
            MaterialTraitsData traits = mate.holder.traits();
            if (traits != null) {
                if (traits.defaultTraits() != null) {
                    this.addDefaultTraits(mate.asMate(), traits.defaultTraits());
                }
                traits.otherTraits().forEach((id, trait) -> this.addTraits(mate.asMate(), id, trait));
            }
        }
        this.addVanillaCurioTraits();
    }

    private void addVanillaCurioTraits() {
        this.addCurioTraits(MaterialIds.wood);
        this.addCurioTraits(MaterialIds.bone);
        this.addCurioTraits(MaterialIds.flint);
        this.addCurioTraits(MaterialIds.copper);
        this.addCurioTraits(MaterialIds.necroticBone, TIModifierData.DEVOURING_LIFE.asEntry());
        this.addCurioTraits(MaterialIds.amethystBronze, TIModifierData.CRUSH.asEntry());
        this.addCurioTraits(MaterialIds.obsidian, TIModifierData.FORGE_FIRE.asEntry());
        this.addCurioTraits(MaterialIds.cobalt, TIModifierData.LITHE.asEntry());
        this.addCurioTraits(MaterialIds.manyullyn, TIModifierData.COLD_BLOODED.asEntry());
    }

    public void addCurioTraits(MaterialId id, ModifierEntry... mod) {
        this.addTraits(id, TIMaterialStats.CURIO, mod);
    }

    public static ModifierEntry entry(ModifierId id) {
        return new ModifierEntry(id, 1);
    }

    @Override
    public String getName() {
        return "Tinkers Ingenuity material traits providers";
    }
}
