package com.kiocg.event.player;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player interacts with a bottle
 */
public abstract class PlayerBottleEvent extends PlayerEvent implements Cancellable {

    private final Block blockClicked;
    private final EquipmentSlot hand;
    private final ItemStack bottle;
    private ItemStack resultItem;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerBottleEvent(@NotNull final Player player, @NotNull final Block blockClicked, @NotNull final ItemStack bottle, @NotNull final ItemStack resultItem, @NotNull final EquipmentSlot hand) {
        super(player);
        this.blockClicked = blockClicked;
        this.bottle = bottle;
        this.resultItem = resultItem;
        this.hand = hand;
    }

    /**
     * Gets the block involved in this event.
     *
     * @return The Block which block is involved in this event
     */
    @NotNull
    public final Block getBlock() {
        return this.blockClicked;
    }

    /**
     * Returns the bottle used in this event
     *
     * @return the used bottle
     */
    @NotNull
    public ItemStack getBottle() {
        return this.bottle;
    }

    /**
     * Get the hand that was used in this event.
     *
     * @return the hand
     */
    @NotNull
    public EquipmentSlot getHand() {
        return this.hand;
    }

    /**
     * Get the resulting item in hand after the bottle event
     *
     * @return ItemStack hold in hand after the event.
     */
    @NotNull
    public ItemStack getResultItem() {
        return this.resultItem;
    }

    /**
     * Set the item in hand after the event
     *
     * @param itemStack the new held ItemStack after the bottle event.
     */
    public void setResultItem(@NotNull ItemStack itemStack) {
        this.resultItem = itemStack;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
