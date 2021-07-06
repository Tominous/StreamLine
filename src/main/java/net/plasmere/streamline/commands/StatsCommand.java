package net.plasmere.streamline.commands;

import net.md_5.bungee.api.CommandSender;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StatsCommand extends Command implements TabExecutor {
    public StatsCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            Player player = PlayerUtils.getStat(sender);

            if (player == null) {
                PlayerUtils.addStat(new Player((ProxiedPlayer) sender));
                player = PlayerUtils.getStat(sender);
                if (player == null) {
                    StreamLine.getInstance().getLogger().severe("CANNOT INSTANTIATE THE PLAYER: " + sender.getName());
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    return;
                }
            }

            if (args.length <= 0 || ! ConfigUtils.comBStatsOthers) {
                PlayerUtils.info(player, player);
            } else {
                if (player.hasPermission(ConfigUtils.comBStatsPermOthers)){
                    if (! PlayerUtils.exists(args[0])) {
                        MessagingUtils.sendBUserMessage(sender, PlayerUtils.noStatsFound);
                        return;
                    }

                    Player stat = PlayerUtils.getStat(args[0]);

                    if (stat == null) {
                        PlayerUtils.addStat(new Player(args[0]));
                        stat = PlayerUtils.getStat(args[0]);
                        if (stat == null) {
                            StreamLine.getInstance().getLogger().severe("CANNOT INSTANTIATE THE PLAYER: " + args[0]);
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                            return;
                        }
                    }

                    PlayerUtils.info(player, stat);
                } else {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPerm);
                }
            }
        } else {
            if (args.length <= 0) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.onlyPlayers);
            } else {
                if (! PlayerUtils.exists(args[0])) {
                    MessagingUtils.sendBUserMessage(sender, PlayerUtils.noStatsFound);
                    return;
                }

                Player stat = PlayerUtils.getStat(args[0]);

                if (stat == null) {
                    PlayerUtils.addStat(new Player(args[0]));
                    stat = PlayerUtils.getStat(args[0]);
                    if (stat == null) {
                        StreamLine.getInstance().getLogger().severe("CANNOT INSTANTIATE THE PLAYER: " + args[0]);
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                        return;
                    }
                }

                PlayerUtils.info(sender, stat);
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        Collection<ProxiedPlayer> players = StreamLine.getInstance().getProxy().getPlayers();
        List<String> strPlayers = new ArrayList<>();

        for (ProxiedPlayer player : players){
            strPlayers.add(player.getName());
        }

        if (sender.hasPermission(ConfigUtils.comBStatsPermOthers)) {
            return TextUtils.getCompletion(strPlayers, args[0]);
        }

        else return new ArrayList<>();
    }
}
