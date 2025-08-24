package com.xiaoyue.tinkers_ingenuity.content.shared.material;

import net.minecraft.world.level.ItemLike;
import slimeknights.mantle.registration.object.FluidObject;

import javax.annotation.Nullable;

public record MaterialRecipeData(@Nullable ItemLike craftItem, @Nullable FluidObject<?> meltingFluid) {
}
