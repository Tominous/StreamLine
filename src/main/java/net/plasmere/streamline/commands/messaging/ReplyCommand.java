package net.plasmere.streamline.commands.messaging;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.users.ConsolePlayer;
import net.plasmere.streamline.objects.users.Player;
import net.plasmere.streamline.objects.users.SavableUser;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PlayerUtils;
import net.plasmere.streamline.utils.TextUtils;

public class ReplyCommand extends Command {
    public ReplyCommand(String base, String perm, String[] aliases){
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
        } else {
            if (stat instanceof ConsolePlayer || stat.hasPermission(ConfigUtils.comBReplyPerm)) {
                SavableUser statTo = PlayerUtils.getOrGetSavableUser(stat.replyToUUID);

                if (statTo == null) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer);
                    return;
                }

                PlayerUtils.doMessageWithIgnoreCheck(stat, statTo, TextUtils.normalize(args), true);
            } else {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPerm);
            }
        }
    }
}
