package com.xiaoyue.tinkers_ingenuity.content.shared.hooks.defense;

import com.xiaoyue.tinkers_ingenuity.register.TIHooks;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface LivingEventModifierHook {

    LivingEventModifierHook EMPTY = new LivingEventModifierHook() {
    };

    default void onDeath(IToolStackView tool, ModifierEntry modifier, LivingDeathEvent event, EquipmentContext context, EquipmentSlot slot) {
    }

    static void postDeath(IToolStackView tool, EquipmentContext context, LivingDeathEvent event, EquipmentSlot slot) {
        for(ModifierEntry e : tool.getModifierList()) {
            e.getHook(TIHooks.LIVING_EVENT).onDeath(tool, e, event, context, slot);
        }
    }

    record AllMerger(Collection<LivingEventModifierHook> modules) implements LivingEventModifierHook {
        @Override
        public void onDeath(IToolStackView tool, ModifierEntry modifier, LivingDeathEvent event, EquipmentContext context, EquipmentSlot slot) {
            for(LivingEventModifierHook module : this.modules) {
                module.onDeath(tool, modifier, event, context, slot);
            }

        }
    }
}
