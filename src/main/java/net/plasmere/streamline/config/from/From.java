package net.plasmere.streamline.config.from;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.objects.lists.SingleSet;
import net.plasmere.streamline.utils.MessagingUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.TreeMap;

public abstract class From {
    public enum FileType {
        CONFIG,
        TRANSLATION,
        SERVERCONFIG,
        DISCORDBOT,
        COMMANDS,
    }

    // TreeMap < order , SingleSet < locale name , SingleSet < path , stringed value > > >
    public TreeMap<Integer, SingleSet<String, SingleSet<String, Object>>> locales = new TreeMap<>();
    // TreeMap < order , SingleSet < path , stringed value > >
    public TreeMap<Integer, SingleSet<String, Object>> config = new TreeMap<>();
    public TreeMap<Integer, SingleSet<String, Object>> serverConfig = new TreeMap<>();
    public TreeMap<Integer, SingleSet<String, Object>> discordBot = new TreeMap<>();
    public TreeMap<Integer, SingleSet<String, Object>> commands = new TreeMap<>();

    public Configuration c;
    public Configuration m;
    public Configuration sc;
    public Configuration dis;
    public Configuration comm;

    //    public static final StreamLine inst = StreamLine.getInstance();
    public final String cstring = "config.yml";
    public final File cfile = new File(StreamLine.getInstance().getDataFolder(), cstring);
    public final File translationPath = new File(StreamLine.getInstance().getDataFolder() + File.separator + "translations" + File.separator);
    public final String en_USString = "en_US.yml";
    public final File en_USFile = new File(translationPath, en_USString);
    public final String fr_FRString = "fr_FR.yml";
    public final File fr_FRFile = new File(translationPath, fr_FRString);
    public final String setstring = "settings.yml";
    public final File scfile = new File(StreamLine.getInstance().getConfDir(), setstring);
    public final String disbotString = "discord-bot.yml";
    public final File disbotFile = new File(StreamLine.getInstance().getDataFolder(), disbotString);
    public final String commandString = "commands.yml";
    public final File commandFile = new File(StreamLine.getInstance().getDataFolder(), commandString);

    public String language = "";

    public File mfile(String language) {
        return new File(translationPath, (language.endsWith(".yml") ? language : language + ".yml"));
    }

    public abstract String versionFrom();

    public From(String language) {
        this.language = language;
        getAllConfigurations();

        setupConfigFix();
        setupTranslationsFix();
        setupServerConfigFix();
        setupDiscordBotFix();
        setupCommandsFix();

        applyConfig();
        applylocales();
        applyServerConfig();
        applyDiscordBot();
        applyCommands();

        MessagingUtils.logInfo("Updated your files from previous version: " + versionFrom());
    }

    public void getAllConfigurations() {
        c = getFirstConfig();
        m = getFirstTranslations(this.language);
        sc = getFirstServerConfig();
        dis = getFirstDiscordBot();
        comm = getFirstCommands();
    }

