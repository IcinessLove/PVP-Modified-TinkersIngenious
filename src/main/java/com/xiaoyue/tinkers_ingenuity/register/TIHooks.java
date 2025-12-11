package com.xiaoyue.tinkers_ingenuity.register;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.attack.GenericCombatModifierHook;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.attack.SweepEdgeModifierHook;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.defense.LivingEventModifierHook;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.specail.MenuSlotClickModifierHook;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.specail.TinkersCurioModifierHook;
import slimeknights.mantle.data.registry.IdAwareComponentRegistry;
import slimeknights.tconstruct.library.module.ModuleHook;
import net.minecraftforge.registries.RegisterEvent;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Function;

/**
 * TIHooks: 延迟注册版本 —— 不在类加载时做 Mantle/TConstruct 的动作
 */
public class TIHooks {

    /** 确保只注册一次（关键修复） **/
    private static boolean initialized = false;

    /** Mantle 的组件注册器（延迟创建） **/
    private static IdAwareComponentRegistry<ModuleHook<?>> LOADER;

    public static ModuleHook<LivingEventModifierHook> LIVING_EVENT;
    public static ModuleHook<GenericCombatModifierHook> GENERIC_COMBAT;
    public static ModuleHook<SweepEdgeModifierHook> SWEEP_EDGE;
    public static ModuleHook<MenuSlotClickModifierHook> MENU_SLOT_CLICK;
    public static ModuleHook<TinkersCurioModifierHook> TINKERS_CURIO;

    /** RegisterEvent 触发时执行 —— 但必须确保只执行一次 **/
    public static void register(RegisterEvent event) {
        if (initialized) {
            return;
        }
        initialized = true;

        LOADER = new IdAwareComponentRegistry<>("Tinkers Ingenuity Modifier Hook");

        LIVING_EVENT = registerHook("living_event",
                LivingEventModifierHook.class,
                LivingEventModifierHook.AllMerger::new,
                LivingEventModifierHook.EMPTY);

        GENERIC_COMBAT = registerHook("generic_combat",
                GenericCombatModifierHook.class,
                GenericCombatModifierHook.AllMerger::new,
                GenericCombatModifierHook.EMPTY);

        SWEEP_EDGE = registerHook("sweep_edge",
                SweepEdgeModifierHook.class,
                SweepEdgeModifierHook.AllMerger::new,
                SweepEdgeModifierHook.EMPTY);

        MENU_SLOT_CLICK = registerHook("menu_slot_click",
                MenuSlotClickModifierHook.class,
                MenuSlotClickModifierHook.AllMerger::new,
                MenuSlotClickModifierHook.EMPTY);

        TINKERS_CURIO = registerHook("tinkers_curio",
                TinkersCurioModifierHook.class,
                TinkersCurioModifierHook.AllMerger::new,
                TinkersCurioModifierHook.EMPTY);
    }

    private static <T> ModuleHook<T> registerHook(
            String name,
            Class<T> filter,
            @Nullable Function<Collection<T>, T> merger,
            T defaultInstance) {

        return LOADER.register(
                new ModuleHook<>(
                        TinkersIngenuity.loc(name + "_hook"),
                        filter,
                        merger,
                        defaultInstance
                )
        );
    }
}