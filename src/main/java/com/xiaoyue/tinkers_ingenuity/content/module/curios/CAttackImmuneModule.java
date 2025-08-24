package com.xiaoyue.tinkers_ingenuity.content.module.curios;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.generic.ISimpleModule;
import com.xiaoyue.tinkers_ingenuity.content.generic.SerialLoader;
import com.xiaoyue.tinkers_ingenuity.content.shared.holder.CurioStackView;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.specail.TinkersCurioModifierHook;
import com.xiaoyue.tinkers_ingenuity.content.shared.json.action.LivingEntityAction;
import com.xiaoyue.tinkers_ingenuity.register.TIHooks;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.mantle.data.loadable.primitive.DoubleLoadable;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.data.predicate.damage.DamageSourcePredicate;
import slimeknights.mantle.data.registry.GenericLoaderRegistry;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;

import javax.annotation.Nullable;
import java.util.List;

public record CAttackImmuneModule(IJsonPredicate<DamageSource> source, double chance, @Nullable LivingEntityAction action, float actionAmount)
        implements ISimpleModule, TinkersCurioModifierHook {

    public static final RecordLoadable<CAttackImmuneModule> LOADER = RecordLoadable.create(
            DamageSourcePredicate.LOADER.requiredField("source", CAttackImmuneModule::source),
            DoubleLoadable.ANY.requiredField("chance", CAttackImmuneModule::chance),
            LivingEntityAction.LOADER.nullableField("action", CAttackImmuneModule::action),
            FloatLoadable.ANY.defaultField("action_amount", 0f, CAttackImmuneModule::actionAmount),
            CAttackImmuneModule::new
    );

    @SerialLoader
    public static void onInit() {
        TinkersIngenuity.REGISTRATE.module("curio_attack_immune", LOADER);
    }

    @Override
    public boolean canImmuneAttack(CurioStackView curio, int level, LivingEntity entity, DamageSource source, float damage) {
        if (this.source.matches(source) && chance(level * this.chance)) {
            if (action != null) {
                action.apply(entity, actionAmount);
            }
            return true;
        }
        return false;
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(TIHooks.TINKERS_CURIO);
    }

    @Override
    public RecordLoadable<? extends GenericLoaderRegistry.IHaveLoader> getLoader() {
        return LOADER;
    }

    public static CAttackImmuneModule get(IJsonPredicate<DamageSource> source, double chance, @Nullable LivingEntityAction action, float actionAmount) {
        return new CAttackImmuneModule(source, chance, action, actionAmount);
    }
}
