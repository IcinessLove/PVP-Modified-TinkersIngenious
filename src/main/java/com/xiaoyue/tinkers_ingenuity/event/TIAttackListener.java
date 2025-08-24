package com.xiaoyue.tinkers_ingenuity.event;

import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.attack.GenericCombatModifierHook;
import com.xiaoyue.tinkers_ingenuity.utils.TinkerUtils;
import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class TIAttackListener implements AttackListener {

    public void onCreateSource(CreateSourceEvent event) {
        LivingEntity attacker = event.getAttacker();
        ToolStack tool = TinkerUtils.getAttackTool(attacker, event.getDirect());
        if (tool != null) {
            GenericCombatModifierHook.postCreateSource(tool, event);
        }
    }
}
