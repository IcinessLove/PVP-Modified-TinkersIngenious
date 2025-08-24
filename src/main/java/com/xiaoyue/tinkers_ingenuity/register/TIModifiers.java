package com.xiaoyue.tinkers_ingenuity.register;

import com.xiaoyue.tinkers_ingenuity.content.modifier.defense.PastMemories;
import com.xiaoyue.tinkers_ingenuity.content.modifier.defense.KnightBloodline;
import com.xiaoyue.tinkers_ingenuity.content.modifier.defense.Spotless;
import com.xiaoyue.tinkers_ingenuity.content.modifier.general.EnderResonance;
//import com.xiaoyue.tinkers_ingenuity.content.modifier.general.FinalReinforcement;
import com.xiaoyue.tinkers_ingenuity.content.modifier.general.PenetratingStar;
import com.xiaoyue.tinkers_ingenuity.content.modifier.general.FreshWater;
import com.xiaoyue.tinkers_ingenuity.data.modifier.TIModifierData;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierManager;

import java.util.function.Supplier;

public class TIModifiers {

    public static void onRegister(ModifierManager.ModifierRegistrationEvent event) {
        modifier(event, TIModifierData.PAST_MEMORIES, PastMemories::new);
        //modifier(event, TIModifierData.FINAL_REINFORCEMENT, FinalReinforcement::new);
        modifier(event, TIModifierData.SPOTLESS, Spotless::new);
        modifier(event, TIModifierData.ENDER_RESONANCE, EnderResonance::new);
        modifier(event, TIModifierData.FRESH_WATER, FreshWater::new);
        modifier(event, TIModifierData.KNIGHT_BLOODLINE, KnightBloodline::new);
        modifier(event, TIModifierData.PENETRATING_STAR, PenetratingStar::new);
    }

    public static <T extends Modifier> void modifier(ModifierManager.ModifierRegistrationEvent event, TIModifierData id, Supplier<T> modifier) {
        event.registerStatic(id.getId(), modifier.get());
    }
}
