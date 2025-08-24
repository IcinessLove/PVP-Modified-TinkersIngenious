package com.xiaoyue.tinkers_ingenuity.content.generic;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import dev.xkmc.l2library.capability.player.PlayerCapabilityHolder;
import dev.xkmc.l2library.capability.player.PlayerCapabilityNetworkHandler;
import dev.xkmc.l2library.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

@SerialClass
public class MeleeCacheCapability extends PlayerCapabilityTemplate<MeleeCacheCapability> {

    public static final Capability<MeleeCacheCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static final PlayerCapabilityHolder<MeleeCacheCapability> HOLDER = new PlayerCapabilityHolder<>(
            TinkersIngenuity.loc("melee_cache"), CAPABILITY, MeleeCacheCapability.class, MeleeCacheCapability::new,
            PlayerCapabilityNetworkHandler::new);

    @SerialField
    private final Map<UUID, ItemStack> cache = new TreeMap<>();

    public static void saveTool(Player player, InteractionHand hand) {
        player.getCapability(CAPABILITY).ifPresent((cache) ->
                cache.saveTool(player.getUUID(), player.getItemInHand(hand)));
    }

    public static ItemStack getTool(Player player) {
        return player.getCapability(CAPABILITY).resolve().map((cache) ->
                cache.getTool(player.getUUID())).orElse(ItemStack.EMPTY);
    }

    public static void removeCache(Player player) {
        player.getCapability(CAPABILITY).ifPresent((cache) ->
                cache.removeCache(player.getUUID()));
    }

    public void saveTool(UUID attacker, ItemStack tool) {
        this.cache.put(attacker, tool);
    }

    public ItemStack getTool(UUID attacker) {
        return this.cache.get(attacker) == null ? ItemStack.EMPTY : this.cache.get(attacker);
    }

    public void removeCache(UUID attacker) {
        this.cache.remove(attacker);
    }

    public static void register() {
    }
}
