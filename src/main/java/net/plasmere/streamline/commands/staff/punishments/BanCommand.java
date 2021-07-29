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
import net.plasmere.streamline.utils.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class BanCommand extends Command implements TabExecutor {
    private Configuration bans = StreamLine.bans.getBans();

    public BanCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length <= 1) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
        } else if (args.length > 2 && ! args[0].equals("add") && ! args[0].equals("temp")) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsLess);
        } else {
            String otherName = args[1];
            String otherUUID = UUIDFetcher.getCachedUUID(otherName);

            if (args[0].equals("add")) {
                if (PlayerUtils.hasOfflinePermission(ConfigUtils.punBansBypass, otherUUID)) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.banCannot);
                    return;
                }

                if (args.length == 4 && ( args[2].endsWith("y") || args[2].endsWith("mo") || args[2].endsWith("w") || args[2].endsWith("d") || args[2].endsWith("h") || args[2].endsWith("m") || args[2].endsWith("s"))) {
                    if (! ConfigUtils.punBansReplaceable) {
                        if (bans.contains(otherUUID)) {
                            if (bans.getBoolean(otherUUID + ".banned")) {
                                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.banBTempAlready
                                        .replace("%player%", otherName)
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

                    bans.set(otherUUID + ".banned", true);
                    bans.set(otherUUID + ".reason", reason);
                    bans.set(otherUUID + ".till", till);
                    bans.set(otherUUID + ".sentenced", Instant.now().toString());
                    StreamLine.bans.saveConfig();

                    if (PlayerUtils.isOnline(otherName)) {
                        ProxiedPlayer pp = PlayerUtils.getPPlayerByUUID(otherUUID);

                        if (pp != null) {
                            pp.disconnect(TextUtils.codedText(MessageConfUtils.punBannedTemp
                                    .replace("%reason%", reason)
                                    .replace("%date%", new Date(Long.parseLong(till)).toString())
                            ));
                        }
                    }

                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.banBTempSender
                            .replace("%player%", otherName)
                            .replace("%reason%", reason)
                            .replace("%date%", new Date(Long.parseLong(till)).toString())
                    );

                    if (ConfigUtils.punBansDiscord) {
                        MessagingUtils.sendDiscordEBMessage(
                                new DiscordMessage(
                                        sender,
                                        MessageConfUtils.banEmbed,
                                        MessageConfUtils.banBTempDiscord
                                                .replace("%punisher%", sender.getName())
                                                .replace("%player%", otherName)
                                                .replace("%reason%", reason)
                                                .replace("%date%", new Date(Long.parseLong(till)).toString())
                                        ,
                                        ConfigUtils.textChannelBans
                                )
                        );
                    }

                    MessagingUtils.sendPermissionedMessageNonSelf(sender, ConfigUtils.staffPerm, MessageConfUtils.banBTempStaff
                            .replace("%punisher%", sender.getName())
                            .replace("%player%", otherName)
                            .replace("%reason%", reason)
                            .replace("%date%", new Date(Long.parseLong(till)).toString())
                    );

                    return;
                }

                if (! ConfigUtils.punBansReplaceable) {
                    if (bans.contains(otherUUID)) {
                        if (bans.getBoolean(otherUUID + ".banned")) {
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.banBPermAlready
                                    .replace("%player%", otherName)
                            );
                            return;
                        }
                    }
                }

                String reason = TextUtils.argsToStringMinus(args, 0, 1);

                bans.set(otherUUID + ".banned", true);
                bans.set(otherUUID + ".reason", reason);
                bans.set(otherUUID + ".till", "");
                bans.set(otherUUID + ".sentenced", Instant.now().toString());
                StreamLine.bans.saveConfig();

                if (PlayerUtils.isOnline(otherName)) {
                    ProxiedPlayer pp = PlayerUtils.getPPlayerByUUID(otherUUID);

                    if (pp != null) {
                        pp.disconnect(TextUtils.codedText(MessageConfUtils.punBannedPerm
                                .replace("%reason%", reason)
                        ));
                    }
                }

                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.banBPermSender
                        .replace("%player%", otherName)
                        .replace("%reason%", reason)
                );

                if (ConfigUtils.punBansDiscord) {
                    MessagingUtils.sendDiscordEBMessage(
                            new DiscordMessage(
                                    sender,
                                    MessageConfUtils.banEmbed,
                                    MessageConfUtils.banBPermDiscord
                                            .replace("%punisher%", sender.getName())
                                            .replace("%player%", otherName)
                                            .replace("%reason%", reason)
                                    ,
                                    ConfigUtils.textChannelBans
                            )
                    );
                }

                MessagingUtils.sendPermissionedMessageNonSelf(sender, ConfigUtils.staffPerm, MessageConfUtils.banBPermStaff
                        .replace("%punisher%", sender.getName())
                        .replace("%player%", otherName)
                        .replace("%reason%", reason)
                );
            } else if (args[0].equals("temp")) {
                if (PlayerUtils.hasOfflinePermission(ConfigUtils.punBansBypass, otherUUID)) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.banCannot);
                    return;
                }

                if (args.length < 4) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                } else {
                    if (! ConfigUtils.punBansReplaceable) {
                        if (bans.contains(otherUUID)) {
                            if (bans.getBoolean(otherUUID + ".banned")) {
                                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.muteMTempAlready
                                        .replace("%player%", otherName)
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

                    bans.set(otherUUID + ".banned", true);
                    bans.set(otherUUID + ".reason", reason);
                    bans.set(otherUUID + ".till", till);
                    bans.set(otherUUID + ".sentenced", Instant.now().toString());
                    StreamLine.bans.saveConfig();

                    if (PlayerUtils.isOnline(otherName)) {
                        ProxiedPlayer pp = PlayerUtils.getPPlayerByUUID(otherUUID);

                        if (pp != null) {
                            pp.disconnect(TextUtils.codedText(MessageConfUtils.punBannedTemp
                                    .replace("%reason%", reason)
                                    .replace("%date%", new Date(Long.parseLong(till)).toString())
                            ));
                        }
                    }

                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.banBTempSender
                            .replace("%player%", otherName)
                            .replace("%reason%", reason)
                            .replace("%date%", new Date(Long.parseLong(till)).toString())
                    );

                    if (ConfigUtils.punBansDiscord) {
                        MessagingUtils.sendDiscordEBMessage(
                                new DiscordMessage(
                                        sender,
                                        MessageConfUtils.banEmbed,
                                        MessageConfUtils.banBTempDiscord
                                                .replace("%punisher%", sender.getName())
                                                .replace("%player%", otherName)
                                                .replace("%reason%", reason)
                                                .replace("%date%", new Date(Long.parseLong(till)).toString())
                                        ,
                                        ConfigUtils.textChannelBans
                                )
                        );
                    }

                    MessagingUtils.sendPermissionedMessageNonSelf(sender, ConfigUtils.staffPerm, MessageConfUtils.banBTempStaff
                            .replace("%punisher%", sender.getName())
                            .replace("%player%", otherName)
                            .replace("%reason%", reason)
                            .replace("%date%", new Date(Long.parseLong(till)).toString())
                    );
                }
            } else if (args[0].equals("remove")) {
                if (! bans.contains(otherUUID)) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.banUnAlready
                            .replace("%player%", otherName)
                    );
                    return;
                }

                bans.set(otherUUID + ".banned", false);
                StreamLine.bans.saveConfig();

                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.banUnSender
                        .replace("%player%", otherName)
                );

                if (ConfigUtils.punBansDiscord) {
                    MessagingUtils.sendDiscordEBMessage(
                            new DiscordMessage(
                                    sender,
                                    MessageConfUtils.banEmbed,
                                    MessageConfUtils.banUnDiscord
                                            .replace("%punisher%", sender.getName())
                                            .replace("%player%", otherName)
                                    ,
                                    ConfigUtils.textChannelBans
                            )
                    );
                }

                MessagingUtils.sendPermissionedMessageNonSelf(sender, ConfigUtils.staffPerm, MessageConfUtils.banUnStaff
                        .replace("%punisher%", sender.getName())
                        .replace("%player%", otherName)
                );
            } else if (args[0].equals("check")) {
                String reason = bans.getString(otherUUID + ".reason");
                String bannedMillis = bans.getString(otherUUID + ".till");
                if (bannedMillis == null) bannedMillis = "";

                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.banCheckMain
                        .replace("%player%", otherName)
                        .replace("%check%", bans.getBoolean(otherUUID + ".banned") ? MessageConfUtils.banCheckBanned
                                .replace("%date%", (! bannedMillis.equals("") ? new Date(Long.parseLong(bannedMillis)).toString() : MessageConfUtils.banCheckNoDate))
                                .replace("%reason%", reason)
                                : MessageConfUtils.banCheckUnBanned)
                );
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
            if (uuid.contains("_")) continue;
            if (bans.getBoolean(uuid + ".banned")) banned.add(UUIDFetcher.getCachedName(uuid));
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
