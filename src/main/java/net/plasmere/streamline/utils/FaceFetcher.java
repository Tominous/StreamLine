package net.plasmere.streamline.utils;

import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.objects.Guild;
import net.plasmere.streamline.objects.savable.users.Player;

public class FaceFetcher {
    public static String getFaceAvatarURL(Player player){
        return getPlaceholdersApplied(player, ConfigUtils.moduleAvatarLink);
    }

    public static String getFaceAvatarURL(String username){
        return getFaceAvatarURL(PlayerUtils.getPlayerStat(username));
    }

    public static String getPlaceholdersApplied(Player player, String string) {
        Guild guild = GuildUtils.getGuild(player);

        return string
                .replace("%player_absolute%", PlayerUtils.getAbsoluteBungee(player))
                .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(player))
                .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                .replace("%player_uuid%", player.uuid)
                .replace("%guild_uuid%", guild == null ? "" : guild.leaderUUID)
                .replace("%guild_name%", guild == null ? "" : guild.name)
                ;
    }
}
