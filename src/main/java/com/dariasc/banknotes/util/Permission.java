package com.dariasc.banknotes.util;

import org.bukkit.entity.Player;

public enum Permission {

    WITHDRAW(),
    DEPOSIT(),
    DEPOSIT_MASS(),

    ;

    private String permission;

    Permission() {
        this.permission = "banknotes." + name().toLowerCase().replace('_', '.');
    }

    public boolean has(Player player) {
        return player.hasPermission(permission);
    }

}
