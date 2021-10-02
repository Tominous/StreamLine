package net.plasmere.streamline.commands.staff.settings;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.enums.ChatChannel;
import net.plasmere.streamline.objects.enums.MessageServerType;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

                        sendSetMessageNumbered(sender, MessageConfUtils.settingsSetMOTD(), rest, at);
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

                        sendSetMessage(sender, MessageConfUtils.settingsSetMOTDTime(), motdtime);
                        break;
                    case "version":
                        String version = TextUtils.argsToStringMinus(args, 0, 1);

                        StreamLine.serverConfig.setVersion(version);

                        sendSetMessage(sender, MessageConfUtils.settingsSetVersion(), version);
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

                        sendSetMessageNumbered(sender, MessageConfUtils.settingsSetSample(), Integer.toString(atS), sample);
                        break;
                    case "max-players":
                        String maxp = TextUtils.argsToStringMinus(args, 0, 1);

                        StreamLine.serverConfig.setMaxPlayers(maxp);

                        sendSetMessage(sender, MessageConfUtils.settingsSetMaxP(), maxp);
                        break;
                    case "online-players":
                        String onp = TextUtils.argsToStringMinus(args, 0, 1);

                        StreamLine.serverConfig.setOnlinePlayers(onp);

                        sendSetMessage(sender, MessageConfUtils.settingsSetOnlineP(), onp);
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

                        sendSetMessage(sender, MessageConfUtils.settingsSetPCEnabled(), cuspBool);
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

                        sendSetMessage(sender, MessageConfUtils.settingsSetChatToConsole(), toConsoleBool);
                        break;
                    case "proxy-chat-chats-local-bungee":
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

                        String setLocals = TextUtils.argsToStringMinus(args, 0, 1, 2);
                        StreamLine.serverConfig.setProxyChatChatsAtLocal(atChat, ChatChannel.LOCAL, MessageServerType.BUNGEE, setLocals);

                        sendSetMessageNumberedPC(sender, setLocals, atChat, ChatChannel.LOCAL, MessageServerType.BUNGEE);
                        break;
                    case "proxy-chat-chats-global-bungee":
                        if (args.length < 4) {
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
                            return;
                        }

                        int atCh = 0;
                        try {
                            atCh = Integer.parseInt(args[2]);
                        } catch (Exception e) {
                            e.printStackTrace();
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorInt());
                            return;
                        }

                        String setGlobals = TextUtils.argsToStringMinus(args, 0, 1, 2);
                        StreamLine.serverConfig.setProxyChatChatsAtLocal(atCh, ChatChannel.GLOBAL, MessageServerType.BUNGEE, setGlobals);

                        sendSetMessageNumberedPC(sender, setGlobals, atCh, ChatChannel.GLOBAL, MessageServerType.BUNGEE);
                        break;
                    case "proxy-chat-base-perm":
                        String baseP = TextUtils.argsToStringMinus(args, 0, 1);

                        StreamLine.serverConfig.setChatBasePerm(baseP);

                        sendSetMessage(sender, MessageConfUtils.settingsSetPCBPerm(), baseP);
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

                        sendSetMessage(sender, MessageConfUtils.settingsSetTagsEnablePing(), enableP);
                        break;
                    case "tags-tag-prefix":
                        String tagPrefix = TextUtils.argsToStringMinus(args, 0, 1);

                        StreamLine.serverConfig.setTagsPrefix(tagPrefix);

                        sendSetMessage(sender, MessageConfUtils.settingsSetTagsTagPrefix(), tagPrefix);
                        break;
                    case "emotes":
                        if (args.length < 4) {
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
                            return;
                        }

                        String emote = args[2];
                        String toEmote = TextUtils.argsToStringMinus(args, 0, 1, 2);

                        StreamLine.serverConfig.setEmote(emote, toEmote);

                        sendSetMessageEmote(sender, MessageConfUtils.settingsSetEmotes(), emote, toEmote);
                        break;
                    case "emote-permissions":
                        if (args.length < 4) {
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
                            return;
                        }

                        String theEmote = args[2];
                        String toEPerm = TextUtils.argsToStringMinus(args, 0, 1, 2);

                        StreamLine.serverConfig.setEmotePermission(theEmote, toEPerm);

                        sendSetMessageEmote(sender, MessageConfUtils.settingsSetEmotePermissions(), theEmote, toEPerm);
                        break;
                    case "maintenance-mode-enabled":
                        String enabledMM = TextUtils.argsToStringMinus(args, 0, 1);

                        boolean enabledM = false;
                        try {
                            enabledM = Boolean.parseBoolean(enabledMM);
                        } catch (Exception e) {
                            e.printStackTrace();
                            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd());
                            return;
                        }

                        StreamLine.serverConfig.setTagsPingEnabled(enabledM);

                        sendSetMessage(sender, MessageConfUtils.settingsSetMaintenanceModeEnabled(), enabledM);
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
                        sendGetMessageNumbered(sender, MessageConfUtils.settingsGetMOTD(), StreamLine.serverConfig.getMOTDat(getInt(sender, args)), args);
                        break;
                    case "motd-time":
                        sendGetMessage(sender, MessageConfUtils.settingsGetMOTDTime(), StreamLine.serverConfig.getMOTDTime());
                        break;
                    case "version":
                        sendGetMessage(sender, MessageConfUtils.settingsGetVersion(), StreamLine.serverConfig.getVersion());
                        break;
                    case "sample":
                        sendGetMessageNumbered(sender, MessageConfUtils.settingsGetSample(), StreamLine.serverConfig.getSampleAt(getInt(sender, args)), args);
                        break;
                    case "max-players":
                        sendGetMessage(sender, MessageConfUtils.settingsGetMaxP(), StreamLine.serverConfig.getMaxPlayers());
                        break;
                    case "online-players":
                        sendGetMessage(sender, MessageConfUtils.settingsGetOnlineP(), StreamLine.serverConfig.getOnlinePlayers());
                        break;
                    case "proxy-chat-enabled":
                        sendGetMessage(sender, MessageConfUtils.settingsGetPCEnabled(), StreamLine.serverConfig.getProxyChatEnabled());
                        break;
                    case "proxy-chat-to-console":
                        sendGetMessage(sender, MessageConfUtils.settingsGetChatToConsole(), StreamLine.serverConfig.getProxyChatConsoleEnabled());
                        break;
                    case "proxy-chat-chats-local-bungee":
                        sendGetMessageNumberedPC(sender, args, ChatChannel.LOCAL, MessageServerType.BUNGEE);
                        break;
                    case "proxy-chat-chats-global-bungee":
                        sendGetMessageNumberedPC(sender, args, ChatChannel.GLOBAL, MessageServerType.BUNGEE);
                        break;
                    case "proxy-chat-chats-local-discord":
                        sendGetMessageNumberedPC(sender, args, ChatChannel.LOCAL, MessageServerType.DISCORD);
                        break;
                    case "proxy-chat-chats-global-discord":
                        sendGetMessageNumberedPC(sender, args, ChatChannel.GLOBAL, MessageServerType.DISCORD);
                        break;
                    case "proxy-chat-chats-guild-discord":
                        sendGetMessageNumberedPC(sender, args, ChatChannel.GUILD, MessageServerType.DISCORD);
                        break;
                    case "proxy-chat-base-perm":
                        sendGetMessage(sender, MessageConfUtils.settingsGetPCBPerm(), StreamLine.serverConfig.getChatBasePerm());
                        break;
                    case "tags-enable-ping":
                        sendGetMessage(sender, MessageConfUtils.settingsGetTagsEnablePing(), StreamLine.serverConfig.getTagsPingEnabled());
                        break;
                    case "tags-tag-prefix":
                        sendGetMessage(sender, MessageConfUtils.settingsGetTagsTagPrefix(), StreamLine.serverConfig.getTagsPrefix());
                        break;
                    case "emotes":
                        sendGetMessageEmote(sender, MessageConfUtils.settingsGetEmotes(), StreamLine.serverConfig.getEmote(args[2]), args);
                        break;
                    case "emote-permissions":
                        sendGetMessageEmote(sender, MessageConfUtils.settingsGetEmotePermissions(), StreamLine.serverConfig.getEmotePermission(args[2]), args);
                        break;
                    case "maintenance-mode-enabled":
                        sendGetMessage(sender, MessageConfUtils.settingsGetMaintenanceModeEnabled(), StreamLine.serverConfig.getMaintenanceMode());
                        break;
                }
                break;
        }
    }

    public int getInt(CommandSender sender, String[] args) {
        if (args.length < 3) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
            return -10000000;
        }

        int atChat = 0;
        try {
            atChat = Integer.parseInt(args[2]);
        } catch (Exception e) {
            e.printStackTrace();
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorInt());
            return -10000000;
        }

        return atChat;
    }

    public void sendGetMessage(CommandSender sender, String message, Object get) {
        MessagingUtils.sendBUserMessage(sender, message
                .replace("%set%", get.toString())
        );
    }

    public void sendSetMessage(CommandSender sender, String message, Object set) {
        MessagingUtils.sendBUserMessage(sender, message
                .replace("%set%", set.toString())
        );
    }

    public void sendGetMessageEmote(CommandSender sender, String message, String get, String[] args) {
        if (args.length < 3) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
            return;
        }

        MessagingUtils.sendBUserMessage(sender, message
                .replace("%emote%", args[2])
                .replace("%set%", get)
        );
    }

    public void sendSetMessageEmote(CommandSender sender, String message, String set, String from) {
        MessagingUtils.sendBUserMessage(sender, message
                .replace("%emote%", from)
                .replace("%set%", set)
        );
    }

    public void sendGetMessageNumbered(CommandSender sender, String message, String get, String[] args) {
        if (get.equals("")) return;

        if (args.length < 3) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
            return;
        }

        MessagingUtils.sendBUserMessage(sender, message
                .replace("%number%", args[2])
                .replace("%set%", get)
        );
    }

    public void sendGetMessageNumberedPC(CommandSender sender, String[] args, ChatChannel chatChannel, MessageServerType messageServerType) {
        sendGetMessageNumbered(sender, MessageConfUtils.settingsGetPCChats()
                        .replace("%channel%", chatChannel.toString())
                        .replace("%server_type%", messageServerType.toString())
                , StreamLine.serverConfig.getProxyChatChatsAt(getInt(sender, args), chatChannel, messageServerType), args);
    }

    public void sendSetMessageNumbered(CommandSender sender, String message, String set, Object from) {
        MessagingUtils.sendBUserMessage(sender, message
                .replace("%number%", from.toString())
                .replace("%set%", set)
        );
    }

    public void sendSetMessageNumberedPC(CommandSender sender, String set, Object from, ChatChannel chatChannel, MessageServerType messageServerType) {
        sendSetMessageNumbered(sender, MessageConfUtils.settingsSetPCChats()
                    .replace("%channel%", chatChannel.toString())
                    .replace("%server_type%", messageServerType.toString())
                , set, from);
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
        options2.add("proxy-chat-chats-local-bungee");
        options2.add("proxy-chat-chats-global-bungee");
        options2.add("proxy-chat-chats-local-discord");
        options2.add("proxy-chat-chats-global-discord");
        options2.add("proxy-chat-chats-guild-discord");
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
