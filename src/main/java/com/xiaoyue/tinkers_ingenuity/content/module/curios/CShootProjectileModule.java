package com.xiaoyue.tinkers_ingenuity.content.module.curios;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.generic.ISimpleModule;
import com.xiaoyue.tinkers_ingenuity.content.generic.SerialLoader;
import com.xiaoyue.tinkers_ingenuity.content.shared.holder.CurioStackView;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.specail.TinkersCurioModifierHook;
import com.xiaoyue.tinkers_ingenuity.content.shared.json.action.ProjectileDataAction;
import com.xiaoyue.tinkers_ingenuity.content.shared.json.variable.LevelingFormula;
import com.xiaoyue.tinkers_ingenuity.register.TIHooks;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;
import slimeknights.mantle.data.registry.GenericLoaderRegistry;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import java.util.List;

public record CShootProjectileModule(IJsonPredicate<LivingEntity> shooter, ProjectileDataAction action, LevelingFormula modifier)
        implements ISimpleModule, TinkersCurioModifierHook {

    public static final RecordLoadable<CShootProjectileModule> LOADER = RecordLoadable.create(
            LivingEntityPredicate.LOADER.requiredField("shooter", CShootProjectileModule::shooter),
            ProjectileDataAction.LOADER.requiredField("action", CShootProjectileModule::action),
            LevelingFormula.LOADER.requiredField("modifier", CShootProjectileModule::modifier),
            CShootProjectileModule::new
    );

    @SerialLoader
    public static void onInit() {
        TinkersIngenuity.REGISTRATE.module("curio_shoot_projctile", LOADER);
    }

    @Override
    public void onShootProjectile(CurioStackView curio, int level, LivingEntity shooter, Projectile proj, @Nullable AbstractArrow arrow, ModDataNBT nbt) {
        if (this.test(shooter, this.shooter)) {
            this.action.apply(proj, this.modifier, level);
        }
    }

    @Override
    public RecordLoadable<? extends GenericLoaderRegistry.IHaveLoader> getLoader() {
        return LOADER;
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(TIHooks.TINKERS_CURIO);
    }

    public static CShootProjectileModule any(ProjectileDataAction action, LevelingFormula modifier) {
        return new CShootProjectileModule(LivingEntityPredicate.ANY, action, modifier);
    }

    public static CShootProjectileModule get(IJsonPredicate<LivingEntity> shooter, ProjectileDataAction action, LevelingFormula modifier) {
        return new CShootProjectileModule(shooter, action, modifier);
    }
}
