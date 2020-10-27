package net.plasmere.streamline.commands;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PartyUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PartyCommand extends Command {
    private final StreamLine plugin;

    public PartyCommand(StreamLine streamLine, String perm, String[] aliases){
        super("party", perm, aliases);
        this.plugin = streamLine;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer){
            // Usage: /party <join|leave|create|promote|demote|chat|list|open|close|disband|accept|deny|invite>
            try {
                if (args.length <= 0 || args[0].length() <= 0) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParJoinAliases)) {
                    if (args.length <= 1) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } else {
                        PartyUtils.joinParty((ProxiedPlayer) sender, plugin.getProxy().getPlayer(args[1]));
                    }
                } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParLeaveAliases)) {
                    PartyUtils.leaveParty((ProxiedPlayer) sender);
                } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParCreateAliases)) {
                    PartyUtils.createParty(plugin, (ProxiedPlayer) sender);
                } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParPromoteAliases)) {
                    if (args.length <= 1) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } else {
                        PartyUtils.promotePlayer(plugin.getProxy().getPlayer(args[1]));
                    }
                } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParDemoteAliases)) {
                    if (args.length <= 1) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } else {
                        PartyUtils.demotePlayer(plugin.getProxy().getPlayer(args[1]));
                    }
                } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParChatAliases)) {
                    if (args.length <= 1) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } else {
                        PartyUtils.sendChat((ProxiedPlayer) sender, args[1]);
                    }
                } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParListAliases)) {
                    PartyUtils.listParty((ProxiedPlayer) sender);
                } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParOpenAliases)) {
                    if (args.length <= 1) {
                        PartyUtils.openParty((ProxiedPlayer) sender);
                    } else {
                        PartyUtils.openPartySized((ProxiedPlayer) sender, Integer.parseInt(args[1]));
                    }
                } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParCloseAliases)) {
                    PartyUtils.closeParty((ProxiedPlayer) sender);
                } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParDisbandAliases)) {
                    PartyUtils.disband((ProxiedPlayer) sender);
                } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParAcceptAliases)) {
                    if (args.length <= 1) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } else {
                        PartyUtils.acceptInvite((ProxiedPlayer) sender, plugin.getProxy().getPlayer(args[1]));
                    }
                } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParDenyAliases)) {
                    if (args.length <= 1) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } else {
                        PartyUtils.denyInvite((ProxiedPlayer) sender, plugin.getProxy().getPlayer(args[1]));
                    }
                } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParInvAliases)) {
                    if (args.length <= 1) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } else {
                        PartyUtils.sendInvite((ProxiedPlayer) sender, plugin.getProxy().getPlayer(args[1]));
                    }
                } else {
                    PartyUtils.sendInvite(plugin.getProxy().getPlayer(args[0]), (ProxiedPlayer) sender);
                }
            } catch (Throwable e) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                e.printStackTrace();
            }
        } else {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.onlyPlayers);
        }
    }
}
