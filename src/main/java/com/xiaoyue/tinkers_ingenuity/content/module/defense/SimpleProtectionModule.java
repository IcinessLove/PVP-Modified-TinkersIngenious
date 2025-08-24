package com.xiaoyue.tinkers_ingenuity.content.module.defense;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.generic.ISimpleModule;
import com.xiaoyue.tinkers_ingenuity.content.generic.SerialLoader;
import com.xiaoyue.tinkers_ingenuity.content.shared.json.variable.LevelingFormula;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.data.predicate.damage.DamageSourcePredicate;
import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;
import slimeknights.mantle.data.registry.GenericLoaderRegistry;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ProtectionModifierHook;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public record SimpleProtectionModule(IJsonPredicate<LivingEntity> entity, IJsonPredicate<DamageSource> source, LevelingFormula protection)
        implements ISimpleModule, ProtectionModifierHook {

    public static final RecordLoadable<SimpleProtectionModule> LOADER = RecordLoadable.create(
            LivingEntityPredicate.LOADER.requiredField("entity", SimpleProtectionModule::entity),
            DamageSourcePredicate.LOADER.requiredField("source", SimpleProtectionModule::source),
            LevelingFormula.LOADER.requiredField("protection", SimpleProtectionModule::protection),
            SimpleProtectionModule::new
    );

    @SerialLoader
    public static void onInit() {
        TinkersIngenuity.REGISTRATE.module("simple_protection", LOADER);
    }

    @Override
    public float getProtectionModifier(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slot, DamageSource source, float cap) {
        if (test(context.getEntity(), this.entity) && this.source.matches(source)) {
            return this.protection.apply(cap, modifier);
        }
        return cap;
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(ModifierHooks.PROTECTION);
    }

    @Override
    public RecordLoadable<? extends GenericLoaderRegistry.IHaveLoader> getLoader() {
        return LOADER;
    }

    public static SimpleProtectionModule any(IJsonPredicate<DamageSource> source, LevelingFormula protection) {
        return new SimpleProtectionModule(LivingEntityPredicate.ANY, source, protection);
    }
}
