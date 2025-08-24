package com.xiaoyue.tinkers_ingenuity.data.modifier;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import slimeknights.tconstruct.common.TinkerTags.Modifiers;
import slimeknights.tconstruct.library.data.tinkering.AbstractModifierTagProvider;

public class TIModifierTagGen extends AbstractModifierTagProvider {
    public TIModifierTagGen(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, "tinkers_ingenuity", existingFileHelper);
    }

    protected void addTags() {
        this.tag(Modifiers.OVERSLIME_FRIEND)
                .add(TIModifierData.COLORFUL_SLIME.getId(), TIModifierData.COOPERATION.getId());
    }

    public String getName() {
        return "Tinkers Ingenuity modifier tag providers";
    }
}
