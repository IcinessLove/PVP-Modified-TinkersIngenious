package com.xiaoyue.tinkers_ingenuity.content.shared.json.action;

import net.minecraft.world.entity.LivingEntity;
import slimeknights.mantle.data.loadable.primitive.EnumLoadable;

public enum LivingEntityAction {
    heal_entity {
        @Override
        public void apply(LivingEntity entity, float amount) {
            entity.heal(amount);
        }
    };

    public static final EnumLoadable<LivingEntityAction> LOADER = new EnumLoadable<>(LivingEntityAction.class);

    public abstract void apply(LivingEntity entity, float amount);
}
