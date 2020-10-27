package net.plasmere.streamline.discordbot.commands;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.utils.PermissionHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandsCommand {
    private static final EmbedBuilder eb = new EmbedBuilder();

    public static void sendMessage(String command, MessageReceivedEvent event, StreamLine plugin){
        event.getChannel().sendMessage(eb.setDescription(compileCommands(event)).build()).queue();
        plugin.getLogger().info("Sent message for \"" + command + "\"!");
    }

    private static String compileCommands(MessageReceivedEvent event){
        String prefix = ConfigUtils.botPrefix;

        StringBuilder commands = new StringBuilder();
        commands.append("-- Commands --");
        if (ConfigUtils.comDCommands && PermissionHelper.checkRoleIDPerms(event, ConfigUtils.comDCommandsPerm))
            commands.append("\n").append(prefix).append("commands : Shows you this list.");
        if (ConfigUtils.comDOnline && PermissionHelper.checkRoleIDPerms(event, ConfigUtils.comDOnlinePerm))
            commands.append("\n").append(prefix).append("online : Shows you who is all online...");
        if (ConfigUtils.comDReport && PermissionHelper.checkRoleIDPerms(event, ConfigUtils.comDReportPerm))
            commands.append("\n").append(prefix).append("report <explanation> : Reports a player from the Discord.");
        if (ConfigUtils.comDStaffChat && PermissionHelper.checkRoleIDPerms(event, ConfigUtils.comDStaffChatPerm))
            commands.append("\n").append(prefix).append("staffchat <message> : Puts a message in chat to all staff online.");
        if (ConfigUtils.comDStaffOnline && PermissionHelper.checkRoleIDPerms(event, ConfigUtils.comDStaffOnlinePerm))
            commands.append("\n").append(prefix).append("staffonline : shows the amount of staff and the actual staff online.");

        return commands.toString();
    }
}
