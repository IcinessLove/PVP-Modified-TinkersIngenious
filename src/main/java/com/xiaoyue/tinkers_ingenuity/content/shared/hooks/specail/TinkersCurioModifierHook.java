package com.xiaoyue.tinkers_ingenuity.content.shared.hooks.specail;

import com.xiaoyue.tinkers_ingenuity.content.items.ModifiableCurio;
import com.xiaoyue.tinkers_ingenuity.content.shared.holder.CurioStackView;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurio.DropRule;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.UUID;
import java.util.function.BiConsumer;

public interface TinkersCurioModifierHook {

    TinkersCurioModifierHook EMPTY = new TinkersCurioModifierHook() {
    };

    default void onCurioTick(CurioStackView curio, int level, LivingEntity entity) {
    }

    default void addAttributes(CurioStackView curio, int level, UUID uuid, BiConsumer<Attribute, AttributeModifier> cons) {
    }

    default int getLootingLevel(CurioStackView curio, int level, DamageSource source, LivingEntity target, int origin) {
        return origin;
    }

    default int getFortuneLevel(CurioStackView curio, int level, LootContext loot, int origin) {
        return origin;
    }

    default void onChangeCurio(CurioStackView curio, int level, ItemStack exchanged, ModifiableCurio.ChangeType type) {
    }

    default boolean canChangeCurio(CurioStackView curio, int level, ModifiableCurio.ChangeType type) {
        return true;
    }

    default ICurio.DropRule getCurioDropRule(CurioStackView curio, int level, DamageSource source, int looting, boolean recentlyHit) {
        return DropRule.DEFAULT;
    }

    default boolean canImmuneAttack(CurioStackView curio, int level, LivingEntity entity, DamageSource source, float damage) {
        return false;
    }

    default void onDamageTargetPre(CurioStackView curio, int level, LivingEntity attacker, LivingEntity target, LivingHurtEvent event) {
    }

    default void onDamageTargetPost(CurioStackView curio, int level, LivingEntity attacker, LivingEntity target, LivingDamageEvent event) {
    }

    default void onTakenDamagePre(CurioStackView curio, int level, LivingEntity entity, DamageSource source, LivingHurtEvent event) {
    }

    default void onTakenDamagePost(CurioStackView curio, int level, LivingEntity entity, DamageSource source, LivingDamageEvent event) {
    }

    default float onLivingHeal(CurioStackView curio, int level, LivingEntity entity, float amount) {
        return amount;
    }

    default void onShootProjectile(CurioStackView curio, int level, LivingEntity shooter, Projectile proj, @Nullable AbstractArrow arrow, ModDataNBT nbt) {
    }

    default boolean onProjectileHit(CurioStackView curio, int level, LivingEntity shooter, Projectile projectile, @Nullable AbstractArrow arrow, HitResult hit, ModDataNBT nbt) {
        return false;
    }

    default void getBreakSpeed(CurioStackView curio, int level, LivingEntity entity, PlayerEvent.BreakSpeed event, boolean isEffective) {
    }

    default void onBreakBlock(CurioStackView curio, int level, LivingEntity entity, BlockState state, BlockPos pos) {
    }

    record AllMerger(Collection<TinkersCurioModifierHook> modules) implements TinkersCurioModifierHook {
        @Override
        public void onCurioTick(CurioStackView curio, int level, LivingEntity entity) {
            for(TinkersCurioModifierHook module : this.modules) {
                module.onCurioTick(curio, level, entity);
            }
        }

        @Override
        public void addAttributes(CurioStackView tool, int level, UUID uuid, BiConsumer<Attribute, AttributeModifier> cons) {
            for(TinkersCurioModifierHook module : this.modules) {
                module.addAttributes(tool, level, uuid, cons);
            }
        }

        @Override
        public int getLootingLevel(CurioStackView curio, int level, DamageSource source, LivingEntity target, int origin) {
            for(TinkersCurioModifierHook module : this.modules) {
                origin = module.getLootingLevel(curio, level, source, target, origin);
            }
            return origin;
        }

