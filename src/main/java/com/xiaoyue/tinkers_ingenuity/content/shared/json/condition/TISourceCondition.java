package com.xiaoyue.tinkers_ingenuity.content.shared.json.condition;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.generic.SerialLoader;
import net.minecraft.world.damagesource.DamageTypes;
import slimeknights.mantle.data.predicate.damage.DamageSourcePredicate;

public class TISourceCondition {

    public static final DamageSourcePredicate IS_MAGIC = DamageSourcePredicate.simple(e ->
            e.is(DamageTypes.MAGIC)
    );

    @SerialLoader
    public static void registerPredicate() {
        DamageSourcePredicate.LOADER.register(TinkersIngenuity.loc("is_magic"), IS_MAGIC.getLoader());
    }
}
