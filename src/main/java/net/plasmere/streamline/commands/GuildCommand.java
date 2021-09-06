package net.plasmere.streamline.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.CommandsConfUtils;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.Guild;
import net.plasmere.streamline.objects.users.SavableUser;
import net.plasmere.streamline.utils.*;

import java.util.*;

public class GuildCommand extends Command implements TabExecutor {
    public GuildCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        SavableUser stat = PlayerUtils.getOrGetSavableUser(sender);
        
        if (stat == null) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorNoYou);
            return;
        }

        // Usage: /guild <join !|leave !|create !|promote !|demote !|chat !|list !|open !|close !|disband !|accept !|deny !|invite !|kick|mute|warp>
        if (args.length <= 0 || args[0].length() <= 0) {
            try {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
            } catch (Exception e) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                e.printStackTrace();
            }
        } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildJoinAliases)) {
            if (args.length <= 1) {
                try {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            } else {
                try {
                    SavableUser user = PlayerUtils.getOrGetSavableUser(args[1]);
                    if (user == null) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer);
                        return;
                    }

                    GuildUtils.joinGuild(stat, user);
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            }
        } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildLeaveAliases)) {
            try {
                GuildUtils.leaveGuild(stat);
            } catch (Exception e) {
                MessagingUtils.sendBUserMessage(stat.findSender(), MessageConfUtils.bungeeCommandErrorUnd);
                e.printStackTrace();
            }
        } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildCreateAliases)) {
            if (args.length <= 1) {
                try {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            } else {
                try {
                    GuildUtils.createGuild(stat, TextUtils.argsToStringMinus(args, 0));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            }
        } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildPromoteAliases)) {
            if (args.length <= 1) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
            } else {
                try {
                    SavableUser user = PlayerUtils.getOrGetSavableUser(args[1]);
                    if (user == null) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer);
                        return;
                    }

                    GuildUtils.promotePlayer(stat, user);
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(stat.findSender(), MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            }
        } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildDemoteAliases)) {
            if (args.length <= 1) {
                try {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            } else {
                try {
                    SavableUser user = PlayerUtils.getOrGetSavableUser(args[1]);
                    if (user == null) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer);
                        return;
                    }

                    GuildUtils.demotePlayer(stat, user);
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            }
        } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildChatAliases)) {
            if (args.length <= 1) {
                try {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            } else {
                try {
                    GuildUtils.sendChat(stat, TextUtils.argsToStringMinus(args, 0));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            }
        } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildListAliases)) {
            try {
                GuildUtils.listGuild(stat);
            } catch (Exception e) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                e.printStackTrace();
            }
        } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildOpenAliases)) {
            try {
                GuildUtils.openGuild(stat);
            } catch (Exception e) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                e.printStackTrace();
            }
        } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildCloseAliases)) {
            try {
                GuildUtils.closeGuild(stat);
            } catch (Exception e) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                e.printStackTrace();
            }
        } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildDisbandAliases)) {
            try {
                GuildUtils.disband(stat);
            } catch (Throwable e) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                e.printStackTrace();
            }
        } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildAcceptAliases)) {
            if (args.length <= 1) {
                try {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            } else {
                try {
                    SavableUser user = PlayerUtils.getOrGetSavableUser(args[1]);
                    if (user == null) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer);
                        return;
                    }

                    GuildUtils.acceptInvite(stat, user);
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            }
        } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildDenyAliases)) {
            if (args.length <= 1) {
                try {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            } else {
                try {
                    SavableUser user = PlayerUtils.getOrGetSavableUser(args[1]);
                    if (user == null) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer);
                        return;
                    }

                    GuildUtils.denyInvite(stat, user);
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            }
        } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildInvAliases)) {
            if (args.length <= 1) {
                try {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            } else {
                try {
                    SavableUser user = PlayerUtils.getOrGetSavableUser(args[1]);
                    if (user == null) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer);
                        return;
                    }

                    GuildUtils.sendInvite(user, stat);
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            }
        } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildKickAliases)) {
            if (args.length <= 1) {
                try {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            } else {
                try {
                    SavableUser user = PlayerUtils.getOrGetSavableUser(args[1]);
                    if (user == null) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer);
                        return;
                    }

                    GuildUtils.kickMember(stat, user);
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            }
        } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildMuteAliases)) {
            try {
                GuildUtils.muteGuild(stat);
            } catch (Throwable e) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                e.printStackTrace();
            }
        } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildWarpAliases)) {
            try {
                GuildUtils.warpGuild(stat);
            } catch (Throwable e) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                e.printStackTrace();
            }
        } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildInfoAliases)) {
            try {
                GuildUtils.info(stat);
            } catch (Throwable e) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                e.printStackTrace();
            }
        } else if (MessagingUtils.compareWithList(args[0], CommandsConfUtils.comBGuildRenameAliases)) {
            if (args.length <= 1) {
                try {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            } else {
                try {
                    GuildUtils.rename(stat, TextUtils.argsToStringMinus(args, 0));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            }
        } else {
            try {
                SavableUser user = PlayerUtils.getOrGetSavableUser(args[0]);
                if (user == null) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer);
                    return;
                }

                GuildUtils.sendInvite(user, stat);
            } catch (Exception e) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                e.printStackTrace();
            }
        }

        try {
            Guild guild = GuildUtils.getGuild(stat);
            if (guild == null) return;
            guild.saveInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Usage: /guild <join|leave|create|promote|demote|chat|list|open|close|disband|accept|deny|invite|kick|mute|warp>
    @Override
    public Iterable<String> onTabComplete(final CommandSender sender, final String[] args)
    {
        Collection<ProxiedPlayer> players = StreamLine.getInstance().getProxy().getPlayers();
        List<String> strPlayers = new ArrayList<>();

        for (ProxiedPlayer player : players){
            strPlayers.add(player.getName());
        }

        strPlayers.add("%");

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
            tabArgs1.add("rename");

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
