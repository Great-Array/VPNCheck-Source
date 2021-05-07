package me.great_array.vpncheck.GUI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class BlacklistConfirm {

    public static void openGui(Player player) {

        Inventory gui = Bukkit.getServer().createInventory(player, 9, "VPNCheck > Info > Confirm Blacklist Clear");

        confirmItem(gui);
        cancelItem(gui);

        player.openInventory(gui);

    }

    private static void confirmItem(Inventory gui) {

        ItemStack confirm = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta confirmData = confirm.getItemMeta();
        confirmData.setDisplayName(color("&cConfirm"));

        confirm.setItemMeta(confirmData);

        gui.setItem(3, confirm);

    }

    private static void cancelItem(Inventory gui) {

        ItemStack confirm = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta confirmData = confirm.getItemMeta();
        confirmData.setDisplayName(color("&aCancel"));

        confirm.setItemMeta(confirmData);

        gui.setItem(5, confirm);

    }

    private static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
