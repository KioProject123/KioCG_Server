package com.kiocg.event.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * 当玩家把物品从一个库存移动到另一个库存时调用, 其中一个库存是玩家的物品栏.
 * 当玩家使用收纳袋时, 物品的流向以收纳袋的来源为准.
 * <p>
 * Because InventoryExchangeItemEvent occurs within a modification of the Inventory,
 * not all Inventory related methods are safe to use.
 * <p>
 * Methods that change the view a player is looking at should never be invoked
 * by an EventHandler for InventoryExchangeItemEvent using the HumanEntity or
 * InventoryView associated with this event.
 * Examples of these include:
 * <ul>
 * <li>{@link HumanEntity#closeInventory()}
 * <li>{@link HumanEntity#openInventory(Inventory)}
 * <li>{@link InventoryView#close()}
 * </ul>
 * To invoke one of these methods, schedule a task using
 * {@link BukkitScheduler#runTask(Plugin, Runnable)}, which will run the task
 * on the next tick. Also be aware that this is not an exhaustive list, and
 * other methods could potentially create issues as well.
 */
@NullMarked
public class InventoryExchangeItemEvent extends InventoryInteractEvent {
    private static final HandlerList handlers = new HandlerList();

    private final @Nullable Inventory sourceInventory;
    private final @Nullable Inventory destinationInventory;
    private final ItemStack itemStack;

    public InventoryExchangeItemEvent(final InventoryView view, final @Nullable Inventory sourceInventory, final ItemStack itemStack, final @Nullable Inventory destinationInventory) {
        super(view);
        this.sourceInventory = sourceInventory;
        this.destinationInventory = destinationInventory;
        this.itemStack = itemStack;
    }

    @Nullable
    public Inventory getSource() {
        return sourceInventory;
    }

    @Nullable
    public Inventory getDestination() {
        return destinationInventory;
    }

    public ItemStack getItem() {
        return itemStack;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
