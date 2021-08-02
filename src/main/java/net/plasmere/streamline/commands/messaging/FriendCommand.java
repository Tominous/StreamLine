package net.plasmere.streamline.commands.messaging;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.users.ConsolePlayer;
import net.plasmere.streamline.objects.users.Player;
import net.plasmere.streamline.objects.users.SavableUser;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PlayerUtils;
import net.plasmere.streamline.utils.TextUtils;
import net.plasmere.streamline.utils.UUIDUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FriendCommand extends Command implements TabExecutor {
    public FriendCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        SavableUser stat = PlayerUtils.getOrCreateSavableUser(sender);

        if (stat == null) {
            stat = PlayerUtils.getOrCreateSavableUser(sender);
            if (stat == null) {
                MessagingUtils.logSevere("CANNOT INSTANTIATE THE PLAYER: " + sender.getName());
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
            SavableUser other;

            if (args[1].equals("%")) {
                other = PlayerUtils.getOrCreateSUByUUID("%");
            } else {
                if (! PlayerUtils.exists(args[1])) {
                    MessagingUtils.sendBUserMessage(sender, PlayerUtils.noStatsFound);
                    return;
                }

                other = PlayerUtils.getOrGetPlayerStat(args[1]);
            }

            if (other == null) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer);
                return;
            }

            switch (args[0]) {
                case "add":
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
                    if ((other instanceof Player && ((Player) other).online) || other instanceof ConsolePlayer) {
                        MessagingUtils.sendBUserMessage(other.sender, MessageConfUtils.friendReqOther
                                .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(stat))
                                .replace("%sender_normal%", (stat instanceof Player ? stat.latestName : stat.uuid))
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

                    if ((other instanceof Player && ((Player) other).online) || other instanceof ConsolePlayer) {
                        MessagingUtils.sendBUserMessage(other.sender, MessageConfUtils.friendAcceptOther
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
                    if ((other instanceof Player && ((Player) other).online) || other instanceof ConsolePlayer) {
                        MessagingUtils.sendBUserMessage(other.sender, MessageConfUtils.friendDenyOther
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
                    if ((other instanceof Player && ((Player) other).online) || other instanceof ConsolePlayer) {
                        MessagingUtils.sendBUserMessage(other.sender, MessageConfUtils.friendRemOther
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

            SavableUser player = PlayerUtils.getOrCreateSavableUser(sender);

            if (player == null) return new ArrayList<>();

            for (String uuid : player.friendList) {
                friends.add(UUIDUtils.getCachedName(uuid));
            }

            for (String uuid : player.pendingFromFriendList) {
                pending.add(UUIDUtils.getCachedName(uuid));
            }

            for (ProxiedPlayer pl : players) {
                if (pl.equals(sender)) continue;
                strPlayers.add(pl.getName());
            }

            strPlayers.add("%");

            List<String> options = new ArrayList<>();

            options.add("request");
            options.add("accept");
            options.add("deny");
            options.add("remove");
            options.add("list");
            options.add("add");

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
