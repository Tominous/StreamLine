package net.plasmere.streamline.discordbot.commands;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.Config;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.utils.TextUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class StaffChatCommand {
    private static final EmbedBuilder eb = new EmbedBuilder();

    public static void sendMessage(String command, MessageReceivedEvent event, StreamLine plugin){
        String om = event.getMessage().getContentDisplay();
        String prefix = ConfigUtils.botPrefix;

        String msg = om.substring((prefix + command + " ").length());

        Collection<ProxiedPlayer> staff = plugin.getProxy().getPlayers();
        Set<ProxiedPlayer> staffs = new HashSet<>(staff);

        for (ProxiedPlayer player : staff){
            try {
                if (! player.hasPermission("streamline.staff")) {
                    staffs.remove(player);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        for (ProxiedPlayer player : staffs) {
            player.sendMessage(TextUtils.codedText(MessageConfUtils.bungeeStaffChatMessage
                            .replace("%user%", event.getAuthor().getName())
                            .replace("%from%", MessageConfUtils.bungeeStaffChatFrom)
                            .replace("%message%", msg)
                            .replace("%newline%", "\n")
                    )
            );
        }

        try {
            event.getChannel().sendMessage(eb.setTitle("Success!").setDescription("Message sent!").build()).queue();
        } catch (Exception e){
            e.printStackTrace();
        }

        plugin.getLogger().info("Sent message for \"" + command + "\"!");
    }
}
