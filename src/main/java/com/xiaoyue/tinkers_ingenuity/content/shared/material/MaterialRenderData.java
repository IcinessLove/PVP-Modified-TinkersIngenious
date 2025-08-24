package com.xiaoyue.tinkers_ingenuity.content.shared.material;

import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.library.client.data.spritetransformer.GreyToColorMapping;
import slimeknights.tconstruct.library.client.data.spritetransformer.GreyToSpriteTransformer;
import slimeknights.tconstruct.library.client.data.spritetransformer.IColorMapping;
import slimeknights.tconstruct.library.client.data.spritetransformer.ISpriteTransformer;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;

public record MaterialRenderData(int color, String[] fallbacks, MaterialStatsId[] parts, IColorMapping sprite, ISpriteTransformer transformer) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int color;
        private String[] fallbacks;
        private MaterialStatsId[] parts;
        private IColorMapping sprite;
        private ISpriteTransformer transformer;

        public Builder color(int color) {
            this.color = color;
            return this;
        }

        public Builder fallbacks(String... fallbacks) {
            this.fallbacks = fallbacks;
            return this;
        }

        public Builder parts(MaterialStatsId[] parts) {
            this.parts = parts;
            return this;
        }

        public Builder sprite(IColorMapping sprite) {
            this.sprite = sprite;
            return this;
        }

        public Builder transformer(ISpriteTransformer transformer) {
            this.transformer = transformer;
            return this;
        }

        public GreyToColorMapping color(int rgb1, int rgb2, int rgb3, int rgb4, int rgb5, int rgb6) {
            return GreyToColorMapping.builderFromBlack().addARGB(63, rgb1).addARGB(102, rgb2).addARGB(140, rgb3).addARGB(178, rgb4).addARGB(216, rgb5).addARGB(255, rgb6).build();
        }

        public GreyToSpriteTransformer trans(ResourceLocation tex1, ResourceLocation tex2, ResourceLocation tex3) {
            return GreyToSpriteTransformer.builderFromBlack().addTexture(63, tex1).addTexture(178, tex2).addTexture(255, tex3).build();
        }

        public MaterialRenderData build() {
            return new MaterialRenderData(this.color, this.fallbacks, this.parts, this.sprite, this.transformer);
        }
    }
}
