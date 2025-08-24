package com.xiaoyue.tinkers_ingenuity.content.module.mixed;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.generic.ISimpleModule;
import com.xiaoyue.tinkers_ingenuity.content.generic.SerialLoader;
import com.xiaoyue.tinkers_ingenuity.content.shared.json.action.MultiBonusHelper;
import com.xiaoyue.tinkers_ingenuity.content.shared.json.variable.LevelingFormula;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;
import slimeknights.mantle.data.registry.GenericLoaderRegistry;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.BreakSpeedModifierHook;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;

import java.util.List;

public record MixedModificationModule(IJsonPredicate<LivingEntity> holder, MultiBonusHelper multiBonus, LevelingFormula bonus)
        implements ISimpleModule, MeleeDamageModifierHook, ConditionalStatModifierHook, BreakSpeedModifierHook {

    public static final RecordLoadable<MixedModificationModule> LOADER = RecordLoadable.create(
            LivingEntityPredicate.LOADER.defaultField("entity", MixedModificationModule::holder),
            MultiBonusHelper.ALL.requiredField("multi_bonus", MixedModificationModule::multiBonus),
            LevelingFormula.LOADER.requiredField("bonus", MixedModificationModule::bonus),
            MixedModificationModule::new
    );

    @SerialLoader
    public static void onInit() {
        TinkersIngenuity.REGISTRATE.module("mixed_modification", LOADER);
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        return this.multiBonus.meleeDamage() && this.test(context.getAttacker(), this.holder) ? this.bonus.apply(damage, modifier) : damage;
    }

    @Override
    public float modifyStat(IToolStackView tool, ModifierEntry modifier, LivingEntity entity, FloatToolStat stat, float multiplier, float value) {
        return this.test(entity, this.holder) && this.multiBonus.testStat(stat) ? this.bonus.apply(value, modifier) : value;
    }

    @Override
    public void onBreakSpeed(IToolStackView tool, ModifierEntry modifier, PlayerEvent.BreakSpeed event, Direction direction, boolean isEffective, float mul) {
        if (this.test(event.getEntity(), this.holder)) {
            event.setNewSpeed(this.bonus.apply(event.getNewSpeed(), modifier));
        }
    }

    @Override
    public RecordLoadable<? extends GenericLoaderRegistry.IHaveLoader> getLoader() {
        return LOADER;
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(ModifierHooks.MELEE_DAMAGE, ModifierHooks.CONDITIONAL_STAT, ModifierHooks.BREAK_SPEED);
    }

    public static MixedModificationModule get(IJsonPredicate<LivingEntity> holder, MultiBonusHelper multiBonus, LevelingFormula bonus) {
        return new MixedModificationModule(holder, multiBonus, bonus);
    }
}
