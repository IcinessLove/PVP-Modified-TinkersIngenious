package com.xiaoyue.tinkers_ingenuity.content.shared.hooks.specail;

import com.xiaoyue.tinkers_ingenuity.register.TIHooks;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemStackedOnOtherEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Collection;

public interface MenuSlotClickModifierHook {

    MenuSlotClickModifierHook EMPTY = new MenuSlotClickModifierHook() {
    };

    default void onClickTool(ToolStack tool, ModifierEntry modifier, ItemStackedOnOtherEvent event, ItemStack onItem, ClickAction action) {
    }

    static void post(ToolStack tool, ItemStackedOnOtherEvent event) {
        for(ModifierEntry e : tool.getModifierList()) {
            e.getHook(TIHooks.MENU_SLOT_CLICK).onClickTool(tool, e, event, event.getStackedOnItem(), event.getClickAction());
        }
    }

    record AllMerger(Collection<MenuSlotClickModifierHook> modules) implements MenuSlotClickModifierHook {
        @Override
        public void onClickTool(ToolStack tool, ModifierEntry modifier, ItemStackedOnOtherEvent event, ItemStack onItem, ClickAction action) {
            for(MenuSlotClickModifierHook module : this.modules) {
                module.onClickTool(tool, modifier, event, onItem, action);
            }

        }
    }
}
