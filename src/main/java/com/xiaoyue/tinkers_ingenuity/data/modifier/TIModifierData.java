package com.xiaoyue.tinkers_ingenuity.data.modifier;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierId;

public enum TIModifierData {
    AFTERSHOCK("aftershock", "Post dinner dessert.",
            "Deal an additional magic damage after the attack"),
    PURGATORY("purgatory", "The flames of hell are tempering me!",
            "When you're on fire, the tool's attack damage and mining speed are greatly increased"),
    QUENCHED_BODY("quenched_body", "I have been born again in the fire!",
            "Increased armor's protection against fire damage, and additionally increased protection against non-fire damage when you're on fire"),
    ENDER_RESONANCE("ender_resonance", "No one can trap me!",
            "Right-click to shoot an ender pearl in front of you, then go on cooldown"),
    FINAL_REINFORCEMENT("final_reinforcement", "I have reached the end of the world!",
            "Attack damage is increased, and the damage dealt pierces through armor protection"),
    PAST_MEMORIES("past_memories", "What just flashed past?",
            "Avoid critical damage and teleport you back to the spawn point, then go on cooldown"),
    BE_IMPOLITE("be_impolite", "Aren't you big or small?",
            "Your normal attacks will be treated as critical hits, and when you fire arrows, they will be counted as full charges"),
    VULNERABILITY_INSURANCE("vulnerability_insurance", "The same trick didn't work for me!",
            "Each time you are attacked, you will be less likely to deal damage from subsequent attacks of this type"),
    PENETRATING_STAR("penetrating_star", "I will go through the stars!",
            "Your attacks are immune to damage that pierces the target"),
    DELICATE("delicate", "I have a glass 'star'.",
            "The tool will be much more effective, but it will become more fragile"),
    CRYSTALLIZATION("crystallization", "It's starting to grow!",
            "This piece of armor becomes more pliable and lightweight over time"),
    SPOTLESS("spotless", "In pristine condition.",
            "This piece of armor will help protect you from negative effects"),
    FRESH_WATER("fresh_water", "The surface of the water is like a mirror, reflecting you and me.",
            "Use a part or the same tool that is compatible with the tool in your backpack, right-click on the tool to numerize the feature or tool onto the tool"),
    DEADLY_PLAGUE("deadly_plague", "Beware of infections!",
            "After attacking, the target will take Plague damage over time based on their current health"),
    COLORFUL_SLIME("colorful_slime", "The world is so colorful!",
            "The stats of this tool will increase as the maximum overslime increases"),
    COOPERATION("cooperation", "We are united!",
            "The durability and overslime of this tool will increase as the amount of slime material on the tool"),
    CHIVALRY("chivalry", "All are welcome!",
            "This tool stacks damage absorption for the user each time it attacks, and when it deals a critical hit, it consumes all damage absorbed and increases attack damage"),
    KNIGHT_BLOODLINE("knight_bloodline", "Justice leads me!",
            "Increases damage absorption for the wearer when attacked, and provides more protection when possessing damage absorption"),
    RAPID_FIRE("rapid_fire", "Shoot while running.",
            "The user will no longer be affected by movement speed falloff when the bow is drawn"),
    COLD_BLOODED("cold_blooded", "Atrocious!",
            "Greatly increases the attack damage you deal when attacking a target at full health"),
    LITHE("lithe", "Light as a feather.",
            "The wearer's digging speed, attack speed, and arrow speed are increased"),
    FORGE_FIRE("forge_fire", "Flame Tempering!",
            "When hit by a fire-type attack, there is a chance that you will be immune to the attack and regain health"),
    CRUSH("crush", "Don't break your bones!",
            "Greatly increases mining speed when mining blocks within the range of the self-mining level"),
    DEVOURING_LIFE("devouring_life", "I'm a vampire!",
            "Restores the wearer's health when attacking with full force, and even more when dealing critical hits"),
    BLOT_OUT("blot_out", "Did you see that?",
            "Eye contact with endermen does not provoke hatred towards it"),
    WALK_SNOW("walk_snow", "Light work on the snow.",
            "The wearer is able to walk on fine snow"),
    GOLDEN("golden", "I'm a good friend of the piglins!",
            "Piglins do not actively develop hatred towards the wearer"),
    MYSTERIOUS("mysterious", "Mythical metal!",
            "This tool will get more upgrade slots"),
    DEXTEROUS("dexterous", "It's easy to get started.",
            "This tool's mining speed, attack speed, and arrow velocity will be given additional boosts"),
    DEMONIC("demonic", "Don't hit me with these unrealistic things.",
            "This armor provides the wearer with additional protection against magic damage"),
    ;

    public final String id;
    public final String flavor;
    public final String desc;

    TIModifierData(String id, String flavor, String desc) {
        this.id = id;
        this.flavor = flavor;
        this.desc = desc;
    }

    public String asName() {
        return RegistrateLangProvider.toEnglishName(this.id);
    }

    public ModifierId getId() {
        return this.getId("tinkers_ingenuity");
    }

    public ModifierId getId(String modid) {
        return new ModifierId(modid, this.id);
    }

    public ModifierEntry asEntry(int lv) {
        return new ModifierEntry(this.getId(), lv);
    }

    public ModifierEntry asEntry() {
        return this.asEntry(1);
    }

    public String langKey() {
        return "modifier.tinkers_ingenuity." + this.id;
    }

    public String flavorKey() {
        return this.langKey() + ".flavor";
    }

    public String descKey() {
        return this.langKey() + ".description";
    }
}
