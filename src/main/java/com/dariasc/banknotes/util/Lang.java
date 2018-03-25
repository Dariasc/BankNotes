package com.dariasc.banknotes.util;

import com.dariasc.banknotes.BankNotes;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public enum Lang {

    COMMAND_NO_PERMISSION("&cYou do not have permission for that command"),
    PLAYER_ONLY_COMMAND("This command is only available to players"),
    INVALID_ARGUMENTS("&cInvalid arguments"),
    UNKNOWN_ERROR("&cAn unknown error has happened"),
    INVALID_AMOUNT("&cInvalid amount"),
    INSUFFICIENT_FUNDS("&cInsufficient funds"),

    DEPOSIT_SUCCESS("&aSuccessfully deposited &l{value}$"),
    DEPOSIT_ITEM_NOT_NOTE("&cInvalid note item"),

    WITHDRAW_SUCCESS("&aSuccessfully withdrawn &l{value}$"),

    ;

    private String def;
    private FileConfiguration lang;
    private final File LANG_FILE = new File(BankNotes.plugin.getDataFolder(), "messages.yml");

    Lang(String def) {
        this.def = def;
        saveDefault();
        reload();
    }

    public void reload() {
        lang = YamlConfiguration.loadConfiguration(LANG_FILE);
    }

    private void saveDefault() {
        if (!LANG_FILE.exists()) {
            BankNotes.plugin.saveResource("messages.yml", false);
        }
    }

    public String getKey() {
        return name().toLowerCase().replace("_", "-");
    }

    public String get() {
        String value = lang.getString(getKey(), def);
        return ChatColor.translateAlternateColorCodes('&', value);
    }

    @Override
    public String toString() {
        return get();
    }

    public ModifiableLang modifiable() {
        return new ModifiableLang();
    }

    // Quick access msg methods
    public void msg(Player player) {
        msg(player, get());
    }

    public void msg(Player player, String msg) {
        if (BankNotes.plugin.isPlaceholderApiHooked) {
            msg = PlaceholderAPI.setPlaceholders(player, msg);
        }
        player.sendMessage(msg);
    }

    public void msg(CommandSender sender) {
        String msg = ChatColor.stripColor(get());
        sender.sendMessage(msg);
    }

    public class ModifiableLang {
        String string;

        private ModifiableLang() {
            string = get();
        }

        public ModifiableLang replace(String tag, String replace) {
            string = string.replace(tag, replace);
            return this;
        }

        public void msg(Player player) {
            Lang.this.msg(player, string);
        }
    }

}