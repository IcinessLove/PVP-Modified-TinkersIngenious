package com.xiaoyue.tinkers_ingenuity.content.shared.json.action;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import slimeknights.mantle.data.loadable.primitive.EnumLoadable;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.primitive.StringLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;

public record EntityStatGetter(Type type, String operation, float factor) {

    public static final EnumLoadable<Type> TYPE_LOADER = new EnumLoadable<>(Type.class);
    public static final RecordLoadable<EntityStatGetter> LOADER = RecordLoadable.create(
            TYPE_LOADER.requiredField("type", EntityStatGetter::type),
            StringLoadable.DEFAULT.requiredField("operation", EntityStatGetter::operation),
            FloatLoadable.ANY.defaultField("factor", 1.0f, EntityStatGetter::factor),
            EntityStatGetter::new
    );

    public float apply(LivingEntity entity) {
        return switch (this.operation) {
            case "multiplier" -> this.type.get(entity) * this.factor;
            case "addition" -> this.type.get(entity) + this.factor;
            default -> throw new IllegalStateException("Unexpected value: " + this.operation);
        };
    }

    public enum Type {
        absorption {
            @Override
            public float get(LivingEntity entity) {
                if (entity instanceof Player player) {
                    return player.getAbsorptionAmount();
                }
                return 0f;
            }
        },
        health {
            @Override
            public float get(LivingEntity entity) {
                return entity.getHealth();
            }
        },
        miss_health {
            @Override
            public float get(LivingEntity entity) {
                return entity.getMaxHealth() - entity.getHealth();
            }
        },
        max_health {
            @Override
            public float get(LivingEntity entity) {
                return entity.getMaxHealth();
            }
        };

        public abstract float get(LivingEntity var1);
    }
}
