package com.kiocg.inventory;

import com.kiocg.event.inventory.InventoryExchangeItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public interface LockedInventoryHolder extends InventoryHolder {
    static void onInventoryClick(final @NotNull InventoryClickEvent e) {
        if (e.getAction() == InventoryAction.CLONE_STACK) {
            return;
        }

        final Inventory clickedInventory = e.getClickedInventory();
        if (clickedInventory != null && clickedInventory.getHolder(false) instanceof LockedInventoryHolder) {
            e.setCancelled(true);
        }
    }

    static void onInventoryExchangeItem(final @NotNull InventoryExchangeItemEvent e) {
        final Inventory source = e.getSource();
        final Inventory destination = e.getDestination();
        if (source != null && source.getHolder(false) instanceof LockedInventoryHolder
            || destination != null && destination.getHolder(false) instanceof LockedInventoryHolder) {
            e.setCancelled(true);
        }
    }
}
