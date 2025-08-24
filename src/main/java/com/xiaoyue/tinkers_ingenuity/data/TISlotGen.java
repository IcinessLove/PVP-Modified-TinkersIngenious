package com.xiaoyue.tinkers_ingenuity.data;

import com.xiaoyue.celestial_invoker.content.generator.RegistrateRecordDataProvider;
import dev.xkmc.l2library.compat.curios.CurioEntityBuilder;
import dev.xkmc.l2library.compat.curios.CurioSlotBuilder;
import dev.xkmc.l2library.compat.curios.CurioSlotBuilder.Operation;
import dev.xkmc.l2library.compat.curios.SlotCondition;
import dev.xkmc.l2library.serial.config.RecordDataProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class TISlotGen extends RecordDataProvider {
    public TISlotGen(DataGenerator generator) {
        super(generator, "Tinkers Ingenuity slot providers");
    }

    public static void onRecordGen(RegistrateRecordDataProvider pvd) {
        pvd.map.put("tinkers_ingenuity/curios/slots/tinkers_curio",
                new CurioSlotBuilder(22, (new ResourceLocation("curios",
                        "slot/empty_curio_slot")).toString(), 1, Operation.SET));
        pvd.map.put("tinkers_ingenuity/curios/entities/player_vanilla",
                new CurioEntityBuilder(new ArrayList<>(List.of(new ResourceLocation("player"))),
                        new ArrayList<>(List.of("tinkers_curio")), SlotCondition.of()));
    }

    @Override
    public void add(BiConsumer<String, Record> cons) {
        cons.accept("tinkers_ingenuity/curios/slots/tinkers_curio",
                new CurioSlotBuilder(22, (new ResourceLocation("curios",
                        "slot/empty_curio_slot")).toString(), 1, Operation.SET));
        cons.accept("tinkers_ingenuity/curios/entities/player_vanilla",
                new CurioEntityBuilder(new ArrayList<>(List.of(new ResourceLocation("player"))),
                        new ArrayList<>(List.of("tinkers_curio")), SlotCondition.of()));
    }
}
