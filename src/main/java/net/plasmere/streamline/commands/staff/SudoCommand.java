package net.plasmere.streamline.commands.staff;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PlayerUtils;
import net.plasmere.streamline.utils.TextUtils;

import java.util.*;

public class SudoCommand extends Command implements TabExecutor {
    public SudoCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length <= 1) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
        } else {
            ProxiedPlayer sudoOn = StreamLine.getInstance().getProxy().getPlayer(args[0]);

            if (sudoOn.hasPermission(ConfigUtils.noSudoPerm)){
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.sudoNoSudo()
                        .replace("%user%", sudoOn.getDisplayName())
                );
                return;
            }

            if (StreamLine.getInstance().getProxy().getPluginManager().dispatchCommand(sudoOn, TextUtils.argsToStringMinus(args, 0))){
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.sudoWorked()
                        .replace("%user%", sudoOn.getDisplayName())
                );
            } else {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.sudoNoWork()
                        .replace("%user%", sudoOn.getDisplayName())
                );
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(final CommandSender sender, final String[] args) {
        Collection<ProxiedPlayer> players = StreamLine.getInstance().getProxy().getPlayers();
        List<String> strPlayers = new ArrayList<>();

        for (ProxiedPlayer player : players){
            strPlayers.add(player.getName());
        }

        Collection<Map.Entry<String, Command>> commands = StreamLine.getInstance().getProxy().getPluginManager().getCommands();
        List<String> strCommands = new ArrayList<>();

        for (Map.Entry<String, Command> com : commands){
            strCommands.add(com.getValue().getName());
        }

        if (args.length == 1) {
            return TextUtils.getCompletion(strPlayers, args[0]);
        } else if (args.length == 2){
            return TextUtils.getCompletion(strCommands, args[1]);
        }

        return new ArrayList<>();
    }
}
