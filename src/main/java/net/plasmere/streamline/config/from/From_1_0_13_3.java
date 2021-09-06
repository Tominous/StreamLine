package net.plasmere.streamline.config.from;

import net.md_5.bungee.config.Configuration;
import net.plasmere.streamline.StreamLine;

import java.io.File;
import java.nio.file.Files;
import java.util.TreeMap;

public class From_1_0_13_3 extends From {

    public From_1_0_13_3(String language) {
        super(language);
    }

    @Override
    public String versionFrom() {
        return "1.0.13.3";
    }

    @Override
    public void setupConfigFix() {
        addUpdatedConfigEntry("modules.automatically-update-configs", true);
    }

    @Override
    public void setupTranslationsFix() {
        try {
            Files.copy((new File(StreamLine.getInstance().getDataFolder(), "messages.yml").toPath()), translationPath.toPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setupServerConfigFix() {

    }

    @Override
    public void setupDiscordBotFix() {
        findDeepKeys(c, "bot", FileType.DISCORDBOT);
        findDeepKeys(c, "discord", FileType.DISCORDBOT);
    }

    @Override
    public void setupCommandsFix() {
        findDeepKeys(c, "commands", FileType.COMMANDS);
    }
}
