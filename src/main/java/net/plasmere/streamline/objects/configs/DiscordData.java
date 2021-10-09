package net.plasmere.streamline.objects.configs;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.objects.Guild;
import net.plasmere.streamline.objects.Party;
import net.plasmere.streamline.objects.enums.ChatChannel;
import net.plasmere.streamline.objects.enums.MessageServerType;
import net.plasmere.streamline.objects.lists.SingleSet;
import net.plasmere.streamline.objects.lists.SingleSetThree;
import net.plasmere.streamline.objects.messaging.DiscordMessage;
import net.plasmere.streamline.objects.savable.users.Player;
import net.plasmere.streamline.utils.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;

public class DiscordData {
    private Configuration conf;
    private final String fileString = "discord-data.yml";
    private final File file = new File(StreamLine.getInstance().getConfDir(), fileString);
    public TreeMap<Long, SingleSetThree<String, String, Boolean>> loadedChannels = new TreeMap<>();

    public TreeMap<String, Integer> toVerify = new TreeMap<>();

    public DiscordData(){
        if (! StreamLine.getInstance().getConfDir().exists()) {
            if (StreamLine.getInstance().getConfDir().mkdirs()) {
                if (ConfigUtils.debug) MessagingUtils.logInfo("Made folder: " + StreamLine.getInstance().getConfDir().getName());
            }
        }

        conf = loadConfig();
        loadChannels();

        MessagingUtils.logInfo("Loaded discord data!");
    }

    public Configuration getConf() {
        reloadConfig();
        return conf;
    }

