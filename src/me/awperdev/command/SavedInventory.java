package me.awperdev.command;

import org.bukkit.inventory.ItemStack;

public class SavedInventory {
    private ItemStack[] inventory;
    private ItemStack[] armor;

    public SavedInventory(ItemStack[] inventory, ItemStack[] armor) {
        this.inventory = inventory;
        this.armor = armor;
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    public void setInventory(ItemStack[] inventory) {
        this.inventory = inventory;
    }

    public ItemStack[] getArmor() {
        return armor;
    }

    public void setArmor(ItemStack[] armor) {
        this.armor = armor;
    }
}
