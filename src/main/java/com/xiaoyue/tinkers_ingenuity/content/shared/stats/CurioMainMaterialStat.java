package com.xiaoyue.tinkers_ingenuity.content.shared.stats;

import com.google.common.collect.ImmutableList;
import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.register.TIToolStats;
import net.minecraft.network.chat.Component;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.materials.stats.IRepairableMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

import java.util.ArrayList;
import java.util.List;

public record CurioMainMaterialStat(float curio_movement_speed, float curio_max_health, float curio_armor, float curio_melee_attack, float curio_projectile_attack) implements IRepairableMaterialStats {

    public static final MaterialStatsId ID = new MaterialStatsId("tinkers_ingenuity", "curio_main");

    public static final MaterialStatType<CurioMainMaterialStat> TYPE = new MaterialStatType<>(ID, new CurioMainMaterialStat(
            0.0F, 0.0F, 0.0F, 0.0F, 0.0F),RecordLoadable.create(
                    FloatLoadable.ANY.defaultField("curio_movement_speed", 0.0F, true, CurioMainMaterialStat::curio_movement_speed),
                    FloatLoadable.ANY.defaultField("curio_max_health", 0.0F, true, CurioMainMaterialStat::curio_max_health),
                    FloatLoadable.ANY.defaultField("curio_armor", 0.0F, true, CurioMainMaterialStat::curio_armor),
                    FloatLoadable.ANY.defaultField("curio_melee_attack", 0.0F, true, CurioMainMaterialStat::curio_melee_attack),
                    FloatLoadable.ANY.defaultField("curio_projectile_attack", 0.0F, true, CurioMainMaterialStat::curio_projectile_attack),
                    CurioMainMaterialStat::new));

    private static final String MOVEMENT_SPEED_BONUS_PREFIX = IMaterialStats.makeTooltipKey(TinkersIngenuity.loc("curio_movement_speed"));
    private static final String MAX_HEALTH_BONUS_PREFIX = IMaterialStats.makeTooltipKey(TinkersIngenuity.loc("curio_max_health"));;
    private static final String ARMOR_BONUS_PREFIX = IMaterialStats.makeTooltipKey(TinkersIngenuity.loc("curio_armor"));
    private static final String MELEE_ATTACK_BONUS_PREFIX = IMaterialStats.makeTooltipKey(TinkersIngenuity.loc("curio_melee_attack"));
    private static final String PROJECTILE_ATTACK_BONUS_PREFI = IMaterialStats.makeTooltipKey(TinkersIngenuity.loc("curio_projectile_attack"));

    private static final List<Component> DESCRIPTION = ImmutableList.of(IMaterialStats.makeTooltip(TinkersIngenuity.loc("curio_movement_speed.description")),
            IMaterialStats.makeTooltip(TinkersIngenuity.loc("curio_max_health.description")),
            IMaterialStats.makeTooltip(TinkersIngenuity.loc("curio_armor.description")),
            IMaterialStats.makeTooltip(TinkersIngenuity.loc("curio_melee_attack.description")),
            IMaterialStats.makeTooltip(TinkersIngenuity.loc("curio_projectile_attack.description")));


    @Override
    public MaterialStatType<?> getType() {
        return TYPE;
    }

    @Override
    public MaterialStatsId getIdentifier() {
        return ID;
    }

    @Override
    public List<Component> getLocalizedInfo() {
        List<Component> info = new ArrayList();
        info.add(IToolStat.formatColoredPercentBoost(MOVEMENT_SPEED_BONUS_PREFIX, this.curio_movement_speed));
        info.add(IToolStat.formatColoredBonus(MAX_HEALTH_BONUS_PREFIX, this.curio_max_health));
        info.add(IToolStat.formatColoredPercentBoost(ARMOR_BONUS_PREFIX, this.curio_armor));
        info.add(IToolStat.formatColoredPercentBoost(MELEE_ATTACK_BONUS_PREFIX, this.curio_melee_attack));
        info.add(IToolStat.formatColoredPercentBoost(PROJECTILE_ATTACK_BONUS_PREFI, this.curio_projectile_attack));
        return info;
    }

    @Override
    public List<Component> getLocalizedDescriptions() {
        return DESCRIPTION;
    }

    @Override
    public void apply(ModifierStatsBuilder builder, float scale) {
        TIToolStats.CURIO_MOVEMENT_SPEED.update(builder, this.curio_movement_speed * scale);
        TIToolStats.CURIO_MAX_HEALTH.update(builder, this.curio_max_health * scale);
        TIToolStats.CURIO_ARMOR.update(builder, this.curio_armor * scale);
        TIToolStats.CURIO_MELEE_ATTACK.update(builder, this.curio_melee_attack * scale);
        TIToolStats.CURIO_PROJECTILE_ATTACK.update(builder, this.curio_projectile_attack * scale);
    }

    @Override
    public int durability() {
        return 0;
    }
}
