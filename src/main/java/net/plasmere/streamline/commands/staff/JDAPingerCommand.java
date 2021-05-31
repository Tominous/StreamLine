package net.plasmere.streamline.commands.staff;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.utils.JDAPingerUtils;
import net.plasmere.streamline.utils.TextUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import net.dv8tion.jda.api.JDA;

import java.util.Objects;

public class JDAPingerCommand extends Command {
    public JDAPingerCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] strings) {
        JDA jda = StreamLine.getJda();

        ProxiedPlayer player = (ProxiedPlayer) sender;
        player.sendMessage(TextUtils.codedText("&aAttempting to ping..."));

        JDAPingerUtils.sendMessage(Objects.requireNonNull(jda.getTextChannelById("805575446107914260")));
    }
}
