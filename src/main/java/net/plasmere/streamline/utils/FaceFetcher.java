package net.plasmere.streamline.utils;

import net.plasmere.streamline.objects.savable.users.Player;

public class FaceFetcher {
    public static String getFaceAvatarURL(Player player){
        return "https://minotar.net/avatar/" + player.latestName + "/1280.png";
    }

    public static String getFaceAvatarURL(String player){
        return "https://minotar.net/avatar/" + player + "/1280.png";
    }
}
