package net.plasmere.streamline.discordbot.commands;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.CommandsConfUtils;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.DiscordBotConfUtils;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PermissionHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandsCommand {
    public static void sendMessage(String command, MessageReceivedEvent event){
        EmbedBuilder eb = new EmbedBuilder();
        event.getChannel().sendMessage(eb.setDescription(compileCommands(event)).build()).queue();
        if (ConfigUtils.debug) MessagingUtils.logInfo("Sent message for \"" + command + "\"!");
    }

    private static String compileCommands(MessageReceivedEvent event){
        String prefix = DiscordBotConfUtils.botPrefix;

        StringBuilder commands = new StringBuilder();
        commands.append("-- Commands --");
        if (CommandsConfUtils.comDCommands && PermissionHelper.checkRoleIDPerms(event, CommandsConfUtils.comDCommandsPerm))
            commands.append("\n").append(prefix).append("commands : Shows you this list.");
        if (CommandsConfUtils.comDOnline && PermissionHelper.checkRoleIDPerms(event, CommandsConfUtils.comDOnlinePerm))
            commands.append("\n").append(prefix).append("online : Shows you who is all online...");
        if (CommandsConfUtils.comDReport && PermissionHelper.checkRoleIDPerms(event, CommandsConfUtils.comDReportPerm))
            commands.append("\n").append(prefix).append("report <explanation> : Reports a player from the Discord.");
        if (CommandsConfUtils.comDStaffChat && PermissionHelper.checkRoleIDPerms(event, CommandsConfUtils.comDStaffChatPerm))
            commands.append("\n").append(prefix).append("staffchat <message> : Puts a message in chat to all staff online.");
        if (CommandsConfUtils.comDStaffOnline && PermissionHelper.checkRoleIDPerms(event, CommandsConfUtils.comDStaffOnlinePerm))
            commands.append("\n").append(prefix).append("staffonline : shows the amount of staff and the actual staff online.");

        return commands.toString();
    }
}
