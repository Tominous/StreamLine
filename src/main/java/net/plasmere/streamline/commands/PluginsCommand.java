package net.plasmere.streamline.commands;

import net.plasmere.streamline.StreamLine;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Collection;

public class PluginsCommand extends Command {

    public PluginsCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length > 0) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "Sorry, but you entered too many arguments..."));
            sender.sendMessage(new TextComponent(ChatColor.RED + "Usage: /plugins"));
        } else {
            TextComponent msg = new TextComponent(ChatColor.GOLD + "Plugins" + ChatColor.DARK_GRAY + ": " + getPluginList());

            sender.sendMessage(msg);
        }
    }

    private String getPluginList(){
        Collection<Plugin> plugins = StreamLine.getInstance().getProxy().getPluginManager().getPlugins();

        StringBuilder pl = new StringBuilder();
        int i = 0;

        for (Plugin plugin : plugins){
            if (!(i == plugins.size() - 1))
                pl.append(ChatColor.GREEN).append(plugin.getDescription().getName()).append(ChatColor.DARK_GRAY).append(", ");
            else
                pl.append(ChatColor.GREEN).append(plugin.getDescription().getName()).append(ChatColor.DARK_GRAY).append(".");
            i++;
        }

        return pl.toString();
    }
}
