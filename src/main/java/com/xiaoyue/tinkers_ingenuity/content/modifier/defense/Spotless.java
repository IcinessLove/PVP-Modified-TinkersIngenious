package com.xiaoyue.tinkers_ingenuity.content.modifier.defense;
import java.util.ArrayList;
import java.util.List;
import com.xiaoyue.tinkers_ingenuity.content.generic.SimpleModifier;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.function.Predicate;

public class Spotless extends SimpleModifier implements InventoryTickModifierHook {
    
    @Override
    public boolean isSingleLevel() {
        return false;
    }

    @Override
    protected void addHooks(ModuleHookMap.Builder builder) {
        builder.addHook(this, ModifierHooks.INVENTORY_TICK);
    }
    
    private int cooldown = 0;

    @Override
public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity entity, int index, boolean select, boolean current, ItemStack stack) {
    if (!current || level.isClientSide) return;
    
    if (cooldown > 0) {
        cooldown--;
        return;
    }
    
    boolean removedAny = false;
    
    // 创建一个副本来避免 ConcurrentModificationException
    List<MobEffectInstance> effectsToRemove = new ArrayList<>();
    
    // 首先收集所有需要移除的效果
    for (MobEffectInstance effect : entity.getActiveEffects()) {
        if (effect.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
            effectsToRemove.add(effect);
        }
    }
    
    // 然后移除所有收集到的效果
    for (MobEffectInstance effect : effectsToRemove) {
        entity.removeEffect(effect.getEffect());
        removedAny = true;
    }
    
    if (removedAny) {
        int levelValue = Math.max(1, modifier.getLevel());
        cooldown = 10 + 120 / levelValue; // 等级越高冷却时间越短
    }
}
}
