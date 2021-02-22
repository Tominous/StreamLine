package net.plasmere.streamline.commands.staff.events;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PlayerUtils;

public class BTagCommand extends Command {
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
}
