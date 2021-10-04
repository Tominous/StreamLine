package net.plasmere.streamline.discordbot;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.CommandsConfUtils;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.DiscordBotConfUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.discordbot.commands.*;
import net.plasmere.streamline.utils.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Objects;

public class MessageListener extends ListenerAdapter {
    private static EmbedBuilder eb = new EmbedBuilder();

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        String em = event.getMessage().getContentRaw();
        String prefix = DiscordBotConfUtils.botPrefix;

        if (event.getAuthor().isBot()) return;

        if (ConfigUtils.moduleStaffChatToMinecraft && event.getChannel().equals(event.getJDA().getTextChannelById(DiscordBotConfUtils.textChannelStaffChat))) {
            if (ConfigUtils.moduleSCOnlyStaffRole){
                try {
                    if (Objects.requireNonNull(event.getMessage().getMember()).getRoles().contains(event.getJDA().getRoleById(DiscordBotConfUtils.roleStaff))) {
                        MessagingUtils.sendStaffMessageFromDiscord(event.getAuthor().getName(), MessageConfUtils.discordStaffChatFrom(), em);
                    } else
                        return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                MessagingUtils.sendStaffMessageFromDiscord(event.getAuthor().getName(), MessageConfUtils.discordStaffChatFrom(), em);
            }

            if (ConfigUtils.debug) MessagingUtils.logInfo("Someone talked in staffchat (discord)... sending to bungee...");
        }

        if (ConfigUtils.moduleDPC && ! em.startsWith(prefix)) {
            if (StreamLine.discordData.isChannel(event.getChannel().getIdLong())) {
                Member member = event.getMessage().getMember();

                if (member == null) {
                    eb = new EmbedBuilder();
                    event.getChannel().sendMessageEmbeds(eb.setTitle("ERROR").setDescription("Error sending Discord message to Minecraft!").build());
                }

                StreamLine.discordData.sendBungeeChannel(member.getIdLong(), event.getChannel().getIdLong(), em);
            }
        }

        if (! event.getMessage().getContentRaw().toLowerCase().startsWith(prefix)) return;

        String[] args = event.getMessage().getContentRaw().toLowerCase().substring(DiscordBotConfUtils.botPrefix.length()).split(" ");

        if (ConfigUtils.debug) MessagingUtils.logInfo("[ " + event.getAuthor().getName() + " ] " + event.getMessage().getContentRaw());

        // Commands.
        if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comDCommandsAliases)) {
            if (CommandsConfUtils.comDCommands && PermissionHelper.checkRoleIDPerms(event, CommandsConfUtils.comDCommandsPerm)) {
                if (ConfigUtils.debug) MessagingUtils.logInfo("So... Switching on case \"commands\"...");
                try {
                    CommandsCommand.sendMessage(args[0], event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ConfigUtils.moduleSayCommandDisabled.equals("yes")) {
                event.getChannel().sendMessageEmbeds(eb.setTitle("Error!").setDescription(MessageConfUtils.discordCommandDisabled().replace("%newline%", "\n")).build()).queue();
            } else if (ConfigUtils.moduleSayCommandDisabled.equals("staff")) {
                if (Objects.requireNonNull(event.getMessage().getMember()).getRoles().contains(event.getJDA().getRoleById(DiscordBotConfUtils.roleStaff)))
                    event.getChannel().sendMessageEmbeds(eb.setTitle("Error!").setDescription(MessageConfUtils.discordCommandDisabled().replace("%newline%", "\n")).build()).queue();
            } else if (ConfigUtils.moduleSayCommandDisabled.equals("owner")) {
                if (Objects.requireNonNull(event.getMessage().getMember()).isOwner())
                    event.getChannel().sendMessageEmbeds(eb.setTitle("Error!").setDescription(MessageConfUtils.discordCommandDisabled().replace("%newline%", "\n")).build()).queue();
            }
        // Online.
        } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comDOnlineAliases)) {
            if (CommandsConfUtils.comDOnline && PermissionHelper.checkRoleIDPerms(event, CommandsConfUtils.comDOnlinePerm)) {
                if (ConfigUtils.debug) MessagingUtils.logInfo("So... Switching on case \"online\"...");
                try {
                    OnlineCommand.sendMessage(args[0], event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ConfigUtils.moduleSayCommandDisabled.equals("yes")) {
                event.getChannel().sendMessageEmbeds(eb.setTitle("Error!").setDescription(MessageConfUtils.discordCommandDisabled().replace("%newline%", "\n")).build()).queue();
            } else if (ConfigUtils.moduleSayCommandDisabled.equals("staff")) {
                if (Objects.requireNonNull(event.getMessage().getMember()).getRoles().contains(event.getJDA().getRoleById(DiscordBotConfUtils.roleStaff)))
                    event.getChannel().sendMessageEmbeds(eb.setTitle("Error!").setDescription(MessageConfUtils.discordCommandDisabled().replace("%newline%", "\n")).build()).queue();
            } else if (ConfigUtils.moduleSayCommandDisabled.equals("owner")) {
                if (Objects.requireNonNull(event.getMessage().getMember()).isOwner())
                    event.getChannel().sendMessageEmbeds(eb.setTitle("Error!").setDescription(MessageConfUtils.discordCommandDisabled().replace("%newline%", "\n")).build()).queue();
            }
        // Report.
        } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comDReportAliases)) {
            if (CommandsConfUtils.comDReport && PermissionHelper.checkRoleIDPerms(event, CommandsConfUtils.comDReportPerm)) {
                if (ConfigUtils.debug) MessagingUtils.logInfo("So... Switching on case \"report\"...");
                try {
                    ReportCommand.sendMessage(args[0], event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ConfigUtils.moduleSayCommandDisabled.equals("yes")) {
                event.getChannel().sendMessageEmbeds(eb.setTitle("Error!").setDescription(MessageConfUtils.discordCommandDisabled().replace("%newline%", "\n")).build()).queue();
            } else if (ConfigUtils.moduleSayCommandDisabled.equals("staff")) {
                if (Objects.requireNonNull(event.getMessage().getMember()).getRoles().contains(event.getJDA().getRoleById(DiscordBotConfUtils.roleStaff)))
                    event.getChannel().sendMessageEmbeds(eb.setTitle("Error!").setDescription(MessageConfUtils.discordCommandDisabled().replace("%newline%", "\n")).build()).queue();
            } else if (ConfigUtils.moduleSayCommandDisabled.equals("owner")) {
                if (Objects.requireNonNull(event.getMessage().getMember()).isOwner())
                    event.getChannel().sendMessageEmbeds(eb.setTitle("Error!").setDescription(MessageConfUtils.discordCommandDisabled().replace("%newline%", "\n")).build()).queue();
            }
        // StaffChat.
        } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comDStaffChatAliases)) {
            if (CommandsConfUtils.comDStaffChat && PermissionHelper.checkRoleIDPerms(event, CommandsConfUtils.comDStaffChatPerm)) {
                if (ConfigUtils.debug) MessagingUtils.logInfo("So... Switching on case \"staffchat\"...");
                try {
                    StaffChatCommand.sendMessage(args[0], event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ConfigUtils.moduleSayCommandDisabled.equals("yes")) {
                event.getChannel().sendMessageEmbeds(eb.setTitle("Error!").setDescription(MessageConfUtils.discordCommandDisabled().replace("%newline%", "\n")).build()).queue();
            } else if (ConfigUtils.moduleSayCommandDisabled.equals("staff")) {
                if (Objects.requireNonNull(event.getMessage().getMember()).getRoles().contains(event.getJDA().getRoleById(DiscordBotConfUtils.roleStaff)))
                    event.getChannel().sendMessageEmbeds(eb.setTitle("Error!").setDescription(MessageConfUtils.discordCommandDisabled().replace("%newline%", "\n")).build()).queue();
            } else if (ConfigUtils.moduleSayCommandDisabled.equals("owner")) {
                if (Objects.requireNonNull(event.getMessage().getMember()).isOwner())
                    event.getChannel().sendMessageEmbeds(eb.setTitle("Error!").setDescription(MessageConfUtils.discordCommandDisabled().replace("%newline%", "\n")).build()).queue();
            }
        // StaffOnline.
        } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comDStaffOnlineAliases)) {
            if (CommandsConfUtils.comDStaffOnline && PermissionHelper.checkRoleIDPerms(event, CommandsConfUtils.comDStaffOnlinePerm)) {
                if (ConfigUtils.debug) MessagingUtils.logInfo("So... Switching on case \"staffonline\"...");
                try {
                    StaffOnlineCommand.sendMessage(args[0], event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ConfigUtils.moduleSayCommandDisabled.equals("yes")) {
                event.getChannel().sendMessageEmbeds(eb.setTitle("Error!").setDescription(MessageConfUtils.discordCommandDisabled().replace("%newline%", "\n")).build()).queue();
            } else if (ConfigUtils.moduleSayCommandDisabled.equals("staff")) {
                if (Objects.requireNonNull(event.getMessage().getMember()).getRoles().contains(event.getJDA().getRoleById(DiscordBotConfUtils.roleStaff)))
                    event.getChannel().sendMessageEmbeds(eb.setTitle("Error!").setDescription(MessageConfUtils.discordCommandDisabled().replace("%newline%", "\n")).build()).queue();
            } else if (ConfigUtils.moduleSayCommandDisabled.equals("owner")) {
                if (Objects.requireNonNull(event.getMessage().getMember()).isOwner())
                    event.getChannel().sendMessageEmbeds(eb.setTitle("Error!").setDescription(MessageConfUtils.discordCommandDisabled().replace("%newline%", "\n")).build()).queue();
            }
            // IF NOT.
        } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comDChannelAliases)) {
            if (CommandsConfUtils.comDChannel && PermissionHelper.checkRoleIDPerms(event, CommandsConfUtils.comDChannelPerm)) {
                if (ConfigUtils.debug) MessagingUtils.logInfo("So... Switching on case \"channel\"...");
                try {
                    ChannelCommand.sendMessage(args[0], event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ConfigUtils.moduleSayCommandDisabled.equals("yes")) {
                event.getChannel().sendMessageEmbeds(eb.setTitle("Error!").setDescription(MessageConfUtils.discordCommandDisabled().replace("%newline%", "\n")).build()).queue();
            } else if (ConfigUtils.moduleSayCommandDisabled.equals("staff")) {
                if (Objects.requireNonNull(event.getMessage().getMember()).getRoles().contains(event.getJDA().getRoleById(DiscordBotConfUtils.roleStaff)))
                    event.getChannel().sendMessageEmbeds(eb.setTitle("Error!").setDescription(MessageConfUtils.discordCommandDisabled().replace("%newline%", "\n")).build()).queue();
            } else if (ConfigUtils.moduleSayCommandDisabled.equals("owner")) {
                if (Objects.requireNonNull(event.getMessage().getMember()).isOwner())
                    event.getChannel().sendMessageEmbeds(eb.setTitle("Error!").setDescription(MessageConfUtils.discordCommandDisabled().replace("%newline%", "\n")).build()).queue();
            }
            // IF NOT.
        } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comDVerifyAliases)) {
            if (CommandsConfUtils.comDVerify && PermissionHelper.checkRoleIDPerms(event, CommandsConfUtils.comDVerifyPerm)) {
                if (ConfigUtils.debug) MessagingUtils.logInfo("So... Switching on case \"verify\"...");
                try {
                    VerifyCommand.sendMessage(args[0], event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ConfigUtils.moduleSayCommandDisabled.equals("yes")) {
                event.getChannel().sendMessageEmbeds(eb.setTitle("Error!").setDescription(MessageConfUtils.discordCommandDisabled().replace("%newline%", "\n")).build()).queue();
            } else if (ConfigUtils.moduleSayCommandDisabled.equals("staff")) {
                if (Objects.requireNonNull(event.getMessage().getMember()).getRoles().contains(event.getJDA().getRoleById(DiscordBotConfUtils.roleStaff)))
                    event.getChannel().sendMessageEmbeds(eb.setTitle("Error!").setDescription(MessageConfUtils.discordCommandDisabled().replace("%newline%", "\n")).build()).queue();
            } else if (ConfigUtils.moduleSayCommandDisabled.equals("owner")) {
                if (Objects.requireNonNull(event.getMessage().getMember()).isOwner())
                    event.getChannel().sendMessageEmbeds(eb.setTitle("Error!").setDescription(MessageConfUtils.discordCommandDisabled().replace("%newline%", "\n")).build()).queue();
            }
            // IF NOT.
        } else {
            if (ConfigUtils.debug) MessagingUtils.logInfo("So... Switching on case default...");
            switch (ConfigUtils.moduleSayNotACommand) {
                case "yes":
                    event.getChannel().sendMessageEmbeds(eb.setDescription(MessageConfUtils.discordNotACommand().replace("%newline%", "\n")).build()).queue();
                    break;
                case "staff":
                    if (Objects.requireNonNull(event.getMessage().getMember()).getRoles().contains(event.getJDA().getRoleById(DiscordBotConfUtils.roleStaff)))
                        event.getChannel().sendMessageEmbeds(eb.setDescription(MessageConfUtils.discordNotACommand().replace("%newline%", "\n")).build()).queue();
                    break;
                case "owner":
                    if (Objects.requireNonNull(event.getMessage().getMember()).isOwner())
                        event.getChannel().sendMessageEmbeds(eb.setDescription(MessageConfUtils.discordNotACommand().replace("%newline%", "\n")).build()).queue();
                    break;
            }
        }
    }
}
