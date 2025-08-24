package com.xiaoyue.tinkers_ingenuity.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.attack.SweepEdgeModifierHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.definition.module.weapon.SweepWeaponAttack;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

@Mixin(value = {SweepWeaponAttack.class}, remap = false)
public class SweepAttackMixin {
    @ModifyVariable(at = @At("STORE"), method = {"afterMeleeHit"}, ordinal = 2)
    public float tinkers_ingenuity$afterMeleeHit$setSweepDmg(float value, @Local(argsOnly = true) IToolStackView tool, @Local(argsOnly = true) ToolAttackContext context) {
        return SweepEdgeModifierHook.post(tool, context, value);
    }
}
