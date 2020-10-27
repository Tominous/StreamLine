package net.plasmere.streamline.discordbot;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.Config;
import net.plasmere.streamline.utils.ConfigUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.md_5.bungee.config.Configuration;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ReadyListener implements EventListener {
    private final StreamLine plugin;
    private final Configuration config = Config.getConf();

    public ReadyListener(StreamLine streamLine){
        this.plugin = streamLine;
    }

    private static final EmbedBuilder eb = new EmbedBuilder();

    @Override
    public void onEvent(@Nonnull GenericEvent event) {
        if (ConfigUtils.moduleStartups)
            if (event instanceof ReadyEvent)
                try {
                    Objects.requireNonNull(event.getJDA().getTextChannelById(ConfigUtils.textChannelOfflineOnline)).sendMessage(eb.setDescription("Bot online!").build()).queue();
                } catch (NullPointerException n) {
                    n.printStackTrace();
                } catch (Exception e) {
                    plugin.getLogger().warning("An unknown error occurred with sending online message...");
                    e.printStackTrace();
                }
    }
}
