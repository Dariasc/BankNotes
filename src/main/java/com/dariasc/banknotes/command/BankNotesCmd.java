package com.dariasc.banknotes.command;

import com.dariasc.banknotes.BankNotes;
import com.dariasc.banknotes.Note;
import com.dariasc.banknotes.util.Lang;
import com.dariasc.banknotes.util.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BankNotesCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Permission.ADMIN.has(sender)) {
            Lang.COMMAND_NO_PERMISSION.msg(sender);
            return true;
        }

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                Lang.reload();
                BankNotes.notes.reloadConfig();
                Lang.ADMIN_RELOAD.msg(sender);
            } else if (args[0].equalsIgnoreCase("give")) {
                if (args.length == 3) {
                    Player target = Bukkit.getPlayerExact(args[1]);
                    if (target != null) {
                        Double amount;
                        try {
                            amount = Double.parseDouble(args[2]);
                        } catch (NumberFormatException e) {
                            Lang.INVALID_AMOUNT.msg(sender);
                            return true;
                        }
                        if (amount <= 0) {
                            Lang.INVALID_AMOUNT.msg(sender);
                            return true;
                        }

                        Note note = new Note(amount, BankNotes.notes.getConfig().getString("issuer.override", ""));
                        target.getInventory().addItem(note.getItem());
                    } else {
                        Lang.ADMIN_PLAYER_NOTFOUND.modifiable().replace("{player}", args[1]);
                    }
                } else {
                    Lang.INVALID_ARGUMENTS.msg(sender);
                }
            }
        } else {
            Lang.INVALID_ARGUMENTS.msg(sender);
        }
        return true;
    }

}
