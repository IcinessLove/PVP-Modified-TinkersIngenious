package com.xiaoyue.tinkers_ingenuity.content.items;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.xiaoyue.tinkers_ingenuity.content.shared.holder.CurioStackView;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.specail.TinkersCurioModifierHook;
import com.xiaoyue.tinkers_ingenuity.data.modifier.TIModifierData;
import com.xiaoyue.tinkers_ingenuity.register.TIHooks;
import com.xiaoyue.tinkers_ingenuity.register.TIToolStats;
import com.xiaoyue.tinkers_ingenuity.utils.TinkerUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.data.ModifierIds;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurio.DropRule;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

public class ModifiableCurio extends ModifiableItem implements ICurioItem {
    public ModifiableCurio(ToolDefinition toolDefinition) {
        super((new Item.Properties()).stacksTo(1), toolDefinition);
    }

    public static List<SlotResult> findAll(LivingEntity entity) {
        Optional<ICuriosItemHandler> inv = CuriosApi.getCuriosInventory(entity).resolve();
        return inv.isPresent() ? inv.get().findCurios(TinkerUtils::checkTool) : List.of();
    }

    public static void postAction(LivingEntity entity, BiConsumer<CurioStackView, ModifierEntry> cons) {
        if (!findAll(entity).isEmpty()) {
            for (SlotResult result : findAll(entity)) {
                CurioStackView tool = CurioStackView.of(result);
                for (ModifierEntry entry : tool.getModifiers()) {
                    cons.accept(tool, entry);
                }
            }
        }
    }

    public static void postAction(LivingEntity entity, Modifier modifier, BiConsumer<CurioStackView, Integer> cons) {
        if (!findAll(entity).isEmpty()) {
            for (SlotResult result : findAll(entity)) {
                CurioStackView tool = CurioStackView.of(result);
                int level = tool.tool().getModifierLevel(modifier);
                if (level > 0) {
                    cons.accept(tool, level);
                }
            }
        }
    }

    public static void calculateProjDamage(LivingEntity shooter, Projectile projectile) {
        if (!findAll(shooter).isEmpty()) {
            for (SlotResult result : findAll(shooter)) {
                float bonus = ToolStack.from(result.stack()).getStats().get(TIToolStats.CURIO_PROJECTILE_ATTACK);
                if (projectile instanceof AbstractArrow arrow) {
                    arrow.setBaseDamage(arrow.getBaseDamage() * (double) (1.0F + bonus));
                }
            }

        }
    }

