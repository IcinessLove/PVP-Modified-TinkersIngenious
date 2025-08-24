package com.xiaoyue.tinkers_ingenuity.content.shared.hooks.attack;

import com.xiaoyue.tinkers_ingenuity.register.TIHooks;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface SweepEdgeModifierHook {

    SweepEdgeModifierHook EMPTY = new SweepEdgeModifierHook() {
    };

    default float getSweepDamage(IToolStackView tool, int level, ToolAttackContext context, float damage) {
        return damage;
    }

    static float post(IToolStackView tool, ToolAttackContext context, float damage) {
        for (ModifierEntry e : tool.getModifierList()) {
            return e.getHook(TIHooks.SWEEP_EDGE).getSweepDamage(tool, e.getLevel(), context, damage);
        }
        return damage;
    }

    record AllMerger(Collection<SweepEdgeModifierHook> modules) implements SweepEdgeModifierHook {
        @Override
        public float getSweepDamage(IToolStackView tool, int level, ToolAttackContext context, float damage) {
            for (SweepEdgeModifierHook module : modules) {
                return module.getSweepDamage(tool, level, context, damage);
            }
            return damage;
        }
    }
}
