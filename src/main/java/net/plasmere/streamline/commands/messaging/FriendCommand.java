package net.plasmere.streamline.commands.messaging;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PlayerUtils;
import net.plasmere.streamline.utils.TextUtils;
import net.plasmere.streamline.utils.UUIDFetcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class FriendCommand extends Command implements TabExecutor {
    public FriendCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxyServer) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.onlyPlayers);
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        Player stat = PlayerUtils.getStat(player);

        if (stat == null) {
            PlayerUtils.addStat(new Player(player));
            stat = PlayerUtils.getStat(sender);
            if (stat == null) {
                StreamLine.getInstance().getLogger().severe("CANNOT INSTANTIATE THE PLAYER: " + args[0]);
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                return;
            }
        }

        if (args.length <= 0) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
        } else if (args.length < 2 && args[0].equals("list")) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.friendListMain
                    .replace("%player%", PlayerUtils.getOffOnDisplayBungee(stat))
                    .replace("%friends%", PlayerUtils.getFriended(stat))
                    .replace("%pending-to%", PlayerUtils.getPTFriended(stat))
                    .replace("%pending-from%", PlayerUtils.getPFFriended(stat))
            );
        } else if (args.length < 2) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
        } else if (args.length > 2) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsLess);
        } else {
            Player other = PlayerUtils.getStat(args[1]);

            if (other == null) {
                PlayerUtils.addStat(new Player(UUIDFetcher.getCachedUUID(args[1])));
                other = PlayerUtils.getStat(args[1]);
                if (other == null) {
                    StreamLine.getInstance().getLogger().severe("CANNOT INSTANTIATE THE PLAYER: " + args[1]);
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    return;
                }
            }

            if (other.uuid == null) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer);
                return;
            }

            switch (args[0]) {
                case "request":
                    if (stat.equals(other)) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.friendReqNSelf);
                        return;
                    }

                    if (stat.friendList.contains(other.uuid)) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.friendReqAlready
                                .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                        );
                        return;
                    }

                    if (other.ignoredList.contains(stat.uuid)) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.friendReqIgnored
                                .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                        );
                        return;
                    }

                    stat.tryAddNewPendingToFriend(other.uuid);
                    other.tryAddNewPendingFromFriend(stat.uuid);
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.friendReqSelf
                            .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                    );
                    if (other.online) {
                        MessagingUtils.sendBUserMessage(UUIDFetcher.getPPlayerByUUID(other.uuid), MessageConfUtils.friendReqOther
                                .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(stat))
                                .replace("%sender_name%", stat.latestName)
                        );
                    }
                    break;
                case "accept":
                    if (! stat.pendingFromFriendList.contains(other.uuid)) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.friendAcceptNone
                                .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                        );
                        return;
                    }

                    stat.tryAddNewFriend(other.uuid);
                    other.tryAddNewFriend(stat.uuid);
                    other.tryRemPendingToFriend(stat.uuid);
                    stat.tryRemPendingFromFriend(other.uuid);

                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.friendAcceptSelf
                            .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                    );
                    if (other.online) {
                        MessagingUtils.sendBUserMessage(UUIDFetcher.getPPlayerByUUID(other.uuid), MessageConfUtils.friendAcceptOther
                                .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(stat))
                        );
                    }
                    break;
                case "deny":
                    if (! stat.pendingFromFriendList.contains(other.uuid)) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.friendDenyNone
                                .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                        );
                        return;
                    }

                    stat.tryRemPendingFromFriend(other.uuid);
                    other.tryRemPendingToFriend(stat.uuid);

                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.friendDenySelf
                            .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                    );
                    if (other.online) {
                        MessagingUtils.sendBUserMessage(UUIDFetcher.getPPlayerByUUID(other.uuid), MessageConfUtils.friendDenyOther
                                .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(stat))
                        );
                    }
                    break;
                case "remove":
                    if (stat.equals(other)) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.friendRemNSelf);
                        return;
                    }

                    if (! stat.friendList.contains(other.uuid)) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.friendRemAlready
                                .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                        );
                        return;
                    }

                    stat.tryRemPendingToFriend(other.uuid);
                    other.tryRemPendingToFriend(stat.uuid);
                    stat.tryRemPendingFromFriend(other.uuid);
                    other.tryRemPendingFromFriend(stat.uuid);
                    stat.tryRemFriend(other.uuid);
                    other.tryRemFriend(stat.uuid);
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.friendRemSelf
                            .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                    );
                    if (other.online) {
                        MessagingUtils.sendBUserMessage(UUIDFetcher.getPPlayerByUUID(other.uuid), MessageConfUtils.friendRemOther
                                .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(stat))
                        );
                    }
                    break;
                case "list":
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.friendListMain
                            .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                            .replace("%friends%", PlayerUtils.getFriended(other))
                            .replace("%pending-to%", PlayerUtils.getPTFriended(other))
                            .replace("%pending-from%", PlayerUtils.getPFFriended(other))
                    );
                    break;
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(final CommandSender sender, final String[] args) {
        if (sender instanceof ProxiedPlayer) {
            Collection<ProxiedPlayer> players = StreamLine.getInstance().getProxy().getPlayers();
            List<String> strPlayers = new ArrayList<>();
            List<String> friends = new ArrayList<>();
            List<String> pending = new ArrayList<>();

            ProxiedPlayer p = (ProxiedPlayer) sender;

            Player player = PlayerUtils.getOrCreate(p.getUniqueId().toString());

            for (String uuid : player.friendList) {
                friends.add(UUIDFetcher.getCachedName(uuid));
            }

            for (String uuid : player.pendingFromFriendList) {
                pending.add(UUIDFetcher.getCachedName(uuid));
            }

            for (ProxiedPlayer pl : players) {
                if (pl.equals(sender)) continue;
                strPlayers.add(pl.getName());
            }

            List<String> options = new ArrayList<>();

            options.add("request");
            options.add("accept");
            options.add("deny");
            options.add("remove");
            options.add("list");

            if (args.length == 1) {
                return TextUtils.getCompletion(options, args[0]);
            } else if (args.length == 2) {
                if (args[0].equals("accept") || args[0].equals("deny")) {
                    return TextUtils.getCompletion(pending, args[1]);
                } else if (args[0].equals("remove")) {
                    return TextUtils.getCompletion(friends, args[1]);
                } else {
                    return TextUtils.getCompletion(strPlayers, args[1]);
                }
            }

            return new ArrayList<>();
        }

        return new ArrayList<>();
    }
}
