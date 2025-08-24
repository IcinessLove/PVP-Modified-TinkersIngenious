package com.xiaoyue.tinkers_ingenuity.content.shared.holder;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.shared.material.*;
import net.minecraft.world.level.ItemLike;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.tools.modules.ArmorModuleBuilder;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public record MaterialBuildHolder(MaterialDescData desc, @Nullable MaterialRecipeData recipe, @Nullable MaterialDefinitionData definition, @Nullable MaterialStatsData stats, @Nullable MaterialTraitsData traits, @Nullable MaterialRenderData render) {

    public MaterialId asMate() {
        return new MaterialId(TinkersIngenuity.loc(this.desc.id()));
    }

    public static Builder builder(String id) {
        return new Builder(id);
    }

    public static class Builder {
        private final String id;
        private MaterialDescData desc;
        private MaterialRecipeData recipe;
        private MaterialDefinitionData definition;
        private MaterialStatsData stats;
        private MaterialTraitsData traits;
        private MaterialRenderData render;

        public Builder(String id) {
            this.id = id;
        }

        public Builder desc(Consumer<MaterialDescData.Builder> cons) {
            MaterialDescData.Builder builder = MaterialDescData.builder(this.id);
            cons.accept(builder);
            this.desc = builder.build();
            return this;
        }

        public Builder craftableMaterial(ItemLike item) {
            this.recipe = new MaterialRecipeData(item, null);
            return this;
        }

        public Builder metalMaterial(FluidObject<?> fluid) {
            this.recipe = new MaterialRecipeData(null, fluid);
            return this;
        }

        public Builder definition(Consumer<MaterialDefinitionData.Builder> cons) {
            MaterialDefinitionData.Builder builder = MaterialDefinitionData.builder();
            cons.accept(builder);
            this.definition = builder.build();
            return this;
        }

        public Builder stat(boolean shield, @Nullable ArmorModuleBuilder.ArmorShieldModuleBuilder<? extends IMaterialStats> armor, IMaterialStats... stats) {
            this.stats = new MaterialStatsData(shield, armor, stats);
            return this;
        }

        public Builder statAndShield(ArmorModuleBuilder.ArmorShieldModuleBuilder<? extends IMaterialStats> armor, IMaterialStats... stats) {
            this.stats = new MaterialStatsData(true, armor, stats);
            return this;
        }

        public Builder trait(Consumer<MaterialTraitsData.Builder> cons) {
            MaterialTraitsData.Builder builder = MaterialTraitsData.builder();
            cons.accept(builder);
            this.traits = builder.build();
            return this;
        }

        public Builder defaultTrait(ModifierEntry... trait) {
            this.traits = MaterialTraitsData.builder().addDefault(trait).build();
            return this;
        }

        public Builder render(Consumer<MaterialRenderData.Builder> cons) {
            MaterialRenderData.Builder builder = MaterialRenderData.builder();
            cons.accept(builder);
            this.render = builder.build();
            return this;
        }

        public MaterialBuildHolder build() {
            MaterialDefinitionData def = this.definition == null ? MaterialDefinitionData.builder().build() : this.definition;
            return new MaterialBuildHolder(this.desc, this.recipe, def, this.stats, this.traits, this.render);
        }
    }
}
