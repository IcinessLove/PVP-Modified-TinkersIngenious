package com.xiaoyue.tinkers_ingenuity.content.modifier.defense;

import com.xiaoyue.tinkers_ingenuity.content.generic.SimpleModifier;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.defense.LivingEventModifierHook;
import com.xiaoyue.tinkers_ingenuity.register.TIHooks;
import com.xiaoyue.tinkers_ingenuity.utils.IngenuityUtils;
import com.xiaoyue.tinkers_ingenuity.utils.TinkerUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.DamageBlockModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.DurabilityDisplayModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.DurabilityShieldModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;

public class PastMemories extends SimpleModifier implements ModifyDamageModifierHook {


    @Override
    protected void addHooks(ModuleHookMap.Builder builder) {
        builder.addHook(this, ModifierHooks.MODIFY_DAMAGE);
    }

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slot, DamageSource source, float amount, boolean b) {
        if(context.getEntity() instanceof Player player && !player.isCreative()) {
            int level = modifier.getLevel();
            if (!player.getCooldowns().isOnCooldown(tool.getItem()) && !source.is(DamageTypes.FELL_OUT_OF_WORLD)) {
                    player.getCooldowns().addCooldown(tool.getItem(), 200);
                    return Math.max(0F,amount - (level * 2.0F));
            }
        }
        return amount;
    }
}
