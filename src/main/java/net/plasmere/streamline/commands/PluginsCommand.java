package net.plasmere.streamline.commands;

import net.plasmere.streamline.StreamLine;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Collection;

public class PluginsCommand extends Command {
    private final StreamLine plugin;

    public PluginsCommand(StreamLine streamLine, String perm, String[] aliases){
        super("plugins", perm, aliases);

        this.plugin = streamLine;
    }

    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (args.length > 0) {
            player.sendMessage(new TextComponent(ChatColor.RED + "Sorry, but you entered too many arguments..."));
            player.sendMessage(new TextComponent(ChatColor.RED + "Usage: /plugins"));
        } else {
            TextComponent msg = new TextComponent(ChatColor.GOLD + "Plugins" + ChatColor.DARK_GRAY + ": " + getPluginList());

            player.sendMessage(msg);
        }
    }

    private String getPluginList(){
        Collection<Plugin> plugins = plugin.getProxy().getPluginManager().getPlugins();

        String pl = "";
        int i = 0;

        for (Plugin plugin : plugins){
            if (!(i == plugins.size() - 1))
                pl += ChatColor.GREEN + plugin.getDescription().getName() + ChatColor.DARK_GRAY + ", ";
            else
                pl += ChatColor.GREEN + plugin.getDescription().getName() + ChatColor.DARK_GRAY + ".";
            i++;
        }

        return pl;
    }
}
