package com.kiocg.event.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;
import org.jetbrains.annotations.NotNull;

public class SmithingResultTakenEvent extends InventoryEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final ItemStack oldResult;
    private final ItemStack oldTemplateItem;
    private final ItemStack oldBaseItem;
    private final ItemStack oldAdditionalItem;

    public SmithingResultTakenEvent(@NotNull HumanEntity player, @NotNull InventoryView view, @NotNull ItemStack result, @NotNull ItemStack templateItem, @NotNull ItemStack baseItem, @NotNull ItemStack additionalItem) {
        super(view);
        this.player = (Player) player;
        this.oldResult = result;
        this.oldTemplateItem = templateItem;
        this.oldBaseItem = baseItem;
        this.oldAdditionalItem = additionalItem;
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
    public ItemStack getOldTemplateItem() {
        return oldTemplateItem;
    }

    @NotNull
    public ItemStack getOldBaseItem() {
        return oldBaseItem;
    }

    @NotNull
    public ItemStack getOldAdditionalItem() {
        return oldAdditionalItem;
    }

    @NotNull
    @Override
    public SmithingInventory getInventory() {
        return (SmithingInventory) super.getInventory();
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
