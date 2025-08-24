package com.xiaoyue.tinkers_ingenuity.register;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.shared.stats.CurioMainMaterialStat;
import com.xiaoyue.tinkers_ingenuity.content.shared.stats.TIExtraMaterialStat;
import slimeknights.tconstruct.library.materials.IMaterialRegistry;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;

public class TIMaterialStats {

    public static final MaterialStatsId CURIO = new MaterialStatsId(TinkersIngenuity.loc("curio"));

    public static void register() {
        IMaterialRegistry instance = MaterialRegistry.getInstance();
        instance.registerStatType(CurioMainMaterialStat.TYPE, CURIO);
        instance.registerStatType(TIExtraMaterialStat.CURIO_EXTRA.getType(), CURIO);
    }
}
