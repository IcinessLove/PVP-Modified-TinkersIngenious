package com.xiaoyue.tinkers_ingenuity.content.module.building;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.generic.ISimpleModule;
import com.xiaoyue.tinkers_ingenuity.content.generic.SerialLoader;
import com.xiaoyue.tinkers_ingenuity.content.shared.json.variable.StatOperation;
import com.xiaoyue.tinkers_ingenuity.utils.TinkerUtils;
import net.minecraft.resources.ResourceLocation;
import slimeknights.mantle.data.loadable.primitive.BooleanLoadable;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.primitive.ResourceLocationLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.registry.GenericLoaderRegistry;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.INumericToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

public record StatWithTraitCountModule(INumericToolStat<?> stat, StatOperation operation, float factor, boolean single, ResourceLocation trait)
        implements ISimpleModule, ToolStatsModifierHook {

    public static final RecordLoadable<StatWithTraitCountModule> LOADER = RecordLoadable.create(
            ToolStats.NUMERIC_LOADER.requiredField("stat", StatWithTraitCountModule::stat),
            StatOperation.LOADER.requiredField("operation", StatWithTraitCountModule::operation),
            FloatLoadable.ANY.requiredField("factor", StatWithTraitCountModule::factor),
            BooleanLoadable.DEFAULT.defaultField("single", false, StatWithTraitCountModule::single),
            ResourceLocationLoadable.DEFAULT.requiredField("trait", StatWithTraitCountModule::trait),
            StatWithTraitCountModule::new
    );

    @SerialLoader
    public static void onInit() {
        TinkersIngenuity.REGISTRATE.module("stat_with_trait_count", LOADER);
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        int count = TinkerUtils.getTraitCountWithModifier(context, new ModifierId(this.trait));
        int finalLv = this.single ? 1 : modifier.getLevel();
        this.operation.apply(builder, this.stat, this.factor * (float)count * (float)finalLv);
    }

    @Override
    public RecordLoadable<? extends GenericLoaderRegistry.IHaveLoader> getLoader() {
        return LOADER;
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(ModifierHooks.TOOL_STATS);
    }

    public static StatWithTraitCountModule get(INumericToolStat<?> stat, StatOperation operation, float factor, boolean single, ResourceLocation trait) {
        return new StatWithTraitCountModule(stat, operation, factor, single, trait);
    }
}
