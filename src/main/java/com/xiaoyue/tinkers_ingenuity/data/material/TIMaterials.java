package com.xiaoyue.tinkers_ingenuity.data.material;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.shared.holder.MaterialBuildHolder;
import com.xiaoyue.tinkers_ingenuity.content.shared.material.MaterialDefinitionData;
import com.xiaoyue.tinkers_ingenuity.data.modifier.TIModifierData;
import com.xiaoyue.tinkers_ingenuity.register.TIFluids;
import com.xiaoyue.tinkers_ingenuity.register.TIItems;
import net.minecraft.world.item.Tiers;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.data.ModifierIds;
import slimeknights.tconstruct.tools.stats.StatlessMaterialStats;

import static com.xiaoyue.tinkers_ingenuity.data.material.TIMaterialStatGen.*;

public enum TIMaterials {
    BLACK_GOLD(MaterialBuildHolder.builder("black_gold")
            .metalMaterial(TIFluids.MOLTEN_BLACK_GOLD)
            .desc(b -> b
                    .encyclopedia("After attacking, deal an additional magical damage equivalent to 10% of the original damage per level.")
                    .armor("Increases magic damage protection by 10% per level, reduces the duration of negative effects, and makes piglins more friendly."))
            .definition(b -> b.tier(3))
            .statAndShield(plat(27f).armor(3.0F, 5.0F, 6.0F, 3.0F),
                    head(850, 9.5F, Tiers.DIAMOND, 2.5F),
                    handle().durability(0.9F).miningSpeed(1.2F).attackDamage(1.15F).build(),
                    StatlessMaterialStats.BINDING,
                    limb(840, 0.12F, -0.2F, 0.2F),
                    grip(0.1F, 0.5F, 0.1f),
                    StatlessMaterialStats.MAILLE)
            .trait(b -> b
                    .addDefault(TIModifierData.AFTERSHOCK.asEntry())
                    .addArmor(TIMaterialTraitGen.entry(TinkerModifiers.golden.getId()), TIMaterialTraitGen.entry(ModifierIds.magicProtection)))
            .render(b -> b.color(-11384981).fallbacks("metal")
                    .sprite(b.color(-16777216, -13169380, -13033682, -12966061, -11384981, -9081)))),
    FLAME_STEEL(MaterialBuildHolder.builder("flame_steel")
            .metalMaterial(TIFluids.MOLTEN_FLAME_STEEL)
            .desc(b -> b
                    .encyclopedia("Increases attack damage and mining speed by 20% per level in flame.")
                    .ranged("Increases projectile damage by 20% per level in flame.")
                    .armor("Increases Fire Damage Protection by 20% per level, and Non-Fire Damage Protection by an additional 10% per level while in flames."))
            .statAndShield(plat(45f).armor(3.0F, 6.0F, 8.0F, 3.0F).toughness(1.5F).knockbackResistance(0.1F),
                    head(1450, 7.2F, Tiers.NETHERITE, 3.2F),
                    handle().durability(1.1F).attackDamage(1.25F).attackSpeed(1.1F).build(),
                    StatlessMaterialStats.BINDING,
                    limb(1450, -0.3F, 0.2F, 0.05F),
                    grip(0.1F, 3.2F, 0.05F),
                    StatlessMaterialStats.MAILLE)
            .trait(b -> b
                    .addDefault(TIModifierData.PURGATORY.asEntry())
                    .addArmor(TIModifierData.QUENCHED_BODY.asEntry()))
            .render(b -> b.color(-1480950).fallbacks("metal")
                    .sprite(b.color(-7719387, -2863099, -1810680, -1217506, -88295, -608967)))),
    FINAL_SHELL(MaterialBuildHolder.builder("final_shell")
            .craftableMaterial(TIItems.FINAL_SHELL)
            .desc(b -> b
                    .encyclopedia("The damage you deal pierces armor protection. Right-click to fire an ender pearl with a 20 second cooldown.")
                    .ranged("The damage you deal pierces armor protection.")
                    .armor("Taking critical damage will evade the damage and teleport you back to your spawn point, with a cooldown of 300 seconds."))
            .definition(MaterialDefinitionData.Builder::craftable)
            .statAndShield(plat(31f).armor(3.0F, 5.0F, 7.0F, 3.0F).toughness(2.5F),
                    head(990, 6.5F, Tiers.DIAMOND, 3.4F),
                    handle().durability(0.9F).miningSpeed(0.9F).attackSpeed(1.3F).build(),
                    StatlessMaterialStats.BINDING,limb(980, 0.3F, 0.1F, 0.1F),
                    grip(-0.1F, 3.1F, 0.1F),
                    StatlessMaterialStats.MAILLE)
            .trait(b -> b
                    .addMelee(TIModifierData.ENDER_RESONANCE.asEntry(), TIModifierData.FINAL_REINFORCEMENT.asEntry())
                    .addRanged(TIModifierData.FINAL_REINFORCEMENT.asEntry())
                    .addArmor(TIModifierData.PAST_MEMORIES.asEntry())).render(b -> b
                    .color(-3848061).fallbacks("crystal", "rock")
                    .sprite(b.color(-11662540, -8310180, -7059601, -3848061, -3312220, -2187835)))),
    BLACK_FLASH_ALLOY(MaterialBuildHolder.builder("black_flash_alloy")
            .metalMaterial(TIFluids.MOLTEN_BLACK_FLASH_ALLOY)
            .desc(b -> b
                    .encyclopedia("Your normal attacks will be treated as critical hits.")
                    .ranged("When fired, arrows are counted as fully charged.")
                    .armor("Reduces damage dealt by 5% each time you receive an attack of this type, up to a maximum of 9 times."))
            .statAndShield(plat(65f).armor(3.0F, 6.0F, 8.0F, 3.0F).toughness(2.0F).knockbackResistance(0.1F),
                    head(2100, 8.5F, Tiers.NETHERITE, 5.0F),
                    handle().durability(1.3F).miningSpeed(0.9F).attackSpeed(1.15F).build(),
                    StatlessMaterialStats.BINDING,
                    limb(2100, -0.2F, 0.15F, -0.05F),
                    grip(0.3f, 4.5F, -0.1F),
                    StatlessMaterialStats.MAILLE).trait(b -> b
                    .addDefault(TIModifierData.BE_IMPOLITE.asEntry())
                    .addArmor(TIModifierData.VULNERABILITY_INSURANCE.asEntry()))
            .render(b -> b.color(-12492898).fallbacks("metal")
                    .sprite(b.color(-15134380, -13217394, -12951404, -12492898, -9663283, -5389569)))),
    COLOURED_GLAZE_STAR(MaterialBuildHolder.builder("coloured_glaze_star")
            .craftableMaterial(TIItems.COLOURED_GLAZE_STAR)
            .desc(b -> b
                    .encyclopedia("Reduces durability by 49% per level and increases attack damage and mining speed by 49%. Your attacks are immune to damage that pierces the target.")
                    .ranged("Durability is reduced by 49% per level and projectile damage is increased by 49%. Your attacks are immune to damage that pierces the target.")
                    .armor("You will be immune to negative effects. Every 45 seconds, there is a 45% chance to increase armor by 2%, tenacity, and movement speed by 1%, and this effect can stack up to 10 times."))
            .definition(MaterialDefinitionData.Builder::craftable)
            .statAndShield(plat(40f).armor(3.0F, 5.0F, 7.0F, 3.0F).toughness(3.0F).knockbackResistance(0.1F),
                    head(1300, 8.0F, Tiers.NETHERITE, 5.0F),
                    handle().miningSpeed(1.1F).attackSpeed(1.2F).attackDamage(1.05F).build(),
                    StatlessMaterialStats.BINDING,
                    limb(1300, 0.25F, 0.1F, 0.1F),
                    grip(0.0F, 5.0F, 0.05F),
                    StatlessMaterialStats.MAILLE)
            .trait(b -> b
                    .addDefault(TIModifierData.PENETRATING_STAR.asEntry(), TIModifierData.DELICATE.asEntry())
                    .addArmor(TIModifierData.CRYSTALLIZATION.asEntry(), TIModifierData.SPOTLESS.asEntry()))
            .render(b -> b.color(-3546656).fallbacks("crystal")
                    .sprite(b.color(-10725715, -8358184, -7162447, -3546656, -5769481, -3997953)))),
    ELFS_CRYSTAL(MaterialBuildHolder.builder("elfs_crystal")
            .craftableMaterial(TIItems.ELFS_CRYSTAL)
            .desc(b -> b
                    .encyclopedia("In the backpack, right-click on the tool with a part to characterize the part onto the tool, and the part will be consumed. Right-click the tool with the tool to numerically transform the tool onto the tool, and the tool will remain."))
            .definition(MaterialDefinitionData.Builder::craftable)
            .statAndShield(plat(25f).armor(2.0F, 5.0F, 6.0F, 2.0F),
                    head(790, 7.0F, Tiers.DIAMOND, 2.7F),
                    handle().attackSpeed(1.1F).build(),
                    StatlessMaterialStats.BINDING,
                    limb(790, 0.15F, 0.1F, 0.0F),
                    grip(0.0F, 2.7F, 0.02F),
                    StatlessMaterialStats.MAILLE)
            .defaultTrait(TIModifierData.FRESH_WATER.asEntry())
            .render(b -> b.color(-16711780).fallbacks("crystal")
                    .sprite(b.color(-15186944, -16741010, -16711780, -16728093, -16721921, -16711690)))),
    PLAGUE_BONE(MaterialBuildHolder.builder("plague_bone")
            .craftableMaterial(TIItems.PLAGUE_BONE)
            .desc(b -> b
                    .encyclopedia("After attacking, the target will be inflicted with a deadly plague effect, deducting 1% of the target's current health per level."))
            .definition(b -> b.tier(3).craftable())
            .stat(false, null,
                    head(475, 5.5F, Tiers.IRON, 2.5F),
                    handle().durability(0.9F).attackSpeed(1.1F).attackDamage(1.05F).build(),
                    StatlessMaterialStats.BINDING,
                    limb(475, 0.1F, -0.1F, 0.15F),
                    grip(-0.1F, 2.5F, -0.05F))
            .defaultTrait(TIModifierData.DEADLY_PLAGUE.asEntry())
            .render(b -> b.color(-12619444).fallbacks("bone")
                    .sprite(b.color(-14929118, -13676745, -15122138, -12619444, -10256573, -9464456)))),
    COLORFUL_SLIME(MaterialBuildHolder.builder("colorful_slime")
            .metalMaterial(TIFluids.MOLTEN_COLORFUL_SLIME).desc(b -> b
                    .encyclopedia("Each slime material on this tool increases maximum durability and overslime by 15%. For every 1000 points of max overslime, the full value is increased by 5% per level."))
            .statAndShield(plat(29f).armor(3.0F, 5.0F, 7.0F, 3.0F).toughness(2.0F),
                    head(957, 7.0F, Tiers.DIAMOND, 3.2F),
                    handle().durability(0.9F).attackSpeed(1.2F).attackDamage(1.1F).build(),
                    StatlessMaterialStats.BINDING,
                    limb(957, 0.2F, 0.0F, 0.1F),
                    grip(-0.1F, 3.2F, 0.05F),
                    StatlessMaterialStats.MAILLE)
            .defaultTrait(TIModifierData.COLORFUL_SLIME.asEntry(), TIModifierData.COOPERATION.asEntry(), TIMaterialTraitGen.entry(TinkerModifiers.overslime.getId()))
            .render(b -> b.color(-8166254).fallbacks("slime")
                    .transformer(b.trans(TinkersIngenuity.loc("color/colorful_slime/1"),
                            TinkersIngenuity.loc("color/colorful_slime/2"), TinkersIngenuity.loc("color/colorful_slime/3"))))),
    KNIGHT_CRYSTAL(MaterialBuildHolder.builder("knight_crystal")
            .metalMaterial(TIFluids.MOLTEN_KNIGHT_CRYSTAL)
            .desc(b -> b
                    .encyclopedia("When attacking, increases the user's damage absorption by 1 per level, up to a maximum of 200% of maximum health, and when dealing a critical hit, all damage absorption is consumed and each damage absorbed is increased by 3%.")
                    .armor("When attacked, the damage taken this time with damage absorption will not exceed 45% of the wearer's maximum health."))
            .statAndShield(plat(50f).armor(3.0F, 5.0F, 7.0F, 3.0F).toughness(1.5F).knockbackResistance(0.1F),
                    head(1622, 7.5F, Tiers.NETHERITE, 3.5F),
                    handle().attackDamage(1.2F).durability(1.1F).miningSpeed(1.1F).build(),
                    StatlessMaterialStats.BINDING,
                    limb(1622, 0.1F, 0.15F, -0.05F),
                    grip(0.1F, 3.95F, 0.05F),
                    StatlessMaterialStats.MAILLE)
            .trait(b -> b
                    .addDefault(TIModifierData.CHIVALRY.asEntry())
                    .addArmor(TIModifierData.KNIGHT_BLOODLINE.asEntry()))
            .render(b -> b.color(-6984738).fallbacks("metal", "crystal")
                    .sprite(b.color(-12180661, -10606728, -8636771, -9024802, -6984738, -2637569)))),
    MITHRIL(MaterialBuildHolder.builder("mithril")
            .metalMaterial(TIFluids.MOLTEN_MITHRIL)
            .desc(b -> b
                    .encyclopedia("Each level increases 2 upgrade slots, increasing attack speed and mining speed by 12%.")
                    .ranged("Each level adds 2 upgrade slots and increases the initial velocity of arrows by 12%.")
                    .armor("Adds 2 upgrade slots per level, providing the wearer with 45% magic damage protection."))
            .statAndShield(plat(34f).armor(2f, 5f, 7f, 2f).toughness(3f),
                    head(1055, 8f, Tiers.DIAMOND, 3.1f),
                    handle().attackDamage(0.95f).durability(1.1f).attackSpeed(1.2f).build(),
                    StatlessMaterialStats.BINDING,
                    limb(1055, 0.12f, 0.1f, 0.05f),
                    grip(0.1f, 3f, 0f),
                    StatlessMaterialStats.MAILLE)
            .trait(b -> b
                    .addDefault(TIModifierData.MYSTERIOUS.asEntry(), TIModifierData.DEXTEROUS.asEntry())
                    .addArmor(TIModifierData.MYSTERIOUS.asEntry(), TIModifierData.DEMONIC.asEntry()))
            .render(b -> b.color(0xff1fa8b1).fallbacks("metal")
                    .sprite(b.color(0xff0a546a, 0xff13949c, 0xff1fa8b1, 0xff9dd290, 0xff59c2c9, 0xffd4ffbe))))
    ;

    public final MaterialBuildHolder holder;

    TIMaterials(MaterialBuildHolder.Builder builder) {
        this.holder = builder.build();
    }

    public MaterialId asMate() {
        return this.holder.asMate();
    }
}
