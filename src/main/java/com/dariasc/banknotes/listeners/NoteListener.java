package com.dariasc.banknotes.listeners;

import com.dariasc.banknotes.NoteManager;
import com.dariasc.banknotes.util.Lang;
import com.dariasc.banknotes.Note;
import com.dariasc.banknotes.util.Permission;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class NoteListener implements Listener {

    @EventHandler
    public void playerUseNote(PlayerInteractEvent event) {
        // Use deprecated method to support older versions
        if (event.getHand() == EquipmentSlot.HAND && Note.isNote(event.getPlayer().getItemInHand())) {
            Player player = event.getPlayer();

            if (player.isSneaking()) {
                if (Permission.DEPOSIT_MASS.has(player)) {
                    NoteManager.massDeposit(player);
                } else {
                    Lang.COMMAND_NO_PERMISSION.msg(player);
                }
                return;
            } else {
                if (Permission.DEPOSIT.has(player)) {
                    NoteManager.deposit(player);
                } else {
                    Lang.COMMAND_NO_PERMISSION.msg(player);
                }
            }
            event.setCancelled(true);
        }
    }

}
