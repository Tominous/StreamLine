package net.plasmere.streamline.discordbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.CommandsConfUtils;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.DiscordBotConfUtils;
import net.plasmere.streamline.objects.enums.ChatChannel;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PermissionHelper;

public class ChannelCommand {
    public static void sendMessage(String command, MessageReceivedEvent event){
        EmbedBuilder eb = new EmbedBuilder();
        event.getChannel().sendMessageEmbeds(eb.setDescription(compileCommands(event)).build()).queue();
        if (ConfigUtils.debug) MessagingUtils.logInfo("Sent message for \"" + command + "\"!");
    }

    private static String compileCommands(MessageReceivedEvent event){
        // .channel <set | remove> <global | local | guild | party> <identifier>
        String message = event.getMessage().getContentRaw();
        String[] args = event.getMessage().getContentRaw().toLowerCase().substring(DiscordBotConfUtils.botPrefix.length()).split(" ");

        long channelID = event.getChannel().getIdLong();

        if (args.length < 2) {
            return "Improper syntax!\nUsage: channel <set | remove> <global | local | guild | party> <identifier> <bypass: true or false>";
        }

        switch (args[1]) {
            case "set":
                if (args.length < 4) {
                    return "Improper syntax!\nUsage: channel <set | remove> <global | local | guild | party> <identifier> <bypass: true or false>";
                }

                boolean bypass = false;

                try {
                    bypass = Boolean.parseBoolean(args[4]);
                } catch (Exception e) {
//                    e.printStackTrace();
                    return "Must specify if bypass is enabled!\nUsage: channel <set | remove> <global | local | guild | party> <identifier> <bypass: true or false>";
                }

                switch (args[2]) {
                    case "global":
                        StreamLine.discordData.addChannel(channelID, ChatChannel.GLOBAL.toString(), args[3], bypass);
                        return "Successfully added channel ``" + channelID + "`` to your set channels as: < GLOBAL , " + args[3] + "," + bypass + " >!";
                    case "local":
                        StreamLine.discordData.addChannel(channelID, ChatChannel.LOCAL.toString(), args[3], bypass);
                        return "Successfully added channel ``" + channelID + "`` to your set channels as: < LOCAL , " + args[3] + "," + bypass + " >!";
                    case "guild":
                        StreamLine.discordData.addChannel(channelID, ChatChannel.GUILD.toString(), args[3], bypass);
                        return "Successfully added channel ``" + channelID + "`` to your set channels as: < GUILD , " + args[3] + "," + bypass + " >!";
                    case "party":
                        StreamLine.discordData.addChannel(channelID, ChatChannel.PARTY.toString(), args[3], bypass);
                        return "Successfully added channel ``" + channelID + "`` to your set channels as: < PARTY , " + args[3] + "," + bypass + " >!";
                    default:
                        return "Improper syntax!\nUsage: channel <set | remove> <global | local | guild | party> <identifier> <bypass: true or false>";
                }
            case "remove":
                StreamLine.discordData.remChannel(channelID);
                return "Successfully removed channel ``" + channelID + "`` from your set channels!";
            default:
                return "Improper syntax!\nUsage: channel <set | remove> <global | local | guild | party> <identifier> <bypass: true or false>";
        }
    }
}
