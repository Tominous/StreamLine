package net.plasmere.streamline.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.CommandsConfUtils;
import net.plasmere.streamline.objects.savable.users.SavableUser;
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
            if (args.length <= 0 || ! CommandsConfUtils.comBStatsOthers) {
                PlayerUtils.info(sender, PlayerUtils.getOrGetPlayerStat(sender.getName()));
            } else {
                SavableUser person = PlayerUtils.getOrGetSavableUser(args[0]);

                if (person == null) {
                    MessagingUtils.sendBUserMessage(sender, PlayerUtils.noStatsFound);
                    return;
                }

                PlayerUtils.info(sender, person);
            }
        } else {
            if (args.length <= 0) {
                PlayerUtils.info(sender, PlayerUtils.getConsoleStat());
            } else {
                SavableUser person = PlayerUtils.getOrGetSavableUser(args[0]);

                if (person == null) {
                    MessagingUtils.sendBUserMessage(sender, PlayerUtils.noStatsFound);
                    return;
                }

                PlayerUtils.info(sender, person);
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

        strPlayers.add("%");

        if (sender.hasPermission(CommandsConfUtils.comBStatsPermOthers)) {
            return TextUtils.getCompletion(strPlayers, args[0]);
        }

        return new ArrayList<>();
    }
}

//                if (args[0].equals("%")) {
//                    ConsolePlayer person = PlayerUtils.getConsoleStat();
//
//                    if (person == null) {
//                        MessagingUtils.sendBUserMessage(sender, PlayerUtils.noStatsFound);
//                        return;
//                    }
//
//                    PlayerUtils.info(sender, person);
//                } else {
//                    Player person = PlayerUtils.getOrGetPlayerStat(args[0]);
//
//                    if (person == null) {
//                        MessagingUtils.sendBUserMessage(sender, PlayerUtils.noStatsFound);
//                        return;
//                    }
//
//                    PlayerUtils.info(sender, person);
//                }