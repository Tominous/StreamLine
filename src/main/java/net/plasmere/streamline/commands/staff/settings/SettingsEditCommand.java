package net.plasmere.streamline.commands.staff.settings;

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
import net.plasmere.streamline.utils.TextUtils;
import net.plasmere.streamline.utils.UUIDFetcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SettingsEditCommand extends Command implements TabExecutor {
    public SettingsEditCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);

        if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info("Settings make base: " + base);
        if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info("Settings make perm: " + perm);
        if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info("Settings make aliases: " + Arrays.toString(aliases));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        switch (args[0]) {
            case "set":
                if (args.length <= 2) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    return;
                }
                switch (args[1]) {
                    case "motd":
                        if (args.length < 4) {
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                            return;
                        }

                        int at = 0;
                        try {
                            at = Integer.parseInt(args[2]);
                        } catch (Exception e) {
                            e.printStackTrace();
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorInt);
                            return;
                        }

                        String rest = TextUtils.argsToStringMinus(args, 0, 1, 2);

                        StreamLine.serverConfig.setMOTD(Integer.toString(at), rest);

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsSetMOTD
                                .replace("%number%", Integer.toString(at))
                                .replace("%set%", rest)
                        );
                        break;
                    case "motd-time":
                        String motdtime = TextUtils.argsToStringMinus(args, 0, 1);

                        int time = 0;
                        try {
                            time = Integer.parseInt(motdtime);
                        } catch (Exception e) {
                            e.printStackTrace();
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorInt);
                            return;
                        }

                        StreamLine.serverConfig.setMOTDTime(time);

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsSetMOTDTime
                                .replace("%set%", motdtime)
                        );
                        break;
                    case "version":
                        String version = TextUtils.argsToStringMinus(args, 0, 1);

                        StreamLine.serverConfig.setVersion(version);

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsSetVersion
                                .replace("%set%", version)
                        );
                        break;
                    case "sample":
                        if (args.length < 4) {
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                            return;
                        }

                        int atS = 0;
                        try {
                            atS = Integer.parseInt(args[2]);
                        } catch (Exception e) {
                            e.printStackTrace();
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorInt);
                            return;
                        }

                        String sample = TextUtils.argsToStringMinus(args, 0, 1, 2);

                        StreamLine.serverConfig.setSample(Integer.toString(atS), sample);

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsSetSample
                                .replace("%number%", Integer.toString(atS))
                                .replace("%set%", sample)
                        );
                        break;
                    case "max-players":
                        String maxp = TextUtils.argsToStringMinus(args, 0, 1);

                        StreamLine.serverConfig.setMaxPlayers(maxp);

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsSetMaxP
                                .replace("%set%", maxp)
                        );
                        break;
                    case "online-players":
                        String onp = TextUtils.argsToStringMinus(args, 0, 1);

                        StreamLine.serverConfig.setOnlinePlayers(onp);

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsSetOnlineP
                                .replace("%set%", onp)
                        );
                        break;
                }
                break;
            case "check":
            case "get":
                if (args.length <= 1) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                    return;
                }
                switch (args[1]) {
                    case "motd":
                        if (args.length < 3) {
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                            return;
                        }

                        int at = 0;
                        try {
                            at = Integer.parseInt(args[2]);
                        } catch (Exception e) {
                            e.printStackTrace();
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorInt);
                            return;
                        }

                        String motd = StreamLine.serverConfig.getMOTDat(at);

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsGetMOTD
                                .replace("%number%", Integer.toString(at))
                                .replace("%set%", motd)
                        );
                        break;
                    case "motd-time":
                        String motdtime = Integer.toString(StreamLine.serverConfig.getMOTDTime());

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsGetMOTDTime
                                .replace("%set%", motdtime)
                        );
                        break;
                    case "version":
                        String version = StreamLine.serverConfig.getVersion();

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsGetVersion
                                .replace("%set%", version)
                        );
                        break;
                    case "sample":
                        if (args.length < 3) {
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
                            return;
                        }

                        int atS = 0;
                        try {
                            atS = Integer.parseInt(args[2]);
                        } catch (Exception e) {
                            e.printStackTrace();
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorInt);
                            return;
                        }

                        String sample = StreamLine.serverConfig.getSampleAt(atS);

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsGetSample
                                .replace("%number%", Integer.toString(atS))
                                .replace("%set%", sample)
                        );
                        break;
                    case "max-players":
                        String maxp = StreamLine.serverConfig.getMaxPlayers();

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsGetMaxP
                                .replace("%set%", maxp)
                        );
                        break;
                    case "online-players":
                        String onp = StreamLine.serverConfig.getOnlinePlayers();

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsGetOnlineP
                                .replace("%set%", onp)
                        );
                        break;
                }
                break;
        }
    }

    @Override
    public Iterable<String> onTabComplete(final CommandSender sender, final String[] args) {
        List<String> options = new ArrayList<>();

        options.add("set");
        options.add("check");
        options.add("get");

        List<String> options2 = new ArrayList<>();

        options2.add("motd");
        options2.add("motd-time");
        options2.add("version");
        options2.add("sample");
        options2.add("max-players");
        options2.add("online-players");

        if (args.length == 1) {
            final String param1 = args[0];

            return options.stream()
                    .filter(completion -> completion.startsWith(param1))
                    .collect(Collectors.toList());
        } else if (args.length == 2) {
            final String param2 = args[1];

            return options2.stream()
                    .filter(completion -> completion.startsWith(param2))
                    .collect(Collectors.toList());
        } else if (args.length == 3) {
            final String param3 = args[2];

            if (args[1].equals("motd") || args[1].equals("sample")) {
                List<String> keys = new ArrayList<>();

                for (Integer key : StreamLine.serverConfig.getComparedMOTD().keySet()) {
                    if (keys.contains(String.valueOf(key))) continue;
                    keys.add(String.valueOf(key));
                }

                for (Integer key : StreamLine.serverConfig.getComparedSample().keySet()) {
                    if (keys.contains(String.valueOf(key))) continue;
                    keys.add(String.valueOf(key));
                }

                return keys.stream()
                        .filter(completion -> completion.startsWith(param3))
                        .collect(Collectors.toList());
            }
        }

        return new ArrayList<>();
    }
}
