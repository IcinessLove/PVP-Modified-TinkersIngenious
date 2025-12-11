package com.xiaoyue.tinkers_ingenuity;

import com.mojang.logging.LogUtils;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.xiaoyue.celestial_invoker.content.GeneratorTypes;
import com.xiaoyue.celestial_invoker.simple.SimpleInvoker;
import com.xiaoyue.tinkers_ingenuity.content.generic.MeleeCacheCapability;
import com.xiaoyue.tinkers_ingenuity.content.generic.SerialLoader;
import com.xiaoyue.tinkers_ingenuity.data.*;
import com.xiaoyue.tinkers_ingenuity.data.material.*;
import com.xiaoyue.tinkers_ingenuity.data.modifier.TIModifierGen;
import com.xiaoyue.tinkers_ingenuity.data.modifier.TIModifierTagGen;
import com.xiaoyue.tinkers_ingenuity.data.tools.TIStationLayoutGen;
import com.xiaoyue.tinkers_ingenuity.data.tools.TITinkerPartSpriteGen;
import com.xiaoyue.tinkers_ingenuity.data.tools.TIToolDefinitionGen;
import com.xiaoyue.tinkers_ingenuity.event.TIAttackListener;
import com.xiaoyue.tinkers_ingenuity.register.*;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;
import slimeknights.tconstruct.fluids.data.FluidBlockstateModelProvider;
import slimeknights.tconstruct.fluids.data.FluidBucketModelProvider;
import slimeknights.tconstruct.library.client.data.material.MaterialPartTextureGenerator;
import slimeknights.tconstruct.library.modifiers.ModifierManager;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Mod(TinkersIngenuity.MODID)
@Mod.EventBusSubscriber(modid = TinkersIngenuity.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TinkersIngenuity
{
    public static final String MODID = "tinkers_ingenuity";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final TIRegistrate REGISTRATE = new TIRegistrate(MODID);

    public static final RegistryEntry<CreativeModeTab> ITEMS = REGISTRATE.buildCreativeTab("items",
            e -> e.icon(TIItems.BLACK_GOLD.ingot()::asStack));

    public static final RegistryEntry<CreativeModeTab> FLUIDS = REGISTRATE.buildCreativeTab("fluids",
            e -> e.icon(Objects.requireNonNull(TIFluids.MOLTEN_BLACK_FLASH_ALLOY.getBucket())::getDefaultInstance)
                    .displayItems((p, o) -> o.acceptAll(TIFluids.allBucket())));

    public static final RegistryEntry<CreativeModeTab> TOOLS = REGISTRATE.buildCreativeTab("tools",
            e -> e.icon(TIItems.TINKERS_MEDAL.get()::getRenderTool).displayItems(TIItems::addItemsToTab));
    
    public TinkersIngenuity() {
        TIItems.register();
        TIFluids.register();
        TIEffects.register();
        TIToolStats.register();
        TIDamageTypes.register();
        MeleeCacheCapability.register();
        AttackEventHandler.register(2222, new TIAttackListener());
        REGISTRATE.addDataGenerator(ProviderType.LANG, TILang::addLang);
        REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, TITagGen::addItemTagGen);
        REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, TITagGen::addFluidTagGen);
        REGISTRATE.addDataGenerator(ProviderType.RECIPE, TIRecipeGen::acceptRecipe);
        REGISTRATE.addDataGenerator(GeneratorTypes.RECORD_DATA, TISlotGen::onRecordGen);
    }

    @SubscribeEvent
    public static void onModifierRegister(ModifierManager.ModifierRegistrationEvent event) {
        TIModifiers.onRegister(event);
    }
    
@SubscribeEvent
public static void onRegister(RegisterEvent event) {
    // 先处理 TinkersIngenuity 自己的注册（延迟注册 TIHooks）
    TIHooks.register(event);

    // 你原来的 SerialLoader 调用按需保留
    if (event.getRegistryKey().equals(Registries.RECIPE_SERIALIZER)) {
        SimpleInvoker.invokeModMethod(MODID, SerialLoader.class);
    }
}


    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        boolean server = event.includeServer();
        boolean client = event.includeClient();
        CompletableFuture<HolderLookup.Provider> pvd = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        (new TIDamageTypes(output, pvd, helper)).generate(server, gen);
        TIMaterialDefGen matDef = new TIMaterialDefGen(output);
        TIMaterialSprGen matSpr = new TIMaterialSprGen();
        TITinkerPartSpriteGen partSpr = new TITinkerPartSpriteGen();
        gen.addProvider(server, matDef);
        gen.addProvider(server, new TIStationLayoutGen(output));
        gen.addProvider(server, new TIToolDefinitionGen(output));
        gen.addProvider(server, new TIMaterialStatGen(output, matDef));
        gen.addProvider(server, new TIModifierGen(output));
        gen.addProvider(server, new TIModifierTagGen(output, helper));
        gen.addProvider(server, new TIMaterialTraitGen(output, matDef));
        gen.addProvider(client, new MaterialPartTextureGenerator(output, helper, partSpr, matSpr));
        gen.addProvider(client, new TIMaterialRenderGen(output, matSpr, helper));
        gen.addProvider(client, new FluidModelGen(output));
        gen.addProvider(client, new FluidBucketModelProvider(output, MODID));
        gen.addProvider(client, new FluidBlockstateModelProvider(output, MODID));
    }

    @SubscribeEvent
    public static void commonStep(FMLCommonSetupEvent event) {
        event.enqueueWork(TIMaterialStats::register);
    }

    public static ResourceLocation loc(String s) {
        return new ResourceLocation(MODID, s);
    }
}
