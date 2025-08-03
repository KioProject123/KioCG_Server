package com.kiocg.event.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class SmithingResultTakenEvent extends InventoryEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final ItemStack oldResult;
    private final ItemStack oldTemplateItem;
    private final ItemStack oldBaseItem;
    private final ItemStack oldAdditionalItem;

    public SmithingResultTakenEvent(HumanEntity player, InventoryView view, ItemStack result, ItemStack templateItem, ItemStack baseItem, ItemStack additionalItem) {
        super(view);
        this.player = (Player) player;
        this.oldResult = result;
        this.oldTemplateItem = templateItem;
        this.oldBaseItem = baseItem;
        this.oldAdditionalItem = additionalItem;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getOldResult() {
        return oldResult;
    }

    public ItemStack getOldTemplateItem() {
        return oldTemplateItem;
    }

    public ItemStack getOldBaseItem() {
        return oldBaseItem;
    }

    public ItemStack getOldAdditionalItem() {
        return oldAdditionalItem;
    }

    @Override
    public SmithingInventory getInventory() {
        return (SmithingInventory) super.getInventory();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
