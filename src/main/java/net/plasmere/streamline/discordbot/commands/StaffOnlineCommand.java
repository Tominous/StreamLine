package net.plasmere.streamline.discordbot.commands;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.utils.MessagingUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.utils.PlayerUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class StaffOnlineCommand {
    private static final EmbedBuilder eb = new EmbedBuilder();

    public static void sendMessage(String command, MessageReceivedEvent event, StreamLine plugin){
        Collection<ProxiedPlayer> staffs = plugin.getProxy().getPlayers();
        Set<ProxiedPlayer> lstaffs = new HashSet<>(staffs);

        for (ProxiedPlayer player : staffs){
            try {
                if (! player.hasPermission("streamline.staff")) {
                    lstaffs.remove(player);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        MessagingUtils.sendDSelfMessage(event,
                MessageConfUtils.sOnlineMessageEmbedTitle,
                MessageConfUtils.sOnlineDiscordMain
                        .replace("%amount%", Integer.toString(lstaffs.size()))
                        .replace("%staffbulk%", getStaffList(lstaffs))
        );

        plugin.getLogger().info("Sent message for \"" + command + "\"!");
    }

    private static String getStaffList(Set<ProxiedPlayer> lstaffs){
        StringBuilder staff = new StringBuilder();
        int i = 1;

        for (ProxiedPlayer player : lstaffs){
            if (i < lstaffs.size())
                staff.append(MessageConfUtils.sOnlineDiscordBulkNotLast
                        .replace("%player%", PlayerUtils.getOffOnRegBungee(Objects.requireNonNull(PlayerUtils.getStat(player))))
                        .replace("%server%", player.getServer().getInfo().getName().toLowerCase())
                );
            else
                staff.append(MessageConfUtils.sOnlineDiscordBulkLast
                        .replace("%player%", PlayerUtils.getOffOnRegBungee(Objects.requireNonNull(PlayerUtils.getStat(player))))
                        .replace("%server%", player.getServer().getInfo().getName().toLowerCase())
                );
            i++;
        }

        return staff.toString();
    }
}
