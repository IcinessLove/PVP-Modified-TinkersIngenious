package com.xiaoyue.tinkers_ingenuity.data.tools;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import slimeknights.tconstruct.library.client.data.material.AbstractPartSpriteProvider;

public class TITinkerPartSpriteGen extends AbstractPartSpriteProvider {
    public TITinkerPartSpriteGen() {
        super("tinkers_ingenuity");
    }

    @Override
    protected void addAllSpites() {
        this.addTexture(TinkersIngenuity.loc("tinkers_medal/medal_ribbons"));
        this.addTexture(TinkersIngenuity.loc("tinkers_medal/medal_body"));
        this.addTexture(TinkersIngenuity.loc("blowpipe/blowpipe_tube"));
        this.addTexture(TinkersIngenuity.loc("blowpipe/blowpipe_mouth"));
    }

    @Override
    public String getName() {
        return "Tinkers Ingenuity tinker part sprite providers";
    }
}
