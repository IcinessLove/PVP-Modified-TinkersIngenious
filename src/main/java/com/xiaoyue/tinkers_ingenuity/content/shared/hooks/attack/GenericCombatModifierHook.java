package com.xiaoyue.tinkers_ingenuity.content.shared.hooks.attack;

import com.xiaoyue.tinkers_ingenuity.event.api.TinkerToolCriticalEvent;
import com.xiaoyue.tinkers_ingenuity.register.TIHooks;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface GenericCombatModifierHook {

    GenericCombatModifierHook EMPTY = new GenericCombatModifierHook() {
    };

    default void onCreateSource(IToolStackView tool, ModifierEntry modifier, LivingEntity attacker, CreateSourceEvent event) {
    }

    default void onCriticalHit(IToolStackView tool, ModifierEntry modifier, LivingEntity attacker, TinkerToolCriticalEvent event) {
    }

    static void postCreateSource(IToolStackView tool, CreateSourceEvent event) {
        for(ModifierEntry e : tool.getModifierList()) {
            e.getHook(TIHooks.GENERIC_COMBAT).onCreateSource(tool, e, event.getAttacker(), event);
        }
    }

    static void postCritHit(IToolStackView tool, TinkerToolCriticalEvent event) {
        for(ModifierEntry e : tool.getModifierList()) {
            e.getHook(TIHooks.GENERIC_COMBAT).onCriticalHit(tool, e, event.getAttacker(), event);
        }
    }

    record AllMerger(Collection<GenericCombatModifierHook> modules) implements GenericCombatModifierHook {
        @Override
        public void onCreateSource(IToolStackView tool, ModifierEntry modifier, LivingEntity attacker, CreateSourceEvent event) {
            for(GenericCombatModifierHook module : this.modules) {
                module.onCreateSource(tool, modifier, attacker, event);
            }
        }

        @Override
        public void onCriticalHit(IToolStackView tool, ModifierEntry modifier, LivingEntity attacker, TinkerToolCriticalEvent event) {
            for(GenericCombatModifierHook module : this.modules) {
                module.onCriticalHit(tool, modifier, attacker, event);
            }

        }
    }
}
