package net.plasmere.streamline.config;

import net.plasmere.streamline.StreamLine;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;

public class Config {
    private Configuration conf;
    private Configuration oConf;
    private Configuration mess;
    private Configuration oMess;

    private final String configVer = "12";
    private final String messagesVer = "12";

    //    private static final StreamLine inst = StreamLine.getInstance();
    private static final String cstring = "config.yml";
    private static final File cfile = new File(StreamLine.getInstance().getDataFolder(), cstring);
    private static final String mstring = "messages.yml";
    private static final File mfile = new File(StreamLine.getInstance().getDataFolder(), mstring);

    public Config(){
        if (! StreamLine.getInstance().getDataFolder().exists()) {
            if (StreamLine.getInstance().getDataFolder().mkdir()) {
                StreamLine.getInstance().getLogger().info("Made folder: " + StreamLine.getInstance().getDataFolder().getName());
            }
        }

//        this.configVer = StreamLine.getInstance().getDescription().getVersion();
//        this.messagesVer = StreamLine.getInstance().getDescription().getVersion();

//        System.out.println("config load - start");

        conf = loadConf();
        StreamLine.getInstance().getLogger().info("Loaded configuration!");
        mess = loadMess();
        StreamLine.getInstance().getLogger().info("Loaded messages!");

//        System.out.println("config load - end");
    }

//    public static Configuration getConf() { return conf; }
//    public static Configuration getMess() { return mess; }
//    public static Configuration getoConf() { return oConf; }
//    public static Configuration getoMess() { return oMess; }

    public void reloadConfig(){
        try {
            conf = loadConf();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void reloadMessages(){
        try {
            mess = loadMess();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Configuration loadConf(){
        if (! cfile.exists()){
            try	(InputStream in = StreamLine.getInstance().getResourceAsStream(cstring)){
                Files.copy(in, cfile.toPath());
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        try {
            Configuration thing = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(StreamLine.getInstance().getDataFolder(), cstring));
            if (! this.configVer.equals(thing.getString("version"))){
                thing = iterateConfigs("oldconfig.yml");

                StreamLine.getInstance().getLogger().severe("----------------------------------------------------------");
                StreamLine.getInstance().getLogger().severe("YOU NEED TO UPDATE THE VALUES IN YOUR NEW CONFIG FILE AS");
                StreamLine.getInstance().getLogger().severe("YOUR OLD ONE WAS OUTDATED. I IMPORTED THE NEW ONE FOR YOU.");
                StreamLine.getInstance().getLogger().severe("----------------------------------------------------------");
            }
            return thing;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Configuration loadMess(){
        if (! mfile.exists()){
            try	(InputStream in = StreamLine.getInstance().getResourceAsStream(mstring)){
                Files.copy(in, mfile.toPath());
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        try {
            Configuration thing = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(StreamLine.getInstance().getDataFolder(), mstring));
            if (! this.messagesVer.equals(thing.getString("version"))){
                thing = iterateMessagesConf("oldmessages.yml");

                StreamLine.getInstance().getLogger().severe("----------------------------------------------------------");
                StreamLine.getInstance().getLogger().severe("YOU NEED TO UPDATE THE VALUES IN YOUR NEW MESSAGES FILE AS");
                StreamLine.getInstance().getLogger().severe("YOUR OLD ONE WAS OUTDATED. I IMPORTED THE NEW ONE FOR YOU.");
                StreamLine.getInstance().getLogger().severe("----------------------------------------------------------");
            }
            
            return thing;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        //StreamLine.getInstance().getLogger().info("Loaded messages!");

    }

    private Configuration iterateConfigs(String old) throws IOException {
        File oldfile = new File(StreamLine.getInstance().getDataFolder(), old);
        if (oldfile.exists()) {
            return iterateConfigs("new" + old);
        } else {
            try (InputStream in = StreamLine.getInstance().getResourceAsStream(cstring)) {
                Files.move(cfile.toPath(), oldfile.toPath());
                Files.copy(in, cfile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

            oConf = conf;
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(cfile);
        }
    }

    private Configuration iterateMessagesConf(String old) throws IOException {
        File oldfile = new File(StreamLine.getInstance().getDataFolder(), old);
        if (oldfile.exists()) {
            return iterateMessagesConf("new" + old);
        } else {
            try (InputStream in = StreamLine.getInstance().getResourceAsStream(mstring)) {
                Files.move(mfile.toPath(), oldfile.toPath());
                Files.copy(in, mfile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

            oMess = mess;
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(mfile);
        }
    }

    public void saveConf(){
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(conf, cfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMess(){
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(mess, mfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getConfString(String path) {
        reloadConfig();
        return conf.getString(path);
    }

    public boolean getConfBoolean(String path) {
        reloadConfig();
        return conf.getBoolean(path);
    }

    public int getConfInteger(String path) {
        reloadConfig();
        return conf.getInt(path);
    }

    public List<String> getConfStringList(String path) {
        reloadConfig();
        return conf.getStringList(path);
    }

    public List<Integer> getConfIntegerList(String path) {
        reloadConfig();
        return conf.getIntList(path);
    }

    public Configuration getConfSection(String path) {
        reloadConfig();
        return conf.getSection(path);
    }

    public Object getObjectConf(String path){
        reloadMessages();
        return conf.get(path);
    }

    public void setObjectConf(String path, Object thing){
        conf.set(path, thing);
        reloadMessages();
    }

    public Collection<String> getConfKeys() {
        reloadConfig();
        return conf.getKeys();
    }

    public String getMessString(String path) {
        reloadMessages();
        return mess.getString(path);
    }

    public boolean getMessBoolean(String path) {
        reloadMessages();
        return mess.getBoolean(path);
    }

    public int getMessInteger(String path) {
        reloadMessages();
        return mess.getInt(path);
    }

    public List<String> getMessStringList(String path) {
        reloadMessages();
        return mess.getStringList(path);
    }

    public List<Integer> getMessIntegerList(String path) {
        reloadMessages();
        return conf.getIntList(path);
    }

    public Configuration getMessSection(String path) {
        reloadMessages();
        return mess.getSection(path);
    }

    public Object getObjectMess(String path){
        reloadMessages();
        return mess.get(path);
    }

    public void setObjectMess(String path, Object thing){
        mess.set(path, thing);
        reloadMessages();
    }

    public Collection<String> getMessKeys() {
        reloadMessages();
        return mess.getKeys();
    }
}