    public void reloadConfig(){
        try {
            conf = loadConfig();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Configuration loadConfig(){
        if (! file.exists()){
            try	(InputStream in = StreamLine.getInstance().getResourceAsStream(fileString)){
                Files.copy(in, file.toPath());
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        Configuration thing = new Configuration();

        try {
            thing = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file); // ???
        } catch (Exception e) {
            e.printStackTrace();
        }

        return thing;
    }

    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(conf, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void purgeChannels() {
        loadedChannels = new TreeMap<>();
    }

    public void loadChannels() {
        purgeChannels();

        for (String key : conf.getSection("channels").getKeys()) {
            if (key.contains("put")) continue;

            try {
                long l = Long.parseLong(key);
                loadedChannels.put(l, getChannel(l));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    public boolean hasGlobalChannel() {
//
//    }

    public boolean ifHasChannels(ChatChannel type, String identifier) {
        TreeSet<Long> channels = getChannelsByData(type, identifier);

        if (channels == null) return false;
        if (channels.size() <= 0) return false;

        return true;
    }

    public SingleSet<Boolean, ChatChannel> ifHasChannelsAsSet(ChatChannel type, String identifier) {
        return new SingleSet<>(ifHasChannels(type, identifier), type);
    }

    public void sendDiscordChannel(CommandSender sender, ChatChannel type, String identifier, String message) {
        TreeSet<Long> channels = getChannelsByData(type, identifier);

        for (Long channel : channels) {
            if (type.equals(ChatChannel.LOCAL)) {
                MessagingUtils.sendDiscordEBMessage(
                        new DiscordMessage(
                                sender,
                                TextUtils.formatted(ConfigUtils.moduleDPCDDLocalTitle.replace("%server%", PlayerUtils.getServer(sender))),
                                ConfigUtils.moduleDPCDDLocalMessage
                                        .replace("%message%", message),
                                channel.toString()),
                        ConfigUtils.moduleDPCDDLocalUseAvatar
                );
            }

            if (type.equals(ChatChannel.GLOBAL)) {
                MessagingUtils.sendDiscordEBMessage(
                        new DiscordMessage(
                                sender,
                                TextUtils.formatted(ConfigUtils.moduleDPCDDGlobalTitle.replace("%server%", "network")),
                                ConfigUtils.moduleDPCDDGlobalMessage
                                        .replace("%message%", message),
                                channel.toString()),
                        ConfigUtils.moduleDPCDDGlobalUseAvatar
                );
            }

            if (type.equals(ChatChannel.GUILD)) {
                Guild guild = GuildUtils.getGuild(PlayerUtils.getOrGetSavableUser(sender));

                if (guild == null) return;

                MessagingUtils.sendDiscordEBMessage(
                        new DiscordMessage(
                                sender,
                                TextUtils.formatted(ConfigUtils.moduleDPCDDGuildTitle
                                        .replace("%guild_name%", guild.name)
                                        .replace("%leader_absolute%", PlayerUtils.getOrGetSavableUser(guild.leaderUUID).latestName)
                                        .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrGetSavableUser(guild.leaderUUID)))
                                        .replace("%leader_display%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrGetSavableUser(guild.leaderUUID)))
                                        .replace("%leader_formatted%", PlayerUtils.getJustDisplayDiscord(PlayerUtils.getOrGetSavableUser(guild.leaderUUID)))
                                ),
                                ConfigUtils.moduleDPCDDGuildMessage
                                        .replace("%message%", message),
                                channel.toString()),
                        ConfigUtils.moduleDPCDDGuildUseAvatar
                );
            }

            if (type.equals(ChatChannel.PARTY)) {
                Party party = PartyUtils.getParty(PlayerUtils.getOrGetSavableUser(sender).uuid);

                if (party == null) return;

                MessagingUtils.sendDiscordEBMessage(
                        new DiscordMessage(
                                sender,
                                TextUtils.formatted(ConfigUtils.moduleDPCDDPartyTitle
                                        .replace("%leader_absolute%", PlayerUtils.getOrGetSavableUser(party.leaderUUID).latestName)
                                        .replace("%leader_normal%", PlayerUtils.getOffOnRegDiscord(PlayerUtils.getOrGetSavableUser(party.leaderUUID)))
                                        .replace("%leader_display%", PlayerUtils.getOffOnDisplayDiscord(PlayerUtils.getOrGetSavableUser(party.leaderUUID)))
                                        .replace("%leader_formatted%", PlayerUtils.getJustDisplayDiscord(PlayerUtils.getOrGetSavableUser(party.leaderUUID)))
                                ),
                                ConfigUtils.moduleDPCDDPartyMessage
                                        .replace("%message%", message),
                                channel.toString()),
                        ConfigUtils.moduleDPCDDPartyUseAvatar
                );
            }
        }
    }

    public void sendBungeeChannel(long userID, long channelID, String message) {
        User user = StreamLine.getJda().getUserById(userID);

        if (user == null) return;
        if (! isChannel(channelID)) return;

        SingleSetThree<String, String, Boolean> channelData = getChannel(channelID);

        MessagingUtils.logWarning("DD > Member id : " + userID + " | Message : " + message);
        MessagingUtils.logWarning("DD > channelData : < " + channelData.key + " , " + channelData.middle + " >");

        if (! isVerified(userID)) {
            if (channelData.key.equals(ChatChannel.GLOBAL.toString())) {
                if (channelData.middle.equals("")) {
                    MessagingUtils.sendGlobalMessageFromDiscord(user.getName(), StreamLine.serverConfig.getDefaultFormatGlobal(MessageServerType.DISCORD), message);
                } else {
                    MessagingUtils.sendPermissionedMessage(channelData.middle, StreamLine.serverConfig.getDefaultFormatGlobal(MessageServerType.DISCORD)
                            .replace("%sender_display%", user.getName())
                            .replace("%message%", message));
                }
            }

            if (channelData.key.equals(ChatChannel.LOCAL.toString())) {
                ServerInfo server = StreamLine.getInstance().getProxy().getServerInfo(channelData.middle);

                if (server == null) return;

                MessagingUtils.sendServerMessageFromDiscord(user.getName(), server, StreamLine.serverConfig.getDefaultFormatLocal(MessageServerType.DISCORD), message);
            }

            if (channelData.key.equals(ChatChannel.GUILD.toString())) {
                Guild guild = GuildUtils.getGuild(channelData.middle);

                if (guild == null) return;

                GuildUtils.sendChatFromDiscord(user.getName(), guild, StreamLine.serverConfig.getDefaultFormatGuild(MessageServerType.DISCORD), message);
            }

            if (channelData.key.equals(ChatChannel.PARTY.toString())) {
                Party party = PartyUtils.getParty(channelData.middle);

                if (party == null) return;

                PartyUtils.sendChatFromDiscord(user.getName(), party, StreamLine.serverConfig.getDefaultFormatParty(MessageServerType.DISCORD), message);
            }
        } else {
            Player player = PlayerUtils.getOrCreatePlayerStatByUUID(StreamLine.discordData.getVerified(userID));

            if (player == null) {
                MessagingUtils.logWarning("Could not send bungee message for " + userID);
                return;
            }

            if (channelData.key.equals(ChatChannel.GLOBAL.toString())) {
                if (channelData.middle.equals("")) {
                    MessagingUtils.sendGlobalMessageFromDiscord(player
                            , StreamLine.serverConfig.getDefaultFormatGlobal(MessageServerType.DISCORD), message);
                } else {
                    MessagingUtils.sendPermissionedMessage(channelData.middle, StreamLine.serverConfig.getDefaultFormatGlobal(MessageServerType.DISCORD)
                            .replace("%sender_display%", PlayerUtils.getJustDisplayBungee(player))
                            .replace("%message%", message));
                }
            }

            if (channelData.key.equals(ChatChannel.LOCAL.toString())) {
                ServerInfo server = StreamLine.getInstance().getProxy().getServerInfo(channelData.middle);

                if (server == null) {
                    MessagingUtils.logWarning("Could not send bungee message for " + userID + " | Context : server == null");
                    return;
                }

                MessagingUtils.sendServerMessageFromDiscord(player, server, StreamLine.serverConfig.getDefaultFormatLocal(MessageServerType.DISCORD), message);
            }

            if (channelData.key.equals(ChatChannel.GUILD.toString())) {
                Guild guild = GuildUtils.getGuild(channelData.middle);

                if (guild == null) {
                    return;
                }

                GuildUtils.sendChatFromDiscord(player, guild, StreamLine.serverConfig.getDefaultFormatGuild(MessageServerType.DISCORD), message);
            }

            if (channelData.key.equals(ChatChannel.PARTY.toString())) {
                Party party = PartyUtils.getParty(channelData.middle);

                if (party == null) return;

                PartyUtils.sendChatFromDiscord(player, party, StreamLine.serverConfig.getDefaultFormatParty(MessageServerType.DISCORD), message);
            }
        }
    }

    public TreeSet<Long> getChannelsByData(ChatChannel type, String identifier) {
        loadChannels();

        TreeSet<Long> toReturn = new TreeSet<>();

        for (Long id : loadedChannels.keySet()) {
            SingleSetThree<String, String, Boolean> set = loadedChannels.get(id);

            if (type.equals(ChatChannel.GLOBAL)) {
                if (set.key.equals(type.toString())) toReturn.add(id);
                continue;
            }

            if (set.key.equals(type.toString()) && set.middle.equals(identifier)) toReturn.add(id);
        }

        return toReturn;
    }

    public void addVerified(long discordID, String uuid) {
        conf.set("verified." + discordID, uuid);

        saveConfig();
        reloadConfig();
    }

    public void remVerified(long discordID) {
        conf.set("verified." + discordID, null);

        saveConfig();
        reloadConfig();
    }

    public String getVerified(long discordID) {
        reloadConfig();

        return conf.getString("verified." + discordID);
    }

    public boolean isVerified(Long discordID) {
        reloadConfig();

        for (String keys : conf.getSection("verified").getKeys()) {
            if (keys.equals(discordID.toString())) return true;
        }

        return false;
    }

    public void addChannel(long channelID, String type, String identifier, boolean bypass) {
        addChannel(channelID, new SingleSet<>(type, identifier), bypass);
    }

    public void addChannel(long channelID, SingleSet<String, String> set, boolean bypass) {
        conf.set("channels." + channelID + ".type", set.key);
        conf.set("channels." + channelID + ".identifier", set.value);
        conf.set("channels." + channelID + ".bypass", bypass);
        saveConfig();
        reloadConfig();
        loadChannels();
    }

    public void remChannel(long channelID) {
        conf.set("channels." + channelID + ".type", null);
        conf.set("channels." + channelID + ".identifier", null);
        conf.set("channels." + channelID + ".bypass", null);
        conf.set("channels." + channelID, null);
        saveConfig();
        reloadConfig();
    }

    public TreeMap<Long, Boolean> ifChannelBypasses(ChatChannel type, String identifier) {
        TreeSet<Long> channels = getChannelsByData(type, identifier);
        TreeMap<Long, Boolean> toReturn = new TreeMap<>();

        for (long l : channels) {
            toReturn.put(l, ifChannelBypassesByChannelId(l));
        }

        return toReturn;
    }

    public boolean ifChannelBypassesByChannelId(long channelID) {
        return getChannel(channelID).last;
    }

    public SingleSetThree<String, String, Boolean> getChannel(long channelID) {
        reloadConfig();

        return new SingleSetThree<>(
                conf.getString("channels." + channelID + ".type"),
                conf.getString("channels." + channelID + ".identifier"),
                conf.getBoolean("channels." + channelID + ".bypass")
        );
    }

    public boolean isChannel(Long channelID) {
        reloadConfig();

        for (String keys : conf.getSection("channels").getKeys()) {
            if (keys.equals(channelID.toString())) return true;
        }

        return false;
    }

    public void setObject(String pathTo, Object object) {
        conf.set(pathTo, object);
        saveConfig();
        reloadConfig();
    }

    public int getVerification(String uuid) {
        Random RNG = new Random();
        Integer number = toVerify.get(uuid);

        if (number == null) {
            int v = RNG.nextInt(9999);
            toVerify.put(uuid, v);

            return v;
        }

        return number;
    }

    public boolean tryVerify(String uuid, int verify) {
        int number = getVerification(uuid);

        return number == verify;
    }

    public void doVerify(String uuid, long discordID, long guildID) {
        Player player = PlayerUtils.getOrGetPlayerStatByUUID(uuid);
        if (player == null) return;

        toVerify.remove(uuid);

        addVerified(discordID, uuid);
        player.setDiscordID(discordID);

        Guild guild = GuildUtils.getGuild(player.uuid);

        if (ConfigUtils.moduleDPCChangeOnVerify) {
            if (ConfigUtils.moduleDPCChangeOnVerifyType.equals("discord")) {
                StreamLine.getJda().getGuildById(guildID).getMemberById(discordID).modifyNickname(ConfigUtils.moduleDPCChangeOnVerifyTo
                        .replace("%player_absolute%", player.getName())
                        .replace("%player_normal%", PlayerUtils.getOffOnRegDiscord(player))
                        .replace("%player_display%", PlayerUtils.getOffOnDisplayDiscord(player))
                        .replace("%player_formatted%", PlayerUtils.getJustDisplayDiscord(player))
                        .replace("%player_uuid%", player.uuid)
                        .replace("%guild_uuid%", guild == null ? "" : guild.leaderUUID)
                        .replace("%guild_name%", guild == null ? "" : guild.name)
                );
            }
            if (ConfigUtils.moduleDPCChangeOnVerifyType.equals("bungee")) {
                StreamLine.getJda().getGuildById(guildID).getMemberById(discordID).modifyNickname(ConfigUtils.moduleDPCChangeOnVerifyTo
                        .replace("%player_absolute%", player.getName())
                        .replace("%player_normal%", PlayerUtils.getOffOnRegBungee(player))
                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(player))
                        .replace("%player_formatted%", PlayerUtils.getJustDisplayBungee(player))
                        .replace("%player_uuid%", player.uuid)
                        .replace("%guild_uuid%", guild == null ? "" : guild.leaderUUID)
                        .replace("%guild_name%", guild == null ? "" : guild.name)
                );
            }
        }

        try {
            for (String roleID : ConfigUtils.moduleDPCOnVerifyAdd) {
                Role role = StreamLine.getJda().getRoleById(roleID);
                if (role == null) continue;

                StreamLine.getJda().getGuildById(guildID).addRoleToMember(discordID, role);
            }

            for (String roleID : ConfigUtils.moduleDPCOnVerifyRemove) {
                Role role = StreamLine.getJda().getRoleById(roleID);
                if (role == null) continue;

                StreamLine.getJda().getGuildById(guildID).removeRoleFromMember(discordID, role);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        reloadConfig();
    }
}
