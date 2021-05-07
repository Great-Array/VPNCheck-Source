package me.great_array.vpncheck.Commands;

import me.great_array.vpncheck.GUI.Panel;
import me.great_array.vpncheck.VPN;
import me.great_array.vpncheck.util.Configs;
import me.great_array.vpncheck.util.FileOperator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;

public class UniCommand implements CommandExecutor {

    VPN plugin = VPN.getPlugin();
    FileOperator fileOperator = new  FileOperator();
    HashSet<String> toggled = plugin.getToggled();
    Configuration config = plugin.getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("vpn")) {

            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (player.hasPermission(plugin.vpnCommandPermission)) {

                    switch (args.length) {

                        case 1:

                            switch (args[0].toLowerCase()) {

                                case "help":
                                    sendMessage(player, plugin.helpMessage);
                                    break;

                                case "panel":
                                    // Open GUI here
                                    Panel.openPanel(player);

                                    break;

                                case "whitelist":

                                case "unwhitelist":
                                    sendMessage(player, plugin.whitelistMessage);
                                    break;

                                case "blacklist":

                                case "unblacklist":
                                    sendMessage(player, plugin.blacklistedMessage);
                                    break;

                                case "notify":
                                    sendMessage(player, plugin.notifyMessage);
                                    break;

                                case "clear":
                                    sendMessage(player, plugin.clearMessage);
                                    break;

                                case "country":
                                    sendMessage(player, plugin.countryMessage);
                                    break;

                                case "reload":
                                    Configs.reloadWhitelist();
                                    Configs.reloadBlacklist();
                                    Configs.reloadStatistics();
                                    plugin.reloadConfig();
                                    sendMessage(player, plugin.reloadMessage);
                                    break;

                                default:
                                    sendMessage(player, plugin.helpMessage);
                                    break;

                            }
                            break;

                        case 2:

                            switch (args[0].toLowerCase()) {

                                case "whitelist":

                                    if (validIP(args[1])) {
                                        List<String> whitelist = fileOperator.getWhitelist();

                                        if (whitelist.contains(args[1])) {
                                            sendMessage(player, plugin.alreadyWhitelistedMessage.replace("$IP", args[1]));
                                        } else {
                                            sendMessage(player, plugin.addedToWhitelistMessage.replace("$IP", args[1]));

                                            fileOperator.addToWhitelist(args[1]);
                                        }
                                    } else {
                                        sendMessage(player, plugin.notValidIPMessage.replace("$IP", args[1]));
                                    }

                                    break;

                                case "unwhitelist":

                                    List<String> whitelist = fileOperator.getWhitelist();

                                    if (whitelist.contains(args[1])) {
                                        fileOperator.removeFromWhitelist(args[1]);

                                        sendMessage(player, plugin.removedFromWhitelistMessage.replace("$IP", args[1]));
                                    } else {
                                        sendMessage(player, plugin.notWhitelistedMessage.replace("$IP", args[1]));
                                    }

                                    break;

                                case "blacklist":

                                    if (validIP(args[1])) {
                                        List<String> blacklistList = fileOperator.getBlacklist();

                                        if (blacklistList.contains(args[1])) {
                                            sendMessage(player, plugin.alreadyBlacklistedMessage.replace("$IP", args[1]));
                                        } else {
                                            sendMessage(player, plugin.addedToBlacklistMessage.replace("$IP", args[1]));

                                            fileOperator.addToBlacklist(args[1]);
                                            fileOperator.addStatisticsBlacklistedToday();
                                        }
                                    } else {
                                        sendMessage(player, plugin.notValidIPMessage.replace("$IP", args[1]));
                                    }

                                    break;

                                case "unblacklist":

                                    List<String> blacklistList = fileOperator.getBlacklist();

                                    if (blacklistList.contains(args[1])) {
                                        fileOperator.removeFromBlacklist(args[1]);

                                        sendMessage(player, plugin.removedFromBlacklistMessage.replace("$IP", args[1]));
                                    } else {
                                        sendMessage(player, plugin.notBlacklistedMessage.replace("$IP", args[1]));
                                    }

                                    break;

                                case "notify":

                                    if (args[1].equalsIgnoreCase("on")) {

                                        if (toggled.contains(player.getName())) {
                                            sendMessage(player, plugin.notifyAlreadyOnMessage);
                                        } else {
                                            toggled.add(player.getName());
                                            sendMessage(player, plugin.notifyNowOnMessage);
                                        }

                                    } else if (args[1].equalsIgnoreCase("off")) {
                                        if (toggled.contains(player.getName())) {
                                            toggled.remove(player.getName());
                                            sendMessage(player, plugin.notifyNowOffMessage);
                                        } else {
                                            sendMessage(player, plugin.notifyAlreadyOffMessage);
                                        }
                                    }

                                    break;

                                case "clear":

                                    if (args[1].equalsIgnoreCase("blacklist")) {
                                        fileOperator.clearBlacklist();
                                        sendMessage(player, plugin.clearedBlacklistMessage);
                                    } else if (args[1].equalsIgnoreCase("whitelist")) {
                                        fileOperator.clearWhitelist();
                                        sendMessage(player, plugin.clearedWhitelistMessage);
                                    } else {
                                        sendMessage(player, plugin.clearMessage);
                                    }

                                    break;

                                case "help":
                                    if (args[1].equalsIgnoreCase("2")) {
                                        sendMessage(player, plugin.helpMessage2);
                                    }
                                    break;

                                default:
                                    sendMessage(player, plugin.helpMessage);
                                    break;

                            }

                            break;

                        case 0:
                            sendMessage(player, plugin.helpMessage);
                            break;
                        case 3:

                            switch (args[0].toLowerCase()) {

                                case "country":
                                    if (args[1].equalsIgnoreCase("ban")) {
                                        List<String> bannedCountries = fileOperator.getCountries();

                                        if (bannedCountries.contains(args[2])) {
                                            sendMessage(player, plugin.alreadyBannedCountryMessage.replace("$country", args[2]));
                                        } else {
                                            fileOperator.banCountry(args[2]);
                                            sendMessage(player, plugin.bannedCountryMessage.replace("$country", args[2]));
                                        }

                                    } else if (args[1].equalsIgnoreCase("unban")) {
                                        List<String> bannedCountries = fileOperator.getCountries();

                                        if (bannedCountries.contains(args[2])) {
                                            fileOperator.unbanCountry(args[2]);
                                            sendMessage(player, plugin.unbannedCountryMessage.replace("$country", args[2]));
                                        } else {
                                            sendMessage(player, plugin.notBannedCountryMessage.replace("$country", args[2]));
                                        }
                                    } else {
                                        sendMessage(player, plugin.countryMessage);
                                    }
                                    break;

                                default:
                                    sendMessage(player, plugin.helpMessage);
                                    break;
                            }

                            break;

                    }
            } else {
                    sendMessage(player, plugin.noPermissionMessage);
                }

            } else {
                sender.sendMessage("Sorry but only players can use this command");
            }

        }

        return false;
    }

    private void sendMessage(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    private static boolean validIP (String ip) {
        try {
            if ( ip == null || ip.isEmpty() ) {
                return false;
            }

            String[] parts = ip.split( "\\." );
            if ( parts.length != 4 ) {
                return false;
            }

            for ( String s : parts ) {
                int i = Integer.parseInt( s );
                if ( (i < 0) || (i > 255) ) {
                    return false;
                }
            }
            if ( ip.endsWith(".") ) {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

}
