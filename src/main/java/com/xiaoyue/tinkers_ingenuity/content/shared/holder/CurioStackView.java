package com.xiaoyue.tinkers_ingenuity.content.shared.holder;

import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;

public record CurioStackView(ItemStack stack, ToolStack tool, SlotContext context) {

    public ModifierNBT getModifiers() {
        return this.tool.getModifiers();
    }

    public int getModifierLevel(ModifierId id) {
        return this.tool.getModifierLevel(id);
    }

    public boolean hasModifier(ModifierId id) {
        return this.getModifierLevel(id) > 0;
    }

    public static CurioStackView of(SlotResult result) {
        return new CurioStackView(result.stack(), ToolStack.from(result.stack()), result.slotContext());
    }

    public static CurioStackView of(SlotContext context, ItemStack stack) {
        return new CurioStackView(stack, ToolStack.from(stack), context);
    }
}
