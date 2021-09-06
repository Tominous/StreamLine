package net.plasmere.streamline.commands.staff.settings;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.TextUtils;
import org.apache.commons.collections4.list.TreeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SettingsEditCommand extends Command implements TabExecutor {
    public SettingsEditCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);

        if (ConfigUtils.debug) MessagingUtils.logInfo("Settings make base: " + base);
        if (ConfigUtils.debug) MessagingUtils.logInfo("Settings make perm: " + perm);
        if (ConfigUtils.debug) MessagingUtils.logInfo("Settings make aliases: " + Arrays.toString(aliases));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args == null) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
            return;
        }
        if (args.length <= 0) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
            return;
        }

        switch (args[0]) {
            case "set":
                if (args.length <= 2) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
                    return;
                }
                switch (args[1]) {
                    case "motd":
                        if (args.length < 4) {
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
                            return;
                        }

                        int at = 0;
                        try {
                            at = Integer.parseInt(args[2]);
                        } catch (Exception e) {
                            e.printStackTrace();
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorInt());
                            return;
                        }

                        String rest = TextUtils.argsToStringMinus(args, 0, 1, 2);

                        StreamLine.serverConfig.setMOTD(Integer.toString(at), rest);

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsSetMOTD()
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
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorInt());
                            return;
                        }

                        StreamLine.serverConfig.setMOTDTime(time);

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsSetMOTDTime()
                                .replace("%set%", motdtime)
                        );
                        break;
                    case "version":
                        String version = TextUtils.argsToStringMinus(args, 0, 1);

                        StreamLine.serverConfig.setVersion(version);

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsSetVersion()
                                .replace("%set%", version)
                        );
                        break;
                    case "sample":
                        if (args.length < 4) {
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
                            return;
                        }

                        int atS = 0;
                        try {
                            atS = Integer.parseInt(args[2]);
                        } catch (Exception e) {
                            e.printStackTrace();
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorInt());
                            return;
                        }

                        String sample = TextUtils.argsToStringMinus(args, 0, 1, 2);

                        StreamLine.serverConfig.setSample(Integer.toString(atS), sample);

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsSetSample()
                                .replace("%number%", Integer.toString(atS))
                                .replace("%set%", sample)
                        );
                        break;
                    case "max-players":
                        String maxp = TextUtils.argsToStringMinus(args, 0, 1);

                        StreamLine.serverConfig.setMaxPlayers(maxp);

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsSetMaxP()
                                .replace("%set%", maxp)
                        );
                        break;
                    case "online-players":
                        String onp = TextUtils.argsToStringMinus(args, 0, 1);

                        StreamLine.serverConfig.setOnlinePlayers(onp);

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsSetOnlineP()
                                .replace("%set%", onp)
                        );
                        break;
                    case "proxy-chat-enabled":
                        String cusp = TextUtils.argsToStringMinus(args, 0, 1);

                        boolean cuspBool = true;
                        try {
                            cuspBool = Boolean.parseBoolean(cusp);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        StreamLine.serverConfig.setProxyChatEnabled(cuspBool);

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsSetPCEnabled()
                                .replace("%set%", String.valueOf(cuspBool))
                        );
                        break;
                    case "proxy-chat-to-console":
                        String toConsole = TextUtils.argsToStringMinus(args, 0, 1);

                        boolean toConsoleBool = true;
                        try {
                            toConsoleBool = Boolean.parseBoolean(toConsole);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        StreamLine.serverConfig.setProxyChatConsoleEnabled(toConsoleBool);

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsSetChatToConsole()
                                .replace("%set%", String.valueOf(toConsoleBool))
                        );
                        break;
                    case "proxy-chat-chats":
                        if (args.length < 4) {
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
                            return;
                        }

                        int atChat = 0;
                        try {
                            atChat = Integer.parseInt(args[2]);
                        } catch (Exception e) {
                            e.printStackTrace();
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorInt());
                            return;
                        }

                        String chats = StreamLine.serverConfig.getProxyChatChatsAt(atChat);

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsSetPCChats()
                                .replace("%number%", Integer.toString(atChat))
                                .replace("%set%", chats)
                        );
                        break;
                    case "proxy-chat-base-perm":
                        String baseP = TextUtils.argsToStringMinus(args, 0, 1);

                        StreamLine.serverConfig.setChatBasePerm(baseP);

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsSetPCBPerm()
                                .replace("%set%", baseP)
                        );
                        break;
                    case "tags-enable-ping":
                        String enablePing = TextUtils.argsToStringMinus(args, 0, 1);

                        boolean enableP = true;
                        try {
                            enableP = Boolean.parseBoolean(enablePing);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        StreamLine.serverConfig.setTagsPingEnabled(enableP);

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsSetTagsEnablePing()
                                .replace("%set%", String.valueOf(enableP))
                        );
                        break;
                    case "tags-tag-prefix":
                        String tagPrefix = TextUtils.argsToStringMinus(args, 0, 1);

                        StreamLine.serverConfig.setTagsPrefix(tagPrefix);

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsSetTagsTagPrefix()
                                .replace("%set%", tagPrefix)
                        );
                        break;
                    case "emotes":
                        if (args.length < 4) {
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
                            return;
                        }

                        String emote = args[2];
                        String toEmote = TextUtils.argsToStringMinus(args, 0, 1, 2);

                        StreamLine.serverConfig.setEmote(emote, toEmote);

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsSetEmotes()
                                .replace("%emote%", emote)
                                .replace("%set%", toEmote)
                        );
                        break;
                    case "emote-permissions":
                        if (args.length < 4) {
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
                            return;
                        }

                        String theEmote = args[2];
                        String toEPerm = TextUtils.argsToStringMinus(args, 0, 1, 2);

                        StreamLine.serverConfig.setEmotePermission(theEmote, toEPerm);

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsSetEmotePermissions()
                                .replace("%emote%", theEmote)
                                .replace("%set%", toEPerm)
                        );
                        break;
                }
                break;
            case "check":
            case "get":
                if (args.length <= 1) {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
                    return;
                }
                switch (args[1]) {
                    case "motd":
                        if (args.length < 3) {
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
                            return;
                        }

                        int at = 0;
                        try {
                            at = Integer.parseInt(args[2]);
                        } catch (Exception e) {
                            e.printStackTrace();
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorInt());
                            return;
                        }

                        String motd = StreamLine.serverConfig.getMOTDat(at);

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsGetMOTD()
                                .replace("%number%", Integer.toString(at))
                                .replace("%set%", motd)
                        );
                        break;
                    case "motd-time":
                        String motdtime = Integer.toString(StreamLine.serverConfig.getMOTDTime());

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsGetMOTDTime()
                                .replace("%set%", motdtime)
                        );
                        break;
                    case "version":
                        String version = StreamLine.serverConfig.getVersion();

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsGetVersion()
                                .replace("%set%", version)
                        );
                        break;
                    case "sample":
                        if (args.length < 3) {
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
                            return;
                        }

                        int atS = 0;
                        try {
                            atS = Integer.parseInt(args[2]);
                        } catch (Exception e) {
                            e.printStackTrace();
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorInt());
                            return;
                        }

                        String sample = StreamLine.serverConfig.getSampleAt(atS);

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsGetSample()
                                .replace("%number%", Integer.toString(atS))
                                .replace("%set%", sample)
                        );
                        break;
                    case "max-players":
                        String maxp = StreamLine.serverConfig.getMaxPlayers();

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsGetMaxP()
                                .replace("%set%", maxp)
                        );
                        break;
                    case "online-players":
                        String onp = StreamLine.serverConfig.getOnlinePlayers();

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsGetOnlineP()
                                .replace("%set%", onp)
                        );
                        break;
                    case "proxy-chat-enabled":
                        Boolean cusp = StreamLine.serverConfig.getProxyChatEnabled();

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsGetPCEnabled()
                                .replace("%set%", String.valueOf(cusp))
                        );
                        break;
                    case "proxy-chat-to-console":
                        Boolean toConsole = StreamLine.serverConfig.getProxyChatConsoleEnabled();

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsGetChatToConsole()
                                .replace("%set%", String.valueOf(toConsole))
                        );
                        break;
                    case "proxy-chat-chats":
                        if (args.length < 3) {
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
                            return;
                        }

                        int atChat = 0;
                        try {
                            atChat = Integer.parseInt(args[2]);
                        } catch (Exception e) {
                            e.printStackTrace();
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorInt());
                            return;
                        }

                        String chats = StreamLine.serverConfig.getProxyChatChatsAt(atChat);

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsGetPCChats()
                                .replace("%number%", Integer.toString(atChat))
                                .replace("%set%", chats)
                        );
                        break;
                    case "proxy-chat-base-perm":
                        String baseP = StreamLine.serverConfig.getChatBasePerm();

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsGetPCBPerm()
                                .replace("%set%", baseP)
                        );
                        break;
                    case "tags-enable-ping":
                        boolean enableP = StreamLine.serverConfig.getTagsPingEnabled();

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsGetTagsEnablePing()
                                .replace("%set%", String.valueOf(enableP))
                        );
                        break;
                    case "tags-tag-prefix":
                        String tagPrefix = StreamLine.serverConfig.getTagsPrefix();

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsGetTagsTagPrefix()
                                .replace("%set%", tagPrefix)
                        );
                        break;
                    case "emotes":
                        if (args.length < 3) {
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
                            return;
                        }

                        String emote = args[2];

                        String theReturnedEmote = StreamLine.serverConfig.getEmote(emote);

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsGetEmotes()
                                .replace("%emote%", emote)
                                .replace("%set%", theReturnedEmote)
                        );
                        break;
                    case "emote-permissions":
                        if (args.length < 3) {
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
                            return;
                        }

                        String theEmote = args[2];
                        String toEPerm = StreamLine.serverConfig.getEmotePermission(theEmote);

                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.settingsGetEmotePermissions()
                                .replace("%emote%", theEmote)
                                .replace("%set%", toEPerm)
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
        options2.add("proxy-chat-enabled");
        options2.add("proxy-chat-to-console");
        options2.add("proxy-chat-chats");
        options2.add("proxy-chat-base-perm");
        options2.add("tags-enable-ping");
        options2.add("tags-tag-prefix");
        options2.add("emotes");
        options2.add("emote-permissions");

        if (args.length == 1) {
            return TextUtils.getCompletion(options, args[0]);
        } else if (args.length == 2) {
            return TextUtils.getCompletion(options2, args[1]);
        } else if (args.length == 3) {
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

                return TextUtils.getCompletion(keys, args[2]);
            }

            if (args[1].equals("emotes") || args[1].equals("emote-permissions")) {
                return TextUtils.getCompletion(StreamLine.serverConfig.getEmotes(), args[2]);
            }
        }

        return new ArrayList<>();
    }
}
