package com.xiaoyue.tinkers_ingenuity.content.module.curios;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.generic.ISimpleModule;
import com.xiaoyue.tinkers_ingenuity.content.generic.SerialLoader;
import com.xiaoyue.tinkers_ingenuity.content.shared.holder.CurioStackView;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.specail.TinkersCurioModifierHook;
import com.xiaoyue.tinkers_ingenuity.register.TIHooks;
import dev.xkmc.l2library.util.math.MathHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import slimeknights.mantle.data.loadable.Loadables;
import slimeknights.mantle.data.loadable.primitive.DoubleLoadable;
import slimeknights.mantle.data.loadable.primitive.ResourceLocationLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.registry.GenericLoaderRegistry;
import slimeknights.tconstruct.library.json.TinkerLoadables;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public record CAttributeAdderModule(ResourceLocation name, Attribute attribute, AttributeModifier.Operation operation, double amount)
        implements ISimpleModule, TinkersCurioModifierHook {

    public static final RecordLoadable<CAttributeAdderModule> LOADER = RecordLoadable.create(
            ResourceLocationLoadable.DEFAULT.requiredField("name", CAttributeAdderModule::name),
            Loadables.ATTRIBUTE.requiredField("attribute", CAttributeAdderModule::attribute),
            TinkerLoadables.OPERATION.requiredField("operation", CAttributeAdderModule::operation),
            DoubleLoadable.ANY.requiredField("amount", CAttributeAdderModule::amount),
            CAttributeAdderModule::new
    );

    @SerialLoader
    public static void onInit() {
        TinkersIngenuity.REGISTRATE.module("curio_attribute_adder", LOADER);
    }

    @Override
    public void addAttributes(CurioStackView curio, int level, UUID uuid, BiConsumer<Attribute, AttributeModifier> cons) {
        UUID uid = MathHelper.getUUIDFromString(this.name.toString());
        cons.accept(this.attribute, new AttributeModifier(uid, this.name.toString(), this.amount * (double)level, this.operation));
    }

    @Override
    public RecordLoadable<? extends GenericLoaderRegistry.IHaveLoader> getLoader() {
        return LOADER;
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(TIHooks.TINKERS_CURIO);
    }

    public static CAttributeAdderModule get(ResourceLocation name, Attribute attribute, int operation, double amount) {
        return new CAttributeAdderModule(name, attribute, Operation.fromValue(operation), amount);
    }
}
