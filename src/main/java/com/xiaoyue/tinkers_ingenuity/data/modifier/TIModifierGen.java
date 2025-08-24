package com.xiaoyue.tinkers_ingenuity.data.modifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.modifier.defense.Crystallization;
import com.xiaoyue.tinkers_ingenuity.content.modifier.defense.KnightBloodline;
import com.xiaoyue.tinkers_ingenuity.content.modifier.defense.VulnerabilityInsurance;
import com.xiaoyue.tinkers_ingenuity.content.modifier.general.Chivalry;
//import com.xiaoyue.tinkers_ingenuity.content.modifier.general.FinalReinforcement;
import com.xiaoyue.tinkers_ingenuity.content.module.building.AllFloatStatCopyModule;
import com.xiaoyue.tinkers_ingenuity.content.module.building.StatWithTraitCountModule;
import com.xiaoyue.tinkers_ingenuity.content.module.combat.CreateSourceModule;
import com.xiaoyue.tinkers_ingenuity.content.module.combat.CriticalChanceModule;
import com.xiaoyue.tinkers_ingenuity.content.module.combat.ExtraMagicAttackModule;
import com.xiaoyue.tinkers_ingenuity.content.module.combat.ForceAddEffectModule;
import com.xiaoyue.tinkers_ingenuity.content.module.curios.*;
import com.xiaoyue.tinkers_ingenuity.content.module.defense.SimpleProtectionModule;
import com.xiaoyue.tinkers_ingenuity.content.module.defense.SourceOrEntityProtectionModule;
import com.xiaoyue.tinkers_ingenuity.content.module.mixed.MixedModificationModule;
import com.xiaoyue.tinkers_ingenuity.content.shared.json.action.LivingEntityAction;
import com.xiaoyue.tinkers_ingenuity.content.shared.json.action.MultiBonusHelper;
import com.xiaoyue.tinkers_ingenuity.content.shared.json.action.ProjectileDataAction;
import com.xiaoyue.tinkers_ingenuity.content.shared.json.condition.TIEntityCondition;
import com.xiaoyue.tinkers_ingenuity.content.shared.json.condition.TISourceCondition;
import com.xiaoyue.tinkers_ingenuity.content.shared.json.variable.LevelingFormula;
import com.xiaoyue.tinkers_ingenuity.content.shared.json.variable.StatOperation;
import com.xiaoyue.tinkers_ingenuity.data.TIDamageState;
import com.xiaoyue.tinkers_ingenuity.register.TIEffects;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import slimeknights.tconstruct.library.modifiers.modules.combat.MeleeAttributeModule;
import slimeknights.mantle.data.predicate.damage.DamageSourcePredicate;
import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;
import slimeknights.tconstruct.library.data.tinkering.AbstractModifierProvider;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.modifiers.modules.build.ModifierSlotModule;
import slimeknights.tconstruct.library.modifiers.modules.build.StatBoostModule;
import slimeknights.tconstruct.library.modifiers.util.ModifierLevelDisplay;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;