        @Override
        public int getFortuneLevel(CurioStackView curio, int level, LootContext loot, int origin) {
            for(TinkersCurioModifierHook module : this.modules) {
                origin = module.getFortuneLevel(curio, level, loot, origin);
            }
            return origin;
        }

        @Override
        public void onChangeCurio(CurioStackView curio, int level, ItemStack exchanged, ModifiableCurio.ChangeType type) {
            for(TinkersCurioModifierHook module : this.modules) {
                module.onChangeCurio(curio, level, exchanged, type);
            }
        }

        @Override
        public boolean canChangeCurio(CurioStackView curio, int level, ModifiableCurio.ChangeType type) {
            for (TinkersCurioModifierHook module : modules) {
                return module.canChangeCurio(curio, level, type);
            }
            return true;
        }

        @Override
        public ICurio.DropRule getCurioDropRule(CurioStackView curio, int level, DamageSource source, int looting, boolean recent) {
            for (TinkersCurioModifierHook module : modules) {
                return module.getCurioDropRule(curio, level, source, looting, recent);
            }
            return DropRule.DEFAULT;
        }

        @Override
        public boolean canImmuneAttack(CurioStackView curio, int level, LivingEntity entity, DamageSource source, float damage) {
            for (TinkersCurioModifierHook module : modules) {
                return module.canImmuneAttack(curio, level, entity, source, damage);
            }
            return false;
        }

        @Override
        public void onDamageTargetPre(CurioStackView curio, int level, LivingEntity attacker, LivingEntity target, LivingHurtEvent event) {
            for(TinkersCurioModifierHook module : this.modules) {
                module.onDamageTargetPre(curio, level, attacker, target, event);
            }
        }

        @Override
        public void onDamageTargetPost(CurioStackView curio, int level, LivingEntity attacker, LivingEntity target, LivingDamageEvent event) {
            for(TinkersCurioModifierHook module : this.modules) {
                module.onDamageTargetPost(curio, level, attacker, target, event);
            }
        }

        @Override
        public void onTakenDamagePre(CurioStackView curio, int level, LivingEntity entity, DamageSource source, LivingHurtEvent event) {
            for(TinkersCurioModifierHook module : this.modules) {
                module.onTakenDamagePre(curio, level, entity, source, event);
            }

        }

        @Override
        public void onTakenDamagePost(CurioStackView curio, int level, LivingEntity entity, DamageSource source, LivingDamageEvent event) {
            for(TinkersCurioModifierHook module : this.modules) {
                module.onTakenDamagePost(curio, level, entity, source, event);
            }
        }

        @Override
        public float onLivingHeal(CurioStackView curio, int level, LivingEntity entity, float amount) {
            for (TinkersCurioModifierHook module : modules) {
                return module.onLivingHeal(curio, level, entity, amount);
            }
            return amount;
        }

        @Override
        public void onShootProjectile(CurioStackView curio, int level, LivingEntity shooter, Projectile proj, @Nullable AbstractArrow arrow, ModDataNBT nbt) {
            for(TinkersCurioModifierHook module : this.modules) {
                module.onShootProjectile(curio, level, shooter, proj, arrow, nbt);
            }
        }

        @Override
        public boolean onProjectileHit(CurioStackView curio, int level, LivingEntity shooter, Projectile projectile, @Nullable AbstractArrow arrow, HitResult hit, ModDataNBT nbt) {
            for (TinkersCurioModifierHook module : modules) {
                return module.onProjectileHit(curio, level, shooter, projectile, arrow, hit, nbt);
            }
            return false;
        }

        @Override
        public void getBreakSpeed(CurioStackView curio, int level, LivingEntity entity, PlayerEvent.BreakSpeed event, boolean isEffective) {
            for(TinkersCurioModifierHook module : this.modules) {
                module.getBreakSpeed(curio, level, entity, event, isEffective);
            }
        }

        @Override
        public void onBreakBlock(CurioStackView curio, int level, LivingEntity entity, BlockState state, BlockPos pos) {
            for(TinkersCurioModifierHook module : this.modules) {
                module.onBreakBlock(curio, level, entity, state, pos);
            }
        }
    }
}
