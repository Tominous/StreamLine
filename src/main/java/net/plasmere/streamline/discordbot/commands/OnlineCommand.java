package net.plasmere.streamline.discordbot.commands;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.utils.MessagingUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class OnlineCommand {
    private static final EmbedBuilder eb = new EmbedBuilder();

    public static void sendMessage(String command, MessageReceivedEvent event, StreamLine plugin){
        MessagingUtils.sendDSelfMessage(event,
                MessageConfUtils.onlineMessageEmbedTitle,
                MessageConfUtils.onlineMessageDiscord
                        .replace("%amount%", Integer.toString(plugin.getProxy().getOnlineCount()))
                        .replace("%servers%", compileServers(plugin))
                        .replace("%online%", getOnline(plugin))
        );
        plugin.getLogger().info("Sent message for \"" + command + "\"!");
    }

    private static String compileServers(StreamLine plugin){
        StringBuilder text = new StringBuilder();
        for (ServerInfo server : plugin.getProxy().getServers().values()){
            if (server.getPlayers().size() > 0) {
                text.append(server.getName().toUpperCase()).append(": ").append(server.getPlayers().size()).append(" online...").append("\n");
            }
        }

        return text.toString();
    }

    private static String getOnline(StreamLine plugin){
        StringBuilder text = new StringBuilder();

        int i = 1;
        for (ProxiedPlayer player : plugin.getProxy().getPlayers()){
            if (!player.hasPermission("streamline.staff.vanish")){
                if (i < plugin.getProxy().getPlayers().size())
                    text.append(PlayerUtils.getOffOnReg(Objects.requireNonNull(PlayerUtils.getStat(player)))).append(", ");
                else
                    text.append(PlayerUtils.getOffOnReg(Objects.requireNonNull(PlayerUtils.getStat(player)))).append(".");
            } else {
                if (i < plugin.getProxy().getPlayers().size())
                    text.append("HIDDEN").append(", ");
                else
                    text.append("HIDDEN").append(".");
            }
        }

        return text.toString();
    }
}
