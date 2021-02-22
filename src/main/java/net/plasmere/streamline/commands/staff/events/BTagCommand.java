package net.plasmere.streamline.commands.staff.events;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BTagCommand extends Command implements TabExecutor {
    public BTagCommand(String perm, String[] aliases){
        super("btag", perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length <= 0) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
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
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                    return;
                }
            }

            switch (args[1]){
                case "remove":
                case "rem":
                case "r":
                case "-":
                    if (args.length <= 2) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                        return;
                    }
                    PlayerUtils.remTag(sender, stat, args[2]);
                    break;
                case "add":
                case "a":
                case "+":
                    if (args.length <= 2) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                        return;
                    }
                    PlayerUtils.addTag(sender, stat, args[2]);
                    break;
                case "list":
                case "l":
                case "?":
                default:
                    PlayerUtils.listTags(sender, stat);
                    break;
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (! sender.hasPermission(ConfigUtils.comBBTagPerm)) return new ArrayList<>();

        Collection<ProxiedPlayer> players = StreamLine.getInstance().getProxy().getPlayers();
        List<String> strPlayers = new ArrayList<>();
        List<String> secondTab = new ArrayList<>();

        secondTab.add("add");
        secondTab.add("remove");
        secondTab.add("list");

        if (args.length <= 0) {
            for (ProxiedPlayer player : players) {
                strPlayers.add(player.getName());
            }

            return strPlayers;
        } else {
            return secondTab;
        }
    }
}
