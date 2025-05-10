package com.kiocg.event.player;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player empties a bottle
 */
public class PlayerBottleEmptyEvent extends PlayerBottleEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @ApiStatus.Internal
    public PlayerBottleEmptyEvent(@NotNull final Player player, @NotNull final Block blockClicked, @NotNull final ItemStack bottle, @NotNull final ItemStack resultItem, @NotNull final EquipmentSlot hand) {
        super(player, blockClicked, bottle, resultItem, hand);
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
