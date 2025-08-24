package com.xiaoyue.tinkers_ingenuity.content.effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.tools.modifiers.effect.NoMilkEffect;

public class DeadlyPlague extends NoMilkEffect {
    public DeadlyPlague() {
        super(MobEffectCategory.HARMFUL, -12619444, true);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int pAmplifier) {
        if (!entity.isDeadOrDying()) {
            entity.hurt(entity.damageSources().magic(), entity.getHealth() * 0.01f * pAmplifier);
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration % 20 == 0;
    }
}
