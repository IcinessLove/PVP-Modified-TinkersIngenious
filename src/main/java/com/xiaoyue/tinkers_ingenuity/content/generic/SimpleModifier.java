package com.xiaoyue.tinkers_ingenuity.content.generic;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public abstract class SimpleModifier extends Modifier implements ModifierRemovalHook {

    public boolean isSingleLevel() {
        return false;
    }

    @Override
    public @NotNull Component getDisplayName(int level) {
        return this.isSingleLevel() ? super.getDisplayName() : super.getDisplayName(level);
    }

    protected void addHooks(ModuleHookMap.Builder builder) {
    }

    @Override
    public void registerHooks(ModuleHookMap.Builder builder) {
        builder.addHook(this, ModifierHooks.REMOVE);
        this.addHooks(builder);
    }

    @Override
    public @Nullable Component onRemoved(IToolStackView tool, Modifier modifier) {
        if (this.getNBTKey() != null) {
            tool.getPersistentData().remove(this.getNBTKey());
        }

        return null;
    }

    public ResourceLocation getNBTKey() {
        return null;
    }

    public boolean hasThis(IToolStackView tool) {
        return tool.getModifierLevel(this) > 0;
    }

    public boolean chance(double chance) {
        return chance >= TConstruct.RANDOM.nextDouble();
    }

    public void addCD(IToolStackView tool, LivingEntity entity, int time) {
        if (entity instanceof Player player) {
            player.getCooldowns().addCooldown(tool.getItem(), time);
        }

    }

    public boolean noCD(IToolStackView tool, LivingEntity entity) {
        if (entity instanceof Player player) {
            return !player.getCooldowns().isOnCooldown(tool.getItem());
        } else {
            return true;
        }
    }
}
