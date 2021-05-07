package me.great_array.vpncheck;

import me.great_array.vpncheck.Commands.UniCommand;
import me.great_array.vpncheck.Listeners.ClickListener;
import me.great_array.vpncheck.Listeners.PreLogin;
import me.great_array.vpncheck.util.Configs;
import me.great_array.vpncheck.util.Tps;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;

public final class VPN extends JavaPlugin {

    private static VPN plugin;
    private String key = getConfig().getString("Settings.Key");
    private boolean banProxy = getConfig().getBoolean("Settings.BanProxy");
    private boolean vpn = getConfig().getBoolean("Settings.CheckForVpn");
    private int timeout = getConfig().getInt("Settings.Api_Timeout");


    public String helpMessage;
    public String helpMessage2;

    public String reloadMessage;

    public String noPermissionMessage;

    public String vpnCommandPermission;

    public String whitelistMessage;
    public String alreadyWhitelistedMessage;
    public String addedToWhitelistMessage;
    public String removedFromWhitelistMessage;
    public String notWhitelistedMessage;

    public String blacklistedMessage;
    public String alreadyBlacklistedMessage;
    public String addedToBlacklistMessage;
    public String removedFromBlacklistMessage;
    public String notBlacklistedMessage;

    public String notValidIPMessage;

    public String clearMessage;
    public String clearedBlacklistMessage;
    public String clearedWhitelistMessage;

    public String notifyMessage;
    public String notifyAlreadyOnMessage;
    public String notifyAlreadyOffMessage;
    public String notifyNowOnMessage;
    public String notifyNowOffMessage;

    public String countryMessage;
    public String bannedCountryMessage;
    public String alreadyBannedCountryMessage;
    public String notBannedCountryMessage;
    public String unbannedCountryMessage;

    public String blacklistMessage;
    public String scanningMessage;

    public String playerBlacklistedMessage;
    public String playerWhitelistedMessage;
    public String blacklistedTriedToJoinMessage;
    public String bannedCountryJoinMessage;

    HashSet<String> toggled = new HashSet<>();


    @Override
    public void onEnable() {

        plugin = this;

        setupMessages();
        setupConfig();
        setupCustomConfigs();
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Tps(), 100L, 1L);
        registerEvents();
        registerCommands();
        setupDailyTimer();

