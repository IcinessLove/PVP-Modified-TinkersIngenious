package com.xiaoyue.tinkers_ingenuity.register;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.attack.GenericCombatModifierHook;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.attack.SweepEdgeModifierHook;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.defense.LivingEventModifierHook;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.specail.MenuSlotClickModifierHook;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.specail.TinkersCurioModifierHook;
import slimeknights.mantle.data.registry.IdAwareComponentRegistry;
import slimeknights.tconstruct.library.module.ModuleHook;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Function;

public class TIHooks {

    public static final IdAwareComponentRegistry<ModuleHook<?>> LOADER = new IdAwareComponentRegistry<>("Tinkers Ingenuity Modifier Hook");

    public static final ModuleHook<LivingEventModifierHook> LIVING_EVENT;
    public static final ModuleHook<GenericCombatModifierHook> GENERIC_COMBAT;
    public static final ModuleHook<SweepEdgeModifierHook> SWEEP_EDGE;
    public static final ModuleHook<MenuSlotClickModifierHook> MENU_SLOT_CLICK;
    public static final ModuleHook<TinkersCurioModifierHook> TINKERS_CURIO;

    static {
        LIVING_EVENT = register("living_event", LivingEventModifierHook.class,
                LivingEventModifierHook.AllMerger::new, LivingEventModifierHook.EMPTY);
        GENERIC_COMBAT = register("generic_combat", GenericCombatModifierHook.class,
                GenericCombatModifierHook.AllMerger::new, GenericCombatModifierHook.EMPTY);
        SWEEP_EDGE = register("sweep_edge", SweepEdgeModifierHook.class, SweepEdgeModifierHook.AllMerger::new,
                SweepEdgeModifierHook.EMPTY);
        MENU_SLOT_CLICK = register("menu_slot_click", MenuSlotClickModifierHook.class,
                MenuSlotClickModifierHook.AllMerger::new, MenuSlotClickModifierHook.EMPTY);
        TINKERS_CURIO = register("tinkers_curio", TinkersCurioModifierHook.class,
                TinkersCurioModifierHook.AllMerger::new, TinkersCurioModifierHook.EMPTY);
    }

    public static <T> ModuleHook<T> register(String name, Class<T> filter, @Nullable Function<Collection<T>, T> merger, T defaultInstance) {
        return LOADER.register(new ModuleHook<>(TinkersIngenuity.loc(name + "_hook"), filter, merger, defaultInstance));
    }

    public static void register() {
    }
}
