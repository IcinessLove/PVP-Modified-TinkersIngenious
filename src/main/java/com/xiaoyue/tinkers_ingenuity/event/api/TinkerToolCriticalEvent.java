package com.xiaoyue.tinkers_ingenuity.event.api;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Event;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class TinkerToolCriticalEvent extends Event {

    private final IToolStackView tool;
    private final LivingEntity attacker;
    private final Entity target;
    private final InteractionHand hand;
    private final boolean isExtraAttack;
    private boolean isCritical;

    public TinkerToolCriticalEvent(IToolStackView tool, LivingEntity attacker, Entity target, InteractionHand hand, boolean isExtraAttack, boolean isCritical) {
        this.tool = tool;
        this.attacker = attacker;
        this.target = target;
        this.hand = hand;
        this.isExtraAttack = isExtraAttack;
        this.isCritical = isCritical;
    }

    public IToolStackView getTool() {
        return this.tool;
    }

    public LivingEntity getAttacker() {
        return this.attacker;
    }

    public Entity getTarget() {
        return this.target;
    }

    public InteractionHand getHand() {
        return this.hand;
    }

    public boolean isExtraAttack() {
        return this.isExtraAttack;
    }

    public boolean isCritical() {
        return this.isCritical;
    }

    public void setCritical(boolean critical) {
        this.isCritical = critical;
    }
}
