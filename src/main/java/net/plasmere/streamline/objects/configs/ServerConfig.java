package net.plasmere.streamline.objects.configs;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.objects.enums.ChatChannel;
import net.plasmere.streamline.objects.enums.MessageServerType;
import net.plasmere.streamline.objects.savable.users.SavableUser;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.TextUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Locale;
import java.util.Objects;
import java.util.TreeMap;
import java.util.TreeSet;

public class ServerConfig {
    private Configuration serverConfig;
    private final String setstring = "settings.yml";
    private final File scfile = new File(StreamLine.getInstance().getConfDir(), setstring);

    public ServerConfig(){
        if (! StreamLine.getInstance().getConfDir().exists()) {
            if (! ConfigUtils.scMakeDefault) return;

            if (StreamLine.getInstance().getConfDir().mkdirs()) {
                if (ConfigUtils.debug) MessagingUtils.logInfo("Made folder: " + StreamLine.getInstance().getConfDir().getName());
            }
        }

        serverConfig = loadConfig();

        MessagingUtils.logInfo("Loaded serverConfig!");
    }

    public Configuration getServerConfig() {
        reloadConfig();
        return serverConfig;
    }

    public void reloadConfig(){
        try {
            serverConfig = loadConfig();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Configuration loadConfig(){
        if (! scfile.exists()){
            try	(InputStream in = StreamLine.getInstance().getResourceAsStream(setstring)){
                Files.copy(in, scfile.toPath());
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        Configuration thing = new Configuration();

        try {
            thing = ConfigurationProvider.getProvider(YamlConfiguration.class).load(scfile); // ???
        } catch (Exception e) {
            e.printStackTrace();
        }

        return thing;
    }

    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(serverConfig, scfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TreeMap<Integer, String> getComparedMOTD() {
        try {
            return TextUtils.comparedConfiguration(serverConfig.getSection("motd"));
        } catch (Exception e) {
            e.printStackTrace();
            return new TreeMap<>();
        }
    }

    public TreeMap<Integer, String> getComparedSample() {
        try {
            return TextUtils.comparedConfiguration(serverConfig.getSection("sample"));
        } catch (Exception e) {
            e.printStackTrace();
            return new TreeMap<>();
        }
    }

    public String[] getSampleArray() {
        String[] array = new String[getComparedSample().size()];
        int i = 0;
        for (int it : getComparedSample().keySet()) {
            array[i] = getComparedSample().get(it);
            i ++;
        }

        return array;
    }

    public void setMOTD(String integer, String motd) {
        serverConfig.set("motd." + integer, motd);
        saveConfig();
        reloadConfig();
    }

    public String getMOTDat(int at) {
        reloadConfig();
        return serverConfig.getString("motd." + at);
    }

    public void setMOTDTime(int time) {
        serverConfig.set("motd-time", time);
        saveConfig();
        reloadConfig();
    }

    public int getMOTDTime() {
        reloadConfig();

        String string = serverConfig.getString("motd-time");

        if (string.contains("'") || string.contains("\"")) {
            return Integer.parseInt(string);
        }

        return serverConfig.getInt("motd-time");
    }

    public void setVersion(String version) {
        serverConfig.set("version", version);
        saveConfig();
        reloadConfig();
    }

    public String getVersion() {
        reloadConfig();
        return serverConfig.getString("version");
    }

    public void setSample(String integer, String sample) {
        serverConfig.set("sample." + integer, sample);
        saveConfig();
        reloadConfig();
    }

    public String getSampleAt(int at) {
        reloadConfig();
        return serverConfig.getString("sample." + at);
    }

    public void setMaxPlayers(String value) {
        serverConfig.set("max-players", value);
        saveConfig();
        reloadConfig();
    }

    public String getMaxPlayers() {
        reloadConfig();
        String string = serverConfig.getString("max-players");

        string = string.replace("'", "");

        try {
            return (string.equals("") ? Integer.toString(serverConfig.getInt("max-players")) : string);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public int maxPlayers() {
        String thing = getMaxPlayers();

        if (thing.equals("exact")) {
            return StreamLine.getInstance().getProxy().getPlayers().size();
        }

        if (thing.startsWith("exact")) {
            int i = Integer.parseInt(thing.substring("exact+".length()));
            if (thing.startsWith("exact+")){
                try {
                    return StreamLine.getInstance().getProxy().getPlayers().size() + i;
                } catch (Exception e) {
                    e.printStackTrace();
                    return StreamLine.getInstance().getProxy().getConfig().getPlayerLimit();
                }
            } else if (thing.startsWith("exact-")) {
                try {
                    return StreamLine.getInstance().getProxy().getPlayers().size() - i;
                } catch (Exception e) {
                    e.printStackTrace();
                    return StreamLine.getInstance().getProxy().getConfig().getPlayerLimit();
                }
            }
        }

        if (thing.startsWith("+")){
            try {
                return StreamLine.getInstance().getProxy().getConfig().getPlayerLimit() + Integer.parseInt(thing.substring(1));
            } catch (Exception e) {
                e.printStackTrace();
                return StreamLine.getInstance().getProxy().getConfig().getPlayerLimit();
            }
        } else if (thing.startsWith("-")) {
            try {
                return StreamLine.getInstance().getProxy().getConfig().getPlayerLimit() - Integer.parseInt(thing.substring(1));
            } catch (Exception e) {
                e.printStackTrace();
                return StreamLine.getInstance().getProxy().getConfig().getPlayerLimit();
            }
        } else {
            try {
                return Integer.parseInt(thing);
            } catch (Exception e) {
                e.printStackTrace();
                return StreamLine.getInstance().getProxy().getConfig().getPlayerLimit();
            }
        }
    }

    public void setOnlinePlayers(String value) {
        serverConfig.set("online-players", value);
        saveConfig();
        reloadConfig();
    }

    public String getOnlinePlayers() {
        reloadConfig();
        String string = serverConfig.getString("online-players");

        string = string.replace("'", "");

        try {
            return (string.equals("") ? Integer.toString(serverConfig.getInt("online-players")) : string);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public int onlinePlayers() {
        String thing = getOnlinePlayers();

        if (thing.equals("exact")) {
            return StreamLine.getInstance().getProxy().getPlayers().size();
        }

        if (thing.startsWith("exact")) {
            int i = Integer.parseInt(thing.substring("exactx".length()));
            if (thing.startsWith("exact+")){
                try {
                    return StreamLine.getInstance().getProxy().getPlayers().size() + i;
                } catch (Exception e) {
                    e.printStackTrace();
                    return StreamLine.getInstance().getProxy().getConfig().getPlayerLimit();
                }
            } else if (thing.startsWith("exact-")) {
                try {
                    return StreamLine.getInstance().getProxy().getPlayers().size() - i;
                } catch (Exception e) {
                    e.printStackTrace();
                    return StreamLine.getInstance().getProxy().getConfig().getPlayerLimit();
                }
            }
        }


        if (thing.startsWith("+")) {
            try {
                return StreamLine.getInstance().getProxy().getPlayers().size() + Integer.parseInt(thing.substring(1));
            } catch (Exception e) {
                e.printStackTrace();
                return StreamLine.getInstance().getProxy().getPlayers().size();
            }
        } else if (thing.startsWith("-")) {
            try {
                return StreamLine.getInstance().getProxy().getPlayers().size() - Integer.parseInt(thing.substring(1));
            } catch (Exception e) {
                e.printStackTrace();
                return StreamLine.getInstance().getProxy().getPlayers().size();
            }
        } else {
            try {
                return Integer.parseInt(thing);
            } catch (Exception e) {
                e.printStackTrace();
                return StreamLine.getInstance().getProxy().getPlayers().size();
            }
        }
    }

    public void setProxyChatEnabled(boolean bool) {
        serverConfig.set("proxy-chat.enabled", bool);
        saveConfig();
        reloadConfig();
    }

    public void toggleProxyChatEnabled() {
        setProxyChatEnabled(! getProxyChatEnabled());
    }

    public boolean getProxyChatEnabled() {
        reloadConfig();
        return serverConfig.getBoolean("proxy-chat.enabled");
    }

    public void setProxyChatConsoleEnabled(boolean bool) {
        serverConfig.set("proxy-chat.to-console", bool);
        saveConfig();
        reloadConfig();
    }

    public void toggleProxyChatConsoleEnabled() {
        setProxyChatConsoleEnabled(! getProxyChatConsoleEnabled());
    }

    public boolean getProxyChatConsoleEnabled() {
        reloadConfig();
        return serverConfig.getBoolean("proxy-chat.to-console");
    }

    public void setChatBasePerm(String set) {
        serverConfig.set("proxy-chat.base-perm", set);
        saveConfig();
        reloadConfig();
    }

    public String getChatBasePerm() {
        reloadConfig();
        return serverConfig.getString("proxy-chat.base-perm");
    }

    public Configuration getProxyChatChatsLocal(MessageServerType messageServerType) {
        reloadConfig();
        return serverConfig.getSection("proxy-chat.chats.local." + messageServerType.toString().toLowerCase(Locale.ROOT));
    }

    public Configuration getProxyChatChatsGlobal(MessageServerType messageServerType) {
        reloadConfig();
        return serverConfig.getSection("proxy-chat.chats.global." + messageServerType.toString().toLowerCase(Locale.ROOT));
    }

    public Configuration getProxyChatChatsGuild(MessageServerType messageServerType) {
        reloadConfig();
        return serverConfig.getSection("proxy-chat.chats.guild." + messageServerType.toString().toLowerCase(Locale.ROOT));
    }

    public Configuration getProxyChatChatsParty(MessageServerType messageServerType) {
        reloadConfig();
        return serverConfig.getSection("proxy-chat.chats.party." + messageServerType.toString().toLowerCase(Locale.ROOT));
    }

    public TreeMap<Integer, String> getChatsMapLocal(MessageServerType messageServerType) {
        reloadConfig();
        TreeMap<Integer, String> map = new TreeMap<>();

        if (getProxyChatChatsLocal(messageServerType) == null) return map;
        if (getProxyChatChatsLocal(messageServerType).getKeys().size() <= 0) return map;

        for (String key : getProxyChatChatsLocal(messageServerType).getKeys()) {
            int thing = 0;

            try {
                thing = Integer.parseInt(key);
            } catch (Exception e) {
                MessagingUtils.logSevere("You have an error with your proxy chats! Keys can only be integers!");
                e.printStackTrace();
                continue;
            }

            if (map.containsKey(thing)) {
                MessagingUtils.logSevere("You have an error with your proxy chats! Keys cannot be the same! Skipping...");
                continue;
            }
            map.put(thing, getProxyChatChatsLocal(messageServerType).getString(key));
        }

        return map;
    }

    public TreeMap<Integer, String> getChatsMapGlobal(MessageServerType messageServerType) {
        reloadConfig();
        TreeMap<Integer, String> map = new TreeMap<>();

        if (getProxyChatChatsGlobal(messageServerType) == null) return map;
        if (getProxyChatChatsGlobal(messageServerType).getKeys().size() <= 0) return map;

        for (String key : getProxyChatChatsGlobal(messageServerType).getKeys()) {
            int thing = 0;

            try {
                thing = Integer.parseInt(key);
            } catch (Exception e) {
                MessagingUtils.logSevere("You have an error with your proxy chats! Keys can only be integers!");
                e.printStackTrace();
                continue;
            }

            if (map.containsKey(thing)) {
                MessagingUtils.logSevere("You have an error with your proxy chats! Keys cannot be the same! Skipping...");
                continue;
            }
            map.put(thing, getProxyChatChatsGlobal(messageServerType).getString(key));
        }

        return map;
    }

    public TreeMap<Integer, String> getChatsMapGuild(MessageServerType messageServerType) {
        reloadConfig();
        TreeMap<Integer, String> map = new TreeMap<>();

        if (getProxyChatChatsGuild(messageServerType) == null) return map;
        if (getProxyChatChatsGuild(messageServerType).getKeys().size() <= 0) return map;

        for (String key : getProxyChatChatsGuild(messageServerType).getKeys()) {
            int thing = 0;

            try {
                thing = Integer.parseInt(key);
            } catch (Exception e) {
                MessagingUtils.logSevere("You have an error with your proxy chats! Keys can only be integers!");
                e.printStackTrace();
                continue;
            }

            if (map.containsKey(thing)) {
                MessagingUtils.logSevere("You have an error with your proxy chats! Keys cannot be the same! Skipping...");
                continue;
            }
            map.put(thing, getProxyChatChatsGuild(messageServerType).getString(key));
        }

        return map;
    }

    public TreeMap<Integer, String> getChatsMapParty(MessageServerType messageServerType) {
        reloadConfig();
        TreeMap<Integer, String> map = new TreeMap<>();

        if (getProxyChatChatsParty(messageServerType) == null) return map;
        if (getProxyChatChatsParty(messageServerType).getKeys().size() <= 0) return map;

        for (String key : getProxyChatChatsParty(messageServerType).getKeys()) {
            int thing = 0;

            try {
                thing = Integer.parseInt(key);
            } catch (Exception e) {
                MessagingUtils.logSevere("You have an error with your proxy chats! Keys can only be integers!");
                e.printStackTrace();
                continue;
            }

            if (map.containsKey(thing)) {
                MessagingUtils.logSevere("You have an error with your proxy chats! Keys cannot be the same! Skipping...");
                continue;
            }
            map.put(thing, getProxyChatChatsParty(messageServerType).getString(key));
        }

        return map;
    }

    public boolean hasProxyChatPermissionLocal(SavableUser user, MessageServerType messageServerType) {
        for (Integer integer : getChatsMapLocal(messageServerType).keySet()) {
            if (user.hasPermission(getChatBasePerm() + integer)) return true;
        }
        return false;
    }

    public boolean hasProxyChatPermissionGlobal(SavableUser user, MessageServerType messageServerType) {
        for (Integer integer : getChatsMapGlobal(messageServerType).keySet()) {
            if (user.hasPermission(getChatBasePerm() + integer)) return true;
        }
        return false;
    }

    public boolean hasProxyChatPermissionGuild(SavableUser user, MessageServerType messageServerType) {
        for (Integer integer : getChatsMapGuild(messageServerType).keySet()) {
            if (user.hasPermission(getChatBasePerm() + integer)) return true;
        }
        return false;
    }

    public boolean hasProxyChatPermissionParty(SavableUser user, MessageServerType messageServerType) {
        for (Integer integer : getChatsMapParty(messageServerType).keySet()) {
            if (user.hasPermission(getChatBasePerm() + integer)) return true;
        }
        return false;
    }

    public String getDefaultFormatLocal(MessageServerType messageServerType) {
        return getChatsMapLocal(messageServerType).firstEntry().getValue();
    }

    public String getDefaultFormatGlobal(MessageServerType messageServerType) {
        return getChatsMapGlobal(messageServerType).firstEntry().getValue();
    }

    public String getDefaultFormatGuild(MessageServerType messageServerType) {
        return getChatsMapGuild(messageServerType).firstEntry().getValue();
    }

    public String getDefaultFormatParty(MessageServerType messageServerType) {
        return getChatsMapParty(messageServerType).firstEntry().getValue();
    }

    public String getPermissionedProxyChatMessageLocal(SavableUser user, MessageServerType messageServerType){
        if (getChatsMapLocal(messageServerType).size() <= 0) return "&r<&d%sender_display%&r> %message%";

        if (! hasProxyChatPermissionLocal(user, messageServerType)) return getDefaultFormatLocal(messageServerType);

        String msg = "";
        int highest = getChatsMapLocal(messageServerType).lastKey();

        return findFromChatsMapLocal(user, highest, 0, messageServerType); // Allow for one extra run?
    }

    public String getPermissionedProxyChatMessageGlobal(SavableUser user, MessageServerType messageServerType){
        if (getChatsMapGlobal(messageServerType).size() <= 0) return "&r<&d%sender_display%&r> %message%";

        if (! hasProxyChatPermissionGlobal(user, messageServerType)) return getDefaultFormatGlobal(messageServerType);

        String msg = "";
        int highest = getChatsMapGlobal(messageServerType).lastKey();

        return findFromChatsMapGlobal(user, highest, 0, messageServerType); // Allow for one extra run?
    }

    public String getPermissionedProxyChatMessageGuild(SavableUser user, MessageServerType messageServerType){
        if (getChatsMapGuild(messageServerType).size() <= 0) return "&r<&d%sender_display%&r> %message%";

        if (! hasProxyChatPermissionGuild(user, messageServerType)) return getDefaultFormatGuild(messageServerType);

        String msg = "";
        int highest = getChatsMapGuild(messageServerType).lastKey();

        return findFromChatsMapGuild(user, highest, 0, messageServerType); // Allow for one extra run?
    }

    public String getPermissionedProxyChatMessageParty(SavableUser user, MessageServerType messageServerType){
        if (getChatsMapParty(messageServerType).size() <= 0) return "&r<&d%sender_display%&r> %message%";

        if (! hasProxyChatPermissionParty(user, messageServerType)) return getDefaultFormatParty(messageServerType);

        String msg = "";
        int highest = getChatsMapParty(messageServerType).lastKey();

        return findFromChatsMapParty(user, highest, 0, messageServerType); // Allow for one extra run?
    }

    public String findFromChatsMapLocal(SavableUser user, int trial, int running, MessageServerType messageServerType) {
        if (running > getChatsMapLocal(messageServerType).size()) return "&r<&d%sender_display%&r> %message%";

        running ++;

        String perm = getChatBasePerm() + trial;
        if (user.hasPermission(perm)) return getChatsMapLocal(messageServerType).get(trial);
        return findFromChatsMapLocal(user, iterateChatsMapFromHigherLocal(trial, messageServerType), running, messageServerType);
    }

    public String findFromChatsMapGlobal(SavableUser user, int trial, int running, MessageServerType messageServerType) {
        if (running > getChatsMapGlobal(messageServerType).size()) return "&r<&d%sender_display%&r> %message%";

        running ++;

        String perm = getChatBasePerm() + trial;
        if (user.hasPermission(perm)) return getChatsMapGlobal(messageServerType).get(trial);
        return findFromChatsMapGlobal(user, iterateChatsMapFromHigherGlobal(trial, messageServerType), running, messageServerType);
    }

    public String findFromChatsMapGuild(SavableUser user, int trial, int running, MessageServerType messageServerType) {
        if (running > getChatsMapGuild(messageServerType).size()) return "&r<&d%sender_display%&r> %message%";

        running ++;

        String perm = getChatBasePerm() + trial;
        if (user.hasPermission(perm)) return getChatsMapGuild(messageServerType).get(trial);
        return findFromChatsMapGuild(user, iterateChatsMapFromHigherGuild(trial, messageServerType), running, messageServerType);
    }

    public String findFromChatsMapParty(SavableUser user, int trial, int running, MessageServerType messageServerType) {
        if (running > getChatsMapParty(messageServerType).size()) return "&r<&d%sender_display%&r> %message%";

        running ++;

        String perm = getChatBasePerm() + trial;
        if (user.hasPermission(perm)) return getChatsMapParty(messageServerType).get(trial);
        return findFromChatsMapParty(user, iterateChatsMapFromHigherParty(trial, messageServerType), running, messageServerType);
    }

    public boolean hasProxyChatPermissionLocal(ProxiedPlayer player, MessageServerType messageServerType) {
        for (Integer integer : getChatsMapLocal(messageServerType).keySet()) {
            if (player.hasPermission(getChatBasePerm() + integer)) return true;
        }
        return false;
    }

    public boolean hasProxyChatPermissionGlobal(ProxiedPlayer player, MessageServerType messageServerType) {
        for (Integer integer : getChatsMapGlobal(messageServerType).keySet()) {
            if (player.hasPermission(getChatBasePerm() + integer)) return true;
        }
        return false;
    }

    public boolean hasProxyChatPermissionGuild(ProxiedPlayer player, MessageServerType messageServerType) {
        for (Integer integer : getChatsMapGuild(messageServerType).keySet()) {
            if (player.hasPermission(getChatBasePerm() + integer)) return true;
        }
        return false;
    }

    public boolean hasProxyChatPermissionParty(ProxiedPlayer player, MessageServerType messageServerType) {
        for (Integer integer : getChatsMapParty(messageServerType).keySet()) {
            if (player.hasPermission(getChatBasePerm() + integer)) return true;
        }
        return false;
    }

    public String getPermissionedProxyChatMessageLocal(ProxiedPlayer player, MessageServerType messageServerType){
        if (getChatsMapLocal(messageServerType).size() <= 0) return "&r<&d%sender_display%&r> %message%";

        if (! hasProxyChatPermissionLocal(player, messageServerType)) return getChatsMapLocal(messageServerType).firstEntry().getValue();

        String msg = "";
        int highest = getChatsMapLocal(messageServerType).lastKey();

        return findFromChatsMapLocal(player, highest, 0, messageServerType); // Allow for one extra run?
    }

    public String getPermissionedProxyChatMessageGlobal(ProxiedPlayer player, MessageServerType messageServerType){
        if (getChatsMapGlobal(messageServerType).size() <= 0) return "&r<&d%sender_display%&r> %message%";

        if (! hasProxyChatPermissionGlobal(player, messageServerType)) return getChatsMapGlobal(messageServerType).firstEntry().getValue();

        String msg = "";
        int highest = getChatsMapGlobal(messageServerType).lastKey();

        return findFromChatsMapGlobal(player, highest, 0, messageServerType); // Allow for one extra run?
    }

    public String getPermissionedProxyChatMessageGuild(ProxiedPlayer player, MessageServerType messageServerType){
        if (getChatsMapGuild(messageServerType).size() <= 0) return "&r<&d%sender_display%&r> %message%";

        if (! hasProxyChatPermissionGuild(player, messageServerType)) return getChatsMapGuild(messageServerType).firstEntry().getValue();

        String msg = "";
        int highest = getChatsMapGuild(messageServerType).lastKey();

        return findFromChatsMapGuild(player, highest, 0, messageServerType); // Allow for one extra run?
    }

    public String getPermissionedProxyChatMessageParty(ProxiedPlayer player, MessageServerType messageServerType){
        if (getChatsMapParty(messageServerType).size() <= 0) return "&r<&d%sender_display%&r> %message%";

        if (! hasProxyChatPermissionParty(player, messageServerType)) return getChatsMapParty(messageServerType).firstEntry().getValue();

        String msg = "";
        int highest = getChatsMapParty(messageServerType).lastKey();

        return findFromChatsMapParty(player, highest, 0, messageServerType); // Allow for one extra run?
    }

    public String findFromChatsMapLocal(ProxiedPlayer player, int trial, int running, MessageServerType messageServerType) {
        if (running > getChatsMapLocal(messageServerType).size()) return "&r<&d%sender_display%&r> %message%";

        running ++;

        String perm = getChatBasePerm() + trial;
        if (player.hasPermission(perm)) return getChatsMapLocal(messageServerType).get(trial);
        return findFromChatsMapLocal(player, iterateChatsMapFromHigherLocal(trial, messageServerType), running, messageServerType);
    }

    public String findFromChatsMapGlobal(ProxiedPlayer player, int trial, int running, MessageServerType messageServerType) {
        if (running > getChatsMapGlobal(messageServerType).size()) return "&r<&d%sender_display%&r> %message%";

        running ++;

        String perm = getChatBasePerm() + trial;
        if (player.hasPermission(perm)) return getChatsMapGlobal(messageServerType).get(trial);
        return findFromChatsMapGlobal(player, iterateChatsMapFromHigherGlobal(trial, messageServerType), running, messageServerType);
    }

    public String findFromChatsMapGuild(ProxiedPlayer player, int trial, int running, MessageServerType messageServerType) {
        if (running > getChatsMapGuild(messageServerType).size()) return "&r<&d%sender_display%&r> %message%";

        running ++;

        String perm = getChatBasePerm() + trial;
        if (player.hasPermission(perm)) return getChatsMapGuild(messageServerType).get(trial);
        return findFromChatsMapGuild(player, iterateChatsMapFromHigherGuild(trial, messageServerType), running, messageServerType);
    }

    public String findFromChatsMapParty(ProxiedPlayer player, int trial, int running, MessageServerType messageServerType) {
        if (running > getChatsMapParty(messageServerType).size()) return "&r<&d%sender_display%&r> %message%";

        running ++;

        String perm = getChatBasePerm() + trial;
        if (player.hasPermission(perm)) return getChatsMapParty(messageServerType).get(trial);
        return findFromChatsMapParty(player, iterateChatsMapFromHigherParty(trial, messageServerType), running, messageServerType);
    }

    public int iterateChatsMapFromHigherLocal(int fromHigher, MessageServerType messageServerType){
        return getChatsMapLocal(messageServerType).lowerKey(fromHigher);
    }

    public int iterateChatsMapFromHigherGlobal(int fromHigher, MessageServerType messageServerType){
        return getChatsMapGlobal(messageServerType).lowerKey(fromHigher);
    }

    public int iterateChatsMapFromHigherGuild(int fromHigher, MessageServerType messageServerType){
        return getChatsMapGuild(messageServerType).lowerKey(fromHigher);
    }

    public int iterateChatsMapFromHigherParty(int fromHigher, MessageServerType messageServerType){
        return getChatsMapParty(messageServerType).lowerKey(fromHigher);
    }

    public String getProxyChatChatsAt(int integer, ChatChannel chatChannel, MessageServerType messageServerType) {
        reloadConfig();
        return serverConfig.getString("proxy-chat.chats." + chatChannel.toString().toLowerCase(Locale.ROOT) + "." + messageServerType.toString().toLowerCase(Locale.ROOT) + "." + integer);
    }

    public void setProxyChatChatsAt(int integer, ChatChannel chatChannel, MessageServerType messageServerType, String set) {
        serverConfig.set("proxy-chat.chats." + chatChannel.toString().toLowerCase(Locale.ROOT) + "." + messageServerType.toString().toLowerCase(Locale.ROOT) + "." + integer, set);
        saveConfig();
        reloadConfig();
    }

    public void setTagsPingEnabled(boolean bool) {
        serverConfig.set("proxy-chat.tags.enable-ping", bool);
        saveConfig();
        reloadConfig();
    }

    public boolean getTagsPingEnabled() {
        reloadConfig();
        return serverConfig.getBoolean("proxy-chat.tags.enable-ping");
    }

    public void setTagsPrefix(String prefix) {
        serverConfig.set("proxy-chat.tags.tag-prefix", prefix);
        saveConfig();
        reloadConfig();
    }

    public String getTagsPrefix() {
        reloadConfig();
        return serverConfig.getString("proxy-chat.tags.tag-prefix");
    }

    public void setEmote(String emote, String value) {
        serverConfig.set("proxy-chat.emotes." + emote + ".emote", value);
        if (getEmotePermission(emote) == null) setEmotePermission(emote, "");
        if (Objects.equals(getEmotePermission(emote), "")) setEmotePermission(emote, "");
        saveConfig();
        reloadConfig();
    }

    public String getEmote(String emote) {
        reloadConfig();
        return serverConfig.getString("proxy-chat.emotes." + emote + ".emote");
    }

    public void setEmotePermission(String emote, String permission) {
        serverConfig.set("proxy-chat.emotes." + emote + ".permission", permission);
        saveConfig();
        reloadConfig();
    }

    public String getEmotePermission(String emote) {
        reloadConfig();
        return serverConfig.getString("proxy-chat.emotes." + emote + ".permission");
    }

    public TreeSet<String> getEmotes() {
        reloadConfig();
        return new TreeSet<>(serverConfig.getSection("proxy-chat.emotes").getKeys());
    }

    public void setAllowGlobal(boolean bool) {
        serverConfig.set("proxy-chat.allow.global", bool);
        saveConfig();
        reloadConfig();
    }

    public boolean getAllowGlobal() {
        reloadConfig();
        return serverConfig.getBoolean("proxy-chat.allow.global");
    }

    public void setAllowLocal(boolean bool) {
        serverConfig.set("proxy-chat.allow.local", bool);
        saveConfig();
        reloadConfig();
    }

    public boolean getAllowLocal() {
        reloadConfig();
        return serverConfig.getBoolean("proxy-chat.allow.local");
    }

    public void setMaintenanceMode(boolean bool) {
        serverConfig.set("maintenance-mode.enabled", bool);
        saveConfig();
        reloadConfig();
    }

    public boolean getMaintenanceMode() {
        reloadConfig();
        return serverConfig.getBoolean("maintenance-mode.enabled");
    }

    public void setObject(String pathTo, Object object) {
        serverConfig.set(pathTo, object);
        saveConfig();
        reloadConfig();
    }
}
