package net.plasmere.streamline.commands.staff.punishments;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.DiscordBotConfUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.messaging.DiscordMessage;
import net.plasmere.streamline.objects.savable.users.Player;
import net.plasmere.streamline.utils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class MuteCommand extends Command implements TabExecutor {
    public MuteCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
        } else if (args.length > 2 && ! (args[0].equals("add") || args[0].equals("temp"))) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsLess());
        } else if (args.length > 3) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsLess());
        } else {
            Player other = PlayerUtils.getOrGetPlayerStat(args[1]);

            if (other == null) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer());
                return;
            }

            if (args[0].equals("add")) {
                if (PlayerUtils.hasOfflinePermission(ConfigUtils.punMutesBypass, other.uuid)) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.muteCannot());
                    return;
                }

                if (args.length == 3) {
                    if (! ConfigUtils.punMutesReplaceable) {
                        if (other.muted) {
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.muteMTempAlready());
                            return;
                        }
                    }

                    final double timeAmount = TimeUtil.convertStringTimeToDouble(args[2]);

                    if (timeAmount == -1d) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorInt());
                        return;
                    }

                    other.updateMute(true, new Date((long) (System.currentTimeMillis() + timeAmount)));

                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.muteMTempSender()
                            .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(other))
                            .replace("%date%", other.mutedTill.toString())
                    );
                    if (other.online) {
                        MessagingUtils.sendBUserMessage(PlayerUtils.getPPlayerByUUID(other.uuid), MessageConfUtils.muteMTempMuted()
                                .replace("%sender_display%", sender instanceof ProxyServer ? "CONSOLE" : PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreatePlayerStatByUUID(((ProxiedPlayer) sender).getUniqueId().toString())))
                                .replace("%date%", other.mutedTill.toString())
                        );
                    }

                    if (ConfigUtils.moduleDEnabled) {
                        if (ConfigUtils.punMutesDiscord) {
                            MessagingUtils.sendDiscordEBMessage(
                                    new DiscordMessage(
                                            sender,
                                            MessageConfUtils.muteEmbed(),
                                            MessageConfUtils.muteMTempDiscord()
                                                    .replace("%punisher%", sender.getName())
                                                    .replace("%player_display%", other.latestName)
                                                    .replace("%date%", other.mutedTill.toString())
                                            ,
                                            DiscordBotConfUtils.textChannelMutes
                                    )
                            );
                        }
                    }

                    MessagingUtils.sendPermissionedMessageNonSelf(sender, ConfigUtils.staffPerm, MessageConfUtils.muteMTempStaff()
                            .replace("%punisher%", sender.getName())
                            .replace("%player_display%", other.latestName)
                            .replace("%date%", other.mutedTill.toString())
                    );

                    return;
                }

                if (! ConfigUtils.punMutesReplaceable) {
                    if (other.muted) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.muteMPermAlready());
                        return;
                    }
                }

                other.setMuted(true);
                other.removeMutedTill();

                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.muteMPermSender()
                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(other))
                );
                if (other.online) {
                    MessagingUtils.sendBUserMessage(PlayerUtils.getPPlayerByUUID(other.uuid), MessageConfUtils.muteMPermMuted()
                            .replace("%sender_display%", sender instanceof ProxyServer ? "CONSOLE" : PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreatePlayerStatByUUID(((ProxiedPlayer) sender).getUniqueId().toString())))
                    );
                }

                if (ConfigUtils.moduleDEnabled) {
                    if (ConfigUtils.punMutesDiscord) {
                        MessagingUtils.sendDiscordEBMessage(
                                new DiscordMessage(
                                        sender,
                                        MessageConfUtils.muteEmbed(),
                                        MessageConfUtils.muteMPermDiscord()
                                                .replace("%punisher%", sender.getName())
                                                .replace("%player_display%", other.latestName)
                                        ,
                                        DiscordBotConfUtils.textChannelMutes
                                )
                        );
                    }
                }

                MessagingUtils.sendPermissionedMessageNonSelf(sender, ConfigUtils.staffPerm, MessageConfUtils.muteMPermStaff()
                        .replace("%punisher%", sender.getName())
                        .replace("%player_display%", other.latestName)
                );
            } else if (args[0].equals("temp")) {
                if (args.length < 3) {
                    return;
                }

                if (PlayerUtils.hasOfflinePermission(ConfigUtils.punMutesBypass, other.uuid)) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.muteCannot());
                    return;
                }
                if (!ConfigUtils.punMutesReplaceable) {
                    if (other.muted) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.muteMTempAlready());
                        return;
                    }
                }

                final double timeAmount = TimeUtil.convertStringTimeToDouble(args[2]);

                if (timeAmount == -1d) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorInt());
                    return;
                }

                other.updateMute(true, new Date((long) (System.currentTimeMillis() + timeAmount)));

                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.muteMTempSender()
                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(other))
                        .replace("%date%", other.mutedTill.toString())
                );
                if (other.online) {
                    MessagingUtils.sendBUserMessage(PlayerUtils.getPPlayerByUUID(other.uuid), MessageConfUtils.muteMTempMuted()
                            .replace("%sender_display%", sender instanceof ProxyServer ? "CONSOLE" : PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreatePlayerStatByUUID(((ProxiedPlayer) sender).getUniqueId().toString())))
                            .replace("%date%", other.mutedTill.toString())
                    );
                }

                if (ConfigUtils.punMutesDiscord) {
                    MessagingUtils.sendDiscordEBMessage(
                            new DiscordMessage(
                                    sender,
                                    MessageConfUtils.muteEmbed(),
                                    MessageConfUtils.muteMTempDiscord()
                                            .replace("%punisher%", sender.getName())
                                            .replace("%player_display%", other.latestName)
                                            .replace("%date%", other.mutedTill.toString())
                                    ,
                                    DiscordBotConfUtils.textChannelMutes
                            )
                    );
                }

                MessagingUtils.sendPermissionedMessageNonSelf(sender, ConfigUtils.staffPerm, MessageConfUtils.muteMTempStaff()
                        .replace("%punisher%", sender.getName())
                        .replace("%player_display%", other.latestName)
                        .replace("%date%", other.mutedTill.toString())
                );
            } else if (args[0].equals("remove")) {
                if (! other.muted) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.muteUnAlready());
                    return;
                }

                other.setMuted(false);
                other.removeMutedTill();

                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.muteUnSender()
                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(other))
                );
                if (other.online) {
                    MessagingUtils.sendBUserMessage(PlayerUtils.getPPlayerByUUID(other.uuid), MessageConfUtils.muteUnMuted()
                            .replace("%sender_display%", sender instanceof ProxyServer ? "CONSOLE" : PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreatePlayerStatByUUID(((ProxiedPlayer) sender).getUniqueId().toString())))
                    );
                }

                if (ConfigUtils.punMutesDiscord) {
                    MessagingUtils.sendDiscordEBMessage(
                            new DiscordMessage(
                                    sender,
                                    MessageConfUtils.muteEmbed(),
                                    MessageConfUtils.muteUnDiscord()
                                            .replace("%punisher%", sender.getName())
                                            .replace("%player_display%", other.latestName)
                                    ,
                                    DiscordBotConfUtils.textChannelMutes
                            )
                    );
                }

                MessagingUtils.sendPermissionedMessageNonSelf(sender, ConfigUtils.staffPerm, MessageConfUtils.muteUnStaff()
                        .replace("%punisher%", sender.getName())
                        .replace("%player_display%", other.latestName)
                );
            } else if (args[0].equals("check")) {
                Date checked = other.mutedTill;

                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.muteCheckMain()
                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(other))
                        .replace("%check%", other.muted ? MessageConfUtils.muteCheckMuted()
                                .replace("%date%", (! (checked == null) ? checked.toString() : MessageConfUtils.muteCheckNoDate()))
                                : MessageConfUtils.muteCheckUnMuted())
                );
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(final CommandSender sender, final String[] args) {
        Collection<ProxiedPlayer> players = StreamLine.getInstance().getProxy().getPlayers();
        List<String> strPlayers = new ArrayList<>();

        for (ProxiedPlayer player : players){
            if (sender instanceof ProxiedPlayer) if (player.equals(sender)) continue;
            strPlayers.add(player.getName());
        }

        List<String> options = new ArrayList<>();

        options.add("add");
        options.add("temp");
        options.add("remove");
        options.add("check");

        if (args.length == 1) {
            return TextUtils.getCompletion(options, args[0]);
        } else if (args.length == 2) {
            return TextUtils.getCompletion(strPlayers, args[1]);
        }

        return new ArrayList<>();
    }
}
