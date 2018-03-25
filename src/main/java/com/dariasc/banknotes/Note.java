package com.dariasc.banknotes;

import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Note {

    private double value;

    public Note(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public ItemStack getItem() {
        ItemStack bukkitItem = new ItemStack(Material.PAPER);
        NBTItem nbtItem = new NBTItem(bukkitItem);
        nbtItem.setDouble("noteValue", value);
        return nbtItem.getItem();
    }

    // Static Factory and Utility methods
    public static boolean isNote(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.hasKey("noteValue");
    }

    public static Note fromItem(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        double value = nbtItem.getDouble("noteValue");
        return new Note(value);
    }

    public static double getValue(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.getDouble("noteValue");
    }

}
