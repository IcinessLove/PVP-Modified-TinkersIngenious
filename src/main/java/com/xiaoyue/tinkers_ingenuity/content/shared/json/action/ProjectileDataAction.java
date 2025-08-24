package com.xiaoyue.tinkers_ingenuity.content.shared.json.action;

import com.xiaoyue.tinkers_ingenuity.content.shared.json.variable.LevelingFormula;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import slimeknights.mantle.data.loadable.primitive.EnumLoadable;

public enum ProjectileDataAction {
    modify_damage {
        @Override
        public void apply(Projectile proj, LevelingFormula formula, int level) {
            if (proj instanceof AbstractArrow arrow) {
                arrow.setBaseDamage(formula.apply((float)arrow.getBaseDamage(), level));
            }
        }
    },
    scale_velocity {
        @Override
        public void apply(Projectile proj, LevelingFormula formula, int level) {
            float x = formula.apply((float)proj.getDeltaMovement().x(), level);
            float y = formula.apply((float)proj.getDeltaMovement().y(), level);
            float z = formula.apply((float)proj.getDeltaMovement().z(), level);
            proj.setDeltaMovement(new Vec3(x, y, z));
        }
    };

    public static final EnumLoadable<ProjectileDataAction> LOADER = new EnumLoadable<>(ProjectileDataAction.class);

    public abstract void apply(Projectile proj, LevelingFormula formula, int level);
}
