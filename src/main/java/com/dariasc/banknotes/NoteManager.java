package com.dariasc.banknotes;

import com.dariasc.banknotes.event.NoteEvent;
import com.dariasc.banknotes.util.Lang;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NoteManager {

    private NoteManager() { }

    // Global deposit function
    public static boolean deposit(Player player) {
        if (Note.isNote(player.getItemInHand())) {
            Note note = Note.fromItem(player.getItemInHand());
            double value = note.getValue();

            NoteEvent event = new NoteEvent(player, value, NoteEvent.NoteEventType.DEPOSIT);
            event.setNoteItem(player.getItemInHand());
            Bukkit.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                value = event.getAmount();
                EconomyResponse ecoResponse = BankNotes.notes.economy.depositPlayer(player, value);
                if (ecoResponse.transactionSuccess()) {
                    // why don't people just upgrade... forget the 1.8 life
                    player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
                    Lang.DEPOSIT_SUCCESS.modifiable().replace("{value}", String.valueOf(value)).msg(player);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    // Global mass deposit function
    public static boolean massDeposit(Player player) {
        double accumulatedValue = 0;
        for (ItemStack item : player.getInventory()) {
            if (Note.isNote(item)) {
                Note note = Note.fromItem(item);
                int noteQuantity = item.getAmount();
                double value = noteQuantity * note.getValue();

                NoteEvent event = new NoteEvent(player, value, NoteEvent.NoteEventType.DEPOSIT);
                event.setNoteItem(item);
                Bukkit.getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    value = event.getAmount();
                    if (BankNotes.notes.economy.depositPlayer(player, value).transactionSuccess()) {
                        accumulatedValue += value;
                        item.setAmount(0);
                        player.updateInventory();
                    }
                }
            }
        }
        if (accumulatedValue > 0) {
            Lang.DEPOSIT_SUCCESS.modifiable().replace("{value}", accumulatedValue).msg(player);
        }
        return true;
    }

    // Global withdraw function
    public static boolean withdraw(Player player, double amount) {
        NoteEvent event = new NoteEvent(player, amount, NoteEvent.NoteEventType.WITHDRAW);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            amount = event.getAmount();
            EconomyResponse ecoResponse = BankNotes.notes.economy.withdrawPlayer(player, amount);
            if (ecoResponse.transactionSuccess()) {
                Note note = new Note(amount);
                if (BankNotes.notes.getConfig().getBoolean("issuer.enable", false)) {
                    note = new Note(amount, player.getUniqueId());
                }
                player.getInventory().addItem(note.getItem());
                Lang.WITHDRAW_SUCCESS.modifiable().replace("{value}", amount).msg(player);
                return true;
            } else {
                Lang.INSUFFICIENT_FUNDS.msg(player);
                return false;
            }
        }
        return false;
    }

}