    private void addAttributeStats(ToolStack curio, UUID uuid, Multimap<Attribute, AttributeModifier> map) {
        StatsNBT stats = curio.getStats();
        map.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, "curio_speed_stat", (double) stats.get(TIToolStats.CURIO_MOVEMENT_SPEED), Operation.MULTIPLY_BASE));
        map.put(Attributes.MAX_HEALTH, new AttributeModifier(uuid, "curio_max_health_stat", (double) stats.get(TIToolStats.CURIO_MAX_HEALTH), Operation.ADDITION));
        map.put(Attributes.ARMOR, new AttributeModifier(uuid, "curio_armor_stat", (double) stats.get(TIToolStats.CURIO_ARMOR), Operation.MULTIPLY_BASE));
        map.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "curio_attack_stat", (double) stats.get(TIToolStats.CURIO_MELEE_ATTACK), Operation.MULTIPLY_BASE));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext context, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> map = LinkedHashMultimap.create();
        CurioStackView tool = CurioStackView.of(context, stack);
        this.addAttributeStats(tool.tool(), uuid, map);
        for (ModifierEntry entry : tool.getModifiers()) {
            TinkersCurioModifierHook hook = entry.getHook(TIHooks.TINKERS_CURIO);
            hook.addAttributes(tool, entry.getLevel(), uuid, map::put);
        }
        return map;
    }

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        CurioStackView tool = CurioStackView.of(context, stack);
        for (ModifierEntry entry : tool.getModifiers()) {
            entry.getHook(TIHooks.TINKERS_CURIO).onCurioTick(tool, entry.getLevel(), context.entity());
        }
    }

    @Override
    public void onEquip(SlotContext context, ItemStack prevStack, ItemStack stack) {
        CurioStackView tool = CurioStackView.of(context, stack);
        for (ModifierEntry entry : tool.getModifiers()) {
            entry.getHook(TIHooks.TINKERS_CURIO).onChangeCurio(tool, entry.getLevel(), prevStack, ModifiableCurio.ChangeType.EQUIP);
        }
    }

    @Override
    public void onUnequip(SlotContext context, ItemStack newStack, ItemStack stack) {
        CurioStackView tool = CurioStackView.of(context, stack);
        for (ModifierEntry entry : tool.getModifiers()) {
            entry.getHook(TIHooks.TINKERS_CURIO).onChangeCurio(tool, entry.getLevel(), newStack, ModifiableCurio.ChangeType.UNEQUIP);
        }
    }

    @Override
    public boolean canEquip(SlotContext context, ItemStack stack) {
        CurioStackView tool = CurioStackView.of(context, stack);
        Iterator<ModifierEntry> iterator = tool.getModifiers().iterator();
        if (iterator.hasNext()) {
            ModifierEntry entry = iterator.next();
            return entry.getHook(TIHooks.TINKERS_CURIO).canChangeCurio(tool, entry.getLevel(), ModifiableCurio.ChangeType.EQUIP);
        } else {
            return true;
        }
    }

    @Override
    public boolean canUnequip(SlotContext context, ItemStack stack) {
        CurioStackView tool = CurioStackView.of(context, stack);
        Iterator<ModifierEntry> iterator = tool.getModifiers().iterator();
        if (iterator.hasNext()) {
            ModifierEntry entry = iterator.next();
            return entry.getHook(TIHooks.TINKERS_CURIO).canChangeCurio(tool, entry.getLevel(), ModifiableCurio.ChangeType.UNEQUIP);
        } else {
            return true;
        }
    }

    @Override
    public int getLootingLevel(SlotContext context, DamageSource source, LivingEntity target, int baseLooting, ItemStack stack) {
        int origin = ICurioItem.super.getLootingLevel(context, source, target, baseLooting, stack);
        CurioStackView tool = CurioStackView.of(context, stack);
        for (ModifierEntry entry : tool.getModifiers()) {
            origin = entry.getHook(TIHooks.TINKERS_CURIO).getLootingLevel(tool, entry.getLevel(), source, target, origin);
        }
        return origin;
    }

    @Override
    public int getFortuneLevel(SlotContext context, LootContext loot, ItemStack stack) {
        int origin = ICurioItem.super.getFortuneLevel(context, loot, stack);
        CurioStackView tool = CurioStackView.of(context, stack);
        for (ModifierEntry entry : tool.getModifiers()) {
            origin = entry.getHook(TIHooks.TINKERS_CURIO).getFortuneLevel(tool, entry.getLevel(), loot, origin);
        }
        return origin;
    }

    @NotNull
    @Override
    public ICurio.@NotNull DropRule getDropRule(SlotContext context, DamageSource source, int lootingLevel, boolean recentlyHit, ItemStack stack) {
        CurioStackView tool = CurioStackView.of(context, stack);
        for (ModifierEntry entry : tool.getModifiers()) {
            return entry.getHook(TIHooks.TINKERS_CURIO).getCurioDropRule(tool, entry.getLevel(), source, lootingLevel, recentlyHit);
        }
        return tool.getModifierLevel(ModifierIds.soulbound) > 0 ? DropRule.ALWAYS_KEEP : DropRule.DEFAULT;
    }

    @Override
    public boolean isEnderMask(SlotContext slotContext, EnderMan enderMan, ItemStack stack) {
        CurioStackView tool = CurioStackView.of(slotContext, stack);
        return tool.hasModifier(TIModifierData.BLOT_OUT.getId());
    }

    @Override
    public boolean canWalkOnPowderedSnow(SlotContext slotContext, ItemStack stack) {
        CurioStackView tool = CurioStackView.of(slotContext, stack);
        return tool.hasModifier(TIModifierData.WALK_SNOW.getId());
    }

    @Override
    public boolean makesPiglinsNeutral(SlotContext slotContext, ItemStack stack) {
        CurioStackView tool = CurioStackView.of(slotContext, stack);
        return tool.hasModifier(TIModifierData.GOLDEN.getId());
    }

    public List<Component> getCurioStatsInfo(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        TooltipBuilder builder = new TooltipBuilder(tool, tooltips);
        TinkerUtils.addStatInfo(builder, tool, TIToolStats.CURIO_MAX_HEALTH);
        TinkerUtils.addPerStatInfo(builder, tool, TIToolStats.CURIO_ARMOR);
        TinkerUtils.addPerStatInfo(builder, tool, TIToolStats.CURIO_MELEE_ATTACK);
        TinkerUtils.addPerStatInfo(builder, tool, TIToolStats.CURIO_PROJECTILE_ATTACK);
        TinkerUtils.addPerStatInfo(builder, tool, TIToolStats.CURIO_MOVEMENT_SPEED);
        builder.addAllFreeSlots();
        for (ModifierEntry entry : tool.getModifierList()) {
            entry.getHook(ModifierHooks.TOOLTIP).addTooltip(tool, entry, player, tooltips, key, tooltipFlag);
        }
        return tooltips;
    }

    @Override
    public List<Component> getStatInformation(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        return this.getCurioStatsInfo(tool, player, tooltips, key, tooltipFlag);
    }

    public enum ChangeType {
        EQUIP,
        UNEQUIP;
    }
}
