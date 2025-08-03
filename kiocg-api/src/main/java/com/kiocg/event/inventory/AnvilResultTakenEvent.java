package com.kiocg.event.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class AnvilResultTakenEvent extends InventoryEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final ItemStack oldResult;
    private final ItemStack oldFirstItem;
    private final ItemStack oldSecondItem;

    public AnvilResultTakenEvent(HumanEntity player, InventoryView view, ItemStack result, ItemStack firstItem, ItemStack secondItem) {
        super(view);
        this.player = (Player) player;
        this.oldResult = result;
        this.oldFirstItem = firstItem;
        this.oldSecondItem = secondItem;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getOldResult() {
        return oldResult;
    }

    public ItemStack getOldFirstItem() {
        return oldFirstItem;
    }

    public ItemStack getOldSecondItem() {
        return oldSecondItem;
    }

    @Override
    public AnvilInventory getInventory() {
        return (AnvilInventory) super.getInventory();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
