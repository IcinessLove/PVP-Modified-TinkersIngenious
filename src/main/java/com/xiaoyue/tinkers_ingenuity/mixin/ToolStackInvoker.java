package com.xiaoyue.tinkers_ingenuity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mixin(value = {ToolStack.class}, remap = false)
public interface ToolStackInvoker {

    @Invoker
    void callSetStats(StatsNBT stats);

}
