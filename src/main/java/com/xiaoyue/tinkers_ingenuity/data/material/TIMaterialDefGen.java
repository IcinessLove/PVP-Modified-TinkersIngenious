package com.xiaoyue.tinkers_ingenuity.data.material;

import com.xiaoyue.tinkers_ingenuity.content.shared.material.MaterialDefinitionData;
import net.minecraft.data.PackOutput;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;

public class TIMaterialDefGen extends AbstractMaterialDataProvider {
    public TIMaterialDefGen(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void addMaterials() {
        for(TIMaterials mate : TIMaterials.values()) {
            MaterialDefinitionData def = mate.holder.definition();
            if (def != null) {
                this.addMaterial(def.getMate(mate.asMate()), def.condition());
            }
        }
    }

    @Override
    public String getName() {
        return "Tinkers Ingenuity material providers";
    }
}
