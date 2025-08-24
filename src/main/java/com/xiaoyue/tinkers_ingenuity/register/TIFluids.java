package com.xiaoyue.tinkers_ingenuity.register;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType.Properties;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import slimeknights.mantle.registration.object.FlowingFluidObject;
import slimeknights.mantle.registration.object.FluidObject;

import java.util.ArrayList;
import java.util.List;

public class TIFluids {

    public static final List<FluidObject<ForgeFlowingFluid>> FLUID_OBJECTS = new ArrayList<>();

    public static final FluidObject<ForgeFlowingFluid> MOLTEN_BLACK_GOLD, MOLTEN_FLAME_STEEL, DRAGON_BREATH, ENDER_COMPOUND,
            SCULK_GENE, MOLTEN_BLACK_FLASH_ALLOY, MOLTEN_COLORFUL_SLIME, MOLTEN_KNIGHT_CRYSTAL, TERRESTRIAL_SOLUTION, MOLTEN_MITHRIL;

    static {
        MOLTEN_BLACK_GOLD = fluid("molten_black_gold", 1200, 0);
        MOLTEN_FLAME_STEEL = fluid("molten_flame_steel", 1500, 0);
        DRAGON_BREATH = fluid("dragon_breath", 2200, 5);
        ENDER_COMPOUND = fluid("ender_compound", 2000, 5);
        SCULK_GENE = fluid("sculk_gene", 1500, 0);
        MOLTEN_BLACK_FLASH_ALLOY = fluid("molten_black_flash_alloy", 1500, 0);
        MOLTEN_COLORFUL_SLIME = fluid("molten_colorful_slime", 1200, 0);
        MOLTEN_KNIGHT_CRYSTAL = fluid("molten_knight_crystal", 1400, 0);
        TERRESTRIAL_SOLUTION = fluid("terrestrial_solution", 900, 2);
        MOLTEN_MITHRIL = fluid("molten_mithril", 1550, 0);
    }


    public static FluidObject<ForgeFlowingFluid> fluid(String id, int template, int light) {
        FlowingFluidObject<ForgeFlowingFluid> fluid = TinkersIngenuity.REGISTRATE.mantleFluid(id)
                .type(Properties.create().density(2000).viscosity(10000).temperature(template)
                        .sound(SoundActions.BUCKET_FILL, SoundEvents.LAVA_AMBIENT)).block(MapColor.COLOR_BLACK,
                        light).commonTag().bucket().flowing();
        FLUID_OBJECTS.add(fluid);
        return fluid;
    }

    public static List<ItemStack> allBucket() {
        List<ItemStack> list = new ArrayList<>();
        for(FluidObject<ForgeFlowingFluid> fluid : FLUID_OBJECTS) {
            list.add(fluid.getBucket().getDefaultInstance());
        }
        return list;
    }

    public static void register() {
    }
}
