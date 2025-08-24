package com.xiaoyue.tinkers_ingenuity.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.xiaoyue.tinkers_ingenuity.content.generic.MeleeCacheCapability;
import com.xiaoyue.tinkers_ingenuity.event.api.TinkerToolCriticalEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.function.DoubleSupplier;

@Mixin(value = {ToolAttackUtil.class}, remap = false)
public abstract class ToolAttackUtilMixin {

    @Inject(at = {@At("HEAD")}, method = {"attackEntity(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/entity/Entity;Ljava/util/function/DoubleSupplier;ZLnet/minecraft/world/entity/EquipmentSlot;)Z"})
    private static void tinkers_ingenuity$attackEntity$putCache(IToolStackView tool, LivingEntity attackerLiving, InteractionHand hand, Entity targetEntity, DoubleSupplier cooldownFunction, boolean isExtraAttack, EquipmentSlot sourceSlot, CallbackInfoReturnable<Boolean> cir) {
        if (attackerLiving instanceof Player player) {
            MeleeCacheCapability.saveTool(player, hand);
        }
    }

    @Inject(at = {@At("RETURN")}, method = {"attackEntity(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/entity/Entity;Ljava/util/function/DoubleSupplier;ZLnet/minecraft/world/entity/EquipmentSlot;)Z"})
    private static void tinkers_ingenuity$attackEntity$removeCache(IToolStackView tool, LivingEntity attackerLiving, InteractionHand hand, Entity targetEntity, DoubleSupplier cooldownFunction, boolean isExtraAttack, EquipmentSlot sourceSlot, CallbackInfoReturnable<Boolean> cir) {
        if (attackerLiving instanceof Player player) {
            MeleeCacheCapability.removeCache(player);
        }
    }

    @Inject(at = {@At(value = "INVOKE", target = "Lslimeknights/tconstruct/library/tools/context/ToolAttackContext;<init>(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/LivingEntity;ZFZ)V")}, method = {"attackEntity(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/entity/Entity;Ljava/util/function/DoubleSupplier;ZLnet/minecraft/world/entity/EquipmentSlot;)Z"})
    private static void tinkers_ingenuity$attackEntity$critical(IToolStackView tool, LivingEntity attackerLiving, InteractionHand hand, Entity targetEntity, DoubleSupplier cooldownFunction, boolean isExtraAttack, EquipmentSlot sourceSlot, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 2) LocalBooleanRef critical) {
        TinkerToolCriticalEvent event = new TinkerToolCriticalEvent(tool, attackerLiving, targetEntity, hand, isExtraAttack, critical.get());
        MinecraftForge.EVENT_BUS.post(event);
        critical.set(event.isCritical());
    }
}
