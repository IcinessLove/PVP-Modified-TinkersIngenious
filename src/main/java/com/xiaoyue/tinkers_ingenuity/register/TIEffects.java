package com.xiaoyue.tinkers_ingenuity.register;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.effects.DeadlyPlague;
import net.minecraft.world.effect.MobEffect;

public class TIEffects {

    public static final RegistryEntry<DeadlyPlague> DEADLY_PLAGUE;

    static {
        DEADLY_PLAGUE = effect("deadly_plague", DeadlyPlague::new,
                "Plague damage is continuously taken based on current health");
    }

    private static <T extends MobEffect> RegistryEntry<T> effect(String name, NonNullSupplier<T> sup, String desc) {
        return TinkersIngenuity.REGISTRATE.effect(name, sup, desc).lang(MobEffect::getDescriptionId).register();
    }

    public static void register() {
    }
}
