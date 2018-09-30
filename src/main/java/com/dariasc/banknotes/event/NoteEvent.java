package com.dariasc.banknotes.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

public class NoteEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    private double amount;
    private NoteEventType eventType;
    private ItemStack noteItem;

    public NoteEvent(Player who, double amount, NoteEventType eventType) {
        super(who);
        this.amount = amount;
        this.eventType = eventType;
    }

    // Actual Note Information
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public NoteEventType getEventType() {
        return eventType;
    }

    public ItemStack getNoteItem() {
        return noteItem;
    }

    public void setNoteItem(ItemStack noteItem) {
        this.noteItem = noteItem;
    }

    public enum NoteEventType {
        WITHDRAW,
        DEPOSIT,
    }

    // Boilerplate
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

}
