package com.dariasc.banknotes;

import com.dariasc.banknotes.util.Lang;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NoteManager {

    private NoteManager() { }

    // Global deposit function
    public static boolean deposit(Player player) {
        double value = Note.getValue(player.getItemInHand());
        EconomyResponse ecoResponse = BankNotes.plugin.economy.depositPlayer(player, value);
        if (ecoResponse.transactionSuccess()) {
            // why don't people just upgrade...
            player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
            Lang.DEPOSIT_SUCCESS.modifiable().replace("{value}", String.valueOf(value)).msg(player);
        } else {
            return false;
        }
        return true;
    }

    // Global mass deposit function
    public static boolean massDeposit(Player player) {
        double accumulatedValue = 0;
        for (ItemStack item : player.getInventory()) {
            if (Note.isNote(item)) {
                int amount = item.getAmount();
                double value = amount * Note.getValue(item);
                if (BankNotes.plugin.economy.withdrawPlayer(player, value).transactionSuccess()) {
                    accumulatedValue += value;
                    item.setAmount(0);
                    player.updateInventory();
                }
            }
        }
        if (accumulatedValue > 0) {
            Lang.DEPOSIT_SUCCESS.modifiable().replace("{value}", String.valueOf(accumulatedValue)).msg(player);
        }
        return true;
    }

    // Global withdraw function
    public static boolean withdraw(Player player, double amount) {
        if (amount <= 0) {
            Lang.INVALID_ARGUMENTS.msg(player);
            return false;
        }

        EconomyResponse ecoResponse = BankNotes.plugin.economy.withdrawPlayer(player, amount);
        if (ecoResponse.transactionSuccess()) {
            Note note = new Note(amount);
            player.getInventory().addItem(note.getItem());
            Lang.WITHDRAW_SUCCESS.modifiable().replace("{value}", String.valueOf(amount)).msg(player);
        } else {
            Lang.INSUFFICIENT_FUNDS.msg(player);
            return false;
        }
        return true;
    }

}
