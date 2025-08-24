package com.xiaoyue.tinkers_ingenuity.content.module.combat;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.generic.ISimpleModule;
import com.xiaoyue.tinkers_ingenuity.content.generic.SerialLoader;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.attack.GenericCombatModifierHook;
import com.xiaoyue.tinkers_ingenuity.content.shared.json.TILoadable;
import com.xiaoyue.tinkers_ingenuity.data.TIDamageState;
import com.xiaoyue.tinkers_ingenuity.register.TIHooks;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;
import slimeknights.mantle.data.registry.GenericLoaderRegistry;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public record CreateSourceModule(TIDamageState source, IJsonPredicate<LivingEntity> attacker)
        implements ISimpleModule, GenericCombatModifierHook {

    public static final RecordLoadable<CreateSourceModule> LOADER = RecordLoadable.create(
            TILoadable.DAMAGE_STATE.requiredField("source", CreateSourceModule::source),
            LivingEntityPredicate.LOADER.requiredField("attacker", CreateSourceModule::attacker),
            CreateSourceModule::new
    );

    @SerialLoader
    public static void onInit() {
        TinkersIngenuity.REGISTRATE.module("create_source", LOADER);
    }

    @Override
    public void onCreateSource(IToolStackView tool, ModifierEntry modifier, LivingEntity attacker, CreateSourceEvent event) {
        if (event.getResult() != null && event.getResult().toRoot().validState(this.source) && this.test(attacker, this.attacker)) {
            event.enable(this.source);
        }
    }

    @Override
    public RecordLoadable<? extends GenericLoaderRegistry.IHaveLoader> getLoader() {
        return LOADER;
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(TIHooks.GENERIC_COMBAT);
    }

    public static CreateSourceModule any(TIDamageState source) {
        return new CreateSourceModule(source, LivingEntityPredicate.ANY);
    }

    public static CreateSourceModule get(TIDamageState source, IJsonPredicate<LivingEntity> attacker) {
        return new CreateSourceModule(source, attacker);
    }
}