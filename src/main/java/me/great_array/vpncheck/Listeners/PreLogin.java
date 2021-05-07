package me.great_array.vpncheck.Listeners;

import me.great_array.vpncheck.API.Pcdetection;
import me.great_array.vpncheck.VPN;
import me.great_array.vpncheck.util.FileOperator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class PreLogin implements Listener {

    VPN plugin = VPN.getPlugin();
    FileOperator fileOperator = new  FileOperator();
    String key = plugin.getKey();
    int timeout = plugin.getTimeout();
    boolean banProxy = plugin.isBanProxy();
    boolean vpn = plugin.isVpn();


    @EventHandler (priority = EventPriority.MONITOR )
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {

        String ip = event.getAddress().getHostAddress();

        if (!fileOperator.blacklistContains(ip)) {

            if (!fileOperator.whitelistContains(ip)) {

                fileOperator.addStatisticsChecked();
                fileOperator.addStatisticsCheckedToday();

                Pcdetection detection = new Pcdetection(null);
                detection.useSSL();
                detection.set_api_key(key);
                detection.setUseAsn(true);
                detection.setUseVpn(vpn);
                detection.set_api_timeout(timeout);

                try {
                    detection.parseResults(ip);
                } catch (ParseException | IOException ex) {
                    ex.printStackTrace();
                }

                if (!fileOperator.bannedCountriesContain(detection.isocode)) {

                    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&', plugin.scanningMessage.replace("$IP", ip)));

                    if (detection.proxy.equalsIgnoreCase("yes")) {
                        fileOperator.addToBlacklist(ip);
                        fileOperator.addStatisticsBlacklistedToday();
                        sendToggledMessage(plugin.playerBlacklistedMessage.replace("$IP", ip));
                    } else {
                        fileOperator.addToWhitelist(ip);
                    }

                } else {
                    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Banned country");
                    sendToggledMessage(plugin.bannedCountryJoinMessage.replace("$IP", ip).replace("$country", detection.country));
                }


            } else {
                event.allow();
                sendToggledMessage(plugin.playerWhitelistedMessage.replace("$IP", ip));
            }

        } else {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&', plugin.blacklistMessage.replace("$IP", ip)));
            sendToggledMessage(plugin.blacklistedTriedToJoinMessage.replace("$IP", ip));
        }

    }

    private void sendToggledMessage(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (plugin.getToggled().contains(player.getName())) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        }
    }

}
