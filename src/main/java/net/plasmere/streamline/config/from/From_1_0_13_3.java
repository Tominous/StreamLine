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

        rename("settings.set.proxy-chat-chats", "settings.set.proxy-chat-chats-local",  FileType.TRANSLATION,"en_US");
        addUpdatedLocalesEntry("settings.set.proxy-chat-chats-global", "&eJust set &cProxy Chat Global &a#%number% &eto &b\"%set%&b\"&e!", "en_US");
        rename("settings.get.proxy-chat-chats", "settings.get.proxy-chat-chats-local",  FileType.TRANSLATION,"en_US");
        addUpdatedLocalesEntry("settings.get.proxy-chat-chats-global", "&cProxy Chat Global &a#%number% &eis &b\"%set%&b\"&e!", "en_US");

        // fr_FR

        addUpdatedLocalesEntry("language.message", "&eVous utilisez maintenant &c%locale% &ecomme paramètre régional&8!", "fr_FR");

        addUpdatedLocalesEntry("language.invalid-locale", "&cDésolé, mais &6%locale% &cest un paramètre régional non valide...!", "fr_FR");

        addUpdatedLocalesEntry("chat-channels.local.switch", "&eChangement du &bCanal Textuel &ede %old_channel% à &e%new_channel%l&8!", "fr_FR");
        addUpdatedLocalesEntry("chat-channels.global.switch", "&eChangement du &bCanal Textuel &ede %old_channel% à &e%new_channel%l&8!", "fr_FR");
        addUpdatedLocalesEntry("chat-channels.guild.switch", "&eChangement du &bCanal Textuel &ede %old_channel% à &e%new_channel%l&8!", "fr_FR");
        addUpdatedLocalesEntry("chat-channels.party.switch", "&eChangement du &bCanal Textuel &ede %old_channel% à &e%new_channel%l&8!", "fr_FR");
        addUpdatedLocalesEntry("chat-channels.g-officer.switch", "&eChangement du &bCanal Textuel &ede %old_channel% à &e%new_channel%l&8!", "fr_FR");
        addUpdatedLocalesEntry("chat-channels.p-officer.switch", "&eChangement du &bCanal Textuel &ede %old_channel% à &e%new_channel%l&8!", "fr_FR");

        rename("settings.set.proxy-chat-chats", "settings.set.proxy-chat-chats-local",  FileType.TRANSLATION,"fr_FR");
        addUpdatedLocalesEntry("settings.set.proxy-chat-chats-global", "&eJust set &cProxy Chat Global &a#%number% &eto &b\"%set%&b\"&e!", "fr_FR");
        rename("settings.get.proxy-chat-chats", "settings.get.proxy-chat-chats-local",  FileType.TRANSLATION,"fr_FR");
        addUpdatedLocalesEntry("settings.get.proxy-chat-chats-global", "&cProxy Chat Global &a#%number% &eis &b\"%set%&b\"&e!", "fr_FR");
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
