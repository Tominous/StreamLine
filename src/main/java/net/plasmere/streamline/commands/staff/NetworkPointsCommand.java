package net.plasmere.streamline.commands.staff;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.CommandsConfUtils;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.savable.users.SavableUser;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PlayerUtils;
import net.plasmere.streamline.utils.TextUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NetworkPointsCommand extends Command implements TabExecutor {
    public NetworkPointsCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length <= 0) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
        } else {
            if (! PlayerUtils.exists(args[0])) {
                MessagingUtils.sendBUserMessage(sender, PlayerUtils.noStatsFound);
                return;
            }

            SavableUser stat = PlayerUtils.getOrGetSavableUser(args[0]);

            if (stat == null) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer());
                return;
            }

            if (! stat.latestName.equals(sender.getName())) {
                if (! sender.hasPermission(CommandsConfUtils.comBPointsOPerm)) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPerm());
                    return;
                }
            }

            if (args.length <= 1) {
                if (! stat.latestName.equals(sender.getName())) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.pointsViewO()
                            .replace("%points%", String.valueOf(stat.points))
                            .replace("%other%", stat.latestName)
                    );
                    return;
                } else {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.pointsViewS()
                            .replace("%points%", String.valueOf(stat.points))
                    );
                    return;
                }
            }

            if (! sender.hasPermission(CommandsConfUtils.comBPointsChPerm)) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPerm());
                return;
            }

            switch (args[1]){
                case "remove":
                case "rem":
                case "r":
                case "-":
                    if (args.length <= 2) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
                        return;
                    }

                    try {
                        stat.remPoints(Integer.parseInt(args[2]));

                        if (! stat.latestName.equals(sender.getName())) {
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.pointsRemoveO()
                                    .replace("%points%", args[2])
                                    .replace("%other%", stat.latestName)
                            );
                        } else {
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.pointsRemoveS()
                                    .replace("%points%", args[2])
                            );
                        }
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorInt());
                        if (ConfigUtils.errSendToConsole) e.printStackTrace();
                    }
                    break;
                case "add":
                case "a":
                case "+":
                    if (args.length <= 2) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
                        return;
                    }

                    try {
                        stat.addPoints(Integer.parseInt(args[2]));

                        if (! stat.latestName.equals(sender.getName())) {
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.pointsAddO()
                                    .replace("%points%", args[2])
                                    .replace("%other%", stat.latestName)
                            );
                        } else {
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.pointsAddS()
                                    .replace("%points%", args[2])
                            );
                        }
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorInt());
                        if (ConfigUtils.errSendToConsole) e.printStackTrace();
                    }
                    break;
                case "set":
                case "s":
                case "=":
                default:
                    if (args.length <= 2) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
                        return;
                    }

                    try {
                        stat.setPoints(Integer.parseInt(args[2]));

                        if (! stat.latestName.equals(sender.getName())) {
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.pointsSetO()
                                    .replace("%points%", args[2])
                                    .replace("%other%", stat.latestName)
                            );
                        } else {
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.pointsSetS()
                                    .replace("%points%", args[2])
                            );
                        }
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorInt());
                        if (ConfigUtils.errSendToConsole) e.printStackTrace();
                    }
                    break;
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (! sender.hasPermission(CommandsConfUtils.comBBTagPerm)) return new ArrayList<>();

        Collection<ProxiedPlayer> players = StreamLine.getInstance().getProxy().getPlayers();
        List<String> strPlayers = new ArrayList<>();
        List<String> secondTab = new ArrayList<>();

        secondTab.add("add");
        secondTab.add("remove");
        secondTab.add("set");

        if (args.length == 1) {
            for (ProxiedPlayer player : players) {
                strPlayers.add(player.getName());
            }

            return TextUtils.getCompletion(strPlayers, args[0]);
        } else if (args.length == 2) {
            return TextUtils.getCompletion(secondTab, args[1]);
        } else {
            return new ArrayList<>();
        }
    }
}
