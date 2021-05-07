package me.great_array.vpncheck.util;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Configs {

    private static File blacklist; // Creating the files
    private static File whitelist;
    private static File statistics;

    private static FileConfiguration blacklistConfig;
    private static FileConfiguration whitelistConfig;
    private static FileConfiguration statisticsConfig;

    public static void setup() { // Setting up all the files

        blacklist = new File(Bukkit.getServer().getPluginManager().getPlugin("VPNCheck").getDataFolder(), "blacklist.yml");
        whitelist = new File(Bukkit.getServer().getPluginManager().getPlugin("VPNCheck").getDataFolder(), "whitelist.yml");
        statistics = new File(Bukkit.getServer().getPluginManager().getPlugin("VPNCheck").getDataFolder(), "statistics.yml");

        if (!blacklist.exists()) { // If blacklist.yml does not exist
            try {
                blacklist.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        blacklistConfig = YamlConfiguration.loadConfiguration(blacklist);

        if (!whitelist.exists()) { // If whitelist.yml does not exist
            try {
                whitelist.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        whitelistConfig = YamlConfiguration.loadConfiguration(whitelist);

        if (!statistics.exists()) { // If statistics.yml does not exist
            try {
                statistics.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        statisticsConfig = YamlConfiguration.loadConfiguration(statistics);

    }

    public static FileConfiguration getBlacklist() { return blacklistConfig; }
    public static FileConfiguration getWhitelist() { return whitelistConfig; }
    public static FileConfiguration getStatistics() { return statisticsConfig; }

    public static void saveBlacklist() {
        try {
            blacklistConfig.save(blacklist);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveWhitelist() {
        try {
            whitelistConfig.save(whitelist);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveStatistics() {
        try {
            statisticsConfig.save(statistics);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void reloadBlacklist() { blacklistConfig = YamlConfiguration.loadConfiguration(blacklist); }
    public static void reloadWhitelist() { whitelistConfig = YamlConfiguration.loadConfiguration(whitelist); }
    public static void reloadStatistics() { statisticsConfig = YamlConfiguration.loadConfiguration(statistics); }

}
