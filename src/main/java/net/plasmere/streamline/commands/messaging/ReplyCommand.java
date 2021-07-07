package net.plasmere.streamline.commands.messaging;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PlayerUtils;
import net.plasmere.streamline.utils.TextUtils;

public class ReplyCommand extends Command {
    public ReplyCommand(String base, String perm, String[] aliases){
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

            if (args.length <= 0) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
            } else {
                if (player.hasPermission(ConfigUtils.comBReplyPerm)){
                    Player stat = PlayerUtils.getStat(sender);

                    if (stat == null) {
                        PlayerUtils.addStat(new Player(((ProxiedPlayer) sender).getUniqueId()));
                        stat = PlayerUtils.getStat(sender);
                        if (stat == null) {
                            StreamLine.getInstance().getLogger().severe("CANNOT INSTANTIATE THE PLAYER: " + args[0]);
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                            return;
                        }
                    }

                    Player statTo = PlayerUtils.getPlayerByUUID(stat.lastToUUID);

                    if (statTo == null) {
                        PlayerUtils.addStat(new Player(stat.lastToUUID));
                        statTo = PlayerUtils.getPlayerByUUID(stat.lastToUUID);
                        if (statTo == null) {
                            StreamLine.getInstance().getLogger().severe("CANNOT INSTANTIATE THE PLAYER: " + args[0]);
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                            return;
                        }
                    }

                    PlayerUtils.doMessageWithIgnoreCheck(stat, statTo, TextUtils.normalize(args), true);
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
}
