package com.xiaoyue.tinkers_ingenuity.data.tools;

import com.xiaoyue.tinkers_ingenuity.register.TIItems;
import net.minecraft.data.PackOutput;
import slimeknights.tconstruct.library.data.tinkering.AbstractToolDefinitionDataProvider;
import slimeknights.tconstruct.library.materials.RandomMaterial;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.definition.module.material.DefaultMaterialsModule;
import slimeknights.tconstruct.library.tools.definition.module.material.PartStatsModule;

public class TIToolDefinitionGen extends AbstractToolDefinitionDataProvider {
    public TIToolDefinitionGen(PackOutput packOutput) {
        super(packOutput, "tinkers_ingenuity");
    }

    public static final ToolDefinition TINKERS_MEDAL = ToolDefinition.create(TIItems.TINKERS_MEDAL.getId());
    public static final ToolDefinition BLOWPIPE = ToolDefinition.create(TIItems.BLOWPIPE.getId());

    @Override
    protected void addToolDefinitions() {
        RandomMaterial randomMaterial = RandomMaterial.random().tier(1).build();
        this.define(TINKERS_MEDAL).module(PartStatsModule.parts()
                        .part(TIItems.MEDAL_RIBBONS)
                        .part(TIItems.MEDAL_BODY).build())
                .module(DefaultMaterialsModule.builder()
                        .material(new RandomMaterial[]{randomMaterial, randomMaterial}).build())
                .smallToolStartingSlots().build();
        this.define(BLOWPIPE).module(PartStatsModule.parts()
                        .part(TIItems.BLOWPIPE_TUBE)
                        .part(TIItems.BLOWPIPE_MOUTH).build())
                .module(DefaultMaterialsModule.builder()
                        .material(new RandomMaterial[]{randomMaterial, randomMaterial}).build())
                .smallToolStartingSlots().build();
    }

    @Override
    public String getName() {
        return "Tinkers Ingenuity tool definition data providers";
    }
}
