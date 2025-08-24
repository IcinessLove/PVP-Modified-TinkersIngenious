package com.xiaoyue.tinkers_ingenuity.content.shared.holder;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.function.Consumer;

public record EquipmentHolder(EquipmentContext context, EquipmentSlot slot) {

    public IToolStackView getTool() {
        return this.context.getToolInSlot(this.slot);
    }

    public LivingEntity getUser() {
        return this.context.getEntity();
    }

    public boolean checkTool() {
        return this.getTool() != null && !this.getTool().isBroken();
    }

    public void postTrait(Consumer<ModifierEntry> cons) {
        if (this.checkTool()) {
            for(ModifierEntry entry : this.getTool().getModifierList()) {
                cons.accept(entry);
            }

        }
    }
}
