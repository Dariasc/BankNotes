package com.dariasc.banknotes;

import com.dariasc.banknotes.command.DepositCmd;
import com.dariasc.banknotes.command.WithdrawCmd;
import com.dariasc.banknotes.listeners.PlayerListener;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class BankNotes extends JavaPlugin {

    public static BankNotes plugin;

    public Economy economy;
    public Permission permissions;

    public boolean isPlaceholderApiHooked = false;

    public BankNotes() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        if (!setupEconomy() || !setupPermissions()) {
            getLogger().severe("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        this.getCommand("withdraw").setExecutor(new WithdrawCmd());
        this.getCommand("deposit").setExecutor(new DepositCmd());

        isPlaceholderApiHooked = setupPlaceholderAPI();
    }

    private boolean setupPlaceholderAPI() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().info("Found PlaceholderAPI hooking");
            return true;
        }
        return false;
    }

    // Vault Dependencies
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permissions = rsp.getProvider();
        return permissions != null;
    }
}
