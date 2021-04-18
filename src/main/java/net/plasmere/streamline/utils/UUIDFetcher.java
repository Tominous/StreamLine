package net.plasmere.streamline.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.*;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.objects.Player;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class UUIDFetcher {
    public static Cache<String, UUID> cachedUUIDs = Caffeine.newBuilder().build();
    public static Cache<UUID, String> cachedNames = Caffeine.newBuilder().build();

    public static UUID getCachedUUID(String username) {
        try {
            String finalUsername = username.replace("\"", "");
            return cachedUUIDs.get(username, (u) -> fetch(finalUsername));
        } catch (Exception e) {
            e.printStackTrace();
            return UUID.randomUUID();
        }
    }

    public static String getCachedName(UUID uuid) {
        try {
            return Objects.requireNonNull(cachedNames.get(uuid, (u) -> getName(uuid.toString()))).replace("\"", "");
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    static public UUID fetch(String username) {
        username = username.toLowerCase(Locale.ROOT);
        try {
            String JSONString = "";

            HttpURLConnection connection = (HttpURLConnection) new URL("https://api.mojang.com/users/profiles/minecraft/" + username).openConnection();
            InputStream is = connection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                JSONString = line;
            }

            JsonElement obj = new JsonParser().parse(JSONString);

            JsonObject jo = (JsonObject) obj;

            String id = jo.get("id").getAsString();

            String uuid = formatToUUID(id);

            return UUID.fromString(uuid);
            //return UUID.fromString(id);
        } catch (Exception e){
            e.printStackTrace();
        }
        return UUID.randomUUID();
    }

    public static String getName(String uuid) {
        try {
            String JSONString = "";

            HttpURLConnection connection = (HttpURLConnection) new URL("https://api.mojang.com/user/profiles/" + uuid.replace("-", "") + "/names").openConnection();
            InputStream is = connection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null) {
                JSONString = line;
            }

            Object obj = new JsonParser().parse(JSONString);
            JsonArray jo = (JsonArray) obj;
            String last = jo.get(jo.size() - 1).toString();
            Object job = new JsonParser().parse(last);
            JsonObject njo = (JsonObject) job;

            return njo.get("name").toString();
        } catch (Exception e){
            e.printStackTrace();
        }
        return "error";
    }

    public static String formatToUUID(String unformatted){
        StringBuilder formatted = new StringBuilder();
        int i = 1;
        for (Character character : unformatted.toCharArray()){
            if (i == 9 || i == 13 || i == 17 || i == 21){
                formatted.append("-").append(character);
            } else {
                formatted.append(character);
            }
            i++;
        }

        return formatted.toString();
    }

    public static ProxiedPlayer getPPlayer(UUID uuid){
        try {
            return StreamLine.getInstance().getProxy().getPlayer(uuid);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Player getPlayer(ProxiedPlayer player) {
        try {
            String name = getCachedName(player.getUniqueId());

            if (PlayerUtils.exists(name)) {
                if (PlayerUtils.hasStat(name)) {
                    return getPlayer(name);
                } else {
                    return new Player(name);
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Player getPlayer(CommandSender player) {
        try {
            String name = player.getName();

            if (PlayerUtils.exists(name)) {
                if (PlayerUtils.hasStat(name)) {
                    return getPlayer(name);
                } else {
                    return new Player(name);
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Player getPlayer(UUID uuid){
        try {
            String name = getCachedName(uuid);

            if (PlayerUtils.exists(name)) {
                if (PlayerUtils.hasStat(name)) {
                    return getPlayer(name);
                } else {
                    return PlayerUtils.getOrCreate(uuid);
                }
            } else {
                return null;
            }
        } catch (Exception e){
            return null;
        }
    }

    public static Player getPlayer(String name){
        try {
            if (PlayerUtils.exists(name)) {
                if (PlayerUtils.hasStat(name)) {
                    return PlayerUtils.getStat(name);
                } else {
                    return new Player(name);
                }
            }

            return null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String swapUUID(String uuid){
        if (uuid.contains("-")){
            return uuid.replace("-", "");
        } else {
            return formatToUUID(uuid);
        }
    }
}
