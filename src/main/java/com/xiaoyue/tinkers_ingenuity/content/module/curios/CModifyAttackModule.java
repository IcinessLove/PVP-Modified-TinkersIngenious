package com.xiaoyue.tinkers_ingenuity.content.module.curios;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.generic.ISimpleModule;
import com.xiaoyue.tinkers_ingenuity.content.generic.SerialLoader;
import com.xiaoyue.tinkers_ingenuity.content.shared.holder.CurioStackView;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.specail.TinkersCurioModifierHook;
import com.xiaoyue.tinkers_ingenuity.content.shared.json.variable.LevelingFormula;
import com.xiaoyue.tinkers_ingenuity.register.TIHooks;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;
import slimeknights.mantle.data.registry.GenericLoaderRegistry;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;

import java.util.List;

public record CModifyAttackModule(IJsonPredicate<LivingEntity> attacker, IJsonPredicate<LivingEntity> target, LevelingFormula bonus)
        implements ISimpleModule, TinkersCurioModifierHook {

    public static final RecordLoadable<CModifyAttackModule> LOADER = RecordLoadable.create(
            LivingEntityPredicate.LOADER.requiredField("attacker", CModifyAttackModule::attacker),
            LivingEntityPredicate.LOADER.requiredField("target", CModifyAttackModule::target),
            LevelingFormula.LOADER.requiredField("bonus", CModifyAttackModule::bonus),
            CModifyAttackModule::new
    );

    @SerialLoader
    public static void onInit() {
        TinkersIngenuity.REGISTRATE.module("curio_modify_attack", LOADER);
    }

    @Override
    public void onDamageTargetPre(CurioStackView curio, int level, LivingEntity attacker, LivingEntity target, LivingHurtEvent event) {
        if (this.test(attacker, this.attacker) && this.test(target, this.target)) {
            event.setAmount(this.bonus.apply(event.getAmount(), level));
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

    public static CModifyAttackModule get(IJsonPredicate<LivingEntity> attacker, IJsonPredicate<LivingEntity> target, LevelingFormula bonus) {
        return new CModifyAttackModule(attacker, target, bonus);
    }
}
