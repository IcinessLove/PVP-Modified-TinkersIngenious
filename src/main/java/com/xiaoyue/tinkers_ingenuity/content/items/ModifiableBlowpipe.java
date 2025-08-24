package com.xiaoyue.tinkers_ingenuity.content.items;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.AbstractArrow.Pickup;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.BowAmmoModifierHook;
import slimeknights.tconstruct.library.tools.capability.EntityModifierCapability;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableLauncherItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.function.Predicate;

public class ModifiableBlowpipe extends ModifiableLauncherItem {
    public ModifiableBlowpipe(ToolDefinition toolDefinition) {
        super((new Item.Properties()).stacksTo(1), toolDefinition);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        ToolStack tool = ToolStack.from(stack);
        if (tool.isBroken()) {
            return InteractionResultHolder.fail(stack);
        } else {
            boolean hasAmmo = BowAmmoModifierHook.hasAmmo(tool, stack, pPlayer, this.getAllSupportedProjectiles());
            if (this.toolCheckAndShoot(tool, stack, pPlayer, pLevel, hasAmmo)) {
                ToolDamageUtil.damageAnimated(tool, 2, pPlayer, pUsedHand);
            }
            return InteractionResultHolder.consume(stack);
        }
    }

    public boolean toolCheckAndShoot(IToolStackView tool, ItemStack stack, Player player, Level level, boolean hasAmmo) {
        ItemStack ammo = BowAmmoModifierHook.consumeAmmo(tool, stack, player, player, this.getAllSupportedProjectiles());
        if (!player.getAbilities().instabuild && !hasAmmo) {
            return false;
        } else {
            ArrowItem arrowItem = ammo.getItem() instanceof ArrowItem it ? it : (ArrowItem) Items.ARROW;
            float velocity = ConditionalStatModifierHook.getModifiedStat(tool, player, ToolStats.VELOCITY);
            float inaccuracy = ModifierUtil.getInaccuracy(tool, player);
            int primaryIndex = ammo.getCount() / 2;
            for (int index = 0; index < ammo.getCount(); ++index) {
                AbstractArrow arrow = arrowItem.createArrow(level, ammo, player);
                float angle = getAngleStart(ammo.getCount()) + (float) (10 * index);
                arrow.shootFromRotation(player, player.getXRot() + angle, player.getYRot(), 0.0F, velocity * 3.0F, inaccuracy);
                this.addStatAndModifier(arrow, tool, player);
                ModDataNBT nbt = PersistentDataCapability.getOrWarn(arrow);
                if (player.getAbilities().instabuild) {
                    arrow.pickup = Pickup.CREATIVE_ONLY;
                }
                for (ModifierEntry entry : tool.getModifierList()) {
                    entry.getHook(ModifierHooks.PROJECTILE_LAUNCH).onProjectileLaunch(tool, entry, player, ammo, arrow, arrow, nbt, index == primaryIndex);
                }
                level.addFreshEntity(arrow);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) * 0.5F + (angle / 10f));
            }
            return true;
        }
    }

    public void addStatAndModifier(AbstractArrow arrow, IToolStackView tool, Player player) {
        float baseArrowDamage = (float) (arrow.getBaseDamage() - (double) 2.0F + (double) tool.getStats().get(ToolStats.PROJECTILE_DAMAGE));
        arrow.setBaseDamage(ConditionalStatModifierHook.getModifiedStat(tool, player, ToolStats.PROJECTILE_DAMAGE, baseArrowDamage));
        arrow.getCapability(EntityModifierCapability.CAPABILITY).ifPresent((cap) -> cap.setModifiers(tool.getModifiers()));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.NONE;
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ProjectileWeaponItem.ARROW_ONLY;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }

}
