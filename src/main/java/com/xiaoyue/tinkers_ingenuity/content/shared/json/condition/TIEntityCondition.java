package com.xiaoyue.tinkers_ingenuity.content.shared.json.condition;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.generic.SerialLoader;
import net.minecraft.world.entity.player.Player;
import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;

public class TIEntityCondition {

    public static final LivingEntityPredicate FULL_HEALTH = LivingEntityPredicate.simple(e ->
            e.getHealth() == e.getMaxHealth()
    );

    public static final LivingEntityPredicate FULL_CHARGED = LivingEntityPredicate.simple(e -> {
        if (e instanceof Player p) {
            return p.getAttackStrengthScale(0.5F) > 0.9F;
        }
        return false;
    });

    @SerialLoader
    public static void registerPredicate() {
        LivingEntityPredicate.LOADER.register(TinkersIngenuity.loc("full_health"), FULL_HEALTH.getLoader());
        LivingEntityPredicate.LOADER.register(TinkersIngenuity.loc("full_charged"), FULL_CHARGED.getLoader());
    }
}
