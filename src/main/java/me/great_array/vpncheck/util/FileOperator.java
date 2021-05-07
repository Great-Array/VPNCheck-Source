package me.great_array.vpncheck.util;

import me.great_array.vpncheck.VPN;
import org.bukkit.configuration.Configuration;

import java.util.List;

public class FileOperator {

    VPN plugin = VPN.getPlugin();

    public void addToBlacklist(String ip) {
        Configuration blacklistConf = Configs.getBlacklist();
        List<String> blacklist = Configs.getBlacklist().getStringList("Blacklist");
        blacklist.add(ip);
        blacklistConf.set("Blacklist", blacklist);
        Configs.saveBlacklist();
    }

    public void clearBlacklist() {
        Configuration blacklistConf = Configs.getBlacklist();
        List<String> blacklist = Configs.getBlacklist().getStringList("Blacklist");
        blacklist.clear();
        blacklistConf.set("Blacklist", blacklist);
        Configs.saveBlacklist();
    }

    public void removeFromBlacklist(String ip) {
        Configuration blacklistConf = Configs.getBlacklist();
        List<String> blacklist = Configs.getBlacklist().getStringList("Blacklist");
        blacklist.remove(ip);
        blacklistConf.set("Blacklist", blacklist);
        Configs.saveBlacklist();
    }

    public void addToWhitelist(String ip) {
        Configuration whitelistConf = Configs.getWhitelist();
        List<String> whitelist = Configs.getWhitelist().getStringList("Whitelist");
        whitelist.add(ip);
        whitelistConf.set("Whitelist", whitelist);
        Configs.saveWhitelist();
    }

    public void banCountry(String country) {
        Configuration countryConfig = plugin.getConfig();
        List<String> countryList = plugin.getConfig().getStringList("BannedCountries");
        countryList.add(country);
        countryConfig.set("BannedCountries", countryList);
        plugin.saveConfig();
    }

    public void unbanCountry(String country) {
        Configuration countryConfig = plugin.getConfig();
        List<String> countryList = plugin.getConfig().getStringList("BannedCountries");
        countryList.remove(country);
        countryConfig.set("BannedCountries", countryList);
        plugin.saveConfig();
    }

    public void clearWhitelist() {
        Configuration whitelistConf = Configs.getWhitelist();
        List<String> whitelist = Configs.getWhitelist().getStringList("Whitelist");
        whitelist.clear();
        whitelistConf.set("Whitelist", whitelist);
        Configs.saveWhitelist();
    }

    public void removeFromWhitelist(String ip) {
        Configuration whitelistConf = Configs.getWhitelist();
        List<String> whitelist = Configs.getWhitelist().getStringList("Whitelist");
        whitelist.remove(ip);
        whitelistConf.set("Whitelist", whitelist);
        Configs.saveWhitelist();
    }

    public void addStatisticsChecked() {
        Configuration statsConf = Configs.getStatistics();
        long checked = Configs.getStatistics().getLong("Checked");
        checked++;
        statsConf.set("Checked", checked);
        Configs.saveStatistics();
    }

    public void addStatisticsCheckedToday() {
        Configuration statsConf = Configs.getStatistics();
        long checked = Configs.getStatistics().getLong("Checked-Today");
        checked++;
        statsConf.set("Checked-Today", checked);
        Configs.saveStatistics();
    }

    public void addStatisticsBlacklistedToday() {
        Configuration statsConf = Configs.getStatistics();
        long blacklisted = Configs.getStatistics().getLong("Blacklisted-Today");
        blacklisted++;
        statsConf.set("Blacklisted-Today", blacklisted);
        Configs.saveStatistics();
    }

    public long getBlacklistedToday() {
        Configuration statsConf = Configs.getStatistics();
        return statsConf.getLong("Blacklisted-Today");
    }

    public long getCheckedToday() {
        Configuration statsConf = Configs.getStatistics();
        return statsConf.getLong("Checked-Today");
    }

    public long getChecked() {
        Configuration statsConf = Configs.getStatistics();
        return statsConf.getLong("Checked");
    }

    public boolean whitelistContains(String ip) {

        List<String> whitelist = Configs.getWhitelist().getStringList("Whitelist");

        return whitelist.contains(ip);

    }

    public boolean blacklistContains(String ip) {

        List<String> blacklist = Configs.getBlacklist().getStringList("Blacklist");

        return blacklist.contains(ip);

    }

    public boolean bannedCountriesContain(String contry) {

        List<String> bannedCountryList = plugin.getConfig().getStringList("BannedCountries");

        return bannedCountryList.contains(contry);

    }

    public List<String> getWhitelist() {
        return Configs.getWhitelist().getStringList("Whitelist");
    }

    public List<String> getBlacklist() {
        return Configs.getBlacklist().getStringList("Blacklist");
    }

    public List<String> getCountries() {
        return plugin.getConfig().getStringList("BannedCountries");
    }

    public void saveWhitelist() {
        Configs.saveWhitelist();
    }

    public void saveBlacklist() {
        Configs.saveBlacklist();
    }

    public void saveCountries() {
        plugin.saveConfig();
    }


}
