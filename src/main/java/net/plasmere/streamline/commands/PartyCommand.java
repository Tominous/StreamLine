package net.plasmere.streamline.commands;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PartyUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.apache.commons.collections4.iterators.IteratorChain;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PartyCommand extends Command implements TabExecutor {
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

    // Usage: /party <join|leave|create|promote|demote|chat|list|open|close|disband|accept|deny|invite>
    @Override
    public Iterable<String> onTabComplete(final CommandSender sender, final String[] args)
    {
        Collection<ProxiedPlayer> players = plugin.getProxy().getPlayers();
        List<String> strPlayers = new ArrayList<>();

        for (ProxiedPlayer player : players){
            strPlayers.add(player.getName());
        }

        if (args.length > 2) return new ArrayList<>();
        if (args.length == 1) {
            List<String> tabArgs1 = new ArrayList<>();
            tabArgs1.add("join");
            tabArgs1.add("leave");
            tabArgs1.add("create");
            tabArgs1.add("promote");
            tabArgs1.add("demote");
            tabArgs1.add("chat");
            tabArgs1.add("list");
            tabArgs1.add("open");
            tabArgs1.add("close");
            tabArgs1.add("disband");
            tabArgs1.add("accept");
            tabArgs1.add("deny");
            tabArgs1.add("invite");
            tabArgs1.add("mute");
            tabArgs1.add("warp");

            return tabArgs1;
        }
        if (args.length == 2){
            if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParJoinAliases)) {
                return strPlayers;
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParLeaveAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParCreateAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParPromoteAliases)) {
                return strPlayers;
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParDemoteAliases)) {
                return strPlayers;
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParChatAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParListAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParOpenAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParCloseAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParDisbandAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParAcceptAliases)) {
                return strPlayers;
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParDenyAliases)) {
                return strPlayers;
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParInvAliases)) {
                return strPlayers;
            } else {
                return strPlayers;
            }
        }
        return new ArrayList<>();
    }
}
