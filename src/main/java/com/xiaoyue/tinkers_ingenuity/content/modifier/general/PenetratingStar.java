package com.xiaoyue.tinkers_ingenuity.content.modifier.general;

import com.xiaoyue.tinkers_ingenuity.content.generic.SimpleModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class PenetratingStar extends SimpleModifier implements MeleeDamageModifierHook, ProjectileHitModifierHook {

    // 匠魂保护类型ID
    private static final ResourceLocation MELEE_PROTECTION_ID = new ResourceLocation("tconstruct", "melee_protection");
    private static final ResourceLocation PROJECTILE_PROTECTION_ID = new ResourceLocation("tconstruct", "projectile_protection");

    @Override
    protected void addHooks(ModuleHookMap.Builder builder) {
        builder.addHook(this, ModifierHooks.MELEE_DAMAGE);
        builder.addHook(this, ModifierHooks.PROJECTILE_HIT);
    }

    // 使用ToolStack检测保护层数
    private int getTinkerProtectionLevel(LivingEntity entity, ResourceLocation protectionId) {
        int total = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.ARMOR) continue;
            
            ItemStack armor = entity.getItemBySlot(slot);
            if (armor.isEmpty()) continue;
            
            // 使用ToolStack的静态方法检查是否已初始化
            if (ToolStack.isInitialized(armor)) {
                ToolStack tool = ToolStack.from(armor);
                for (ModifierEntry entry : tool.getModifiers().getModifiers()) {
                    if (entry.getModifier().getId().equals(protectionId)) {
                        total += entry.getLevel();
                    }
                }
            }
        }
        return total;
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
        if (attacker != null && target != null) {
            // 获取所有保护类型
            int vanillaProtection = getVanillaProtectionLevel(target);
            int tinkerMeleeProtection = getTinkerProtectionLevel(target, MELEE_PROTECTION_ID);
            
            // 合并保护等级（匠魂每层相当于原版2层）
            int total = vanillaProtection + tinkerMeleeProtection * 2;
            total = Math.min(20, total);
            
            // 计算无视效果
            int penetration = modifier.getLevel() * 2; // 每级词条无视2层保护
            int effectiveProtection = Math.max(0, total - penetration);
            
            // 防止分母为0
            int denominator = 25 - total;
            if (denominator <= 0) denominator = 1;
            
            // 计算伤害比例
            float ratio = (25 - effectiveProtection) / (float) denominator;
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
            int tinkerProjectileProtection = getTinkerProtectionLevel(target, PROJECTILE_PROTECTION_ID);
            
            // 合并保护等级（匠魂每层相当于原版2层）
            int total = vanillaProtection + vanillaProjectileProtection + tinkerProjectileProtection * 2;
            total = Math.min(20, total);
            
            // 计算无视效果
            int penetration = modifier.getLevel() * 2; // 每级词条无视2层保护
            int effectiveProtection = Math.max(0, total - penetration);
            
            // 防止分母为0
            int denominator = 25 - total;
            if (denominator <= 0) denominator = 1;
            
            // 计算伤害比例
            float ratio = (25 - effectiveProtection) / (float) denominator;
            arrow.setBaseDamage(arrow.getBaseDamage() * ratio);
        }
        return false;
    }
}