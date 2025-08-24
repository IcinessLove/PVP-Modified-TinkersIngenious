package com.xiaoyue.tinkers_ingenuity.content.module.building;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.generic.ISimpleModule;
import com.xiaoyue.tinkers_ingenuity.content.generic.SerialLoader;
import com.xiaoyue.tinkers_ingenuity.content.shared.json.variable.StatOperation;
import com.xiaoyue.tinkers_ingenuity.utils.TinkerUtils;
import slimeknights.mantle.data.loadable.primitive.BooleanLoadable;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.registry.GenericLoaderRegistry;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.*;

import java.util.List;

public record AllFloatStatCopyModule(INumericToolStat<?> source, StatOperation operation, float factor, boolean withMultiplier)
        implements ISimpleModule, ToolStatsModifierHook {

    public static final RecordLoadable<AllFloatStatCopyModule> LOADER = RecordLoadable.create(
            ToolStats.NUMERIC_LOADER.requiredField("source", AllFloatStatCopyModule::source),
            StatOperation.LOADER.requiredField("operation", AllFloatStatCopyModule::operation),
            FloatLoadable.ANY.requiredField("factor", AllFloatStatCopyModule::factor),
            BooleanLoadable.DEFAULT.defaultField("with_multiplier", true, AllFloatStatCopyModule::withMultiplier),
            AllFloatStatCopyModule::new
    );

    @SerialLoader
    public static void onInit() {
        TinkersIngenuity.REGISTRATE.module("all_float_stat_copy", LOADER);
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        float sourceValue = TinkerUtils.getStatValue(builder, this.source, this.withMultiplier);
        for(IToolStat<?> stat : ToolStats.getAllStats()) {
            if (stat instanceof FloatToolStat toolStat) {
                this.operation.apply(builder, toolStat, sourceValue * this.factor * (float)modifier.getLevel());
            }
        }
    }

    @Override
    public RecordLoadable<? extends GenericLoaderRegistry.IHaveLoader> getLoader() {
        return LOADER;
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(ModifierHooks.TOOL_STATS);
    }

    public static AllFloatStatCopyModule get(INumericToolStat<?> source, StatOperation operation, float factor) {
        return new AllFloatStatCopyModule(source, operation, factor, true);
    }
}
