package net.plasmere.streamline.objects.configs;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.objects.lists.SingleSet;
import net.plasmere.streamline.utils.MessagingUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class DiscordData {
    private Configuration conf;
    private final String fileString = "discord-data.yml";
    private final File file = new File(StreamLine.getInstance().getConfDir(), fileString);

    public DiscordData(){
        if (! StreamLine.getInstance().getConfDir().exists()) {
            if (! ConfigUtils.scMakeDefault) return;

            if (StreamLine.getInstance().getConfDir().mkdirs()) {
                MessagingUtils.logInfo("Made folder: " + StreamLine.getInstance().getConfDir().getName());
            }
        }

        conf = loadConfig();

        MessagingUtils.logInfo("Loaded conf!");
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

    public void addChannel(long channelID, String type, String identifier) {
        addChannel(channelID, new SingleSet<>(type, identifier));
    }

    public void addChannel(long channelID, SingleSet<String, String> set) {
        conf.set("channels." + channelID + ".type", set.key);
        conf.set("channels." + channelID + ".identifier", set.value);
        saveConfig();
        reloadConfig();
    }

    public void remChannel(long channelID) {
        conf.set("channels." + channelID + ".type", null);
        conf.set("channels." + channelID + ".identifier", null);
        saveConfig();
        reloadConfig();
    }

    public SingleSet<String, String> getChannel(long channelID) {
        reloadConfig();

        return new SingleSet<>(conf.getString("channels." + channelID + ".type"), conf.getString("channels." + channelID + ".identifier"));
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
}
