package net.plasmere.streamline.commands;

import net.md_5.bungee.api.plugin.TabExecutor;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.NoPlayerFoundException;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.utils.*;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.*;

public class PartyCommand extends Command implements TabExecutor {
    private final StreamLine plugin;

    public PartyCommand(StreamLine streamLine, String perm, String[] aliases){
        super("party", perm, aliases);
        this.plugin = streamLine;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            // Usage: /party <join|leave|create|promote|demote|chat|list|open|close|disband|accept|deny|invite|kick|mute|warp>
            if (args.length <= 0 || args[0].length() <= 0) {
                try {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParJoinAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Player online = tryOnline(args[1]);
                        if (online == null) return;
                        PartyUtils.joinParty(PlayerUtils.getStat(sender), online);
                    } catch (NoPlayerFoundException e){
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParLeaveAliases)) {
                try {
                    PartyUtils.leaveParty(PlayerUtils.getStat(sender));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParCreateAliases)) {
                try {
                    PartyUtils.createParty(plugin, PlayerUtils.getStat(sender));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParPromoteAliases)) {
                if (args.length <= 1) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                } else {
                    try {
                        Player online = tryOnline(args[1]);
                        if (online == null) return;
                        PartyUtils.promotePlayer(PlayerUtils.getStat(sender), online);
                    } catch (NoPlayerFoundException e){
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParDemoteAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Player online = tryOnline(args[1]);
                        if (online == null) return;
                        PartyUtils.demotePlayer(PlayerUtils.getStat(sender), online);
                    } catch (NoPlayerFoundException e){
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParChatAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        args[0] = "";

                        PartyUtils.sendChat(PlayerUtils.getStat(sender), TextUtils.normalize(args));
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParListAliases)) {
                try {
                    PartyUtils.listParty(PlayerUtils.getStat(sender));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParOpenAliases)) {
                if (args.length <= 1) {
                    try {
                        PartyUtils.openParty(PlayerUtils.getStat(sender));
                    }  catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        if (PartyUtils.getParty(PlayerUtils.getStat(sender)) != null) {
                            PartyUtils.openPartySized(PlayerUtils.getStat(sender), Integer.parseInt(args[1]));
                        } else {
                            PartyUtils.createPartySized(plugin, PlayerUtils.getStat(sender), Integer.parseInt(args[1]));
                        }
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParCloseAliases)) {
                try {
                    PartyUtils.closeParty(PlayerUtils.getStat(sender));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParDisbandAliases)) {
                try {
                    PartyUtils.disband(PlayerUtils.getStat(sender));
                } catch (Throwable e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParAcceptAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Player online = tryOnline(args[1]);
                        if (online == null) return;
                        PartyUtils.acceptInvite(PlayerUtils.getStat(sender), online);
                    } catch (NoPlayerFoundException e){
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParDenyAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Player online = tryOnline(args[1]);
                        if (online == null) return;
                        PartyUtils.denyInvite(PlayerUtils.getStat(sender), online);
                    } catch (NoPlayerFoundException e){
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParInvAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Player online = tryOnline(args[1]);
                        if (online == null) return;
                        PartyUtils.sendInvite(online, PlayerUtils.getStat(sender));
                    } catch (NoPlayerFoundException e){
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParKickAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Player online = tryOnline(args[1]);
                        if (online == null) return;
                        PartyUtils.kickMember(PlayerUtils.getStat(sender), online);
                    } catch (NoPlayerFoundException e){
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParMuteAliases)) {
                try {
                    PartyUtils.muteParty(PlayerUtils.getStat(sender));
                } catch (Throwable e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParWarpAliases)) {
                try {
                    PartyUtils.warpParty(PlayerUtils.getStat(sender));
                } catch (Throwable e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                    e.printStackTrace();
                }
            } else {
                try {
                    Player online = tryOnline(args[0]);
                    if (online == null) return;
                    PartyUtils.sendInvite(online, PlayerUtils.getStat(sender));
                } catch (NoPlayerFoundException e){
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer);
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                    e.printStackTrace();
                }
            }
        } else {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.onlyPlayers);
        }
    }

    public Player tryOnline(String player) throws NoPlayerFoundException {
        try {
            for (ProxiedPlayer p : plugin.getProxy().getPlayers()){
                if (p.getName().equals(player))
                    return PlayerUtils.getStat(p);
            }
            throw new NoPlayerFoundException(player);
        } catch (NoPlayerFoundException e){
            throw new NoPlayerFoundException(player);
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    // Usage: /party <join|leave|create|promote|demote|chat|list|open|close|disband|accept|deny|invite|kick|mute|warp>
    @Override
    public Iterable<String> onTabComplete(final CommandSender sender, final String[] args)
    {
        Collection<ProxiedPlayer> players = plugin.getProxy().getPlayers();
        List<String> strPlayers = new ArrayList<>();

        for (ProxiedPlayer player : players){
            strPlayers.add(PlayerUtils.getOffOnReg(Objects.requireNonNull(PlayerUtils.getStat(player))));
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
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParKickAliases)) {
                return strPlayers;
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParMuteAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParWarpAliases)) {
                return new ArrayList<>();
            } else {
                return strPlayers;
            }
        }
        return new ArrayList<>();
    }
}