        if (!key.isEmpty()) {
            sendArt();
            log("----------------------------------");
            log("[VPNCheck] Plugin has been enabled");
        } else {
            sendMissingKeyArt();
            log("----------------------------------");
            log("[VPNCheck] Please register on proxycheck.io and get your own key,\n");
            log(" then place it in the config.yml file!");
        }
        log("----------------------------------");

    }

    @Override
    public void onDisable() {
        log("[VPNCheck] Plugin has been disabled");
    }

    public void setupMessages() {

        blacklistMessage = getConfig().getString("Messages.BlacklistedMessage").replace("[", "").replace("]", "");
        scanningMessage = getConfig().getString("Messages.ScanningMessage").replace("[", "").replace("]", "");

        helpMessage = getConfig().getString("Messages.HelpMessage").replace("[", "").replace("]", "");
        helpMessage2 = getConfig().getString("Messages.HelpMessage2").replace("[", "").replace("]", "");

        reloadMessage = getConfig().getString("Messages.ReloadConfigsMessage").replace("[", "").replace("]", "");

        noPermissionMessage = getConfig().getString("Messages.NoPermissionMessage");

        vpnCommandPermission = getConfig().getString("Permissions.VpnCommand");

        whitelistMessage = getConfig().getString("Messages.WhitelistMessage").replace("[", "").replace("]", "");
        alreadyWhitelistedMessage = getConfig().getString("Messages.AlreadyWhitelistedMessage").replace("[", "").replace("]", "");
        addedToWhitelistMessage = getConfig().getString("Messages.AddedToWhitelistMessage").replace("[", "").replace("]", "");
        removedFromWhitelistMessage = getConfig().getString("Messages.RemovedFromWhitelistMessage").replace("[", "").replace("]", "");
        notWhitelistedMessage = getConfig().getString("Messages.NotWhitelistedMessage").replace("[", "").replace("]", "");

        blacklistedMessage = getConfig().getString("Messages.BlacklistMessage").replace("[", "").replace("]", "");
        alreadyBlacklistedMessage = getConfig().getString("Messages.AlreadyBlacklistedMessage").replace("[", "").replace("]", "");
        addedToBlacklistMessage = getConfig().getString("Messages.AddedToBlacklistMessage").replace("[", "").replace("]", "");
        removedFromBlacklistMessage = getConfig().getString("Messages.RemovedFromBlacklistMessage").replace("[", "").replace("]", "");
        notBlacklistedMessage = getConfig().getString("Messages.NotBlacklistedMessage").replace("[", "").replace("]", "");

        notValidIPMessage = getConfig().getString("Messages.NotValidIpMessage").replace("[", "").replace("]", "");

        clearMessage = getConfig().getString("Messages.ClearMessage").replace("[", "").replace("]", "");
        clearedBlacklistMessage = getConfig().getString("Messages.ClearedBlacklistMessage");
        clearedWhitelistMessage = getConfig().getString("Messages.ClearedWhitelistMessage");

        notifyMessage = getConfig().getString("Messages.NotifyMessage").replace("[", "").replace("]", "");
        notifyAlreadyOnMessage = getConfig().getString("Messages.NotifyAlreadyOnMessage").replace("[", "").replace("]", "");
        notifyAlreadyOffMessage = getConfig().getString("Messages.NotifyAlreadyOffMessage").replace("[", "").replace("]", "");
        notifyNowOnMessage = getConfig().getString("Messages.NotifyNowOnMessage").replace("[", "").replace("]", "");
        notifyNowOffMessage = getConfig().getString("Messages.NotifyNowOffMessage").replace("[", "").replace("]", "");

        countryMessage = getConfig().getString("Messages.CountryMessage").replace("[", "").replace("]", "");
        bannedCountryMessage = getConfig().getString("Messages.BannedCountryMessage");
        alreadyBannedCountryMessage = getConfig().getString("Messages.AlreadyBannedCountryMessage");
        notBannedCountryMessage = getConfig().getString("Messages.NotBannedCountryMessage");
        unbannedCountryMessage = getConfig().getString("Messages.UnbannedCountryMessage");

        playerBlacklistedMessage = getConfig().getString("Messages.PlayerBlacklistedMessage");
        playerWhitelistedMessage = getConfig().getString("Messages.PlayerWhitelistedMessage");
        blacklistedTriedToJoinMessage = getConfig().getString("Messages.BlacklistedTriedToJoinMessage");
        bannedCountryJoinMessage = getConfig().getString("Messages.BannedCountryJoinMessage");

    }

    public static VPN getPlugin() { return plugin; }
    public String getKey() { return key; }
    public boolean isBanProxy() { return banProxy; }
    public boolean isVpn() { return vpn; }
    public int getTimeout() { return timeout; }
    public HashSet<String> getToggled() { return toggled; }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PreLogin(), this);
        Bukkit.getPluginManager().registerEvents(new ClickListener(), this);
    }

    private void registerCommands() {
        getCommand("vpn").setExecutor(new UniCommand());
    }

    private void setupConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void setupCustomConfigs() {
        Configs.setup();

        Configs.getBlacklist().addDefault("Blacklist", "Empty");
        Configs.getBlacklist().options().copyDefaults(true);
        Configs.saveBlacklist();

        Configs.getWhitelist().addDefault("Whitelist", "Empty");
        Configs.getWhitelist().options().copyDefaults(true);
        Configs.saveWhitelist();

        Configs.getStatistics().addDefault("Checked", 0);
        Configs.getStatistics().addDefault("Blacklisted-Today", 0);
        Configs.getStatistics().addDefault("Whitelisted-Today", 0);
        Configs.getStatistics().addDefault("Date", 0);
        Configs.getStatistics().options().copyDefaults(true);
        Configs.saveStatistics();
    }

    private void setupDailyTimer() {

        // This will basically reset the DailyBlacklist and DailyChecked to 0

        if (Configs.getStatistics().getLong("Date") == 0) {
            long min1 = (System.currentTimeMillis() / 1000) / 60;
            Configs.getStatistics().getLong("Date", min1 + 1440);
            Configs.saveStatistics();
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){

            @Override
            public void run() {
                long min2 = (System.currentTimeMillis() / 1000) / 60;
                if (min2 > Configs.getStatistics().getLong("Date")) {
                    Configs.getStatistics().set("Date", min2 + 1440);
                    Configs.getStatistics().set("Blacklisted-Today", 0);
                    Configs.getStatistics().set("Whitelisted-Today", 0);
                    Configs.getStatistics().set("Whitelisted-Today", 0);
                    Configs.saveStatistics();
                }
            }
        }, 1, 20 * 60 * 5);

    }

    private void log(String message) {
        System.out.println(message);
    }

    private String color(String message) {

        return ChatColor.translateAlternateColorCodes('&', message);

    }

    private void sendArt() {
        System.out.println(" \n");
        System.out.println("██╗   ██╗██████╗ ███╗   ██╗ ██████╗██╗  ██╗███████╗ ██████╗██╗  ██╗");
        System.out.println("██║   ██║██╔══██╗████╗  ██║██╔════╝██║  ██║██╔════╝██╔════╝██║ ██╔╝");
        System.out.println("██║   ██║██████╔╝██╔██╗ ██║██║     ███████║█████╗  ██║     █████╔╝ ");
        System.out.println("╚██╗ ██╔╝██╔═══╝ ██║╚██╗██║██║     ██╔══██║██╔══╝  ██║     ██╔═██╗ ");
        System.out.println(" ╚████╔╝ ██║     ██║ ╚████║╚██████╗██║  ██║███████╗╚██████╗██║  ██╗");
        System.out.println("  ╚═══╝  ╚═╝     ╚═╝  ╚═══╝ ╚═════╝╚═╝  ╚═╝╚══════╝ ╚═════╝╚═╝  ╚═╝");
        System.out.println("                                      - Powered by ProxyCheck.io   ");
        System.out.println(" \n");
    }

    private void sendMissingKeyArt() {
        System.out.println(" \n");
        System.out.println("██╗  ██╗███████╗██╗   ██╗██████╗ ");
        System.out.println("██║ ██╔╝██╔════╝╚██╗ ██╔╝╚════██╗");
        System.out.println("█████╔╝ █████╗   ╚████╔╝   ▄███╔╝");
        System.out.println("██╔═██╗ ██╔══╝    ╚██╔╝    ▀▀══╝ ");
        System.out.println("██║  ██╗███████╗   ██║     ██╗   ");
        System.out.println("╚═╝  ╚═╝╚══════╝   ╚═╝     ╚═╝   ");
        System.out.println("     - Missing key in config.yml ");
        System.out.println(" \n");
    }

}
