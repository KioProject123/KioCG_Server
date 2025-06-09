package com.kiocg.event.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class GiveCommandEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final CommandSender sender;
    private final Player target;
    private final ItemStack itemStack;

    private boolean cancelled;

    @ApiStatus.Internal
    public GiveCommandEvent(final CommandSender sender, final Player target, final ItemStack itemStack) {
        super(false);
        this.sender = sender;
        this.target = target;
        this.itemStack = itemStack;
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public Player getTarget() {
        return this.target;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public final boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public final void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
