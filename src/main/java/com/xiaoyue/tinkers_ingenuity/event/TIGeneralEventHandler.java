package com.xiaoyue.tinkers_ingenuity.event;

import com.xiaoyue.tinkers_ingenuity.content.items.ModifiableCurio;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.attack.GenericCombatModifierHook;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.defense.LivingEventModifierHook;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.specail.MenuSlotClickModifierHook;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.specail.TinkersCurioModifierHook;
import com.xiaoyue.tinkers_ingenuity.event.api.TinkerToolCriticalEvent;
import com.xiaoyue.tinkers_ingenuity.register.TIHooks;
import com.xiaoyue.tinkers_ingenuity.utils.TinkerUtils;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemStackedOnOtherEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent.ImpactResult;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.definition.module.mining.IsEffectiveToolHook;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import static com.xiaoyue.tinkers_ingenuity.TinkersIngenuity.MODID;

@EventBusSubscriber(modid = MODID, bus = Bus.FORGE)
public class TIGeneralEventHandler {

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        ModifiableCurio.postAction(entity, (c, e) -> {
            TinkersCurioModifierHook hook = e.getHook(TIHooks.TINKERS_CURIO);
            event.setAmount(hook.onLivingHeal(c, e.getLevel(), entity, event.getAmount()));
        });
    }

    @SubscribeEvent
    public static void onProjHit(ProjectileImpactEvent event) {
        Projectile proj = event.getProjectile();
        if (proj.getOwner() instanceof LivingEntity entity) {
            AbstractArrow arrow = proj instanceof AbstractArrow it ? it : null;
            ModifiableCurio.postAction(entity, (c, e) -> {
                TinkersCurioModifierHook hook = e.getHook(TIHooks.TINKERS_CURIO);
                if (hook.onProjectileHit(c, e.getLevel(), entity, proj, arrow, event.getRayTraceResult(), PersistentDataCapability.getOrWarn(proj))) {
                    event.setImpactResult(ImpactResult.STOP_AT_CURRENT);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onBreakBlock(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ModifiableCurio.postAction(player, (c, e) -> {
            TinkersCurioModifierHook hook = e.getHook(TIHooks.TINKERS_CURIO);
            hook.onBreakBlock(c, e.getLevel(), player, event.getState(), event.getPos());
        });
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();
        ModifiableCurio.postAction(player, (c, e) -> {
            TinkersCurioModifierHook hook = e.getHook(TIHooks.TINKERS_CURIO);
            boolean effective = TinkerUtils.checkTool(stack) && IsEffectiveToolHook.isEffective(ToolStack.from(stack), event.getState());
            hook.getBreakSpeed(c, e.getLevel(), player, event, effective);
        });
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Projectile proj) {
            if (proj.getOwner() instanceof LivingEntity shooter) {
                ModifiableCurio.calculateProjDamage(shooter, proj);
                AbstractArrow arrow = proj instanceof AbstractArrow it ? it : null;
                ModifiableCurio.postAction(shooter, (c, e) -> {
                    TinkersCurioModifierHook hook = e.getHook(TIHooks.TINKERS_CURIO);
                    hook.onShootProjectile(c, e.getLevel(), shooter, proj, arrow, PersistentDataCapability.getOrWarn(proj));
                });
            }
        }
    }

    @SubscribeEvent
    public static void onDamageEvent(LivingDamageEvent event) {
        LivingEntity target = event.getEntity();
        Entity source = event.getSource().getEntity();
        if (source instanceof LivingEntity attacker) {
            ModifiableCurio.postAction(attacker, (c, e) -> {
                TinkersCurioModifierHook hook = e.getHook(TIHooks.TINKERS_CURIO);
                hook.onDamageTargetPost(c, e.getLevel(), attacker, target, event);
            });
        }
        ModifiableCurio.postAction(target, (c, e) -> {
            TinkersCurioModifierHook hook = e.getHook(TIHooks.TINKERS_CURIO);
            hook.onTakenDamagePost(c, e.getLevel(), target, event.getSource(), event);
        });
    }

    @SubscribeEvent
    public static void onHurtEvent(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        Entity source = event.getSource().getEntity();
        if (source instanceof LivingEntity attacker) {
            ModifiableCurio.postAction(attacker, (c, e) -> {
                TinkersCurioModifierHook hook = e.getHook(TIHooks.TINKERS_CURIO);
                hook.onDamageTargetPre(c, e.getLevel(), attacker, target, event);
            });
        }
        ModifiableCurio.postAction(target, (c, e) -> {
            TinkersCurioModifierHook hook = e.getHook(TIHooks.TINKERS_CURIO);
            hook.onTakenDamagePre(c, e.getLevel(), target, event.getSource(), event);
        });
    }

    @SubscribeEvent
    public static void onAttackEvent(LivingAttackEvent event) {
        LivingEntity target = event.getEntity();
        ModifiableCurio.postAction(target, (c, e) -> {
            TinkersCurioModifierHook hook = e.getHook(TIHooks.TINKERS_CURIO);
            if (hook.canImmuneAttack(c, e.getLevel(), target, event.getSource(), event.getAmount())) {
                event.setCanceled(true);
            }
        });
    }

    @SubscribeEvent
    public static void onToolCrit(TinkerToolCriticalEvent event) {
        GenericCombatModifierHook.postCritHit(event.getTool(), event);
    }

    @SubscribeEvent
    public static void onMenuSlotClick(ItemStackedOnOtherEvent event) {
        ItemStack stack = event.getCarriedItem();
        if (TinkerUtils.checkTool(stack)) {
            MenuSlotClickModifierHook.post(ToolStack.from(stack), event);
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        TinkerUtils.postArmorAction(event.getEntity(), holder ->
                LivingEventModifierHook.postDeath(holder.getTool(), holder.context(), event, holder.slot()));
    }
}
