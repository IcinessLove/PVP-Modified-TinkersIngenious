package com.xiaoyue.tinkers_ingenuity.content.modifier.general;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.generic.ISimpleModule;
import com.xiaoyue.tinkers_ingenuity.content.generic.SerialLoader;
import com.xiaoyue.tinkers_ingenuity.content.shared.json.variable.LevelingFormula;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.data.loadable.primitive.IntLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.registry.GenericLoaderRegistry;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileLaunchModifierHook;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import java.util.List;

public record Chivalry(LevelingFormula bonus, int maxMultiplier)
        implements ISimpleModule, MeleeHitModifierHook, MeleeDamageModifierHook, ProjectileHitModifierHook, ProjectileLaunchModifierHook {
            
    public static final RecordLoadable<Chivalry> LOADER = RecordLoadable.create(
            LevelingFormula.LOADER.requiredField("bonus", Chivalry::bonus),
            IntLoadable.ANY_FULL.requiredField("max_multiplier", Chivalry::maxMultiplier),
            Chivalry::new
    );

    @SerialLoader
    public static void onInit() {
        TinkersIngenuity.REGISTRATE.module("chivalry_modifier", LOADER);
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        if (context.getAttacker() instanceof Player player) {
            if (context.isCritical() && context.isFullyCharged()) {
                damage = this.bonus.apply(damage, modifier, player.getAbsorptionAmount()*0.5f);
                player.setAbsorptionAmount(0.0f);
            }
        }
        return damage;
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (context.getAttacker() instanceof Player player && context.getCooldown()>= 0.9f) {
            if (player.getAbsorptionAmount() < player.getMaxHealth() * (float) this.maxMultiplier) {
                float maxAbs = Math.min(4*modifier.getLevel(), player.getAbsorptionAmount() + damageDealt*0.25f);
                player.setAbsorptionAmount(maxAbs);
            }
        }
    }

    @Override
    public void onProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity entity, Projectile projectile, @Nullable AbstractArrow arrow, ModDataNBT persistentData, boolean primary) {
        if (entity instanceof Player player) {
            if (arrow != null && arrow.isCritArrow()) {
                arrow.setBaseDamage(this.bonus.apply((float) arrow.getBaseDamage(), modifier, player.getAbsorptionAmount()*0.5f));
                player.setAbsorptionAmount(0.0f);
            }
        }
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if (attacker instanceof Player player) {
            if (player.getAbsorptionAmount() < player.getMaxHealth() * (float) this.maxMultiplier) {
                float maxAbs = Math.min(4*modifier.getLevel(), player.getAbsorptionAmount() + (float) modifier.getLevel());
                player.setAbsorptionAmount(maxAbs);
            }
        }
        return false;
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(ModifierHooks.MELEE_HIT, ModifierHooks.MELEE_DAMAGE, ModifierHooks.PROJECTILE_HIT, ModifierHooks.PROJECTILE_LAUNCH);
    }

    @Override
    public RecordLoadable<? extends GenericLoaderRegistry.IHaveLoader> getLoader() {
        return LOADER;
    }

    public static Chivalry getIns() {
        return new Chivalry(LevelingFormula.mulBase(0.03f), 1);
    }
}
