package net.plasmere.streamline.commands;

import net.md_5.bungee.api.plugin.TabExecutor;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.Player;
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
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParJoinAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        PartyUtils.joinParty(Objects.requireNonNull(UUIDFetcher.getPlayer(sender)), UUIDFetcher.getPlayer(args[1]));
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParLeaveAliases)) {
                try {
                    PartyUtils.leaveParty(Objects.requireNonNull(UUIDFetcher.getPlayer(sender)));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParCreateAliases)) {
                try {
                    PartyUtils.createParty(Objects.requireNonNull(UUIDFetcher.getPlayer(sender)));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParPromoteAliases)) {
                if (args.length <= 1) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                } else {
                    try {
                        PartyUtils.promotePlayer(Objects.requireNonNull(UUIDFetcher.getPlayer(sender)), UUIDFetcher.getPlayer(args[1]));
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParDemoteAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        PartyUtils.demotePlayer(Objects.requireNonNull(UUIDFetcher.getPlayer(sender)), UUIDFetcher.getPlayer(args[1]));
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParChatAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        PartyUtils.sendChat(Objects.requireNonNull(UUIDFetcher.getPlayer(sender)), TextUtils.argsToStringMinus(args, 0));
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParListAliases)) {
                try {
                    PartyUtils.listParty(Objects.requireNonNull(UUIDFetcher.getPlayer(sender)));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParOpenAliases)) {
                if (args.length <= 1) {
                    try {
                        PartyUtils.openParty(Objects.requireNonNull(UUIDFetcher.getPlayer(sender)));
                    }  catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        if (PartyUtils.getParty(PlayerUtils.getPlayerStat(sender)) != null) {
                            PartyUtils.openPartySized(Objects.requireNonNull(UUIDFetcher.getPlayer(sender)), Integer.parseInt(args[1]));
                        } else {
                            PartyUtils.createPartySized(Objects.requireNonNull(UUIDFetcher.getPlayer(sender)), Integer.parseInt(args[1]));
                        }
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParCloseAliases)) {
                try {
                    PartyUtils.closeParty(Objects.requireNonNull(UUIDFetcher.getPlayer(sender)));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParDisbandAliases)) {
                try {
                    PartyUtils.disband(Objects.requireNonNull(UUIDFetcher.getPlayer(sender)));
                } catch (Throwable e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParAcceptAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        PartyUtils.acceptInvite(Objects.requireNonNull(UUIDFetcher.getPlayer(sender)), UUIDFetcher.getPlayer(args[1]));
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParDenyAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        PartyUtils.denyInvite(Objects.requireNonNull(UUIDFetcher.getPlayer(sender)), UUIDFetcher.getPlayer(args[1]));
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParInvAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        PartyUtils.sendInvite(UUIDFetcher.getPlayer(args[1]), Objects.requireNonNull(UUIDFetcher.getPlayer(sender)));
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParKickAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        PartyUtils.kickMember(Objects.requireNonNull(UUIDFetcher.getPlayer(sender)), UUIDFetcher.getPlayer(args[1]));
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParMuteAliases)) {
                try {
                    PartyUtils.muteParty(Objects.requireNonNull(UUIDFetcher.getPlayer(sender)));
                } catch (Throwable e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBParWarpAliases)) {
                try {
                    PartyUtils.warpParty(Objects.requireNonNull(UUIDFetcher.getPlayer(sender)));
                } catch (Throwable e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            } else {
                try {
                    Player p = UUIDFetcher.getPlayer(args[0]);

                    if (p == null) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                        return;
                    }

                    PartyUtils.sendInvite(p, Objects.requireNonNull(UUIDFetcher.getPlayer(sender)));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            }
        } else {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.onlyPlayers);
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
            if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildJoinAliases)) {
                return TextUtils.getCompletion(strPlayers, args[1]);
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildLeaveAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildCreateAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildPromoteAliases)) {
                return TextUtils.getCompletion(strPlayers, args[1]);
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildDemoteAliases)) {
                return TextUtils.getCompletion(strPlayers, args[1]);
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildChatAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildListAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildOpenAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildCloseAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildDisbandAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildAcceptAliases)) {
                return TextUtils.getCompletion(strPlayers, args[1]);
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildDenyAliases)) {
                return TextUtils.getCompletion(strPlayers, args[1]);
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildInvAliases)) {
                return TextUtils.getCompletion(strPlayers, args[1]);
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildKickAliases)) {
                return TextUtils.getCompletion(strPlayers, args[0]);
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildMuteAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildWarpAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildRenameAliases)) {
                return new ArrayList<>();
            } else {
                return TextUtils.getCompletion(strPlayers, args[1]);
            }
        }
        return new ArrayList<>();
    }
}
