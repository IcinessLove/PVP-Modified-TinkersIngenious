package com.xiaoyue.tinkers_ingenuity.content.shared.stats;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import net.minecraft.network.chat.Component;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

import java.util.List;

public enum TIExtraMaterialStat implements IMaterialStats {
    CURIO_EXTRA("curio_extra");

    private static final List<Component> LOCALIZED = List.of(IMaterialStats.makeTooltip(TConstruct.getResource("extra.no_stats")));
    private static final List<Component> DESCRIPTION = List.of(Component.empty());
    private final MaterialStatType<TIExtraMaterialStat> type;

    TIExtraMaterialStat(String name) {
        this.type = MaterialStatType.singleton(new MaterialStatsId(TinkersIngenuity.loc(name)), this);
    }

    public static MaterialStatsId getCurioExtra() {
        return CURIO_EXTRA.getIdentifier();
    }

    @Override
    public MaterialStatType<TIExtraMaterialStat> getType() {
        return this.type;
    }

    @Override
    public List<Component> getLocalizedInfo() {
        return LOCALIZED;
    }

    @Override
    public List<Component> getLocalizedDescriptions() {
        return DESCRIPTION;
    }

    @Override
    public void apply(ModifierStatsBuilder builder, float scale) {
    }
}
