package net.plasmere.streamline.events;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PlayerUtils;
import net.plasmere.streamline.utils.TextUtils;
import net.plasmere.streamline.utils.UUIDFetcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class EventsHandler {
    private static List<Event> events = new ArrayList<>();

    public static List<Event> getEvents() {
        return events;
    }
    public static void addEvent(Event event){
        StreamLine.getInstance().getLogger().info("Added Event: " + event.toString());

        events.add(event);
    }
    public static void remEvent(Event event){
        events.remove(event);
    }

    public static void unloadEvents() {
        events = new ArrayList<>();
    }

    public static void runEvent(Event event, Player player){
        ProxiedPlayer p = UUIDFetcher.getPPlayer(player.uuid);

        if (p == null) return;

        switch (event.action) {
            case SEND_MESSAGE_TO:
                MessagingUtils.sendBUserMessage(p, adjust(event.actVal, player));
                return;
            case SEND_MESSAGE_AS:
                MessagingUtils.sendBUserAsMessage(p, adjust(event.actVal, player));
                return;
            case SEND_SERVER:
                p.connect(StreamLine.getInstance().getProxy().getServerInfo(adjust(event.actVal, player)));
                return;
            case KICK:
                p.disconnect(TextUtils.codedText(adjust(event.actVal, player)));
                return;
            case RUN_COMMAND_AS_OP:
                StreamLine.getInstance().getProxy().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), adjust(event.actVal, player));
                return;
            case RUN_COMMAND_AS_SELF:
                StreamLine.getInstance().getProxy().getPluginManager().dispatchCommand(p, adjust(event.actVal, player));
                return;
            default:
                StreamLine.getInstance().getLogger().severe("An event wasn't handled correctly...");
                break;
        }
    }

    public static String adjust(String actionValue, Player player){
        return actionValue
                .replace("%player%", PlayerUtils.getOffOnDisplayBungee(player))
                .replace("%player_default%", player.getName())
                .replace("%uniques%", String.valueOf(StreamLine.getInstance().getPlDir().listFiles().length))
                .replace("%time%", String.valueOf(new Date()))
                 ;
    }
}
