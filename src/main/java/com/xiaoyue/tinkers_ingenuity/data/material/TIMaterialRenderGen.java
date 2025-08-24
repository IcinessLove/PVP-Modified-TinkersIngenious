package com.xiaoyue.tinkers_ingenuity.data.material;

import com.xiaoyue.tinkers_ingenuity.content.shared.material.MaterialRenderData;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialRenderInfoProvider;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;

public class TIMaterialRenderGen extends AbstractMaterialRenderInfoProvider {
    public TIMaterialRenderGen(PackOutput packOutput, @Nullable AbstractMaterialSpriteProvider materialSprites, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, materialSprites, existingFileHelper);
    }

    @Override
    protected void addMaterialRenderInfo() {
        for(TIMaterials mate : TIMaterials.values()) {
            MaterialRenderData render = mate.holder.render();
            if (render != null) {
                this.buildRenderInfo(mate.asMate()).color(render.color()).fallbacks(render.fallbacks());
            }
        }
    }

    @Override
    public String getName() {
        return "Tinkers Ingenuity material render info providers";
    }
}
