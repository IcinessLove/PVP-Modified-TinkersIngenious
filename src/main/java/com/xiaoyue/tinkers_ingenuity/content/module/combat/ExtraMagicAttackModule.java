package com.xiaoyue.tinkers_ingenuity.content.module.combat;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.generic.ISimpleModule;
import com.xiaoyue.tinkers_ingenuity.content.generic.SerialLoader;
import com.xiaoyue.tinkers_ingenuity.content.shared.json.variable.LevelingFormula;
import com.xiaoyue.tinkers_ingenuity.data.TIDamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;
import slimeknights.mantle.data.registry.GenericLoaderRegistry;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import java.util.List;

public record ExtraMagicAttackModule(IJsonPredicate<LivingEntity> attacker, IJsonPredicate<LivingEntity> target, LevelingFormula damage)
        implements ISimpleModule, MeleeHitModifierHook, ProjectileHitModifierHook {

    public static final RecordLoadable<ExtraMagicAttackModule> LOADER = RecordLoadable.create(
            LivingEntityPredicate.LOADER.requiredField("attacker", ExtraMagicAttackModule::attacker),
            LivingEntityPredicate.LOADER.requiredField("target", ExtraMagicAttackModule::target),
            LevelingFormula.LOADER.requiredField("damage", ExtraMagicAttackModule::damage),
            ExtraMagicAttackModule::new
    );

    @SerialLoader
    public static void onInit() {
        TinkersIngenuity.REGISTRATE.module("extra_magic_attack", LOADER);
    }

    public void attack(LivingEntity attacker, LivingEntity target, float damage, int lv) {
        float actualDamage = this.damage.apply(damage, lv);
        ToolAttackUtil.attackEntitySecondary(TIDamageTypes.magic(attacker), actualDamage, target, attacker, true);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        LivingEntity livingTarget = context.getLivingTarget();
        if (livingTarget != null) {
            if (this.test(context.getAttacker(), this.attacker) && this.test(livingTarget, this.target)) {
                this.attack(context.getAttacker(), livingTarget, damageDealt, modifier.getLevel());
            }

        }
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if (projectile instanceof AbstractArrow arrow) {
            if (attacker != null && target != null) {
                if (this.test(attacker, this.attacker) && this.test(target, this.target)) {
                    this.attack(attacker, target, (float) arrow.getBaseDamage(), modifier.getLevel());
                }
                return false;
            }
        }
        return false;
    }

    @Override
    public RecordLoadable<? extends GenericLoaderRegistry.IHaveLoader> getLoader() {
        return LOADER;
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(ModifierHooks.MELEE_HIT, ModifierHooks.PROJECTILE_HIT);
    }

    public static ExtraMagicAttackModule any(LevelingFormula formula) {
        return new ExtraMagicAttackModule(LivingEntityPredicate.ANY, LivingEntityPredicate.ANY, formula);
    }

    public static ExtraMagicAttackModule get(IJsonPredicate<LivingEntity> attacker, IJsonPredicate<LivingEntity> target, LevelingFormula formula) {
        return new ExtraMagicAttackModule(attacker, target, formula);
    }
}
