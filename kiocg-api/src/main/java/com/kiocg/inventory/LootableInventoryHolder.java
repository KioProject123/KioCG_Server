package com.kiocg.inventory;

import com.kiocg.event.inventory.InventoryExchangeItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public interface LootableInventoryHolder extends InventoryHolder {
    static void onInventoryExchangeItem(final @NotNull InventoryExchangeItemEvent e) {
        final Inventory destination = e.getDestination();
        if (destination != null && destination.getHolder(false) instanceof LootableInventoryHolder) {
            e.setCancelled(true);
        }
    }
}
