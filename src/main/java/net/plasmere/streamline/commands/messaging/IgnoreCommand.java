package net.plasmere.streamline.commands.messaging;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PlayerUtils;
import net.plasmere.streamline.utils.TextUtils;
import net.plasmere.streamline.utils.UUIDFetcher;

import java.util.*;
import java.util.stream.Collectors;

public class IgnoreCommand extends Command implements TabExecutor {
    public IgnoreCommand(String base, String perm, String[] aliases){
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
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.ignoreListMain
                    .replace("%player%", PlayerUtils.getOffOnDisplayBungee(stat))
                    .replace("%ignored%", PlayerUtils.getIgnored(stat))
            );
        } else if (args.length > 2) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsLess);
        } else {
            Player other = PlayerUtils.getStat(player);

            if (other == null) {
                PlayerUtils.addStat(new Player(UUIDFetcher.getCachedUUID(args[1])));
                other = PlayerUtils.getStat(args[1]);
                if (other == null) {
                    StreamLine.getInstance().getLogger().severe("CANNOT INSTANTIATE THE PLAYER: " + args[0]);
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    return;
                }
            }

            if (other.uuid == null) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer);
                return;
            }

            if (args[0].equals("add")) {
                stat.tryAddNewIgnored(other.uuid);
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.ignoreAddSelf
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                );
                if (other.online) {
                    MessagingUtils.sendBUserMessage(UUIDFetcher.getPPlayerByUUID(other.uuid), MessageConfUtils.ignoreAddIgnored
                            .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(stat))
                    );
                }
            } else if (args[0].equals("remove")) {
                stat.tryRemIgnored(other.uuid);
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.ignoreRemSelf
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                );
                if (other.online) {
                    MessagingUtils.sendBUserMessage(UUIDFetcher.getPPlayerByUUID(other.uuid), MessageConfUtils.ignoreRemIgnored
                            .replace("%sender%", PlayerUtils.getOffOnDisplayBungee(stat))
                    );
                }
            } else if (args[0].equals("list")) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.ignoreListMain
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                        .replace("%ignored%", PlayerUtils.getIgnored(other))
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
        options.add("remove");
        options.add("list");

        if (args.length == 1) {
            final String param1 = args[0];

            return options.stream()
                    .filter(completion -> completion.startsWith(param1))
                    .collect(Collectors.toList());
        } else if (args.length == 2){
            final String param2 = args[1];

            return strPlayers.stream()
                    .filter(completion -> completion.startsWith(param2))
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }
}
