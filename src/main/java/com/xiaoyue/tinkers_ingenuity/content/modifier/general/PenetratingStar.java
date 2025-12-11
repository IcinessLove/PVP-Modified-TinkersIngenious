package com.xiaoyue.tinkers_ingenuity.content.modifier.general;

import com.xiaoyue.tinkers_ingenuity.content.generic.SimpleModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ProtectionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;

public class PenetratingStar extends SimpleModifier implements MeleeDamageModifierHook, ProjectileHitModifierHook {

    @Override
    protected void addHooks(ModuleHookMap.Builder builder) {
        builder.addHook(this, ModifierHooks.MELEE_DAMAGE);
        builder.addHook(this, ModifierHooks.PROJECTILE_HIT);
    }

    private float computeTinkerProtection(LivingEntity entity, DamageSource source) {
    EquipmentContext context = new EquipmentContext(entity);
    float protection = 0f;

    for (EquipmentSlot slot : EquipmentSlot.values()) {
        if (slot.getType() != EquipmentSlot.Type.ARMOR) continue;

        IToolStackView tool = context.getToolInSlot(slot);
        if (tool == null) continue;

        for (ModifierEntry entry : tool.getModifierList()) {

            ProtectionModifierHook hook = entry.getHook(ModifierHooks.PROTECTION);
            if (hook != null) {
                protection = hook.getProtectionModifier(tool, entry, context, slot, source, protection);
            }
        }
    }

    return protection;
}

    // 获取原版保护等级
    private int getVanillaProtectionLevel(LivingEntity entity) {
        int total = 0;
        for (ItemStack armor : entity.getArmorSlots()) {
            total += EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION, armor);
        }
        return total;
    }

    // 获取原版弹射物保护等级
    private int getVanillaProjectileProtectionLevel(LivingEntity entity) {
        int total = 0;
        for (ItemStack armor : entity.getArmorSlots()) {
            total += EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PROJECTILE_PROTECTION, armor) * 2;
        }
        return total;
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity attacker = context.getPlayerAttacker();
        LivingEntity target = context.getLivingTarget();
        Level world = attacker.getCommandSenderWorld();
        if (attacker != null && target != null) {
            // 获取所有保护类型
            int vanillaProtection = getVanillaProtectionLevel(target);
            float tinkerMeleeProtection = computeTinkerProtection(target, new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.PLAYER_ATTACK)));
            double protectionCap = ProtectionModifierHook.getProtectionCap(target);
            
            // 合并保护等级（匠魂每层相当于原版2层）
            double total = vanillaProtection + tinkerMeleeProtection;
            total = Math.min(protectionCap, total);
            
            // 计算无视效果
            int penetration = modifier.getLevel() * 2; // 每级词条无视2层保护
            double effectiveProtection = Math.max(0, total - penetration);
            
            // 防止分母为0
            double denominator = 25 - total;
            if (denominator <= 0) denominator = 1;
            
            // 计算伤害比例
            float ratio = (25 - (float)effectiveProtection) / (float) denominator;
            return damage * ratio;
        }
        return damage;
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if (projectile instanceof AbstractArrow arrow && target != null && attacker != null) {
            // 获取所有保护类型
            int vanillaProtection = getVanillaProtectionLevel(target);
            int vanillaProjectileProtection = getVanillaProjectileProtectionLevel(target);
            float tinkerProjectileProtection = computeTinkerProtection(target, projectile.damageSources().arrow(arrow, attacker));
            double protectionCap = ProtectionModifierHook.getProtectionCap(target);
            
            // 合并保护等级（匠魂每层相当于原版2层）
            double total = vanillaProtection + vanillaProjectileProtection + tinkerProjectileProtection;
            total = Math.min(protectionCap, total);
            
            // 计算无视效果
            int penetration = modifier.getLevel() * 2; // 每级词条无视2层保护
            double effectiveProtection = Math.max(0, total - penetration);
            
            // 防止分母为0
            double denominator = 25 - total;
            if (denominator <= 0) denominator = 1;
            
            // 计算伤害比例
            float ratio = (25 - (float)effectiveProtection) / (float) denominator;
            arrow.setBaseDamage(arrow.getBaseDamage() * ratio);
        }
        return false;
    }
}