package com.xiaoyue.tinkers_ingenuity.content.modifier.general;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.generic.SimpleModifier;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.specail.MenuSlotClickModifierHook;
import com.xiaoyue.tinkers_ingenuity.mixin.ToolStackInvoker;
import com.xiaoyue.tinkers_ingenuity.register.TIHooks;
import com.xiaoyue.tinkers_ingenuity.utils.TinkerUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemStackedOnOtherEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.*;
import slimeknights.tconstruct.library.tools.part.IToolPart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreshWater extends SimpleModifier implements MenuSlotClickModifierHook {

    public final ResourceLocation KEY = TinkersIngenuity.loc("fresh_water");
    public final Map<String, ModifierNBT> TRAIT_MAP = new HashMap<>();

    @Override
    public boolean isSingleLevel() {
        return true;
    }

    @Override
    protected void addHooks(ModuleHookMap.Builder builder) {
        builder.addHook(this, TIHooks.MENU_SLOT_CLICK);
    }

    private void updateTraits(ToolStack tool, ItemStack part) {
        List<ModifierEntry> partTraits = TinkerUtils.getPartTraits(part);
        if (this.TRAIT_MAP.containsKey("partTraits")) {
            for (ModifierEntry entry : this.TRAIT_MAP.get("partTraits")) {
                tool.removeModifier(entry.getId(), entry.getLevel());
            }
        }
        this.TRAIT_MAP.put("partTraits", new ModifierNBT(partTraits));
        for (ModifierEntry trait : partTraits) {
            tool.addModifier(trait.getId(), trait.getLevel());
        }
    }

    public void updateStats(ToolStack tool, ToolStack target) {
        CompoundTag tag = tool.getPersistentData().getCompound(this.KEY);
        if (!tag.contains("stats", 10)) {
            tag.put("stats", tool.getStats().serializeToNBT());
        }
        if (tool.getDefinition().equals(target.getDefinition()) && target.getUpgrades().isEmpty()) {
            ((ToolStackInvoker) tool).callSetStats(target.getStats());
        }
    }

    @Override
    public void onClickTool(ToolStack tool, ModifierEntry modifier, ItemStackedOnOtherEvent event, ItemStack onItem, ClickAction action) {
        if (action.equals(ClickAction.SECONDARY)) {
            if (onItem.getItem() instanceof IToolPart part) {
                if (TinkerUtils.matchesPart(tool, part)) {
                    this.updateTraits(tool, onItem);
                    onItem.shrink(1);
                    event.setCanceled(true);
                }
            }
            if (TinkerUtils.checkTool(onItem)) {
                this.updateStats(tool, ToolStack.from(onItem));
                event.setCanceled(true);
            }
        }
    }

    @Override
    public @Nullable Component onRemoved(IToolStackView tool, Modifier modifier) {
        ModDataNBT data = tool.getPersistentData();
        if (this.TRAIT_MAP.containsKey("partTraits")) {
            for (ModifierEntry entry : this.TRAIT_MAP.get("partTraits")) {
                ((ToolStack) tool).removeModifier(entry.getId(), entry.getLevel());
            }
        }
        if (data.getCompound(this.KEY).contains("stats", 10)) {
            StatsNBT stats = StatsNBT.readFromNBT(data.getCompound(this.KEY).get("stats"));
            ((ToolStackInvoker) tool).callSetStats(stats);
        }
        data.remove(this.KEY);
        return null;
    }
}
