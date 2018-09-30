package com.dariasc.banknotes.command;

import com.dariasc.banknotes.NoteManager;
import com.dariasc.banknotes.util.Lang;
import com.dariasc.banknotes.util.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WithdrawCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!Permission.WITHDRAW.has(player)) {
                Lang.COMMAND_NO_PERMISSION.msg(player);
                return true;
            }

            if (args.length == 1) {
                Double amount;
                try {
                    amount = Double.parseDouble(args[0]);
                } catch (NumberFormatException e) {
                    Lang.INVALID_AMOUNT.msg(player);
                    return true;
                }
                if (amount <= 0) {
                    Lang.INVALID_AMOUNT.msg(player);
                    return true;
                }

                NoteManager.withdraw(player, amount);
            } else {
                Lang.INVALID_ARGUMENTS.msg(player);
            }
            return true;
        }

        Lang.PLAYER_ONLY_COMMAND.msg(sender);
        return true;
    }

}
