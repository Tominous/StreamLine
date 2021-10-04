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
        event.getChannel().sendMessageEmbeds(eb.setDescription(compileCommands(event)).build()).queue();
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
        if (CommandsConfUtils.comDChannel && PermissionHelper.checkRoleIDPerms(event, CommandsConfUtils.comDChannelPerm)) {
            commands.append("\n").append(prefix).append("channel <set | remove> <global | local | guild | party> <identifier> : links a channel to the StreamLine plugin on your proxy server.");
            commands.append("\n   - <identifier> needs to be replaced with the following:");
            commands.append("\n      - If global: either \"\" or \"some.permission\".");
            commands.append("\n      - If local: the name of the server you want to be local chat for.");
            commands.append("\n      - If guild: the UUID of the guild owner.");
            commands.append("\n      - If party: the UUID of the party owner.");
        }

        return commands.toString();
    }
}
