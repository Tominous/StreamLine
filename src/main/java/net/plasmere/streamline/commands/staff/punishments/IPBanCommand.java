package net.plasmere.streamline.commands.staff.punishments;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.md_5.bungee.config.Configuration;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.messaging.DiscordMessage;
import net.plasmere.streamline.objects.users.Player;
import net.plasmere.streamline.utils.*;

import java.text.DateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class IPBanCommand extends Command implements TabExecutor {
    private Configuration bans = StreamLine.bans.getBans();

    public IPBanCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length <= 1) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
        } else if (args.length > 2 && ! args[0].equals("add") && ! args[0].equals("temp")) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsLess);
        } else {
            List<String> ipsToBan = new ArrayList<>();
            if (args[1].contains(".")) {
                ipsToBan.add(args[1]);
            } else {
                Player other = PlayerUtils.getPlayerStat(args[1]);

                if (other == null) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    return;
                }

                ipsToBan.addAll(other.ipList);
            }

            if (args[0].equals("add")) {
                if (! args[1].contains(".")) {
                    Player other = PlayerUtils.getPlayerStat(args[1]);

                    if (other == null) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                        return;
                    }

                    String otherUUID = other.uuid;

                    if (PlayerUtils.hasOfflinePermission(ConfigUtils.punIPBansBypass, otherUUID)) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.ipBanCannot);
                        return;
                    }
                }

                if (args.length == 4 && ( args[2].endsWith("y") || args[2].endsWith("mo") || args[2].endsWith("w") || args[2].endsWith("d") || args[2].endsWith("h") || args[2].endsWith("m") || args[2].endsWith("s"))) {
                    for (String ip : ipsToBan) {
                        if (! ConfigUtils.punIPBansReplaceable) {
                            if (bans.contains(ip)) {
                                if (bans.getBoolean(ip + ".banned")) {
                                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.ipBanBTempAlready
                                            .replace("%ip%", ip)
                                    );
                                    return;
                                }
                            }
                        }

                        double toAdd = 0d;

                        try {
                            toAdd = TimeUtil.convertStringTimeToDouble(args[2]);
                        } catch (Exception e) {
                            e.printStackTrace();
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorSTime);
                            return;
                        }

                        String till = String.valueOf((long) (System.currentTimeMillis() + toAdd));

                        String reason = TextUtils.argsToStringMinus(args, 0, 1, 2);

                        bans.set(ip + ".banned", true);
                        bans.set(ip + ".reason", reason);
                        bans.set(ip + ".till", till);
                        bans.set(ip + ".sentenced", Instant.now().toString());
                        StreamLine.bans.saveConfig();

                        for (Player player : PlayerUtils.getPlayerStatsByIP(ip)) {
                            if (player.online) {
                                ProxiedPlayer pp = PlayerUtils.getPPlayerByUUID(player.uuid);

                                if (pp != null) {
                                    pp.disconnect(TextUtils.codedText(MessageConfUtils.punIPBannedTemp
                                            .replace("%reason%", reason)
                                            .replace("%date%", new Date(Long.parseLong(till)).toString())
                                    ));
                                }
                            }
                        }

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.ipBanBTempSender
                                .replace("%ip%", ip)
                                .replace("%reason%", reason)
                                .replace("%date%", new Date(Long.parseLong(till)).toString())
                        );

                        if (ConfigUtils.punIPBansDiscord) {
                            MessagingUtils.sendDiscordEBMessage(
                                    new DiscordMessage(
                                            sender,
                                            MessageConfUtils.ipBanEmbed,
                                            MessageConfUtils.ipBanBTempDiscord
                                                    .replace("%punisher%", sender.getName())
                                                    .replace("%ip%", ip)
                                                    .replace("%reason%", reason)
                                                    .replace("%date%", new Date(Long.parseLong(till)).toString())
                                            ,
                                            ConfigUtils.textChannelIPBans
                                    )
                            );
                        }

                        MessagingUtils.sendPermissionedMessageNonSelf(sender, ConfigUtils.staffPerm, MessageConfUtils.ipBanBTempStaff
                                .replace("%punisher%", sender.getName())
                                .replace("%ip%", ip)
                                .replace("%reason%", reason)
                                .replace("%date%", new Date(Long.parseLong(till)).toString())
                        );
                    }

                    return;
                }

                for (String ip : ipsToBan) {
                    if (! ConfigUtils.punIPBansReplaceable) {
                        if (bans.contains(ip)) {
                            if (bans.getBoolean(ip + ".banned")) {
                                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.ipBanBPermAlready
                                        .replace("%ip%", ip)
                                );
                                return;
                            }
                        }
                    }

                    String reason = TextUtils.argsToStringMinus(args, 0, 1, 2);

                    bans.set(ip + ".banned", true);
                    bans.set(ip + ".reason", reason);
                    bans.set(ip + ".till", "");
                    bans.set(ip + ".sentenced", Instant.now().toString());
                    StreamLine.bans.saveConfig();

                    for (Player player : PlayerUtils.getPlayerStatsByIP(ip)) {
                        if (player.online) {
                            ProxiedPlayer pp = PlayerUtils.getPPlayerByUUID(player.uuid);

                            if (pp != null) {
                                pp.disconnect(TextUtils.codedText(MessageConfUtils.punIPBannedPerm
                                        .replace("%reason%", reason)
                                ));
                            }
                        }
                    }

                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.ipBanBPermSender
                            .replace("%ip%", ip)
                            .replace("%reason%", reason)
                    );

                    if (ConfigUtils.punIPBansDiscord) {
                        MessagingUtils.sendDiscordEBMessage(
                                new DiscordMessage(
                                        sender,
                                        MessageConfUtils.ipBanEmbed,
                                        MessageConfUtils.ipBanBPermDiscord
                                                .replace("%punisher%", sender.getName())
                                                .replace("%ip%", ip)
                                                .replace("%reason%", reason)
                                        ,
                                        ConfigUtils.textChannelIPBans
                                )
                        );
                    }

                    MessagingUtils.sendPermissionedMessageNonSelf(sender, ConfigUtils.staffPerm, MessageConfUtils.ipBanBPermStaff
                            .replace("%punisher%", sender.getName())
                            .replace("%ip%", ip)
                            .replace("%reason%", reason)
                    );
                }
            } else if (args[0].equals("temp")) {
                if (! args[1].contains(".")) {
                    Player other = PlayerUtils.getPlayerStat(args[1]);

                    if (other == null) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                        return;
                    }

                    String otherUUID = other.uuid;

                    if (PlayerUtils.hasOfflinePermission(ConfigUtils.punIPBansBypass, otherUUID)) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.ipBanCannot);
                        return;
                    }
                }

                if (args.length < 4) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                } else {
                    for (String ip : ipsToBan) {
                        if (! ConfigUtils.punIPBansReplaceable) {
                            if (bans.contains(ip)) {
                                if (bans.getBoolean(ip + ".banned")) {
                                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.ipBanBTempAlready
                                            .replace("%ip%", ip)
                                    );
                                    return;
                                }
                            }
                        }

                        double toAdd = 0d;

                        try {
                            toAdd = TimeUtil.convertStringTimeToDouble(args[2]);
                        } catch (Exception e) {
                            e.printStackTrace();
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorSTime);
                            return;
                        }

                        String till = String.valueOf((long) (System.currentTimeMillis() + toAdd));

                        String reason = TextUtils.argsToStringMinus(args, 0, 1, 2);

                        bans.set(ip + ".banned", true);
                        bans.set(ip + ".reason", reason);
                        bans.set(ip + ".till", till);
                        bans.set(ip + ".sentenced", Instant.now().toString());
                        StreamLine.bans.saveConfig();

                        for (Player player : PlayerUtils.getPlayerStatsByIP(ip)) {
                            if (player.online) {
                                ProxiedPlayer pp = PlayerUtils.getPPlayerByUUID(player.uuid);

                                if (pp != null) {
                                    pp.disconnect(TextUtils.codedText(MessageConfUtils.punIPBannedTemp
                                            .replace("%reason%", reason)
                                            .replace("%date%", new Date(Long.parseLong(till)).toString())
                                    ));
                                }
                            }
                        }

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.ipBanBTempSender
                                .replace("%ip%", ip)
                                .replace("%reason%", reason)
                                .replace("%date%", new Date(Long.parseLong(till)).toString())
                        );

                        if (ConfigUtils.punIPBansDiscord) {
                            MessagingUtils.sendDiscordEBMessage(
                                    new DiscordMessage(
                                            sender,
                                            MessageConfUtils.ipBanEmbed,
                                            MessageConfUtils.ipBanBTempDiscord
                                                    .replace("%punisher%", sender.getName())
                                                    .replace("%ip%", ip)
                                                    .replace("%reason%", reason)
                                                    .replace("%date%", new Date(Long.parseLong(till)).toString())
                                            ,
                                            ConfigUtils.textChannelIPBans
                                    )
                            );
                        }

                        MessagingUtils.sendPermissionedMessageNonSelf(sender, ConfigUtils.staffPerm, MessageConfUtils.ipBanBTempStaff
                                .replace("%punisher%", sender.getName())
                                .replace("%ip%", ip)
                                .replace("%reason%", reason)
                                .replace("%date%", new Date(Long.parseLong(till)).toString())
                        );
                    }
                }
            } else if (args[0].equals("remove")) {
                for (String ip : ipsToBan) {
                    if (! bans.contains(ip)) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.ipBanUnAlready
                                .replace("%ip%", ip)
                        );
                        return;
                    }

                    bans.set(ip + ".banned", false);
                    StreamLine.bans.saveConfig();

                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.ipBanUnSender
                            .replace("%ip%", ip)
                    );

                    if (ConfigUtils.punIPBansDiscord) {
                        MessagingUtils.sendDiscordEBMessage(
                                new DiscordMessage(
                                        sender,
                                        MessageConfUtils.ipBanEmbed,
                                        MessageConfUtils.ipBanUnDiscord
                                                .replace("%punisher%", sender.getName())
                                                .replace("%ip%", ip)
                                        ,
                                        ConfigUtils.textChannelIPBans
                                )
                        );
                    }

                    MessagingUtils.sendPermissionedMessageNonSelf(sender, ConfigUtils.staffPerm, MessageConfUtils.ipBanUnStaff
                            .replace("%punisher%", sender.getName())
                            .replace("%ip%", ip)
                    );
                }
            } else if (args[0].equals("check")) {
                for (String ip : ipsToBan) {
                    String reason = bans.getString(ip + ".reason");
                    String bannedMillis = bans.getString(ip + ".till");
                    if (bannedMillis == null) bannedMillis = "";

                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.ipBanCheckMain
                            .replace("%player%", ip)
                            .replace("%check%", bans.getBoolean(ip + ".banned") ? MessageConfUtils.ipBanCheckBanned
                                    .replace("%date%", (! bannedMillis.equals("") ? new Date(Long.parseLong(bannedMillis)).toString() : MessageConfUtils.ipBanCheckNoDate))
                                    .replace("%reason%", reason)
                                    : MessageConfUtils.ipBanCheckUnBanned)
                    );
                }
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(final CommandSender sender, final String[] args) {
        Collection<ProxiedPlayer> players = StreamLine.getInstance().getProxy().getPlayers();
        List<String> strPlayers = new ArrayList<>();
        List<String> banned = new ArrayList<>();

        for (ProxiedPlayer player : players){
            if (sender instanceof ProxiedPlayer) if (player.equals(sender)) continue;
            strPlayers.add(player.getName());
        }

        for (String uuid : bans.getKeys()) {
            if (bans.getBoolean(uuid + ".banned")) banned.add(UUIDFetcher.getName(uuid));
        }

        List<String> options = new ArrayList<>();

        options.add("add");
        options.add("temp");
        options.add("remove");
        options.add("check");

        if (args.length == 1) {
            return TextUtils.getCompletion(options, args[0]);
        } else if (args.length == 2) {
            if (args[0].equals("remove")) {
                return TextUtils.getCompletion(banned, args[1]);
            } else {
                return TextUtils.getCompletion(strPlayers, args[1]);
            }
        }
//        else if (args.length == 3) {
//            final String param3 = args[2];
//
//
//        }

        return new ArrayList<>();
    }
}