    public Configuration getFirstConfig() {
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

    public Configuration getFirstTranslations(String language) {
        if (! translationPath.exists()) if (! translationPath.mkdirs()) MessagingUtils.logSevere("COULD NOT MAKE TRANSLATION FOLDER(S)!");

        if (! en_USFile.exists()) {
            try (InputStream in = StreamLine.getInstance().getResourceAsStream(en_USString)) {
                Files.copy(in, en_USFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (! fr_FRFile.exists()) {
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

    public Configuration getFirstServerConfig() {
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

    public Configuration getFirstDiscordBot() {
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

    public Configuration getFirstCommands() {
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

    public abstract void setupConfigFix();
    public abstract void setupTranslationsFix();
    public abstract void setupServerConfigFix();
    public abstract void setupDiscordBotFix();
    public abstract void setupCommandsFix();

    public void addUpdatedConfigEntry(String path, Object object) {
        int putInt = 0;

        if (config.size() > 0) putInt = config.lastKey() + 1;

        config.put(putInt, new SingleSet<>(path, object));
    }

    public void addUpdatedLocalesEntry(String path, Object object, String locale) {
        int putInt = 0;

        if (locales.size() > 0) putInt = locales.lastKey() + 1;

        locales.put(putInt, new SingleSet<>(locale, new SingleSet<>(path, object)));
    }

    public void addUpdatedServerConfigEntry(String path, Object object) {
        int putInt = 0;

        if (serverConfig.size() > 0) putInt = serverConfig.lastKey() + 1;

        serverConfig.put(putInt, new SingleSet<>(path, object));
    }

    public void addUpdatedDiscordBotEntry(String path, Object object) {
        int putInt = 0;

        if (discordBot.size() > 0) putInt = discordBot.lastKey() + 1;

        discordBot.put(putInt, new SingleSet<>(path, object));
    }

    public void addUpdatedCommandsEntry(String path, Object object) {
        int putInt = 0;

        if (commands.size() > 0) putInt = commands.lastKey() + 1;

        commands.put(putInt, new SingleSet<>(path, object));
    }

    public int applyConfig() {
        int applied = 0;

        for (int itgr : config.keySet()) {
            c.set(config.get(itgr).key, config.get(itgr).value);
            applied ++;
        }

        if (applied > 0) {
            try {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(c, cfile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return applied;
    }

    public int applylocales() {
        int applied = 0;

        for (int itgr : locales.keySet()) {
            m.set(locales.get(itgr).key, locales.get(itgr).value);
            applied ++;
        }

        if (applied > 0) {
            try {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(m, mfile(this.language));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return applied;
    }

    public int applyServerConfig() {
        int applied = 0;

        for (int itgr : serverConfig.keySet()) {
            sc.set(serverConfig.get(itgr).key, serverConfig.get(itgr).value);
            applied ++;
        }

        if (applied > 0) {
            try {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(sc, scfile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return applied;
    }

    public int applyDiscordBot() {
        int applied = 0;

        for (int itgr : discordBot.keySet()) {
            dis.set(discordBot.get(itgr).key, discordBot.get(itgr).value);
            applied ++;
        }

        if (applied > 0) {
            try {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(dis, disbotFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return applied;
    }

    public int applyCommands() {
        int applied = 0;

        for (int itgr : commands.keySet()) {
            comm.set(commands.get(itgr).key, commands.get(itgr).value);
            applied ++;
        }

        if (applied > 0) {
            try {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(comm, commandFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return applied;
    }

    public void saveAllConfigurations() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(c, cfile);
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(m, mfile(this.language));
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(sc, scfile);
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(dis, disbotFile);
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(comm, commandFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasKeys(Configuration configuration) {
        if (configuration.getKeys().size() > 0) return true;
        else return false;
    }

    public void findDeepKeys(Configuration base, String currSearch, FileType toFileType) {
        if (hasKeys(base)) {
            boolean trial = false;

            try {
                trial = hasKeys(base.getSection(currSearch));
            } catch (Exception e) {
                // do nothing
            }

            if (trial) {
                for (String key : base.getSection(currSearch).getKeys()) {
                    findDeepKeys(base, currSearch + "." + key, toFileType);
                }
            } else {
                switch (toFileType) {
                    case CONFIG:
                        addUpdatedConfigEntry(currSearch, base.get(currSearch));
                        break;
                    case TRANSLATION:
                        addUpdatedLocalesEntry(currSearch, base.get(currSearch), "en_US");
                        break;
                    case SERVERCONFIG:
                        addUpdatedServerConfigEntry(currSearch, base.get(currSearch));
                        break;
                    case DISCORDBOT:
                        addUpdatedDiscordBotEntry(currSearch, base.get(currSearch));
                        break;
                    case COMMANDS:
                        addUpdatedCommandsEntry(currSearch, base.get(currSearch));
                        break;
                }
            }
        }
    }
}
