package com.xiaoyue.tinkers_ingenuity.data;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import dev.xkmc.l2damagetracker.contents.damage.DamageState;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

import java.util.Locale;
import java.util.function.Consumer;

public enum TIDamageState implements DamageState {
    BYPASS_ARMOR(DamageTypeTags.BYPASSES_ARMOR),
    BYPASS_MAGIC(L2DamageTypes.BYPASS_MAGIC.tags()),
    BYPASS_ENTITY_INV(DamageTypeTags.BYPASSES_INVULNERABILITY),
    BYPASS_COOLDOWN(DamageTypeTags.BYPASSES_COOLDOWN);

    private final TagKey<DamageType>[] keys;

    @SafeVarargs
    TIDamageState(TagKey<DamageType>... keys) {
        this.keys = keys;
    }

    @Override
    public void gatherTags(Consumer<TagKey<DamageType>> consumer) {
        for(TagKey<DamageType> key : this.keys) {
            consumer.accept(key);
        }
    }

    @Override
    public void removeTags(Consumer<TagKey<DamageType>> consumer) {
    }

    @Override
    public ResourceLocation getId() {
        return TinkersIngenuity.loc(this.name().toLowerCase(Locale.ROOT));
    }

    @Override
    public boolean overrides(DamageState state) {
        return false;
    }
}
