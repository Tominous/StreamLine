package net.plasmere.streamline.commands.staff.punishments;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PlayerUtils;
import net.plasmere.streamline.utils.TimeUtil;
import net.plasmere.streamline.utils.UUIDFetcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MuteCommand extends Command implements TabExecutor {
    public MuteCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length <= 0) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
        } else if (args.length > 2 && ! (args[0].equals("add") || args[0].equals("temp"))) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsLess);
        } else if (args.length > 3) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsLess);
        } else {
            Player other = PlayerUtils.getStat(args[1]);

            if (other == null) {
                PlayerUtils.addStat(new Player(UUIDFetcher.getCachedUUID(args[1])));
                other = PlayerUtils.getStat(args[1]);
                if (other == null) {
                    StreamLine.getInstance().getLogger().severe("CANNOT INSTANTIATE THE PLAYER: " + args[1]);
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    return;
                }
            }

            if (other.uuid == null) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer);
                return;
            }

            if (args[0].equals("add")) {
                if (args.length == 3) {
                    if (! ConfigUtils.punMutesReplaceable) {
                        if (other.muted) {
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.muteMTempAlready);
                            return;
                        }
                    }

                    final double timeAmount = TimeUtil.convertStringTimeToDouble(args[2]);

                    other.updateMute(true, new Date((long) (System.currentTimeMillis() + timeAmount)));

                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.muteMTempSender
                            .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                            .replace("%date%", other.mutedTill.toString())
                    );
                    if (other.online) {
                        MessagingUtils.sendBUserMessage(UUIDFetcher.getPPlayerByUUID(other.uuid), MessageConfUtils.muteMTempMuted
                                .replace("%sender%", sender instanceof ProxyServer ? "CONSOLE" : PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreate(((ProxiedPlayer) sender).getUniqueId().toString())))
                                .replace("%date%", other.mutedTill.toString())
                        );
                    }
                    return;
                }

                if (! ConfigUtils.punMutesReplaceable) {
                    if (other.muted) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.muteMPermAlready);
                        return;
                    }
                }

                other.setMuted(true);
                other.removeMutedTill();

                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.muteMPermSender
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                );
                if (other.online) {
                    MessagingUtils.sendBUserMessage(UUIDFetcher.getPPlayerByUUID(other.uuid), MessageConfUtils.muteMPermMuted
                            .replace("%sender%", sender instanceof ProxyServer ? "CONSOLE" : PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreate(((ProxiedPlayer) sender).getUniqueId().toString())))
                    );
                }
            } else if (args[0].equals("temp")) {
                if (args.length < 3) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                } else {
                    if (! ConfigUtils.punMutesReplaceable) {
                        if (other.muted) {
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.muteMTempAlready);
                            return;
                        }
                    }

                    final double timeAmount = TimeUtil.convertStringTimeToDouble(args[2]);

                    other.updateMute(true, new Date((long) (System.currentTimeMillis() + timeAmount)));

                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.muteMTempSender
                            .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                            .replace("%date%", other.mutedTill.toString())
                    );
                    if (other.online) {
                        MessagingUtils.sendBUserMessage(UUIDFetcher.getPPlayerByUUID(other.uuid), MessageConfUtils.muteMTempMuted
                                .replace("%sender%", sender instanceof ProxyServer ? "CONSOLE" : PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreate(((ProxiedPlayer) sender).getUniqueId().toString())))
                                .replace("%date%", other.mutedTill.toString())
                        );
                    }
                }
            } else if (args[0].equals("remove")) {
                if (! other.muted) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.muteUnAlready);
                    return;
                }

                other.setMuted(false);
                other.removeMutedTill();

                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.muteUnSender
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                );
                if (other.online) {
                    MessagingUtils.sendBUserMessage(UUIDFetcher.getPPlayerByUUID(other.uuid), MessageConfUtils.muteUnMuted
                            .replace("%sender%", sender instanceof ProxyServer ? "CONSOLE" : PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreate(((ProxiedPlayer) sender).getUniqueId().toString())))
                    );
                }
            } else if (args[0].equals("check")) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.muteCheckMain
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                        .replace("%check%", other.muted ? MessageConfUtils.muteCheckMuted : MessageConfUtils.muteCheckUnMuted)
                );
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(final CommandSender sender, final String[] args) {
        Collection<ProxiedPlayer> players = StreamLine.getInstance().getProxy().getPlayers();
        List<String> strPlayers = new ArrayList<>();

        for (ProxiedPlayer player : players){
            if (sender instanceof ProxiedPlayer) if (player.equals(sender)) continue;
            strPlayers.add(player.getName());
        }

        List<String> options = new ArrayList<>();

        options.add("add");
        options.add("temp");
        options.add("remove");
        options.add("check");

        if (args.length == 1) {
            final String param1 = args[0];

            return options.stream()
                    .filter(completion -> completion.startsWith(param1))
                    .collect(Collectors.toList());
        } else if (args.length == 2) {
            final String param2 = args[1];

            return strPlayers.stream()
                    .filter(completion -> completion.startsWith(param2))
                    .collect(Collectors.toList());
        }
//        else if (args.length == 3) {
//            final String param3 = args[2];
//
//
//        }

        return new ArrayList<>();
    }
}
