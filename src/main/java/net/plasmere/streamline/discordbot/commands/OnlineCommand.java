package net.plasmere.streamline.discordbot.commands;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.utils.MessagingUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.utils.PlayerUtils;
import net.plasmere.streamline.utils.UUIDFetcher;

import java.util.Objects;

public class OnlineCommand {
    private static final EmbedBuilder eb = new EmbedBuilder();

    public static void sendMessage(String command, MessageReceivedEvent event){
        MessagingUtils.sendDSelfMessage(event,
                MessageConfUtils.onlineMessageEmbedTitle,
                MessageConfUtils.onlineMessageDiscord
                        .replace("%amount%", Integer.toString(StreamLine.getInstance().getProxy().getOnlineCount()))
                        .replace("%servers%", compileServers())
                        .replace("%online%", getOnline())
        );
        if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info("Sent message for \"" + command + "\"!");
    }

    private static String compileServers(){
        StringBuilder text = new StringBuilder();
        for (ServerInfo server : StreamLine.getInstance().getProxy().getServers().values()){
            if (server.getPlayers().size() > 0) {
                text.append(server.getName().toUpperCase()).append(": ").append(server.getPlayers().size()).append(" online...").append("\n");
            }
        }

        return text.toString();
    }

    private static String getOnline(){
        StringBuilder text = new StringBuilder();

        int i = 1;
        for (ProxiedPlayer player : StreamLine.getInstance().getProxy().getPlayers()){
            if (!player.hasPermission("streamline.staff.vanish")){
                if (i < StreamLine.getInstance().getProxy().getPlayers().size())
                    text.append(PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreate(player))).append(", ");
                else
                    text.append(PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrCreate(player))).append(".");
            } else {
                if (i < StreamLine.getInstance().getProxy().getPlayers().size())
                    text.append("HIDDEN").append(", ");
                else
                    text.append("HIDDEN").append(".");
            }
        }

        return text.toString();
    }
}
