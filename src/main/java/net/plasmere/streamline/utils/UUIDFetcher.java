package net.plasmere.streamline.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.objects.Player;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

public class UUIDFetcher {
    static public UUID fetch(String username) {
        try {
            String JSONString = "";

            HttpURLConnection connection = (HttpURLConnection) new URL("https://api.mojang.com/users/profiles/minecraft/" + username).openConnection();
            InputStream is = connection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null) {
                JSONString = line;
            }

            Object obj = new JsonParser().parse(JSONString);
            JsonObject jo = (JsonObject) obj;

            String id = jo.get("id").getAsString();

            // String uuid = id.substring(0, 7) + "-" + id.substring(8, 11) + "-"
            //        + id.substring(12, 15) + "-" + id.substring(16, 19) + "-"
            //        + id.substring(20);
            String uuid = formatToUUID(id);

            return UUID.fromString(uuid);
            //return UUID.fromString(id);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
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
        return null;
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

    public static Player getPlayer(UUID uuid){
        try {
            return new Player(StreamLine.getInstance().getProxy().getPlayer(uuid), false);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Player getPlayer(String name){
        ProxyServer proxy = StreamLine.getInstance().getProxy();
        try {
            return new Player(Objects.requireNonNull(proxy).getPlayer(fetch(name)), false);
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
