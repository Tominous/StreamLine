package net.plasmere.streamline.commands;

import net.md_5.bungee.api.plugin.TabExecutor;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.CommandsConfUtils;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.users.Player;
import net.plasmere.streamline.utils.*;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.*;

public class PartyCommand extends Command implements TabExecutor {
    public PartyCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            // Usage: /party <join !|leave |create !|promote !|demote !|chat !|list !|open !|close !|disband |accept !|deny !|invite !|kick !|mute !|warp !>
            if (args.length <= 0 || args[0].length() <= 0) {
                try {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBParJoinAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                        e.printStackTrace();
                    }
                } else {
                    try {
                        PartyUtils.joinParty(PlayerUtils.getOrCreatePlayerStat(sender), PlayerUtils.getOrCreatePlayerStat(args[1]));
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBParLeaveAliases)) {
                try {
                    PartyUtils.leaveParty(PlayerUtils.getOrCreatePlayerStat(sender));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBParCreateAliases)) {
                try {
                    PartyUtils.createParty(PlayerUtils.getOrCreatePlayerStat(sender));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBParPromoteAliases)) {
                if (args.length <= 1) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
                } else {
                    try {
                        PartyUtils.promotePlayer(PlayerUtils.getOrCreatePlayerStat(sender), PlayerUtils.getOrCreatePlayerStat(args[1]));
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBParDemoteAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                        e.printStackTrace();
                    }
                } else {
                    try {
                        PartyUtils.demotePlayer(PlayerUtils.getOrCreatePlayerStat(sender), PlayerUtils.getOrCreatePlayerStat(args[1]));
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBParChatAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                        e.printStackTrace();
                    }
                } else {
                    try {
                        PartyUtils.sendChat(PlayerUtils.getOrCreatePlayerStat(sender), TextUtils.argsToStringMinus(args, 0));
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBParListAliases)) {
                try {
                    PartyUtils.listParty(PlayerUtils.getOrCreatePlayerStat(sender));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBParOpenAliases)) {
                if (args.length <= 1) {
                    try {
                        PartyUtils.openParty(PlayerUtils.getOrCreatePlayerStat(sender));
                    }  catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                        e.printStackTrace();
                    }
                } else {
                    try {
                        if (PartyUtils.getParty(PlayerUtils.getPlayerStat(sender)) != null) {
                            PartyUtils.openPartySized(PlayerUtils.getOrCreatePlayerStat(sender), Integer.parseInt(args[1]));
                        } else {
                            PartyUtils.createPartySized(PlayerUtils.getOrCreatePlayerStat(sender), Integer.parseInt(args[1]));
                        }
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBParCloseAliases)) {
                try {
                    PartyUtils.closeParty(PlayerUtils.getOrCreatePlayerStat(sender));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBParDisbandAliases)) {
                try {
                    PartyUtils.disband(PlayerUtils.getOrCreatePlayerStat(sender));
                } catch (Throwable e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBParAcceptAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                        e.printStackTrace();
                    }
                } else {
                    try {
                        PartyUtils.acceptInvite(PlayerUtils.getOrCreatePlayerStat(sender), PlayerUtils.getOrCreatePlayerStat(args[1]));
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBParDenyAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                        e.printStackTrace();
                    }
                } else {
                    try {
                        PartyUtils.denyInvite(PlayerUtils.getOrCreatePlayerStat(sender), PlayerUtils.getOrCreatePlayerStat(args[1]));
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBParInvAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                        e.printStackTrace();
                    }
                } else {
                    try {
                        PartyUtils.sendInvite(PlayerUtils.getOrCreatePlayerStat(args[1]), PlayerUtils.getOrCreatePlayerStat(sender));
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBParKickAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                        e.printStackTrace();
                    }
                } else {
                    try {
                        PartyUtils.kickMember(PlayerUtils.getOrCreatePlayerStat(sender), PlayerUtils.getOrCreatePlayerStat(args[1]));
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBParMuteAliases)) {
                try {
                    PartyUtils.muteParty(PlayerUtils.getOrCreatePlayerStat(sender));
                } catch (Throwable e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBParWarpAliases)) {
                try {
                    PartyUtils.warpParty(PlayerUtils.getOrCreatePlayerStat(sender));
                } catch (Throwable e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                    e.printStackTrace();
                }
            } else {
                try {
                    Player p = PlayerUtils.getOrCreatePlayerStat(args[0]);

                    if (p == null) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                        return;
                    }

                    PartyUtils.sendInvite(p, PlayerUtils.getOrCreatePlayerStat(sender));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                    e.printStackTrace();
                }
            }
        } else {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.onlyPlayers());
        }
    }

    // Usage: /party <join|leave|create|promote|demote|chat|list|open|close|disband|accept|deny|invite|kick|mute|warp>
    @Override
    public Iterable<String> onTabComplete(final CommandSender sender, final String[] args)
    {
        Collection<ProxiedPlayer> players = StreamLine.getInstance().getProxy().getPlayers();
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

            return TextUtils.getCompletion(tabArgs1, args[0]);
        }
        if (args.length == 2) {
            if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildJoinAliases)) {
                return TextUtils.getCompletion(strPlayers, args[1]);
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildLeaveAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildCreateAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildPromoteAliases)) {
                return TextUtils.getCompletion(strPlayers, args[1]);
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildDemoteAliases)) {
                return TextUtils.getCompletion(strPlayers, args[1]);
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildChatAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildListAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildOpenAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildCloseAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildDisbandAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildAcceptAliases)) {
                return TextUtils.getCompletion(strPlayers, args[1]);
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildDenyAliases)) {
                return TextUtils.getCompletion(strPlayers, args[1]);
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildInvAliases)) {
                return TextUtils.getCompletion(strPlayers, args[1]);
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildKickAliases)) {
                return TextUtils.getCompletion(strPlayers, args[0]);
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildMuteAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildWarpAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildRenameAliases)) {
                return new ArrayList<>();
            } else {
                return TextUtils.getCompletion(strPlayers, args[1]);
            }
        }
        return new ArrayList<>();
    }
}
