package com.xiaoyue.tinkers_ingenuity.register;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;

public class TIToolStats {

    public static final FloatToolStat CURIO_MOVEMENT_SPEED;
    public static final FloatToolStat CURIO_MELEE_ATTACK;
    public static final FloatToolStat CURIO_PROJECTILE_ATTACK;
    public static final FloatToolStat CURIO_MAX_HEALTH;
    public static final FloatToolStat CURIO_ARMOR;

    static {
        CURIO_MOVEMENT_SPEED = TinkersIngenuity.REGISTRATE.floatToolStat("curio_movement_speed",
                -8871731, 0.0F, 0.0F, 2048.0F);
        CURIO_MELEE_ATTACK = TinkersIngenuity.REGISTRATE.floatToolStat("curio_melee_attack",
                -2661276, 0.0F, 0.0F, 4096.0F);
        CURIO_PROJECTILE_ATTACK = TinkersIngenuity.REGISTRATE.floatToolStat("curio_projectile_attack",
                -242829, 0.0F, 0.0F, 2048.0F);
        CURIO_MAX_HEALTH = TinkersIngenuity.REGISTRATE.floatToolStat("curio_max_health",
                -242829, 0.0F, 0.0F, 2048.0F);
        CURIO_ARMOR = TinkersIngenuity.REGISTRATE.floatToolStat("curio_armor",
                -8042548, 0.0F, 0.0F, 2048.0F);
    }

    public static void register() {
    }
}
