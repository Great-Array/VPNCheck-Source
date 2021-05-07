package me.great_array.vpncheck.GUI;

import me.great_array.vpncheck.API.Pcdetection;
import me.great_array.vpncheck.VPN;
import me.great_array.vpncheck.util.FileOperator;
import me.great_array.vpncheck.util.KeyInfo;
import me.great_array.vpncheck.util.Tps;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.List;

public class Panel {

    static VPN plugin = VPN.getPlugin();
    static FileOperator fileOperator = new  FileOperator();
    static String key = plugin.getKey();
    static OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
    static int timeout = plugin.getTimeout();
    static boolean vpn = plugin.isVpn();

    public static void openPanel(Player player) {

        Inventory gui = Bukkit.getServer().createInventory(player, 9, "VPNCheck > Info");

        blacklistItem(gui);
        checkedItem(gui);
        serverItem(gui);
        reloadItem(gui);
        clearBlacklistItem(gui);
        clearWhitelistItem(gui);
        queryItem(gui);

        player.openInventory(gui);

    }

    private static void blacklistItem(Inventory gui) {

        // Blacklist Item
        ItemStack blacklist = new ItemStack(Material.BARRIER);
        ItemMeta blacklistMeta = blacklist.getItemMeta();
        blacklistMeta.setDisplayName(color("&cBlacklist Information"));

        List<String> blacklistList = fileOperator.getBlacklist();
        long blacklistAmount = blacklistList.size();
        long blacklistedAmountToday = fileOperator.getBlacklistedToday();

        String lastBlacklistedAddress;
        String lastBlacklistedCountry;
        String lastBlacklistedType;
        String lastBlacklistedProxy;

        if (!blacklistList.isEmpty()) {

            lastBlacklistedAddress = fileOperator.getBlacklist().get(blacklistList.size() - 1);

            Pcdetection detection = new Pcdetection(null);
            detection.useSSL();
            detection.set_api_key(key);
            detection.setUseAsn(true);
            detection.setUseVpn(vpn);
            detection.set_api_timeout(timeout);

            try {
                detection.parseResults(lastBlacklistedAddress);
            } catch (ParseException | IOException ex) {
                ex.printStackTrace();
            }

            lastBlacklistedCountry = detection.country;
            lastBlacklistedType = detection.type;
            lastBlacklistedProxy = detection.proxy;


        } else {
            lastBlacklistedAddress = "Not Found";
            lastBlacklistedCountry = "Unknown";
            lastBlacklistedType = "Unknown";
            lastBlacklistedProxy = "Unknown";
        }

        ArrayList<String> lore = new ArrayList<>();
        lore.add(color("&eVPNCheck Blacklist Statistics"));
        lore.add("");
        lore.add(color("&eBlacklist:"));
        lore.add(color(" &7Blacklisted: &c" + blacklistAmount));
        lore.add(color(" &7Blacklisted Today: &c" + blacklistedAmountToday));
        lore.add("");
        lore.add(color("&eLast Blacklisted Address:"));
        lore.add(color(" &7Address: &e" + lastBlacklistedAddress));
        lore.add(color(" &7Country: &a" + lastBlacklistedCountry));
        lore.add(color(" &7Type: &c" + lastBlacklistedType));
        blacklistMeta.setLore(lore);

        blacklist.setItemMeta(blacklistMeta);

        gui.setItem(0, blacklist);

    }

    private static void checkedItem(Inventory gui) {

        ItemStack checked = new ItemStack(Material.BOOK);
        ItemMeta checkedMeta = checked.getItemMeta();
        checkedMeta.setDisplayName(color("&cChecked Information"));

        long checkedIPs = fileOperator.getChecked();
        long checkedIPsToday = fileOperator.getCheckedToday();
        List<String> cleanList = fileOperator.getWhitelist();
        long cleanIPs = cleanList.size();

        ArrayList<String> lore = new ArrayList<>();
        lore.add(color("&e&oVPNCheck Statistics on all checked IPs"));
        lore.add("");
        lore.add(color("&eCheck:"));
        lore.add(color(" &7Checked IPs: &e" + checkedIPs));
        lore.add(color(" &7Checked Today: &e" + checkedIPsToday));
        lore.add("");
        lore.add(color("&eClean IPs:"));
        lore.add(color(" &7Clean IPs: &a" + cleanIPs));
        checkedMeta.setLore(lore);

        checked.setItemMeta(checkedMeta);

        gui.setItem(1, checked);

    }

