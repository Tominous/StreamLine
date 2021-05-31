package net.plasmere.streamline.commands;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.TextUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ReportCommand extends Command {

    public ReportCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String msg = TextUtils.normalize(args);

        if (msg.length() <= 0){
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.discordNeedsMore);
            return;
        }

        if (ConfigUtils.moduleReportsMToDiscord)
            MessagingUtils.sendDiscordReportMessage(sender.getName(), true, msg);
        if (ConfigUtils.moduleReportsSendChat)
            MessagingUtils.sendStaffMessageReport(sender.getName(), true, msg);
        if (ConfigUtils.moduleReportsBConfirmation)
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bConfirmReportMessage
                    .replace("%reporter%", sender.getName())
                    .replace("%report%", TextUtils.normalize(args))
            );
    }
}
