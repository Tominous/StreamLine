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
        String thing = "";

        if (PlayerUtils.isInOnlineList(sender.getName())) thing = sender.getName();
        else thing = "%";

        SavableUser stat = PlayerUtils.getOrGetSavableUser(thing);

        if (stat == null) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorNoYou);
            return;
        }

        if (args.length <= 0) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
        } else {
            SavableUser statTo = PlayerUtils.getOrGetSavableUser(stat.replyToUUID);

            if (statTo == null) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer);
                return;
            }

            PlayerUtils.doMessageWithIgnoreCheck(stat, statTo, TextUtils.normalize(args), true);
        }
    }
}
