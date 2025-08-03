package com.kiocg.event.player;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a player fills a bottle
 */
@NullMarked
public class PlayerBottleFillEvent extends PlayerBottleEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @ApiStatus.Internal
    public PlayerBottleFillEvent(final Player player, final Block blockClicked, final ItemStack bottle, final ItemStack resultItem, final EquipmentSlot hand) {
        super(player, blockClicked, bottle, resultItem, hand);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
