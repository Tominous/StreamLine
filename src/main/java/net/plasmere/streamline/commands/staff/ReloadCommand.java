package net.plasmere.streamline.commands.staff;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.Config;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.discordbot.MessageListener;
import net.plasmere.streamline.discordbot.ReadyListener;
import net.plasmere.streamline.utils.*;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.dv8tion.jda.api.JDA;

public class ReloadCommand extends Command {
    private String perm = "";
    private JDA jda = StreamLine.getJda();

    public ReloadCommand(String base, String perm, String[] aliases){
        super("slreload", perm, aliases);

        this.perm = perm;
    }

    @Override
    public void execute(CommandSender sender, String[] strings) {
        if (sender.hasPermission(perm)) {
            try {
                StreamLine.config.reloadConfig();
                StreamLine.config.reloadMessages();
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.prefix + MessageConfUtils.reload);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.prefix + MessageConfUtils.noPerm);
        }
    }
}
