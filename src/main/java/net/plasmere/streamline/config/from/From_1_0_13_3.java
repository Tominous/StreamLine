package net.plasmere.streamline.config.from;

import net.plasmere.streamline.StreamLine;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;

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
        addUpdatedConfigEntry("modules.discord.enabled", true);
        addUpdatedConfigEntry("modules.automatically-update-configs", true);
        addUpdatedConfigEntry("modules.bungee.chat-history.enabled", true);
        addUpdatedConfigEntry("modules.bungee.chat-history.load-history-on-startup", false);
    }

    @Override
    public void setupLocalesFix() {
        try {
            en_USFile.delete();

            Files.copy((new File(StreamLine.getInstance().getDataFolder(), "messages.yml").toPath()), en_USFile.toPath());

            m = getFirstTranslations(this.language);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // en_US

        addUpdatedLocalesEntry("language.message", "&eYou are now using &c%locale% &eas your locale&8!", "en_US");

        addUpdatedLocalesEntry("language.invalid-locale", "&cSorry, but &6%locale% &cis an invalid locale...!", "en_US");

        addUpdatedLocalesEntry("chat-channels.local.switch", "&eJust switched to the %new_channel% &bChat Channel &efrom %old_channel% &bChat Channel&8!", "en_US");
        addUpdatedLocalesEntry("chat-channels.global.switch", "&eJust switched to the %new_channel% &bChat Channel &efrom %old_channel% &bChat Channel&8!", "en_US");
        addUpdatedLocalesEntry("chat-channels.guild.switch", "&eJust switched to the %new_channel% &bChat Channel &efrom %old_channel% &bChat Channel&8!", "en_US");
        addUpdatedLocalesEntry("chat-channels.party.switch", "&eJust switched to the %new_channel% &bChat Channel &efrom %old_channel% &bChat Channel&8!", "en_US");
        addUpdatedLocalesEntry("chat-channels.g-officer.switch", "&eJust switched to the %new_channel% &bChat Channel &efrom %old_channel% &bChat Channel&8!", "en_US");
        addUpdatedLocalesEntry("chat-channels.p-officer.switch", "&eJust switched to the %new_channel% &bChat Channel &efrom %old_channel% &bChat Channel&8!", "en_US");

        // fr_FR

        addUpdatedLocalesEntry("language.message", "&eVous utilisez maintenant &c%locale% &ecomme paramètre régional&8!", "fr_FR");

        addUpdatedLocalesEntry("language.invalid-locale", "&cPardon, mais &6%locale% &cs'agit d'un paramètre régional non valide...!", "fr_FR");

        addUpdatedLocalesEntry("chat-channels.local.switch", "&eJust switched to the %new_channel% &bChat Channel &efrom %old_channel% &bChat Channel&8!", "en_FR");
        addUpdatedLocalesEntry("chat-channels.global.switch", "&eJust switched to the %new_channel% &bChat Channel &efrom %old_channel% &bChat Channel&8!", "fr_FR");
        addUpdatedLocalesEntry("chat-channels.guild.switch", "&eJust switched to the %new_channel% &bChat Channel &efrom %old_channel% &bChat Channel&8!", "fr_FR");
        addUpdatedLocalesEntry("chat-channels.party.switch", "&eJust switched to the %new_channel% &bChat Channel &efrom %old_channel% &bChat Channel&8!", "fr_FR");
        addUpdatedLocalesEntry("chat-channels.g-officer.switch", "&eJust switched to the %new_channel% &bChat Channel &efrom %old_channel% &bChat Channel&8!", "fr_FR");
        addUpdatedLocalesEntry("chat-channels.p-officer.switch", "&eJust switched to the %new_channel% &bChat Channel &efrom %old_channel% &bChat Channel&8!", "fr_FR");
    }

    @Override
    public void setupServerConfigFix() {
        addUpdatedServerConfigEntry("proxy-chat.allow.global", true);
        addUpdatedServerConfigEntry("proxy-chat.allow.local", true);
    }

    @Override
    public void setupDiscordBotFix() {
        findDeepKeys(c, "bot", FileType.DISCORDBOT);
        findDeepKeys(c, "discord", FileType.DISCORDBOT);
    }

    @Override
    public void setupCommandsFix() {
        findDeepKeys(c, "commands", FileType.COMMANDS);

        addUpdatedCommandsEntry("commands.bungee.configs.language.enabled", true);
        addUpdatedCommandsEntry("commands.bungee.configs.language.base", "language");
        addUpdatedCommandsEntry("commands.bungee.configs.language.permission", "streamline.command.language");
        addUpdatedCommandsEntry("commands.bungee.configs.language.aliases", Arrays.asList("lang", "locale", "loc"));
    }
}
