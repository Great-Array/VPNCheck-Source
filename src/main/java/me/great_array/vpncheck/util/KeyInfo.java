package me.great_array.vpncheck.util;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.great_array.vpncheck.VPN;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Scanner;

public class KeyInfo {

    private VPN plugin;
    private String queriesToday;
    private String dailyLimit;
    private String queriesTotal;
    private String accountTier;
    private String burstTokens;

    public KeyInfo(VPN plugin) {
        this.plugin = plugin;
    }

    public String getUsage() {
        try {

            URL url = new URL("https://proxycheck.io/dashboard/export/usage/?key=" + plugin.getKey());
            Scanner sc = new Scanner(url.openStream());
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.connect();

            final Reader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            final StringBuilder sb = new StringBuilder();
            int i;
            while ((i = in.read()) >= 0) {
                sb.append((char)i);
            }
            return sb.toString();


        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }


    public void parseUsage(String response) {
        JSONParser parser = new JSONParser();

        try {
            JSONObject jsonResponse = (JSONObject)parser.parse(response);
            JsonObject queryStatistics = (JsonObject)(new Gson()).fromJson(response, JsonObject.class);
            this.queriesToday = queryStatistics.get("Queries Today").getAsString();
            this.dailyLimit = queryStatistics.get("Daily Limit").getAsString();
            this.queriesTotal = queryStatistics.get("Queries Total").getAsString();
            this.accountTier = jsonResponse.get("Plan Tier").toString();
            this.burstTokens = queryStatistics.get("Burst Tokens Available").toString();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    public String getQueriesToday() {
        return this.queriesToday;
    }

    public String getDailyLimit() {
        return this.dailyLimit;
    }

    public String getQueriesTotal() {
        return this.queriesTotal;
    }

    public String getAccountTier() {
        return this.accountTier;
    }

    public String getBurstTokens() {
        return this.burstTokens;
    }


}
