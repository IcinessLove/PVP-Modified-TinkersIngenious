package com.xiaoyue.tinkers_ingenuity.data.material;

import com.xiaoyue.tinkers_ingenuity.content.shared.material.MaterialStatsData;
import com.xiaoyue.tinkers_ingenuity.content.shared.stats.CurioMainMaterialStat;
import com.xiaoyue.tinkers_ingenuity.content.shared.stats.TIExtraMaterialStat;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Tier;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialStatsDataProvider;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import slimeknights.tconstruct.tools.stats.*;

public class TIMaterialStatGen extends AbstractMaterialStatsDataProvider {
    public TIMaterialStatGen(PackOutput packOutput, AbstractMaterialDataProvider materials) {
        super(packOutput, materials);
    }

    @Override
    protected void addMaterialStats() {
        for(TIMaterials mate : TIMaterials.values()) {
            MaterialStatsData stats = mate.holder.stats();
            if (stats != null) {
                this.addMaterialStats(mate.asMate(), stats.stats());
                if (stats.armor() != null && stats.shield()) {
                    this.addArmorShieldStats(mate.asMate(), stats.armor());
                } else if (stats.armor() != null) {
                    this.addArmorStats(mate.asMate(), stats.armor());
                }
            }
        }
        this.addVanillaCurioMaterialStats();
    }

    private void addVanillaCurioMaterialStats() {
        this.addAllCurioPart(MaterialIds.wood, 0.01f, 0.0f, 0.02F, 0.03F, 0.03F);
        this.addAllCurioPart(MaterialIds.bone, 0.03F, 2.0F, 0.05F, 0.08F, 0.06F);
        this.addAllCurioPart(MaterialIds.flint, 0.0F, 0.0F, 0.04F, 0.05F, 0.04F);
        this.addAllCurioPart(MaterialIds.copper, 0.04F, 4.0F, 0.08F, 0.07F, 0.05F);
        this.addAllCurioPart(MaterialIds.necroticBone, 0.02F, 7.0F, 0.08F, 0.09F, 0.08F);
        this.addAllCurioPart(MaterialIds.amethystBronze, 0.06F, 8.0F, 0.12F, 0.12F, 0.1F);
        this.addAllCurioPart(MaterialIds.obsidian, -0.02F, 8.0F, 0.15F, 0.13F, 0.12F);
        this.addAllCurioPart(MaterialIds.cobalt, 0.1F, 10.0F, 0.18F, 0.13F, 0.11F);
        this.addAllCurioPart(MaterialIds.manyullyn, 0.05F, 12.0F, 0.2F, 0.18F, 0.16F);
    }

    public void addAllCurioPart(MaterialId mat, float speed, float hp, float armor, float melee, float proj) {
        CurioMainMaterialStat main = new CurioMainMaterialStat(speed, hp, armor, melee, proj);
        this.addMaterialStats(mat, main, TIExtraMaterialStat.CURIO_EXTRA);
    }

    public static HeadMaterialStats head(int dur, float min, Tier tier, float attack) {
        return new HeadMaterialStats(dur, min, tier, attack);
    }

    public static HandleMaterialStats.Builder handle() {
        return HandleMaterialStats.multipliers();
    }

    public static LimbMaterialStats limb(int dur, float draw, float vel, float acc) {
        return new LimbMaterialStats(dur, draw, vel, acc);
    }

    public static GripMaterialStats grip(float dur, float melee, float acc) {
        return new GripMaterialStats(dur, acc, melee);
    }

    public static PlatingMaterialStats.Builder plat(float dur) {
        return PlatingMaterialStats.builder().durabilityFactor(dur);
    }

    @Override
    public String getName() {
        return "Tinkers Ingenuity material stats providers";
    }
}
