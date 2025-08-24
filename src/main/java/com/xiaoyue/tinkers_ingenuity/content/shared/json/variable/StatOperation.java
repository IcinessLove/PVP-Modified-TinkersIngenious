package com.xiaoyue.tinkers_ingenuity.content.shared.json.variable;

import slimeknights.mantle.data.loadable.primitive.EnumLoadable;
import slimeknights.tconstruct.library.tools.stat.INumericToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

public enum StatOperation {
    addition {
        @Override
        public void apply(ModifierStatsBuilder builder, INumericToolStat<?> stat, float factor) {
            stat.add(builder, factor);
        }
    },
    multiplier_base {
        @Override
        public void apply(ModifierStatsBuilder builder, INumericToolStat<?> stat, float factor) {
            stat.multiply(builder, (1.0F + factor));
        }
    },
    multiplier_conditional {
        @Override
        public void apply(ModifierStatsBuilder builder, INumericToolStat<?> stat, float factor) {
            builder.multiplier(stat, (1.0F + factor));
        }
    },
    multiplier_all {
        @Override
        public void apply(ModifierStatsBuilder builder, INumericToolStat<?> stat, float factor) {
            stat.multiplyAll(builder, (1.0F + factor));
        }
    };

    public static final EnumLoadable<StatOperation> LOADER = new EnumLoadable<>(StatOperation.class);

    public abstract void apply(ModifierStatsBuilder builder, INumericToolStat<?> stat, float factor);
}
