package com.xiaoyue.tinkers_ingenuity.content.module.combat;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.generic.ISimpleModule;
import com.xiaoyue.tinkers_ingenuity.content.generic.SerialLoader;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.attack.GenericCombatModifierHook;
import com.xiaoyue.tinkers_ingenuity.event.api.TinkerToolCriticalEvent;
import com.xiaoyue.tinkers_ingenuity.register.TIHooks;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.data.loadable.primitive.DoubleLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;
import slimeknights.mantle.data.registry.GenericLoaderRegistry;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileLaunchModifierHook;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import java.util.List;

public record CriticalChanceModule(IJsonPredicate<LivingEntity> attacker, double chance)
        implements ISimpleModule, GenericCombatModifierHook, ProjectileLaunchModifierHook {

    public static final RecordLoadable<CriticalChanceModule> LOADER = RecordLoadable.create(
            LivingEntityPredicate.LOADER.defaultField("attacker", LivingEntityPredicate.ANY, CriticalChanceModule::attacker),
            DoubleLoadable.ANY.defaultField("chance", 1.0, CriticalChanceModule::chance),
            CriticalChanceModule::new
    );

    @SerialLoader
    public static void onInit() {
        TinkersIngenuity.REGISTRATE.module("critical_chance", LOADER);
    }

    @Override
    public void onCriticalHit(IToolStackView tool, ModifierEntry modifier, LivingEntity attacker, TinkerToolCriticalEvent event) {
        if (this.test(attacker, this.attacker) && this.chance(this.chance)) {
            event.setCritical(true);
        }
    }

    @Override
    public void onProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity entity, Projectile projectile, @Nullable AbstractArrow arrow, ModDataNBT modDataNBT, boolean primary) {
        if (this.test(entity, this.attacker) && this.chance(this.chance) && arrow != null) {
            arrow.setCritArrow(true);
        }
    }

    @Override
    public RecordLoadable<? extends GenericLoaderRegistry.IHaveLoader> getLoader() {
        return LOADER;
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(TIHooks.GENERIC_COMBAT, ModifierHooks.PROJECTILE_LAUNCH);
    }

    public static CriticalChanceModule any(double chance) {
        return new CriticalChanceModule(LivingEntityPredicate.ANY, chance);
    }

    public static CriticalChanceModule get(IJsonPredicate<LivingEntity> attacker, double chance) {
        return new CriticalChanceModule(attacker, chance);
    }
}
