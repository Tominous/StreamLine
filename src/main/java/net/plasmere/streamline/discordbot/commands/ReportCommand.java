package net.plasmere.streamline.discordbot.commands;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.utils.MessagingUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ReportCommand {
    public static void sendMessage(String command, MessageReceivedEvent event){
        String om = event.getMessage().getContentDisplay();
        String prefix = ConfigUtils.botPrefix;

        String msg = om.substring((prefix + command + " ").length());

        if (msg.length() <= 0){
            MessagingUtils.sendDSelfMessage(event, MessageConfUtils.discordErrTitle, MessageConfUtils.discordNeedsMore);
            return;
        }

        if (ConfigUtils.moduleReportsDConfirmation)
            MessagingUtils.sendDSelfMessage(event, MessageConfUtils.reportEmbedTitle, MessageConfUtils.dConfirmReportMessage
                    .replace("%report%", msg)
            );

        if (ConfigUtils.moduleReportsDToMinecraft)
            MessagingUtils.sendStaffMessageReport(event.getAuthor().getName(), false, msg);

        if (ConfigUtils.moduleReportToChannel) {
            if (ConfigUtils.moduleReportChannelPingsRole)
                MessagingUtils.sendDiscordPingRoleMessage(ConfigUtils.textChannelReports, ConfigUtils.roleReports);
            MessagingUtils.sendDiscordReportMessage(event.getAuthor().getName(), false, msg);
        }

        if (ConfigUtils.debug) MessagingUtils.logInfo("Sent message for \"" + command + "\"!");
    }
}
