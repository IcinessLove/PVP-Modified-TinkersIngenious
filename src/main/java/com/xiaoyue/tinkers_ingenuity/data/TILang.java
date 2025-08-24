package com.xiaoyue.tinkers_ingenuity.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.xiaoyue.tinkers_ingenuity.content.shared.material.MaterialDescData;
import com.xiaoyue.tinkers_ingenuity.data.material.TIMaterials;
import com.xiaoyue.tinkers_ingenuity.data.modifier.TIModifierData;
import com.xiaoyue.tinkers_ingenuity.register.TIFluids;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import slimeknights.mantle.registration.object.FluidObject;

public enum TILang {
    TINKERS_MEDAL("item.tinkers_ingenuity.tinkers_medal", "Tinkers Medal",
            "A modular cosmetic created by a craftsman that uses two pieces that can be worn in the amulet position and increase the wearer's stats based on stats."),
    BLOWPIPE("item.tinkers_ingenuity.blowpipe", "Blowpipe",
            "A modular ranged weapon that fires in bursts using two parts, but fires arrows at a low velocity."),
    CURIO_MAIN_MATE_STAT("stat.tinkers_ingenuity.curio_main", "The main part of the accessory"),
    CURIO_EXTRA_MATE_STAT("stat.tinkers_ingenuity.curio_extra", "Accessories sub-parts"),
    CURIO_MOVEMENT_SPEED_TOOL_STAT("tool_stat.tinkers_ingenuity.curio_movement_speed", "Movement Speed:",
            "How much more can I increase your movement speed."),
    CURIO_MAX_HEALTH_TOOL_STAT("tool_stat.tinkers_ingenuity.curio_max_health", "Max Health:",
            "How much max life can be boosted for you."),
    CURIO_ARMOR_TOOL_STAT("tool_stat.tinkers_ingenuity.curio_armor", "Armor:",
            "How much projectile attack damage can be increased for you."),
    CURIO_MELEE_ATTACK_TOOL_STAT("tool_stat.tinkers_ingenuity.curio_melee_attack", "Melee Attack Damage:",
            "How much armor can I improve for you."),
    CURIO_PROJECTILE_ATTACK_TOOL_STAT("tool_stat.tinkers_ingenuity.curio_projectile_attack", "Projectile Attack Damage:",
            "How much projectile attack damage can be increased for you."),
    TINKERS_CURIO_SLOT("curios.identifier.tinkers_curio", "Tinkers Curio"),
    TINKERS_CURIO_SLOT_MODIFIER("curios.modifiers.tinkers_curio", "When worn as tinkers curio:");

    private final String key;
    private final String text;
    private String desc;

    TILang(String key, String text) {
        this.key = key;
        this.text = text;
    }

    TILang(String key, String text, String desc) {
        this.key = key;
        this.text = text;
        this.desc = desc;
    }

    public MutableComponent get(boolean isDesc) {
        return isDesc && this.desc != null ? Component.translatable(this.key + ".description") : Component.translatable(this.key);
    }

    public static void addLang(RegistrateLangProvider pvd) {
        for(TILang lang : values()) {
            pvd.add(lang.key, lang.text);
            if (lang.desc != null) {
                pvd.add(lang.key + ".description", lang.desc);
            }
        }

        for(FluidObject<ForgeFlowingFluid> fluid : TIFluids.FLUID_OBJECTS) {
            String fluid_name = fluid.getId().getPath();
            pvd.add(fluid.getType().getDescriptionId(), usName(fluid_name));
            pvd.add(fluid.getBucket().getDescriptionId(), usName(fluid_name + "_bucket"));
        }

        for(TIModifierData holder : TIModifierData.values()) {
            pvd.add(holder.langKey(), holder.asName());
            pvd.add(holder.flavorKey(), holder.flavor);
            pvd.add(holder.descKey(), holder.desc);
        }

        for(TIMaterials mate : TIMaterials.values()) {
            MaterialDescData id = mate.holder.desc();
            pvd.add(id.langKey(), id.name());
            id.caseAddLangData(pvd);
        }

    }

    private static String usName(String id) {
        return RegistrateLangProvider.toEnglishName(id);
    }
}
