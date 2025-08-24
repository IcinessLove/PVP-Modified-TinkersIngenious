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

public record SourceOrEntityProtectionModule(IJsonPredicate<DamageSource> source, IJsonPredicate<LivingEntity> entity, LevelingFormula sourceProtection, LevelingFormula entityProtection)
        implements ISimpleModule, ProtectionModifierHook {

    public static final RecordLoadable<SourceOrEntityProtectionModule> LOADER = RecordLoadable.create(
            DamageSourcePredicate.LOADER.requiredField("source", SourceOrEntityProtectionModule::source),
            LivingEntityPredicate.LOADER.requiredField("entity", SourceOrEntityProtectionModule::entity),
            LevelingFormula.LOADER.requiredField("source_protection", SourceOrEntityProtectionModule::sourceProtection),
            LevelingFormula.LOADER.requiredField("entity_protection", SourceOrEntityProtectionModule::entityProtection),
            SourceOrEntityProtectionModule::new
    );

    @SerialLoader
    public static void onInit() {
        TinkersIngenuity.REGISTRATE.module("source_or_entity_protection", LOADER);
    }

    @Override
    public float getProtectionModifier(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slot, DamageSource source, float cap) {
        if (this.source.matches(source)) {
            return this.sourceProtection.apply(cap, modifier);
        } else {
            return this.test(context.getEntity(), this.entity) ? this.entityProtection.apply(cap, modifier) : cap;
        }
    }

    @Override
    public RecordLoadable<? extends GenericLoaderRegistry.IHaveLoader> getLoader() {
        return LOADER;
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(ModifierHooks.PROTECTION);
    }

    public static SourceOrEntityProtectionModule get(IJsonPredicate<DamageSource> source, IJsonPredicate<LivingEntity> entity, LevelingFormula sourceProtection, LevelingFormula entityProtection) {
        return new SourceOrEntityProtectionModule(source, entity, sourceProtection, entityProtection);
    }
}
