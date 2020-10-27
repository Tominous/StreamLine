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
    private final StreamLine plugin;
    private String perm = "";
    private JDA jda = StreamLine.getJda();

    public ReloadCommand(StreamLine streamLine, String perm, String[] aliases){
        super("slreload", perm, aliases);

        this.perm = perm;
        this.plugin = streamLine;
    }

    @Override
    public void execute(CommandSender sender, String[] strings) {
        if (sender.hasPermission(perm)) {
            try {
                Config.reloadConfig();
                Config.reloadMessages();
            } catch (Exception e) {
                plugin.getLogger().warning("Something went wrong with reloading configs... -->\n" + e.getMessage());
            }

            try {
                Plugin plug = plugin.getProxy().getPluginManager().getPlugin("StreamLine");
                plugin.getProxy().getPluginManager().unregisterCommands(plug);
                plugin.getProxy().getPluginManager().unregisterListeners(plug);
            } catch (Exception e){
                plugin.getLogger().warning("Something went wrong with unregistering the plugin... -->\n" + e.getMessage());
            }

            PluginUtils.loadCommands(plugin);
            PluginUtils.loadListeners(plugin);

//            if (StreamLine.getJda() == null || jda.getStatus() == JDA.Status.DISCONNECTED || jda.getStatus() == JDA.Status.FAILED_TO_LOGIN || jda.getStatus() == JDA.Status.SHUTDOWN){
//                init();
//            }

            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.prefix + MessageConfUtils.reload);
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.prefix + "&cBeware! Your commands may not have reloaded...");
        } else {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.prefix + MessageConfUtils.noPerm);
        }
    }

    private void init(){
        if (jda != null) try { jda.shutdownNow(); jda = null; } catch (Exception e) { e.printStackTrace();}

        try {
            JDABuilder jdaBuilder = JDABuilder.createDefault(ConfigUtils.botToken)
                    .setActivity(Activity.listening(ConfigUtils.botStatusMessage));
            jdaBuilder.addEventListeners(
                    new MessageListener(plugin),
                    new ReadyListener(plugin));
            jda = jdaBuilder.build().awaitReady();
        } catch (Exception e) {
            plugin.getLogger().warning("An unknown error occurred building JDA...");
            return;
        }

        if (jda.getStatus() == JDA.Status.CONNECTED) {
            StreamLine.setReady(true);

            plugin.getLogger().info("JDA status is connected...");
        }
    }
}
