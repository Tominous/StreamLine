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

import java.util.*;

public class IgnoreCommand extends Command implements TabExecutor {
    public IgnoreCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        SavableUser stat = PlayerUtils.getOrGetSavableUser(sender.getName());

        if (stat == null) {
            stat = PlayerUtils.getOrCreateSavableUser(sender);
            if (stat == null) {
                MessagingUtils.logSevere("CANNOT INSTANTIATE THE PLAYER: " + sender.getName());
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                return;
            }
        }

        if (args.length <= 0) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
        } else if (args.length < 2 && args[0].equals("list")) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.ignoreListMain()
                    .replace("%player%", PlayerUtils.getOffOnDisplayBungee(stat))
                    .replace("%ignores%", PlayerUtils.getIgnored(stat))
            );
        } else if (args.length < 2) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
        } else if (args.length > 2) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsLess());
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
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer());
                return;
            }

            switch (args[0]) {
                case "add":
                    if (stat.equals(other)) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.ignoreAddNSelf());
                        return;
                    }

                    if (stat.ignoredList.contains(other.uuid)) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.ignoreAddAlready()
                                .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                        );
                        return;
                    }

                    stat.tryAddNewIgnored(other.uuid);
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.ignoreAddSelf()
                            .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                    );
                    if ((other instanceof Player && ((Player) other).online) || other instanceof ConsolePlayer) {
                        MessagingUtils.sendBUserMessage(other.findSender(), MessageConfUtils.ignoreAddIgnored()
                                .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(stat))
                        );
                    }
                    break;
                case "remove":
                    if (stat.equals(other)) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.ignoreRemNSelf());
                        return;
                    }

                    if (!stat.ignoredList.contains(other.uuid)) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.ignoreRemAlready()
                                .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                        );
                        return;
                    }

                    stat.tryRemIgnored(other.uuid);
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.ignoreRemSelf()
                            .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                    );
                    if ((other instanceof Player && ((Player) other).online) || other instanceof ConsolePlayer) {
                        MessagingUtils.sendBUserMessage(other.findSender(), MessageConfUtils.ignoreRemIgnored()
                                .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(stat))
                        );
                    }
                    break;
                case "list":
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.ignoreListMain()
                            .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                            .replace("%ignores%", PlayerUtils.getIgnored(other))
                    );
                    break;
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(final CommandSender sender, final String[] args) {
        Collection<ProxiedPlayer> players = StreamLine.getInstance().getProxy().getPlayers();
        List<String> strPlayers = new ArrayList<>();
        List<String> ignored = new ArrayList<>();

        SavableUser player = PlayerUtils.getOrCreateSavableUser(sender);

        if (player == null) return new ArrayList<>();

        for (String uuid : player.ignoredList) {
            ignored.add(UUIDUtils.getCachedName(uuid));
        }

        for (ProxiedPlayer pl : players) {
            if (pl.equals(sender)) continue;
            strPlayers.add(pl.getName());
        }

        strPlayers.add("%");

        List<String> options = new ArrayList<>();

        options.add("add");
        options.add("remove");
        options.add("list");

        if (args.length == 1) {
            return TextUtils.getCompletion(options, args[0]);
        } else if (args.length == 2) {
            if (args[0].equals("remove")) {
                return TextUtils.getCompletion(ignored, args[1]);
            } else {
                return TextUtils.getCompletion(strPlayers, args[1]);
            }
        }

        return new ArrayList<>();
    }
}
