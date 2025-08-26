package com.xiaoyue.tinkers_ingenuity.content.modifier.defense;

import com.xiaoyue.tinkers_ingenuity.content.generic.SimpleModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;

public class KnightBloodline extends SimpleModifier {

    private static final TinkerDataCapability.TinkerDataKey<Integer> KBL = TConstruct.createKey("knight_bloodline");

    public KnightBloodline(){
        super();
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, KnightBloodline::onDamage);
    }

    @Override
    public void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addModule(new ArmorLevelModule(KBL, false, null));
    }

    public static void onDamage(LivingDamageEvent event) {
        LivingEntity living = event.getEntity();
        living.getCapability(TinkerDataCapability.CAPABILITY).ifPresent((holder) -> {
            int levels = holder.get(KBL, 0);
            float amount = event.getAmount();
            if (levels > 0 && amount > 0) {
                // 计算最大允许伤害：最大生命值除以系数（最小为5）
                float maxDamage = living.getMaxHealth() / Math.min(5, 1 + 0.4f * levels);
                // 限制最终伤害
                event.setAmount(Math.min(amount, maxDamage));
            }
        }); 
    }

    public static KnightBloodline getIns() {
        return new KnightBloodline();
    }
}