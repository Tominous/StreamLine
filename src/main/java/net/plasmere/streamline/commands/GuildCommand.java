package net.plasmere.streamline.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.plasmere.streamline.StreamLine;
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
        SavableUser stat = PlayerUtils.getOrCreateSavableUser(sender);

        // Usage: /guild <join !|leave !|create !|promote !|demote !|chat !|list !|open !|close !|disband !|accept !|deny !|invite !|kick|mute|warp>
        if (args.length <= 0 || args[0].length() <= 0) {
            try {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
            } catch (Exception e) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                e.printStackTrace();
            }
        } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildJoinAliases)) {
            if (args.length <= 1) {
                try {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            } else {
                try {
                    GuildUtils.joinGuild(stat, PlayerUtils.getOrCreateSavableUser(args[1]));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            }
        } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildLeaveAliases)) {
            try {
                GuildUtils.leaveGuild(stat);
            } catch (Exception e) {
                MessagingUtils.sendBUserMessage(stat.sender, MessageConfUtils.bungeeCommandErrorUnd);
                e.printStackTrace();
            }
        } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildCreateAliases)) {
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
        } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildPromoteAliases)) {
            if (args.length <= 1) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
            } else {
                try {
                    GuildUtils.promotePlayer(stat, PlayerUtils.getOrCreateSavableUser(args[1]));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(stat.sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            }
        } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildDemoteAliases)) {
            if (args.length <= 1) {
                try {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            } else {
                try {
                    GuildUtils.demotePlayer(stat, PlayerUtils.getOrCreateSavableUser(args[1]));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            }
        } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildChatAliases)) {
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
        } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildListAliases)) {
            try {
                GuildUtils.listGuild(stat);
            } catch (Exception e) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                e.printStackTrace();
            }
        } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildOpenAliases)) {
            try {
                GuildUtils.openGuild(stat);
            } catch (Exception e) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                e.printStackTrace();
            }
        } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildCloseAliases)) {
            try {
                GuildUtils.closeGuild(stat);
            } catch (Exception e) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                e.printStackTrace();
            }
        } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildDisbandAliases)) {
            try {
                GuildUtils.disband(stat);
            } catch (Throwable e) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                e.printStackTrace();
            }
        } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildAcceptAliases)) {
            if (args.length <= 1) {
                try {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            } else {
                try {
                    GuildUtils.acceptInvite(stat, PlayerUtils.getOrCreateSavableUser(args[1]));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            }
        } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildDenyAliases)) {
            if (args.length <= 1) {
                try {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            } else {
                try {
                    GuildUtils.denyInvite(stat, PlayerUtils.getOrCreateSavableUser(args[1]));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            }
        } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildInvAliases)) {
            if (args.length <= 1) {
                try {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            } else {
                try {
                    GuildUtils.sendInvite(PlayerUtils.getOrCreateSavableUser(args[1]), stat);
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            }
        } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildKickAliases)) {
            if (args.length <= 1) {
                try {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            } else {
                try {
                    GuildUtils.kickMember(stat, PlayerUtils.getOrCreateSavableUser(args[1]));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    e.printStackTrace();
                }
            }
        } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildMuteAliases)) {
            try {
                GuildUtils.muteGuild(stat);
            } catch (Throwable e) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                e.printStackTrace();
            }
        } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildWarpAliases)) {
            try {
                GuildUtils.warpGuild(stat);
            } catch (Throwable e) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                e.printStackTrace();
            }
        } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildInfoAliases)) {
            try {
                GuildUtils.info(stat);
            } catch (Throwable e) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                e.printStackTrace();
            }
        } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildRenameAliases)) {
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
                GuildUtils.sendInvite(PlayerUtils.getOrCreateSavableUser(args[0]), stat);
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
