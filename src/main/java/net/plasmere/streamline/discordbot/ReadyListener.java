package net.plasmere.streamline.discordbot;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.messaging.DiscordMessage;
import net.plasmere.streamline.utils.MessagingUtils;

import net.dv8tion.jda.api.JDA;

import javax.annotation.Nonnull;

public class ReadyListener implements EventListener {
    private static final EmbedBuilder eb = new EmbedBuilder();

    @Override
    public void onEvent(@Nonnull GenericEvent event) {
        if (ConfigUtils.moduleStartups) {
            if (event instanceof ReadyEvent) {
                try {
                    JDA jda = event.getJDA();

                    MessagingUtils.sendDiscordEBMessage(jda, new DiscordMessage(StreamLine.getInstance().getProxy().getConsole(), MessageConfUtils.startTitle, MessageConfUtils.startMessage, ConfigUtils.textChannelOfflineOnline));

//                    Objects.requireNonNull(event.getJDA().getTextChannelById(ConfigUtils.textChannelOfflineOnline)).sendMessageEmbeds(eb.setDescription("Bot online!").build()).queue();
                } catch (NullPointerException n) {
                    n.printStackTrace();
                } catch (Exception e) {
                    MessagingUtils.logWarning("An unknown error occurred with sending online message...");
                    e.printStackTrace();
                }
            }
        }
    }
}
