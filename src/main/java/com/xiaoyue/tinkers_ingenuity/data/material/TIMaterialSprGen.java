package com.xiaoyue.tinkers_ingenuity.data.material;

import com.xiaoyue.tinkers_ingenuity.content.shared.material.MaterialRenderData;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;

public class TIMaterialSprGen extends AbstractMaterialSpriteProvider {

    @Override
    protected void addAllMaterials() {
        for(TIMaterials mate : TIMaterials.values()) {
            MaterialRenderData render = mate.holder.render();
            if (render != null) {
                AbstractMaterialSpriteProvider.MaterialSpriteInfoBuilder builder;
                if (render.parts() != null) {
                    builder = this.buildMaterial(mate.asMate()).statType(render.parts()).fallbacks(render.fallbacks());
                } else {
                    builder = this.buildMaterial(mate.asMate()).meleeHarvest().ranged().armor().fallbacks(render.fallbacks());
                }
                if (render.transformer() == null) {
                    builder.colorMapper(render.sprite());
                } else {
                    builder.transformer(render.transformer());
                }
            }
        }

    }

    @Override
    public String getName() {
        return "Tinkers Ingenuity material sprite providers";
    }
}
