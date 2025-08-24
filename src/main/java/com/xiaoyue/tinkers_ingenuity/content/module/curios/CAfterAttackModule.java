package com.xiaoyue.tinkers_ingenuity.content.module.curios;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.generic.ISimpleModule;
import com.xiaoyue.tinkers_ingenuity.content.generic.SerialLoader;
import com.xiaoyue.tinkers_ingenuity.content.shared.holder.CurioStackView;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.specail.TinkersCurioModifierHook;
import com.xiaoyue.tinkers_ingenuity.content.shared.json.action.LivingEntityAction;
import com.xiaoyue.tinkers_ingenuity.register.TIHooks;
import dev.xkmc.l2library.init.events.GeneralEventHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import slimeknights.mantle.data.loadable.primitive.BooleanLoadable;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;
import slimeknights.mantle.data.registry.GenericLoaderRegistry;
import slimeknights.tconstruct.library.json.predicate.TinkerPredicate;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;

import java.util.List;

public record CAfterAttackModule(IJsonPredicate<LivingEntity> attacker, IJsonPredicate<LivingEntity> target, LivingEntityAction action, float amount, boolean critBonus)
        implements ISimpleModule, TinkersCurioModifierHook {

    public static final RecordLoadable<CAfterAttackModule> LOADER = RecordLoadable.create(
            LivingEntityPredicate.LOADER.requiredField("attacker", CAfterAttackModule::attacker),
            LivingEntityPredicate.LOADER.requiredField("target", CAfterAttackModule::target),
            LivingEntityAction.LOADER.requiredField("action", CAfterAttackModule::action),
            FloatLoadable.ANY.requiredField("amount", CAfterAttackModule::amount),
            BooleanLoadable.DEFAULT.requiredField("crit_bonus", CAfterAttackModule::critBonus),
            CAfterAttackModule::new
    );

    @SerialLoader
    public static void onInit() {
        TinkersIngenuity.REGISTRATE.module("curio_after_attack", LOADER);
    }

    @Override
    public void onDamageTargetPost(CurioStackView curio, int level, LivingEntity attacker, LivingEntity target, LivingDamageEvent event) {
        if (this.test(attacker, this.attacker) && this.test(target, this.target)) {
            float finalAmount = TinkerPredicate.AIRBORNE.matches(attacker) ? this.amount * 2.0F : this.amount;
            GeneralEventHandler.schedule(() -> this.action.apply(attacker, finalAmount * (float)level));
        }

    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(TIHooks.TINKERS_CURIO);
    }

    @Override
    public RecordLoadable<? extends GenericLoaderRegistry.IHaveLoader> getLoader() {
        return LOADER;
    }

    public static CAfterAttackModule any(LivingEntityAction action, float amount) {
        return new CAfterAttackModule(LivingEntityPredicate.ANY, LivingEntityPredicate.ANY, action, amount, false);
    }

    public static CAfterAttackModule get(IJsonPredicate<LivingEntity> attacker, IJsonPredicate<LivingEntity> target, LivingEntityAction action, float amount, boolean critBonus) {
        return new CAfterAttackModule(attacker, target, action, amount, critBonus);
    }
}
