package net.plasmere.streamline.utils;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.Party;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.dv8tion.jda.api.JDA;

import java.util.*;

public class MessagingUtils {
    private static final JDA jda = StreamLine.getJda();
    private static final EmbedBuilder eb = new EmbedBuilder();

    public static void sendStaffMessage(CommandSender sender, String from, String msg, StreamLine plugin){
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
                            .replace("%user%", sender.getName())
                            .replace("%from%", from)
                            .replace("%message%", msg)
                    )
            );
        }
    }

    public static void sendStaffMessageLogin(ProxiedPlayer person, StreamLine plugin){
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
            player.sendMessage(TextUtils.codedText(MessageConfUtils.sOnlineBungeeOnline
                            .replace("%staff%", person.getName())
                    )
            );
        }
    }

    public static void sendStaffMessageLogoff(ProxiedPlayer person, StreamLine plugin){
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
            player.sendMessage(TextUtils.codedText(MessageConfUtils.sOnlineBungeeOffline
                            .replace("%staff%", person.getName())
                    )
            );
        }
    }

    public static void sendStaffMessageSC(String sender, String from, String msg, StreamLine plugin){
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
                            .replace("%user%", sender)
                            .replace("%from%", from)
                            .replace("%message%", msg)
                    )
            );
        }
    }

    public static void sendStaffMessageReport(String sender, boolean fromBungee, String report, StreamLine plugin){
        Collection<ProxiedPlayer> staff = plugin.getProxy().getPlayers();
        Set<ProxiedPlayer> staffs = new HashSet<>(staff);

        for (ProxiedPlayer player : staff){
            try {
                if (! player.hasPermission("streamline.staff.reports")) {
                    staffs.remove(player);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        for (ProxiedPlayer player : staffs) {
            if (fromBungee)
                player.sendMessage(TextUtils.codedText(MessageConfUtils.bToBReportMessage
                                .replace("%reporter%", sender)
                                .replace("%report%", report)
                        )
                );
            else
                player.sendMessage(TextUtils.codedText(MessageConfUtils.dToBReportMessage
                                .replace("%reporter%", sender)
                                .replace("%report%", report)
                        )
                );
        }
    }

    public static void sendDiscordEBStaffMessage(String title, String msg){
        try {
            Objects.requireNonNull(
                    jda.getTextChannelById(ConfigUtils.textChannelStaffChat))
                    .sendMessage(
                            eb.setTitle(title)
                                    .setDescription(msg)
                                    .build()
                    ).queue();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void sendDiscordStaffMessageSC(CommandSender sender, String msg){
        try {
            Objects.requireNonNull(jda.getTextChannelById(ConfigUtils.textChannelStaffChat)).sendMessage(
                    eb.setTitle(MessageConfUtils.staffChatEmbedTitle)
                    .setDescription(TextUtils.newLined(
                            MessageConfUtils.discordStaffChatMessage
                                .replace("%user%", sender.getName())
                                .replace("%message%", msg)
                                .replace("%newline%", "\n")
                            )
                    ).build()
            ).queue();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void sendDiscordReportMessage(String sender, boolean fromBungee, String report){
        try {
            if (fromBungee)
                Objects.requireNonNull(jda.getTextChannelById(ConfigUtils.textChannelReports)).sendMessage(
                        eb.setTitle(MessageConfUtils.reportEmbedTitle)
                        .setDescription(TextUtils.newLined(
                                MessageConfUtils.bToDReportMessage
                                        .replace("%reporter%", sender)
                                        .replace("%report%", report)
                                )
                        ).build()
                ).queue();
            else
                Objects.requireNonNull(jda.getTextChannelById(ConfigUtils.textChannelReports)).sendMessage(
                        eb.setTitle(MessageConfUtils.reportEmbedTitle)
                        .setDescription(TextUtils.newLined(
                                MessageConfUtils.dToDReportMessage
                                    .replace("%reporter%", sender)
                                    .replace("%report%", report)
                                )
                        ).build()
                ).queue();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void sendDSelfMessage(MessageReceivedEvent context, String title, String description) {
        context.getChannel().sendMessage(
                eb.setTitle(title)
                .setDescription(TextUtils.newLined(description))
                        .build()
        ).queue();
    }

    public static void sendBPUserMessage(Party party, ProxiedPlayer sender, String msg){
        sender.sendMessage(TextUtils.codedText(msg
                .replace("%leader%", party.leader.getDisplayName())
                .replace("%size%", Integer.toString(party.getSize()))
                .replace("%user%", sender.getName())
        ));
    }

    public static void sendBUserMessage(CommandSender sender, String msg){
        sender.sendMessage(TextUtils.codedText(msg));
    }

    public static void sendBBroadcast(CommandSender sender, String msg){
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            player.sendMessage(TextUtils.codedText(msg));
        }
    }

    public static void sendBCLHBroadcast(CommandSender sender, String msg, String hoverPrefix){
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            player.sendMessage(TextUtils.clhText(msg, hoverPrefix));
        }
    }

    public static boolean compareWithList(String toCompare, List<String> list) {
        for (String item : list) {
            if (toCompare.equals(item))
                return true;
        }
        return false;
    }

    public static void sendDiscordPingRoleMessage(String channelId, String roleId){
        Objects.requireNonNull(StreamLine.getJda().getTextChannelById(channelId)).sendMessage(Objects.requireNonNull(StreamLine.getJda().getRoleById(roleId)).getAsMention()).queue();
    }

    public static void logInfo(String msg){
        StreamLine.getInstance().getLogger().info(TextUtils.newLined(msg));
    }

    public static void logWarning(String msg){
        StreamLine.getInstance().getLogger().warning(TextUtils.newLined(msg));
    }

    public static void logSevere(String msg){
        StreamLine.getInstance().getLogger().severe(TextUtils.newLined(msg));
    }
}
