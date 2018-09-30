package com.dariasc.banknotes;

import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Note {

    private double value;
    private UUID uuid;
    // Use when want to override {issuer} tag
    private String issuerOverride;

    public Note(double value) {
        this.value = value;
    }

    public Note(double value, UUID uuid) {
        this.value = value;
        this.uuid = uuid;
    }

    public Note(double value, String issuerOverride) {
        this.value = value;
        this.issuerOverride = issuerOverride;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public ItemStack getItem() {
        Material material = Material.matchMaterial(BankNotes.notes.getConfig().getString("note.material", "PAPER"));
        if (material == null) {
            material = Material.PAPER;
        }

        ItemStack bukkitItem = new ItemStack(material);

        ItemMeta meta = bukkitItem.getItemMeta();
        meta.setDisplayName(parsePlaceholders(BankNotes.notes.getConfig().getString("note.name", "BankNote")));
        meta.setLore(parsePlaceholders(BankNotes.notes.getConfig().getStringList("note.lore")));
        bukkitItem.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(bukkitItem);
        nbtItem.setDouble("value", value);
        if (BankNotes.notes.getConfig().getBoolean("issuer.enable", false)) {
            if (uuid != null) {
                nbtItem.setString("issuer", uuid.toString());
            } else {
                nbtItem.setString("issuer", "admin");
            }
        }
        return nbtItem.getItem();
    }

    public static Note fromItem(ItemStack item) {
        if (isNote(item)) {
            NBTItem nbtItem = new NBTItem(item);
            double value = nbtItem.getDouble("value");
            if (nbtItem.hasKey("issuer")) {
                if (nbtItem.getString("issuer").equalsIgnoreCase("admin")) {
                    return new Note(value, BankNotes.notes.getConfig().getString("issuer.override", ""));
                }
                return new Note(value, UUID.fromString(nbtItem.getString("issuer")));
            }
            return new Note(value);
        }
        return null;
    }

    public static boolean isNote(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.hasKey("value");
    }

    // Parsing
    public String parsePlaceholders(String string) {
        if (uuid != null) {
            string = string.replace("{issuer}", Bukkit.getOfflinePlayer(uuid).getName());
        } else if (issuerOverride != null) {
            string = string.replace("{issuer}", issuerOverride);
        }
        string = string.replace("{value}", String.valueOf(getValue()));
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public List<String> parsePlaceholders(List<String> list) {
        List<String> parsed = new ArrayList<>();
        for (String string : list) {
            parsed.add(parsePlaceholders(string));
        }
        return parsed;
    }

}
