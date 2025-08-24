package com.xiaoyue.tinkers_ingenuity.content.module.combat;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.generic.ISimpleModule;
import com.xiaoyue.tinkers_ingenuity.content.generic.SerialLoader;
import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.l2library.base.effects.EffectUtil.AddReason;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.data.loadable.Loadables;
import slimeknights.mantle.data.loadable.primitive.IntLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.registry.GenericLoaderRegistry;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import java.util.List;

public record ForceAddEffectModule(MobEffect effect, int level, int time)
        implements ISimpleModule, MeleeHitModifierHook, ProjectileHitModifierHook {

    public static final RecordLoadable<ForceAddEffectModule> LOADER = RecordLoadable.create(
            Loadables.MOB_EFFECT.requiredField("effect", ForceAddEffectModule::effect),
            IntLoadable.ANY_FULL.requiredField("level", ForceAddEffectModule::level),
            IntLoadable.ANY_FULL.requiredField("time", ForceAddEffectModule::time),
            ForceAddEffectModule::new
    );

    @SerialLoader
    public static void onInit() {
        TinkersIngenuity.REGISTRATE.module("force_add_effect", LOADER);
    }

    public void addEffect(LivingEntity target, @Nullable Entity attacker) {
        MobEffectInstance ins = new MobEffectInstance(this.effect, this.time, this.level - 1);
        EffectUtil.addEffect(target, ins, AddReason.FORCE, attacker);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (context.getLivingTarget() != null) {
            this.addEffect(context.getLivingTarget(), context.getAttacker());
        }
    }

    @Override
    public void failedMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageAttempted) {
        if (context.getLivingTarget() != null) {
            this.addEffect(context.getLivingTarget(), context.getAttacker());
        }
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if (target != null) {
            this.addEffect(target, attacker);
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

    public static ForceAddEffectModule get(MobEffect effect, int level, int time) {
        return new ForceAddEffectModule(effect, level, time);
    }
}
