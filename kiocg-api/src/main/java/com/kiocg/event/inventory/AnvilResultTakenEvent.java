package com.kiocg.event.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AnvilResultTakenEvent extends InventoryEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final ItemStack oldResult;
    private final ItemStack oldFirstItem;
    private final ItemStack oldSecondItem;

    public AnvilResultTakenEvent(@NotNull HumanEntity player, @NotNull InventoryView view, @NotNull ItemStack result, @NotNull ItemStack firstItem, @NotNull ItemStack secondItem) {
        super(view);
        this.player = (Player) player;
        this.oldResult = result;
        this.oldFirstItem = firstItem;
        this.oldSecondItem = secondItem;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public ItemStack getOldResult() {
        return oldResult;
    }

    @NotNull
    public ItemStack getOldFirstItem() {
        return oldFirstItem;
    }

    @NotNull
    public ItemStack getOldSecondItem() {
        return oldSecondItem;
    }

    @NotNull
    @Override
    public AnvilInventory getInventory() {
        return (AnvilInventory) super.getInventory();
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
