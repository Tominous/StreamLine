package net.plasmere.streamline.config;

import net.plasmere.streamline.StreamLine;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.TextUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

public class ConfigHandler {
    public Configuration conf;
//    public Configuration oConf;
    public Configuration mess;
//    public Configuration oMess;
    public Configuration discordBot;
    public Configuration commands;

    public String language = "";

//    public final String configVer = "13.3";
//    public final String messagesVer = "13.3";

    //    public static final StreamLine inst = StreamLine.getInstance();
    public final String cstring = "config.yml";
    public final File cfile = new File(StreamLine.getInstance().getDataFolder(), cstring);
    public final File translationPath = new File(StreamLine.getInstance().getDataFolder() + File.separator + "translations" + File.separator);
    public final String en_USString = "en_US.yml";
    public final File en_USFile = new File(translationPath, en_USString);
    public final String fr_FRString = "fr_FR.yml";
    public final File fr_FRFile = new File(translationPath, fr_FRString);
    public final String disbotString = "discord-bot.yml";
    public final File disbotFile = new File(StreamLine.getInstance().getDataFolder(), disbotString);
    public final String commandString = "commands.yml";
    public final File commandFile = new File(StreamLine.getInstance().getDataFolder(), commandString);

    public File mfile(String language) {
        return new File(translationPath, (language.endsWith(".yml") ? language : language + ".yml"));
    }

    public ConfigHandler(String language){
        if (! StreamLine.getInstance().getDataFolder().exists()) {
            if (StreamLine.getInstance().getDataFolder().mkdirs()) {
                MessagingUtils.logInfo("Made folder: " + StreamLine.getInstance().getDataFolder().getName());
            }
        }

        this.language = language;

        conf = loadConf();
        mess = loadTrans(language);
        discordBot = loadDiscordBot();
        commands = loadCommands();
    }

    public void setLanguage(String language) throws Exception {
        if (TextUtils.equalsAny(language, acceptableTranslations())) throw new Exception("Unsupported language!");

        this.language = language;
        int localeLineNumber = 3;

        List<String> lines = Files.readAllLines(StreamLine.getInstance().languageFile.toPath(), StandardCharsets.UTF_8);
        lines.set(localeLineNumber - 1, language);
        Files.write(StreamLine.getInstance().languageFile.toPath(), lines, StandardCharsets.UTF_8);
    }

    public void reloadConfig(){
        try {
            conf = loadConf();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void reloadMessages(){
        try {
            mess = loadTrans(this.language);
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

        Configuration thing = new Configuration();

        try {
            thing = ConfigurationProvider.getProvider(YamlConfiguration.class).load(cfile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return thing;
    }

    public static TreeSet<String> acceptableTranslations() {
        TreeSet<String> trans = new TreeSet<>();

        trans.add("en_US");
        trans.add("fr_FR");

        return trans;
    }

    public Configuration loadTrans(String language) {
        if (! mfile(language).exists()){
            if (! translationPath.exists()) if (! translationPath.mkdirs()) MessagingUtils.logSevere("COULD NOT MAKE TRANSLATION FOLDER(S)!");

            try (InputStream in = StreamLine.getInstance().getResourceAsStream(en_USString)) {
                Files.copy(in, en_USFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (InputStream in = StreamLine.getInstance().getResourceAsStream(fr_FRString)) {
                Files.copy(in, fr_FRFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Configuration thing = new Configuration();

        try {
            thing = ConfigurationProvider.getProvider(YamlConfiguration.class).load(mfile(language));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thing;
    }

    public Configuration loadDiscordBot(){
        if (! disbotFile.exists()){
            try	(InputStream in = StreamLine.getInstance().getResourceAsStream(disbotString)){
                Files.copy(in, disbotFile.toPath());
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        Configuration thing = new Configuration();

        try {
            thing = ConfigurationProvider.getProvider(YamlConfiguration.class).load(disbotFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return thing;
    }

    public Configuration loadCommands(){
        if (! commandFile.exists()){
            try	(InputStream in = StreamLine.getInstance().getResourceAsStream(commandString)){
                Files.copy(in, commandFile.toPath());
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        Configuration thing = new Configuration();

        try {
            thing = ConfigurationProvider.getProvider(YamlConfiguration.class).load(commandFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return thing;
    }

    /*
    \        /\        /\        /\        /\        /\        /\        /\        /\        /\        /\        /\        /\        /\        /\        /\        /
     \      /  \      /  \      /  \      /  \      /  \      /  \      /  \      /  \      /  \      /  \      /  \      /  \      /  \      /  \      /  \      /
      \    /    \    /    \    /    \    /    \    /    \    /    \    /    \    /    \    /    \    /    \    /    \    /    \    /    \    /    \    /    \    /
       \  /      \  /      \  /      \  /      \  /      \  /      \  /      \  /      \  /      \  /      \  /      \  /      \  /      \  /      \  /      \  /
        \/        \/        \/        \/        \/        \/        \/        \/        \/        \/        \/        \/        \/        \/        \/        \/
     */

    public void saveConf(){
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(conf, cfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMess(){
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(mess, mfile(this.language));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDiscordBot(){
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(discordBot, disbotFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCommands(){
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(commands, commandFile);
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
        reloadConfig();
        return conf.get(path);
    }

    public void setObjectConf(String path, Object thing){
        conf.set(path, thing);
        reloadConfig();
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

    public String getDisBotString(String path) {
        reloadMessages();
        return discordBot.getString(path);
    }

    public boolean getDisBotBoolean(String path) {
        reloadMessages();
        return discordBot.getBoolean(path);
    }

    public int getDisBotInteger(String path) {
        reloadMessages();
        return discordBot.getInt(path);
    }

    public List<String> getDisBotStringList(String path) {
        reloadMessages();
        return discordBot.getStringList(path);
    }

    public List<Integer> getDisBotIntegerList(String path) {
        reloadMessages();
        return discordBot.getIntList(path);
    }

    public Configuration getDisBotSection(String path) {
        reloadMessages();
        return discordBot.getSection(path);
    }

    public Object getObjectDisBot(String path){
        reloadMessages();
        return discordBot.get(path);
    }

    public void setObjectDisBot(String path, Object thing){
        discordBot.set(path, thing);
        reloadMessages();
    }

    public Collection<String> getDisBotKeys() {
        reloadMessages();
        return discordBot.getKeys();
    }

    public String getCommandString(String path) {
        reloadMessages();
        return commands.getString(path);
    }

    public boolean getCommandBoolean(String path) {
        reloadMessages();
        return commands.getBoolean(path);
    }

    public int getCommandInteger(String path) {
        reloadMessages();
        return commands.getInt(path);
    }

    public List<String> getCommandStringList(String path) {
        reloadMessages();
        return commands.getStringList(path);
    }

    public List<Integer> getCommandIntegerList(String path) {
        reloadMessages();
        return commands.getIntList(path);
    }

    public Configuration getCommandSection(String path) {
        reloadMessages();
        return commands.getSection(path);
    }

    public Object getObjectCommand(String path){
        reloadMessages();
        return commands.get(path);
    }

    public void setObjectCommand(String path, Object thing){
        commands.set(path, thing);
        reloadMessages();
    }

    public Collection<String> getCommandKeys() {
        reloadMessages();
        return commands.getKeys();
    }
}
