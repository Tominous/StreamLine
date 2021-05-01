package net.plasmere.streamline.config;

import net.plasmere.streamline.StreamLine;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class Config {
    private static Configuration conf;
    private static Configuration oConf;
    private static Configuration mess;
    private static Configuration oMess;
    private String configVer = "";
    private String messagesVer = "";

    private static final StreamLine inst = StreamLine.getInstance();
    private static final File cfile = new File(inst.getDataFolder(), "config.yml");
    private static final File mfile = new File(inst.getDataFolder(), "messages.yml");

    public Config(){
        if (! inst.getDataFolder().exists()) {
            if (inst.getDataFolder().mkdir()) {
                inst.getLogger().info("Made folder: " + inst.getDataFolder().getName());
            }
        }

        this.configVer = inst.getDescription().getVersion();
        this.messagesVer = inst.getDescription().getVersion();

        System.out.println("config load - start");

        conf = loadConf();
        mess = loadMess();

        System.out.println("config load - end");
    }

    public static Configuration getConf() { return conf; }
    public static Configuration getMess() { return mess; }
    public static Configuration getoConf() { return oConf; }
    public static Configuration getoMess() { return oMess; }

    public void reloadConfig(){
        try {
            conf = loadConf();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void reloadMessages(){
        try {
            mess = ConfigurationProvider.getProvider(YamlConfiguration.class).load(mfile);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Configuration loadConf(){
        if (! cfile.exists()){
            try	(InputStream in = inst.getResourceAsStream("config.yml")){
                Files.copy(in, cfile.toPath());
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        try {
            conf = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(inst.getDataFolder(), "config.yml"));
            if (! this.configVer.equals(ConfigUtils.version)){
                conf = iterateConfigs("oldconfig.yml");

                inst.getLogger().severe("----------------------------------------------------------");
                inst.getLogger().severe("YOU NEED TO UPDATE THE VALUES IN YOUR NEW CONFIG FILE AS");
                inst.getLogger().severe("YOUR OLD ONE WAS OUTDATED. I IMPORTED THE NEW ONE FOR YOU.");
                inst.getLogger().severe("----------------------------------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        inst.getLogger().info("Loaded configuration!");

        return conf;
    }

    public Configuration loadMess(){
        if (! mfile.exists()){
            try	(InputStream in = inst.getResourceAsStream("messages.yml")){
                Files.copy(in, mfile.toPath());
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        try {
            mess = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(inst.getDataFolder(), "messages.yml"));
            if (! this.messagesVer.equals(MessageConfUtils.version)){
                mess = iterateMessagesConf("oldmessages.yml");

                inst.getLogger().severe("----------------------------------------------------------");
                inst.getLogger().severe("YOU NEED TO UPDATE THE VALUES IN YOUR NEW MESSAGES FILE AS");
                inst.getLogger().severe("YOUR OLD ONE WAS OUTDATED. I IMPORTED THE NEW ONE FOR YOU.");
                inst.getLogger().severe("----------------------------------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        inst.getLogger().info("Loaded messages!");

        return mess;
    }

    private static Configuration iterateConfigs(String old) throws IOException {
        File oldfile = new File(inst.getDataFolder(), old);
        if (oldfile.exists()) {
            iterateConfigs("new" + old);
        } else {
            try (InputStream in = inst.getResourceAsStream("config.yml")) {
                Files.move(cfile.toPath(), oldfile.toPath());
                Files.copy(in, cfile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

            oConf = conf;
            conf = ConfigurationProvider.getProvider(YamlConfiguration.class).load(cfile);
        }

        return conf;
    }

    private static Configuration iterateMessagesConf(String old) throws IOException {
        File oldfile = new File(inst.getDataFolder(), old);
        if (oldfile.exists()) {
            iterateMessagesConf("new" + old);
        } else {
            try (InputStream in = inst.getResourceAsStream("messages.yml")) {
                Files.move(mfile.toPath(), oldfile.toPath());
                Files.copy(in, mfile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

            oMess = mess;
            mess = ConfigurationProvider.getProvider(YamlConfiguration.class).load(mfile);
        }

        return mess;
    }
}
