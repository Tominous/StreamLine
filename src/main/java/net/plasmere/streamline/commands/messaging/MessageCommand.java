package net.plasmere.streamline.commands.messaging;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.users.Player;
import net.plasmere.streamline.objects.users.SavableUser;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PlayerUtils;
import net.plasmere.streamline.utils.TextUtils;
import net.plasmere.streamline.utils.UUIDFetcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MessageCommand extends Command implements TabExecutor {
    public MessageCommand(String base, String perm, String[] aliases) {
        super(base, perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        SavableUser stat = PlayerUtils.getStat(sender);

        if (stat == null) {
            stat = PlayerUtils.getOrCreateStat(sender);
            if (stat == null) {
                StreamLine.getInstance().getLogger().severe("CANNOT INSTANTIATE THE PLAYER: " + sender.getName());
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                return;
            }
        }

        if (args.length <= 0) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
        } else {
            if (stat.hasPermission(ConfigUtils.comBMessagePerm)) {
                SavableUser statTo;

                if (args[0].equals("%")) {
                    statTo = PlayerUtils.getStatByUUID("%");
                } else {
                    if (! PlayerUtils.exists(args[0])) {
                        MessagingUtils.sendBUserMessage(sender, PlayerUtils.noStatsFound);
                        return;
                    }

                    statTo = PlayerUtils.getStat(args[0]);
                }

                if (statTo == null) {
                    PlayerUtils.addStat(stat.lastToUUID);
                    statTo = PlayerUtils.getStatByUUID(stat.lastToUUID);
                    if (statTo == null) {
                        StreamLine.getInstance().getLogger().severe("CANNOT INSTANTIATE THE PLAYER: " + args[0]);
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                        return;
                    }
                }

                PlayerUtils.doMessageWithIgnoreCheck(stat, statTo, TextUtils.argsToStringMinus(args, 0), false);
            } else {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPerm);
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        Collection<ProxiedPlayer> players = StreamLine.getInstance().getProxy().getPlayers();
        List<String> strPlayers = new ArrayList<>();
        List<String> ignored = new ArrayList<>();


        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            Player player = PlayerUtils.getOrCreateByUUID(p.getUniqueId().toString());
            for (String uuid : player.ignoredList) {
                ignored.add(UUIDFetcher.getName(uuid));
            }
        }

        for (ProxiedPlayer pl : players) {
            if (sender instanceof ProxiedPlayer) {
                if (pl.equals(sender)) continue;
                if (ignored.contains(pl.getName())) continue;
            }
            strPlayers.add(pl.getName());
        }

        strPlayers.add("%");

        if (args.length == 1) {
            return TextUtils.getCompletion(strPlayers, args[0]);
        }

        return new ArrayList<>();
    }
}