public class TIModifierGen extends AbstractModifierProvider{
    public TIModifierGen(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void addModifiers() {
        buildModifier(TIModifierData.PURGATORY.getId())
                .addModule(MixedModificationModule.get(LivingEntityPredicate.ON_FIRE, MultiBonusHelper.addAll(ToolStats.PROJECTILE_DAMAGE),
                        LevelingFormula.mulBase(0.2F)));
        buildModifier(TIModifierData.AFTERSHOCK.getId())
                .addModule(ExtraMagicAttackModule.any(LevelingFormula.mulTotal(0.1F)));
        buildModifier(TIModifierData.QUENCHED_BODY.getId())
                .addModule(SourceOrEntityProtectionModule.get(DamageSourcePredicate.tag(DamageTypeTags.IS_FIRE), LivingEntityPredicate.ON_FIRE,
                        LevelingFormula.add(2.0F), LevelingFormula.add(1.0F)));
        buildModifier(TIModifierData.BE_IMPOLITE.getId())
                .addModule(CriticalChanceModule.any(1.0F));
        buildModifier(TIModifierData.FINAL_REINFORCEMENT.getId())
  // 近战固定穿甲
                .addModule(MeleeAttributeModule.builder(Attributes.ARMOR, Operation.MULTIPLY_BASE).eachLevel(-0.1f))
                .addModule(StatBoostModule.multiplyBase(ToolStats.PROJECTILE_DAMAGE).flat(0.2F));
        //buildModifier(TIModifierData.PENETRATING_STAR.getId())
                //.addModule(CreateSourceModule.any(TIDamageState.BYPASS_ENTITY_INV))
                //.addModule(CreateSourceModule.any(TIDamageState.BYPASS_COOLDOWN));
        buildModifier(TIModifierData.DELICATE.getId())
                .addModule(StatBoostModule.multiplyBase(ToolStats.MINING_SPEED).flat(0.49F))
                .addModule(StatBoostModule.multiplyBase(ToolStats.ATTACK_DAMAGE).flat(0.0F))
                .addModule(StatBoostModule.multiplyBase(ToolStats.PROJECTILE_DAMAGE).flat(0.49F))
                .addModule(StatBoostModule.multiplyBase(ToolStats.DURABILITY).flat(-0.49F));
        buildModifier(TIModifierData.DEADLY_PLAGUE.getId())
                .addModule(ForceAddEffectModule.get(TIEffects.DEADLY_PLAGUE.get(), 1, 200));
        buildModifier(TIModifierData.COLORFUL_SLIME.getId())
                .addModule(AllFloatStatCopyModule.get(OverslimeModifier.OVERSLIME_STAT,
                        StatOperation.multiplier_base, 5.0E-5F)).priority(22);
        buildModifier(TIModifierData.COOPERATION.getId()).levelDisplay(ModifierLevelDisplay.NO_LEVELS)
                .addModule(StatWithTraitCountModule.get(ToolStats.DURABILITY, StatOperation.multiplier_base, 0.15F,
                        true, TinkerModifiers.overslime.getId()))
                .addModule(StatWithTraitCountModule.get(OverslimeModifier.OVERSLIME_STAT, StatOperation.multiplier_base, 0.15F,
                        true, TinkerModifiers.overslime.getId()));
        buildModifier(TIModifierData.CRYSTALLIZATION.getId())
                .levelDisplay(ModifierLevelDisplay.NO_LEVELS)
                .addModule(Crystallization.getIns());
        buildModifier(TIModifierData.VULNERABILITY_INSURANCE.getId())
                .levelDisplay(ModifierLevelDisplay.NO_LEVELS)
                .addModule(VulnerabilityInsurance.getIns());
        buildModifier(TIModifierData.CHIVALRY.getId())
                .addModule(Chivalry.getIns());
        buildModifier(TIModifierData.RAPID_FIRE.getId())
                .levelDisplay(ModifierLevelDisplay.NO_LEVELS)
                .addModule(StatBoostModule.add(ToolStats.USE_ITEM_SPEED).flat(1.0F));
        buildModifier(TIModifierData.LITHE.getId())
                .addModule(CAttributeAdderModule.get(TinkersIngenuity.loc("lithe"), Attributes.ATTACK_SPEED, 1, 0.05))
                .addModule(CShootProjectileModule.any(ProjectileDataAction.scale_velocity, LevelingFormula.mulBase(0.05F)))
                .addModule(CBreakSpeedModule.any(LevelingFormula.mulBase(0.08F), false));
        buildModifier(TIModifierData.COLD_BLOODED.getId())
                .addModule(CModifyAttackModule.get(TIEntityCondition.FULL_HEALTH, LivingEntityPredicate.ANY,
                        LevelingFormula.mulBase(0.5F)));
        buildModifier(TIModifierData.CRUSH.getId())
                .addModule(CBreakSpeedModule.any(LevelingFormula.mulBase(0.25F), true));
        buildModifier(TIModifierData.DEVOURING_LIFE.getId())
                .addModule(CAfterAttackModule.get(TIEntityCondition.FULL_CHARGED, LivingEntityPredicate.ANY,
                        LivingEntityAction.heal_entity, 1.0F, true));
        buildModifier(TIModifierData.FORGE_FIRE.getId())
                .addModule(CAttackImmuneModule.get(DamageSourcePredicate.tag(DamageTypeTags.IS_FIRE), 0.1,
                        LivingEntityAction.heal_entity, 1.0f));
        buildModifier(TIModifierData.BLOT_OUT.getId()).levelDisplay(ModifierLevelDisplay.NO_LEVELS);
        buildModifier(TIModifierData.WALK_SNOW.getId()).levelDisplay(ModifierLevelDisplay.NO_LEVELS);
        buildModifier(TIModifierData.GOLDEN.getId()).levelDisplay(ModifierLevelDisplay.NO_LEVELS);
        buildModifier(TIModifierData.MYSTERIOUS.getId())
                .addModule(ModifierSlotModule.slot(SlotType.UPGRADE).eachLevel(2));
        buildModifier(TIModifierData.DEXTEROUS.getId())
                .addModule(MixedModificationModule.get(LivingEntityPredicate.ANY, new MultiBonusHelper(true,
                        false, ToolStats.VELOCITY), LevelingFormula.mulBase(0.12f)))
                .addModule(AttributeModule.builder(Attributes.ATTACK_SPEED, AttributeModifier.Operation.MULTIPLY_BASE).eachLevel(0.12f));
        buildModifier(TIModifierData.DEMONIC.getId())
                .addModule(SimpleProtectionModule.any(TISourceCondition.IS_MAGIC, LevelingFormula.mulBase(0.45f)));
    }

    @Override
    public String getName() {
        return "Tinkers Ingenuity modifier providers";
    }
}
