package com.dariasc.banknotes.util;

import org.bukkit.command.CommandSender;

public enum Permission {

    WITHDRAW(),
    DEPOSIT(),
    DEPOSIT_MASS(),
    ADMIN(),

    ;

    private String permission;

    Permission() {
        this.permission = "banknotes." + name().toLowerCase().replace('_', '.');
    }

    public boolean has(CommandSender sender) {
        return sender.hasPermission(permission);
    }

}
