package com.xiaoyue.tinkers_ingenuity.content.module.curios;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.generic.ISimpleModule;
import com.xiaoyue.tinkers_ingenuity.content.generic.SerialLoader;
import com.xiaoyue.tinkers_ingenuity.content.shared.holder.CurioStackView;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.specail.TinkersCurioModifierHook;
import com.xiaoyue.tinkers_ingenuity.content.shared.json.variable.LevelingFormula;
import com.xiaoyue.tinkers_ingenuity.register.TIHooks;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.mantle.data.loadable.primitive.BooleanLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.data.predicate.block.BlockPredicate;
import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;
import slimeknights.mantle.data.registry.GenericLoaderRegistry;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;

import java.util.List;

public record CBreakSpeedModule(IJsonPredicate<LivingEntity> entity, LevelingFormula modifier, IJsonPredicate<BlockState> block, boolean effective)
        implements ISimpleModule, TinkersCurioModifierHook {

    public static final RecordLoadable<CBreakSpeedModule> LOADER = RecordLoadable.create(
            LivingEntityPredicate.LOADER.requiredField("entity", CBreakSpeedModule::entity),
            LevelingFormula.LOADER.requiredField("modifier", CBreakSpeedModule::modifier),
            BlockPredicate.LOADER.requiredField("block", CBreakSpeedModule::block),
            BooleanLoadable.DEFAULT.requiredField("effective", CBreakSpeedModule::effective),
            CBreakSpeedModule::new
    );

    @SerialLoader
    public static void onInit() {
        TinkersIngenuity.REGISTRATE.module("curio_break_speed", LOADER);
    }

    @Override
    public void getBreakSpeed(CurioStackView curio, int level, LivingEntity entity, PlayerEvent.BreakSpeed event, boolean isEffective) {
        if (this.test(entity, this.entity) && this.test(event.getState(), this.block) && (!this.effective || isEffective)) {
            event.setNewSpeed(this.modifier.apply(event.getNewSpeed(), level));
        }
    }

    @Override
    public RecordLoadable<? extends GenericLoaderRegistry.IHaveLoader> getLoader() {
        return LOADER;
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(TIHooks.TINKERS_CURIO);
    }

    public static CBreakSpeedModule any(LevelingFormula modifier, boolean effective) {
        return new CBreakSpeedModule(LivingEntityPredicate.ANY, modifier, BlockPredicate.ANY, effective);
    }

    public static CBreakSpeedModule get(IJsonPredicate<LivingEntity> entity, LevelingFormula modifier, IJsonPredicate<BlockState> block, boolean effective) {
        return new CBreakSpeedModule(entity, modifier, block, effective);
    }
}
