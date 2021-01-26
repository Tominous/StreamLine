package net.plasmere.streamline.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.Guild;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.utils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class GuildCommand extends Command implements TabExecutor {
    private final StreamLine plugin;

    public GuildCommand(StreamLine streamLine, String perm, String[] aliases){
        super("guild", perm, aliases);
        this.plugin = streamLine;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            // Usage: /guild <join|leave|create|promote|demote|chat|list|open|close|disband|accept|deny|invite|kick|mute|warp>
            if (args.length <= 0 || args[0].length() <= 0) {
                try {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildJoinAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        GuildUtils.joinGuild(Objects.requireNonNull(PlayerUtils.getStat(sender)), UUIDFetcher.getPlayer(args[1]));
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildLeaveAliases)) {
                try {
                    GuildUtils.leaveGuild(Objects.requireNonNull(PlayerUtils.getStat(sender)));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildCreateAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        GuildUtils.createGuild(Objects.requireNonNull(PlayerUtils.getStat(sender)), args[1]);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildPromoteAliases)) {
                if (args.length <= 1) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                } else {
                    try {

                        GuildUtils.promotePlayer(Objects.requireNonNull(PlayerUtils.getStat(sender)), UUIDFetcher.getPlayer(args[1]));
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildDemoteAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        GuildUtils.demotePlayer(Objects.requireNonNull(PlayerUtils.getStat(sender)), UUIDFetcher.getPlayer(args[1]));
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildChatAliases)) {
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

                        GuildUtils.sendChat(Objects.requireNonNull(PlayerUtils.getStat(sender)), TextUtils.normalize(args));
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildListAliases)) {
                try {
                    GuildUtils.listGuild(Objects.requireNonNull(PlayerUtils.getStat(sender)));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildOpenAliases)) {
                try {
                    GuildUtils.openGuild(Objects.requireNonNull(PlayerUtils.getStat(sender)));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildCloseAliases)) {
                try {
                    GuildUtils.closeGuild(Objects.requireNonNull(PlayerUtils.getStat(sender)));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildDisbandAliases)) {
                try {
                    GuildUtils.disband(Objects.requireNonNull(PlayerUtils.getStat(sender)));
                } catch (Throwable e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildAcceptAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        GuildUtils.acceptInvite(Objects.requireNonNull(PlayerUtils.getStat(sender)), UUIDFetcher.getPlayer(args[1]));
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildDenyAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        GuildUtils.denyInvite(Objects.requireNonNull(PlayerUtils.getStat(sender)), UUIDFetcher.getPlayer(args[1]));
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildInvAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        GuildUtils.sendInvite(UUIDFetcher.getPlayer(args[1]), Objects.requireNonNull(PlayerUtils.getStat(sender)));
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildKickAliases)) {
                if (args.length <= 1) {
                    try {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        GuildUtils.kickMember(Objects.requireNonNull(PlayerUtils.getStat(sender)), UUIDFetcher.getPlayer(args[1]));
                    } catch (Exception e) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                        e.printStackTrace();
                    }
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildMuteAliases)) {
                try {
                    GuildUtils.muteGuild(Objects.requireNonNull(PlayerUtils.getStat(sender)));
                } catch (Throwable e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildWarpAliases)) {
                try {
                    GuildUtils.warpGuild(Objects.requireNonNull(PlayerUtils.getStat(sender)));
                } catch (Throwable e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                    e.printStackTrace();
                }
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildInfoAliases)) {
                try {
                    GuildUtils.info(Objects.requireNonNull(PlayerUtils.getStat(sender)));
                } catch (Throwable e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                    e.printStackTrace();
                }
            } else {
                try {
                    GuildUtils.sendInvite(PlayerUtils.getStat(args[0]), Objects.requireNonNull(PlayerUtils.getStat(sender)));
                } catch (Exception e) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandError);
                    e.printStackTrace();
                }
            }
        } else {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.onlyPlayers);
        }

        try {
            assert sender instanceof ProxiedPlayer;
            Guild guild = GuildUtils.getGuild(PlayerUtils.getStat(sender));
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
        Collection<ProxiedPlayer> players = plugin.getProxy().getPlayers();
        List<String> strPlayers = new ArrayList<>();

        for (ProxiedPlayer player : players){
            strPlayers.add(Objects.requireNonNull(PlayerUtils.getStat(player)).getName());
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
            if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildJoinAliases)) {
                return strPlayers;
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildLeaveAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildCreateAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildPromoteAliases)) {
                return strPlayers;
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildDemoteAliases)) {
                return strPlayers;
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
                return strPlayers;
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildDenyAliases)) {
                return strPlayers;
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildInvAliases)) {
                return strPlayers;
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildKickAliases)) {
                return strPlayers;
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildMuteAliases)) {
                return new ArrayList<>();
            } else if (MessagingUtils.compareWithList(args[0], ConfigUtils.comBGuildWarpAliases)) {
                return new ArrayList<>();
            } else {
                return strPlayers;
            }
        }
        return new ArrayList<>();
    }
}