    private static void serverItem(Inventory gui) {

        ItemStack server = new ItemStack(Material.PAPER);
        ItemMeta serverMeta = server.getItemMeta();
        serverMeta.setDisplayName(color("&cServer Information"));

        long ramUsage = Runtime.getRuntime().totalMemory() / 1048576;
        long ramTotal = Runtime.getRuntime().maxMemory() / 1048576;
        long ramFree = Runtime.getRuntime().freeMemory() / 1048576;
        double tps = Tps.getTPS();

        ArrayList<String> lore = new ArrayList<>();
        lore.add(color("&e&oYou can view some server information here"));
        lore.add("");
        lore.add(color("&ePerformance Statistics:"));

        if (tps <= 7) {
            lore.add(color(" &7TPS: &4" + Math.round(tps)));
        } else if (tps <= 12 && tps>= 8) {
            lore.add(color(" &7TPS: &c" + Math.round(tps)));
        } else if (tps <= 17 && tps >= 13) {
            lore.add(color(" &7TPS: &e" + Math.round(tps)));
        } else if (tps <= 20 && tps >= 18) {
            lore.add(color(" &7TPS: &a" + Math.round(tps)));
        } else {
            lore.add(color(" &7TPS: &d&l" + Math.round(tps) + "?!"));
        }

        lore.add(color(" &7RAM: &e" + ramUsage + "MB" + "&7/&c" + ramTotal + "MB " + "&8(&6" + ramFree + "MB free&8)"));
        lore.add(color(" &7CPU: &e" + osBean.getSystemLoadAverage() + "%" + "&7 - &e" + osBean.getAvailableProcessors() + " &7processors"));
        serverMeta.setLore(lore);

        server.setItemMeta(serverMeta);

        gui.setItem(2, server);

    }

    private static void reloadItem(Inventory gui) {

        ItemStack reload = new ItemStack(Material.FEATHER);
        ItemMeta reloadMeta = reload.getItemMeta();
        reloadMeta.setDisplayName(color("&aReload Plugin"));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(color("&7&oClick here to reload the plugin and all its files"));
        reloadMeta.setLore(lore);

        reload.setItemMeta(reloadMeta);

        gui.setItem(8, reload);

    }

    private static void clearBlacklistItem(Inventory gui) {

        ItemStack clearBlacklist = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta clearBlacklistMeta = clearBlacklist.getItemMeta();
        clearBlacklistMeta.setDisplayName(color("&cClear Blacklist"));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(color("&7&oClick me if you want to delete all IPs from"));
        lore.add(color("&7&othe blacklist.yml file &c(Not Recommended)"));
        clearBlacklistMeta.setLore(lore);

        clearBlacklist.setItemMeta(clearBlacklistMeta);

        gui.setItem(7, clearBlacklist);

    }

    private static void clearWhitelistItem(Inventory gui) {

        ItemStack clearWhitelist = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta clearWhitelistMeta = clearWhitelist.getItemMeta();
        clearWhitelistMeta.setDisplayName(color("&cClear Whitelist"));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(color("&7&oClick me if you want to delete all IPs from"));
        lore.add(color("&7&othe whitelist.yml file &c(Not Recommended)"));
        clearWhitelistMeta.setLore(lore);

        clearWhitelist.setItemMeta(clearWhitelistMeta);

        gui.setItem(6, clearWhitelist);

    }

    private static void queryItem(Inventory gui) {

        ItemStack query = new ItemStack(Material.NAME_TAG);
        ItemMeta queryMeta = query.getItemMeta();
        queryMeta.setDisplayName(color("&eYour Key's Query Info"));

        KeyInfo qu = new KeyInfo(plugin);
        String s = qu.getUsage();
        qu.parseUsage(s);


        ArrayList<String> lore = new ArrayList<>();
        lore.add(color("&eAPI Query Statistics"));
        lore.add("");
        lore.add(color("&eQuery Stats:"));
        lore.add(color(" &7Burst Tokens: &e" + qu.getBurstTokens()));
        lore.add(color(" &7Account Plan: &e" + qu.getAccountTier()));
        lore.add(color(" &7Daily Scan Limit: &e" + qu.getDailyLimit()));
        lore.add(color(" &7Scans Done Today: &e" + qu.getQueriesToday()));
        lore.add(color(" &7Scans Done Total: &e" + qu.getQueriesTotal()));
        lore.add("");
        lore.add(color("&eCheck out ProxyCheck.io for your key!"));
        queryMeta.setLore(lore);

        query.setItemMeta(queryMeta);

        gui.setItem(5, query);
    }


    private static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }


}
