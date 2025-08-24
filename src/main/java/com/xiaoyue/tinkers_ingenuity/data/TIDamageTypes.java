package com.xiaoyue.tinkers_ingenuity.data;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import dev.xkmc.l2damagetracker.contents.damage.DamageTypeRoot;
import dev.xkmc.l2damagetracker.contents.damage.DamageTypeWrapper;
import dev.xkmc.l2damagetracker.contents.damage.DamageWrapperTagProvider;
import dev.xkmc.l2damagetracker.init.data.DamageTypeAndTagsGen;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class TIDamageTypes extends DamageTypeAndTagsGen {

    public static final ResourceKey<DamageType> MAGIC = createDamage("magic");
    public static final DamageTypeRoot PLAYER_ATTACK;
    public static final DamageTypeRoot MOB_ATTACK;
    public static final DamageTypeRoot ARROW;
    protected static final List<DamageTypeWrapper> LIST;

    public TIDamageTypes(PackOutput output, CompletableFuture<HolderLookup.Provider> pvd, ExistingFileHelper helper) {
        super(output, pvd, helper, "tinkers_ingenuity");
        (new DamageTypeAndTagsGen.DamageTypeHolder(MAGIC, new DamageType("magic", DamageScaling.NEVER, 0.1F)))
                .add(DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.BYPASSES_COOLDOWN, DamageTypeTags.AVOIDS_GUARDIAN_THORNS, L2DamageTypes.MAGIC, L2DamageTypes.NO_SCALE);
    }

    public static void register() {
        for(TIDamageState state : TIDamageState.values()) {
            PLAYER_ATTACK.add(state);
            MOB_ATTACK.add(state);
            ARROW.add(state);
        }
        DamageTypeRoot.configureGeneration(Set.of("tinkers_ingenuity"), "tinkers_ingenuity", LIST);
    }

    @Override
    protected void addDamageTypes(BootstapContext<DamageType> ctx) {
        super.addDamageTypes(ctx);
        DamageTypeRoot.generateAll();
        for(DamageTypeWrapper wrapper : LIST) {
            ctx.register(wrapper.type(), wrapper.getObject());
        }
    }

    @Override
    protected void addDamageTypeTags(DamageWrapperTagProvider pvd, HolderLookup.Provider lookup) {
        super.addDamageTypeTags(pvd, lookup);
        DamageTypeRoot.generateAll();
        for(DamageTypeWrapper wrapper : LIST) {
            wrapper.gen(pvd, lookup);
        }
    }

    public static DamageSource magic(LivingEntity attacker) {
        return new DamageSource(getDamageSource(attacker.level(), MAGIC), attacker);
    }

    private static Holder.Reference<DamageType> getDamageSource(Level level, ResourceKey<DamageType> key) {
        return level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key);
    }

    private static ResourceKey<DamageType> createDamage(String id) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, TinkersIngenuity.loc(id));
    }

    static {
        PLAYER_ATTACK = new DamageTypeRoot("tinkers_ingenuity", DamageTypes.PLAYER_ATTACK, List.of(L2DamageTypes.DIRECT), (type) -> new DamageType("player", 0.1F));
        MOB_ATTACK = new DamageTypeRoot("tinkers_ingenuity", DamageTypes.MOB_ATTACK, List.of(L2DamageTypes.DIRECT), (type) -> new DamageType("mob", 0.1F));
        ARROW = new DamageTypeRoot("tinkers_ingenuity", DamageTypes.ARROW, List.of(DamageTypeTags.IS_PROJECTILE), (type) -> new DamageType("arrow", 0.1F));
        LIST = new ArrayList<>();
    }
}
