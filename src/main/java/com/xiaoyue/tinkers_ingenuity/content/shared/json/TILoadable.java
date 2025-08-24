package com.xiaoyue.tinkers_ingenuity.content.shared.json;

import com.xiaoyue.tinkers_ingenuity.data.TIDamageState;
import slimeknights.mantle.data.loadable.primitive.EnumLoadable;

public class TILoadable {

    public static final EnumLoadable<TIDamageState> DAMAGE_STATE = new EnumLoadable<>(TIDamageState.class);

}
