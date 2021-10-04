package net.plasmere.streamline.commands.messaging;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.DiscordBotConfUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.savable.users.ChatLevel;
import net.plasmere.streamline.objects.savable.users.Player;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PlayerUtils;
import net.plasmere.streamline.utils.TextUtils;

import java.util.ArrayList;
import java.util.TreeSet;

public class BVerifyCommand extends Command implements TabExecutor {

    public BVerifyCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            long verificationNum = StreamLine.discordData.getVerification(((ProxiedPlayer) sender).getUniqueId().toString());
            MessagingUtils.sendBUserMessage(sender, "&aYour verification number: &6" + verificationNum +
                    "\n&aGo onto the discord and type &d" + DiscordBotConfUtils.botPrefix + "verify " + sender.getName() + " " + verificationNum + " &ato verify!");
        } else {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.onlyPlayers());
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        TreeSet<String> options = new TreeSet<>();

        options.add("local");
        options.add("global");
        options.add("guild");
        options.add("party");
        options.add("g-officer");
        options.add("p-officer");

        if (args.length <= 1) {
            return TextUtils.getCompletion(options, args[0]);
        } else {
            return new ArrayList<>();
        }
    }
}
