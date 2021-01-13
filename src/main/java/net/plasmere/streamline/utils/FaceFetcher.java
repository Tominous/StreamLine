package net.plasmere.streamline.utils;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FaceFetcher {
    public static String getFaceAvatarURL(ProxiedPlayer player){
        return "https://minotar.net/avatar/" + player.getName() + "/1280.png";
    }

    public static String getFaceAvatarURL(String  player){
        return "https://minotar.net/avatar/" + player + "/1280.png";
    }
}
