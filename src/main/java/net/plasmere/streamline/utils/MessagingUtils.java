package net.plasmere.streamline.utils;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.BungeeMassMessage;
import net.plasmere.streamline.objects.BungeeMessage;
import net.plasmere.streamline.objects.DiscordMessage;
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
    private static final StreamLine plugin = StreamLine.getInstance();

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

    public static void sendBungeeMessage(BungeeMassMessage message){
        Collection<ProxiedPlayer> staff = plugin.getProxy().getPlayers();
        Set<ProxiedPlayer> people = new HashSet<>(staff);

        for (ProxiedPlayer player : people){
            try {
                if (! player.hasPermission(message.permission)) {
                    people.remove(player);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        for (ProxiedPlayer player : people) {
            sendBungeeMessage(new BungeeMessage(message.sender, player, message.title, message.transition, message.message));
        }
    }

    public static void sendBungeeMessage(BungeeMessage message){
        message.to.sendMessage(TextUtils.codedText((message.title + message.transition + message.message)
                        .replace("%sender%", message.sender.getName())
                )
        );
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

    public static void sendDiscordEBMessage(DiscordMessage message){
        try {
            Objects.requireNonNull(jda.getTextChannelById(message.channel))
                    .sendMessage(
                            eb.setTitle(message.title.replace("%sender%", message.sender.getName()))
                                    .setDescription(message.message.replace("%sender%", message.sender.getName()))
                                    .build()
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

    public static void sendBPUserMessage(Party party, CommandSender sender, CommandSender to, String msg){
        to.sendMessage(TextUtils.codedText(msg
                .replace("%sender%", ((ProxiedPlayer) sender).getDisplayName())
                .replace("%leader%", party.leader.getName())
                .replace("%size%", Integer.toString(party.getSize()))
                .replace("%max%", Integer.toString(party.maxSize))
                .replace("%maxmax%", Integer.toString(party.getMaxSize(party.leader)))
                .replace("%mods_count%", Integer.toString(party.moderators.size()))
                .replace("%members_count%", Integer.toString(party.members.size()))
                .replace("%invites_count%", Integer.toString(party.invites.size()))
                .replace("%mods%", mods(party))
                .replace("%members%", members(party))
                .replace("%invites%", invites(party))
                .replace("%ispublic%", getIsPublic(party))
                .replace("%ismuted%", getIsMuted(party))
        ));
    }

    public static void sendBUserMessage(CommandSender sender, String msg){
        if (sender instanceof ProxiedPlayer) {
            sender.sendMessage(TextUtils.codedText(msg
                    .replace("%sender%", ((ProxiedPlayer) sender).getDisplayName())
            ));
        } else {
            sender.sendMessage(TextUtils.codedText(msg
                    .replace("%sender%", sender.getName())
            ));
        }
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

    public static String mods(Party party){
        StringBuilder msg = new StringBuilder();

        int i = 1;
        for (ProxiedPlayer m : party.moderators){
            if (i < party.moderators.size()){
                msg.append(MessageConfUtils.partiesModsNLast
                        .replace("%user%", m.getName())
                );
            } else {
                msg.append(MessageConfUtils.partiesModsLast
                        .replace("%user%", m.getName())
                );
            }

            i++;
        }

        return msg.toString();
    }

    public static String members(Party party){
        StringBuilder msg = new StringBuilder();

        int i = 1;
        for (ProxiedPlayer m : party.members){
            if (i < party.members.size()){
                msg.append(MessageConfUtils.partiesMemsNLast
                        .replace("%user%", m.getName())
                );
            } else {
                msg.append(MessageConfUtils.partiesMemsLast
                        .replace("%user%", m.getName())
                );
            }

            i++;
        }

        return msg.toString();
    }

    public static String invites(Party party){
        StringBuilder msg = new StringBuilder();

        int i = 1;
        for (ProxiedPlayer m : party.invites){
            if (i < party.invites.size()){
                msg.append(MessageConfUtils.partiesInvsNLast
                        .replace("%user%", m.getName())
                );
            } else {
                msg.append(MessageConfUtils.partiesInvsLast
                        .replace("%user%", m.getName())
                );
            }

            i++;
        }

        return msg.toString();
    }

    public static String getIsPublic(Party party){
        return party.isPublic ? MessageConfUtils.partiesIsPublicTrue : MessageConfUtils.partiesIsPublicFalse;
    }

    public static String getIsMuted(Party party){
        return party.isMuted ? MessageConfUtils.partiesIsMutedTrue : MessageConfUtils.partiesIsMutedFalse;
    }
}
