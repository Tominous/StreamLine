package net.plasmere.streamline.config.from;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class From_1_0_14_0 extends From {
    public From_1_0_14_0(String language) {
        super(language);
    }

    @Override
    public String versionFrom() {
        return "1.0.14.0";
    }

    @Override
    public void getCatchAll_values() {
        addCatchAll_values("put-id-here", "put_id_here");
    }

    @Override
    public void setupConfigFix() {
        addUpdatedConfigEntry("modules.discord.proxy-chat.enabled", "true");
        addUpdatedConfigEntry("modules.discord.proxy-chat.to-console", "true");
        addUpdatedConfigEntry("modules.discord.proxy-chat.only-role", "true");

        addUpdatedConfigEntry("modules.discord.proxy-chat.display-names.verifying.change.enabled", "true");
        addUpdatedConfigEntry("modules.discord.verifying.display-names.verifying.change.to", "%player_formatted%");
        addUpdatedConfigEntry("modules.discord.verifying.display-names.verifying.change.type", "discord");

        addUpdatedConfigEntry("modules.discord.verifying.add-roles", List.of(new String[]{"put_id_here", "put_id_here"}));
        addUpdatedConfigEntry("modules.discord.verifying.remove-roles", List.of(new String[]{"put_id_here", "put_id_here"}));

        addUpdatedConfigEntry("modules.discord.proxy-chat.display-names.use", "stat");
    }

    @Override
    public void setupLocalesFix() {
        addUpdatedLocalesEntry("settings.set.proxy-chat-chats", "&eJust set &cProxy Chat %channel% &efor &5%server_type% &a#%number% &eto &b\"%set%&b\"&e!", "en_US");
        addUpdatedLocalesEntry("settings.set.proxy-chat-chats-local", null, "en_US");
        addUpdatedLocalesEntry("settings.set.proxy-chat-chats-global", null, "en_US");

        addUpdatedLocalesEntry("settings.set.proxy-chat-chats", "&eJust set &cProxy Chat %channel% &efor &5%server_type% &a#%number% &eto &b\"%set%&b\"&e!", "fr_FR");
        addUpdatedLocalesEntry("settings.set.proxy-chat-chats-local", null, "fr_FR");
        addUpdatedLocalesEntry("settings.set.proxy-chat-chats-global", null, "fr_FR");


        addUpdatedLocalesEntry("settings.get.proxy-chat-chats", "&cProxy Chat %channel% &efor &5%server_type% &a#%number% &eis &b\"%set%&b\"&e!", "en_US");
        addUpdatedLocalesEntry("settings.get.proxy-chat-chats-local", null, "en_US");
        addUpdatedLocalesEntry("settings.get.proxy-chat-chats-global", null, "en_US");

        addUpdatedLocalesEntry("settings.get.proxy-chat-chats", "&cProxy Chat %channel% &efor &5%server_type% &a#%number% &eis &b\"%set%&b\"&e!", "fr_FR");
        addUpdatedLocalesEntry("settings.get.proxy-chat-chats-local", null, "fr_FR");
        addUpdatedLocalesEntry("settings.get.proxy-chat-chats-global", null, "fr_FR");
    }

    @Override
    public void setupServerConfigFix() {
        renameDeep("proxy-chat.chats.local", "proxy-chat.chats.local.bungee", FileType.SERVERCONFIG, "");
        renameDeep("proxy-chat.chats.global", "proxy-chat.chats.global.bungee", FileType.SERVERCONFIG, "");

        addUpdatedServerConfigEntry();
    }

    @Override
    public void setupDiscordBotFix() {
        addUpdatedDiscordBotEntry("discord.text-channels.proxy-chat", "put_id_here");
        addUpdatedDiscordBotEntry("discord.roles.chat", "put_id_here");
    }

    @Override
    public void setupCommandsFix() {
        addUpdatedCommandsEntry("commands.discord.channel.enabled", true);
        addUpdatedCommandsEntry("commands.discord.channel.aliases", List.of("ch", "chel", "chan", "channel", "setchannel"));
        addUpdatedCommandsEntry("commands.discord.channel.permission", "put_id_here");
    }
}
