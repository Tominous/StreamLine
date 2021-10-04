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
        addUpdatedConfigEntry("modules.discord.proxy-chat.enabled", true);
        addUpdatedConfigEntry("modules.discord.proxy-chat.to-console", true);
        addUpdatedConfigEntry("modules.discord.proxy-chat.only-role", true);

        addUpdatedConfigEntry("modules.discord.proxy-chat.display-names.verifying.change.enabled", true);
        addUpdatedConfigEntry("modules.discord.verifying.display-names.verifying.change.to", "%player_formatted%");
        addUpdatedConfigEntry("modules.discord.verifying.display-names.verifying.change.type", "discord");

        addUpdatedConfigEntry("modules.discord.verifying.add-roles", List.of(new String[]{"put_id_here", "put_id_here"}));
        addUpdatedConfigEntry("modules.discord.verifying.remove-roles", List.of(new String[]{"put_id_here", "put_id_here"}));

        addUpdatedConfigEntry("modules.discord.proxy-chat.display-names.use", "stat");

        addUpdatedConfigEntry("modules.discord.proxy-chat.discord-data.channels.local.title", "LOCAL CHAT FOR <to_upper>%server%");
        addUpdatedConfigEntry("modules.discord.proxy-chat.discord-data.channels.local.message", "%message%");
        addUpdatedConfigEntry("modules.discord.proxy-chat.discord-data.channels.local.use-avatar", true);

        addUpdatedConfigEntry("modules.discord.proxy-chat.discord-data.channels.global.title", "GLOBAL CHAT");
        addUpdatedConfigEntry("modules.discord.proxy-chat.discord-data.channels.global.message", "%message%");
        addUpdatedConfigEntry("modules.discord.proxy-chat.discord-data.channels.global.use-avatar", true);

        addUpdatedConfigEntry("modules.discord.proxy-chat.discord-data.channels.guild.title", "GUILD CHAT FOR <to_upper>%guild_name% (OWNED BY: <to_upper>%leader_formatted%)");
        addUpdatedConfigEntry("modules.discord.proxy-chat.discord-data.channels.guild.message", "%message%");
        addUpdatedConfigEntry("modules.discord.proxy-chat.discord-data.channels.guild.use-avatar", true);

        addUpdatedConfigEntry("modules.discord.proxy-chat.discord-data.channels.party.title", "PARTY CHAT FOR <to_upper>%leader_formatted%'S PARTY");
        addUpdatedConfigEntry("modules.discord.proxy-chat.discord-data.channels.party.message", "%message%");
        addUpdatedConfigEntry("modules.discord.proxy-chat.discord-data.channels.party.use-avatar", true);

        addUpdatedConfigEntry("modules.discord.proxy-chat.console.title", "PROXY CHAT");
        addUpdatedConfigEntry("modules.discord.proxy-chat.console.message", "%message%");
        addUpdatedConfigEntry("modules.discord.proxy-chat.console.use-avatar", true);
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

        addUpdatedServerConfigEntry("proxy-chat.chats.local.discord.1", "&e[&9Discord&e] &d%sender_display%&r &7&l>> &f%message%");
        addUpdatedServerConfigEntry("proxy-chat.chats.local.discord.2", "&e[&9Discord&e] &d%sender_display%&r &7&l>> &e%message%");
        addUpdatedServerConfigEntry("proxy-chat.chats.local.discord.3", "&e[&9Discord&e] &d%sender_display%&r &7&l>> &6%message%");

        addUpdatedServerConfigEntry("proxy-chat.chats.global.discord.1", "&e(&dGLOBAL&e) &e[&9Discord&e] &d%sender_display%&r &7&l>> &f%message%");
        addUpdatedServerConfigEntry("proxy-chat.chats.global.discord.2", "&e(&dGLOBAL&e) &e[&9Discord&e] &d%sender_display%&r &7&l>> &e%message%");
        addUpdatedServerConfigEntry("proxy-chat.chats.global.discord.3", "&e(&dGLOBAL&e) &e[&9Discord&e] &d%sender_display%&r &7&l>> &6%message%");

        addUpdatedServerConfigEntry("proxy-chat.chats.guild.discord.1", "&e[&9Discord&e] &2GUILD &9&l>> &b%sender_display% &8: &f%message%");

        addUpdatedServerConfigEntry("proxy-chat.chats.party.discord.1", "&e[&9Discord&e] &3PARTY &9&l>> &b%sender_display% &8: &f%message%");
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

        addUpdatedCommandsEntry("commands.discord.verify.enabled", true);
        addUpdatedCommandsEntry("commands.discord.verify.aliases", List.of("v", "ver", "verify"));
        addUpdatedCommandsEntry("commands.discord.verify.permission", "put_id_here");

        addUpdatedCommandsEntry("commands.bungee.messaging.bverify.enabled", true);
        addUpdatedCommandsEntry("commands.bungee.messaging.bverify.base", "bverify");
        addUpdatedCommandsEntry("commands.bungee.messaging.bverify.aliases", List.of("bv", "bver"));
        addUpdatedCommandsEntry("commands.bungee.messaging.bverify.permission", "streamline.command.verify");
    }
}
