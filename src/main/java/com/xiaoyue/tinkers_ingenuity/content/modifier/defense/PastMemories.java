package com.xiaoyue.tinkers_ingenuity.content.modifier.defense;

import com.xiaoyue.tinkers_ingenuity.content.generic.SimpleModifier;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.defense.LivingEventModifierHook;
import com.xiaoyue.tinkers_ingenuity.register.TIHooks;
import com.xiaoyue.tinkers_ingenuity.utils.IngenuityUtils;
import com.xiaoyue.tinkers_ingenuity.utils.TinkerUtils;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class PastMemories extends SimpleModifier implements LivingEventModifierHook {

    @Override
    public boolean isSingleLevel() {
        return true;
    }

    @Override
    protected void addHooks(ModuleHookMap.Builder builder) {
        builder.addHook(this, TIHooks.LIVING_EVENT);
    }

    @Override
    public void onDeath(IToolStackView tool, ModifierEntry modifier, LivingDeathEvent event, EquipmentContext context, EquipmentSlot slot) {
        LivingEntity entity = context.getEntity();
        ToolStack slotTool = TinkerUtils.getReciprocalSlotArmor(entity);
        if (slotTool != null) {
            if (this.noCD(slotTool, entity) && this.hasThis(slotTool)) {
                event.setCanceled(true);
                entity.setHealth(5.0F);
                IngenuityUtils.teleportHome(entity);
                this.addCD(slotTool, entity, 6000);
            }

        }
    }
}
