package com.xiaoyue.tinkers_ingenuity.utils;

import dev.xkmc.l2library.util.tools.TeleportTool;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class IngenuityUtils {
    public static void teleportHome(LivingEntity entity) {
        Level level = entity.level();
        if (level instanceof ServerLevel sl) {
            if (entity instanceof ServerPlayer sp) {
                TeleportTool.teleportHome(sl, sp);
            }
        }

    }
}
