package com.kiocg.event.inventory;

import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.InventoryView;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class FakeAnvilRenameEvent extends InventoryEvent {
    private static final HandlerList handlers = new HandlerList();
    private final String itemName;

    public FakeAnvilRenameEvent(InventoryView view, String itemName) {
        super(view);
        this.itemName = itemName;
    }

    public String getName() {
        return this.itemName;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
