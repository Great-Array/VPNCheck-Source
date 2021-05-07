package me.great_array.vpncheck.Listeners;

import me.great_array.vpncheck.GUI.BlacklistConfirm;
import me.great_array.vpncheck.GUI.WhitelistConfirm;
import me.great_array.vpncheck.VPN;
import me.great_array.vpncheck.util.Configs;
import me.great_array.vpncheck.util.FileOperator;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClickListener implements Listener {

    VPN plugin = VPN.getPlugin();
    FileOperator fileOperator = new  FileOperator();

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        String name = event.getView().getTitle();

        switch (name.toLowerCase()) {

            case "vpncheck > info":

                switch (slot) {

                    case 8:
                        Configs.reloadWhitelist();
                        Configs.reloadBlacklist();
                        Configs.reloadStatistics();
                        plugin.reloadConfig();
                        event.setCancelled(true);
                        sendMessage(player, plugin.reloadMessage);
                        break;

                    case 7:
                        event.setCancelled(true);
                        BlacklistConfirm.openGui(player);
                        break;

                    case 6:
                        event.setCancelled(true);
                        WhitelistConfirm.openGui(player);
                        break;

                    default:
                        event.setCancelled(true);
                        break;

                }

                break;

            case "vpncheck > info > confirm blacklist clear":

                switch (slot) {

                    case 3:
                        event.setCancelled(true);
                        player.closeInventory();
                        fileOperator.clearBlacklist();
                        sendMessage(player, plugin.clearedBlacklistMessage);
                        break;
                    case 5:
                        player.closeInventory();
                        event.setCancelled(true);
                        break;

                }
            break;

            case "vpncheck > info > confirm whitelist clear":

                switch (slot) {

                    case 3:
                        event.setCancelled(true);
                        player.closeInventory();
                        fileOperator.clearWhitelist();
                        sendMessage(player, plugin.clearedWhitelistMessage);
                        break;
                    case 5:
                        event.setCancelled(true);
                        player.closeInventory();
                        break;

                }

                break;
        }

    }

    private void sendMessage(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

}
