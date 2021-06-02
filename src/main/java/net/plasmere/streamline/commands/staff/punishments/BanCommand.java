package net.plasmere.streamline.commands.staff.punishments;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.md_5.bungee.config.Configuration;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.utils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BanCommand extends Command implements TabExecutor {
    private Configuration bans = StreamLine.bans.getBans();

    public BanCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length <= 1) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
        } else if (args.length > 2 && ! args[0].equals("add") && ! args[0].equals("temp")) {
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
                if (bans.contains(other.uuid)) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.muteMPermAlready
                            .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                    );
                    return;
                }

                String reason = TextUtils.argsToStringMinus(args, 0, 1);

                bans.set(other.uuid + ".banned", true);
                bans.set(other.uuid + ".reason", reason);
                bans.set(other.uuid + ".till", "");

                if (other.online) {
                    ProxiedPlayer pp = UUIDFetcher.getPPlayerByUUID(other.uuid);
                    pp.disconnect(TextUtils.codedText(MessageConfUtils.punBannedPerm
                            .replace("%reason%", reason)
                    ));
                }

                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.banBPermSender
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                );
            } else if (args[0].equals("temp")) {
                if (bans.contains(other.uuid)) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.muteMPermAlready
                            .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                    );
                    return;
                }

                double toAdd = 0d;

                try {
                    toAdd = TimeUtil.convertStringTimeToDouble(args[2]);
                } catch (Exception e) {
                    e.printStackTrace();
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorSTime);
                    return;
                }

                String till = String.valueOf((long) (System.currentTimeMillis() + toAdd));

                String reason = TextUtils.argsToStringMinus(args, 0, 1, 2);

                bans.set(other.uuid + ".banned", true);
                bans.set(other.uuid + ".reason", reason);
                bans.set(other.uuid + ".till", till);

                if (other.online) {
                    ProxiedPlayer pp = UUIDFetcher.getPPlayerByUUID(other.uuid);
                    pp.disconnect(TextUtils.codedText(MessageConfUtils.punBannedTemp
                            .replace("%reason%", reason)
                            .replace("%date%", new Date(Long.parseLong(till)).toString())
                    ));
                }

                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.banBTempSender
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                        .replace("%date%", new Date(Long.parseLong(till)).toString())
                );
            } else if (args[0].equals("remove")) {
                if (! other.muted) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.banUnAlready);
                    return;
                }

                bans.set(other.uuid + ".banned", false);

                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.banUnSender
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                );
            } else if (args[0].equals("check")) {
                String reason = bans.getString(other.uuid + ".reason");
                String bannedMillis = bans.getString(other.uuid + ".till");
                if (bannedMillis == null) bannedMillis = "";

                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.banCheckMain
                        .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
                        .replace("%check%", reason != null ? MessageConfUtils.banCheckBanned : MessageConfUtils.banCheckUnBanned)
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
