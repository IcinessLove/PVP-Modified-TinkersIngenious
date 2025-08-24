package com.xiaoyue.tinkers_ingenuity.register;

import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.xiaoyue.celestial_invoker.content.CelestialRegistrate;
import dev.xkmc.l2library.base.L2Registrate;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.registration.deferred.FluidDeferredRegister;
import slimeknights.tconstruct.common.TinkerTags.Items;
import slimeknights.tconstruct.common.data.model.MaterialModelBuilder;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStatId;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

public class TIRegistrate extends CelestialRegistrate {

    private final FluidDeferredRegister fluidRegister = new FluidDeferredRegister(this.getModid());

    public TIRegistrate(String modid) {
        super(modid);
        this.fluidRegister.register(this.getModEventBus());
    }

    public FluidDeferredRegister.Builder mantleFluid(String id) {
        return this.fluidRegister.register(id);
    }

    public <T extends Item> RegistryEntry<T> simpleItem(String id, NonNullSupplier<T> item) {
        return this.simple(id, Registries.ITEM, item);
    }

    @SafeVarargs
    public final <T extends Item> ItemBuilder<T, L2Registrate> tagItem(String id, String path, NonNullFunction<Item.Properties, T> factory, TagKey<Item>... tags) {
        return this.item(id, factory).model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation[]{pvd.modLoc("item/" + path + "/" + ctx.getName())})).tag(tags);
    }

    public List<ItemEntry<Item>> castItem(String name) {
        ResourceLocation multiCast = new ResourceLocation(this.getModid(), "casts/multi_use/" + name);
        ResourceLocation singleCast = new ResourceLocation(this.getModid(), "casts/single_use/" + name);
        ItemEntry<Item> cast = this.tagItem(name + "_cast", "cast", Item::new, Items.CASTS, Items.GOLD_CASTS, ItemTags.create(multiCast)).register();
        ItemEntry<Item> sandCast = this.tagItem(name + "_sand_cast", "cast", Item::new, Items.CASTS, Items.SAND_CASTS, ItemTags.create(singleCast)).register();
        ItemEntry<Item> redSandCast = this.tagItem(name + "_red_sand_cast", "cast", Item::new, Items.CASTS, Items.RED_SAND_CASTS, ItemTags.create(singleCast)).register();
        return List.of(cast, sandCast, redSandCast);
    }

    public <T extends Item> ItemBuilder<T, L2Registrate> partItem(String id, String tool, NonNullFunction<Item.Properties, T> factory, int x, int y) {
        return this.item(id, factory).model((ctx, pvd) ->
                pvd.withExistingParent(ctx.getName(), "forge:item/default").texture("texture", pvd.modLoc("item/" + tool + "/" + ctx.getName()))
                        .customLoader((b, h) -> new MaterialModelBuilder<>(b, h).offset(x, y)));
    }

    public FloatToolStat floatToolStat(String id, int color, float defValue, float minValue, float maxValue) {
        return ToolStats.register(new FloatToolStat(new ToolStatId(new ResourceLocation(this.getModid(), id)), color, defValue, minValue, maxValue));
    }

    public <T extends ModifierModule> void module(String id, RecordLoadable<? extends T> loader) {
        ModifierModule.LOADER.register(new ResourceLocation(this.getModid(), id), loader);
    }
}
