package com.xiaoyue.tinkers_ingenuity.content.shared.material;

import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.tools.modules.ArmorModuleBuilder;

import javax.annotation.Nullable;

public record MaterialStatsData(boolean shield, @Nullable ArmorModuleBuilder.ArmorShieldModuleBuilder<? extends IMaterialStats> armor, IMaterialStats... stats) {
}
