package com.xiaoyue.tinkers_ingenuity.data;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.register.TIFluids;
import net.minecraft.data.PackOutput;
import slimeknights.mantle.fluid.texture.AbstractFluidTextureProvider;
import slimeknights.mantle.fluid.texture.FluidTexture;
import slimeknights.mantle.registration.object.FluidObject;

public class FluidModelGen extends AbstractFluidTextureProvider {
    public FluidModelGen(PackOutput packOutput) {
        super(packOutput, "tinkers_ingenuity");
    }

    @Override
    public void addTextures() {
        this.molten(TIFluids.MOLTEN_BLACK_GOLD, -11384981);
        this.molten(TIFluids.MOLTEN_FLAME_STEEL, -1480950);
        this.molten(TIFluids.MOLTEN_BLACK_FLASH_ALLOY, -12492898);
        this.liquid(TIFluids.DRAGON_BREATH, -2187835);
        this.liquid(TIFluids.ENDER_COMPOUND, -5493637);
        this.liquid(TIFluids.SCULK_GENE, -16740205);
        this.texture(TIFluids.MOLTEN_COLORFUL_SLIME, "colorful_slime/");
        this.liquid(TIFluids.MOLTEN_KNIGHT_CRYSTAL, -6984738);
        this.liquid(TIFluids.TERRESTRIAL_SOLUTION, 0xff6aecbf);
        this.molten(TIFluids.MOLTEN_MITHRIL, 0xff6aecbf);
    }

    public FluidTexture.Builder texture(FluidObject<?> fluid, String id) {
        return this.texture(fluid).textures(TinkersIngenuity.loc("fluid/material/" + id), false, false);
    }

    public FluidTexture.Builder molten(FluidObject<?> fluid, int color) {
        return this.texture(fluid).color(color).textures(TinkersIngenuity.loc("fluid/molten/"), false, false);
    }

    public FluidTexture.Builder liquid(FluidObject<?> fluid, int color) {
        return this.texture(fluid).color(color).textures(TinkersIngenuity.loc("fluid/liquid/"), false, false);
    }

    @Override
    public String getName() {
        return "Tinkers Ingenuity fluid texture providers";
    }
}
