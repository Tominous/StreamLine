package net.plasmere.streamline.events;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.TextUtils;
import net.plasmere.streamline.utils.UUIDFetcher;

import java.util.ArrayList;
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

    public static void runEvent(Event event, Player player){
        ProxiedPlayer p = UUIDFetcher.getPPlayer(player.uuid);

        if (p == null) return;

        switch (event.action) {
            case SEND_MESSAGE_TO:
                MessagingUtils.sendBUserMessage(p, event.actVal);
                return;
            case SEND_MESSAGE_AS:
                MessagingUtils.sendBUserAsMessage(p, event.actVal);
                return;
            case SEND_SERVER:
                p.connect(StreamLine.getInstance().getProxy().getServerInfo(event.actVal));
                return;
            case KICK:
                p.disconnect(TextUtils.codedText(event.actVal));
                return;
            case RUN_COMMAND_AS_OP:
                StreamLine.getInstance().getProxy().getPluginManager().dispatchCommand(p, event.actVal);
                return;
            case RUN_COMMAND_AS_SELF:
                if (StreamLine.getInstance().getProxy().getPluginManager().isExecutableCommand(event.actVal.split(" ")[0], p)){
                    StreamLine.getInstance().getProxy().getPluginManager().dispatchCommand(p, event.actVal);
                }
                return;
            default:
                StreamLine.getInstance().getLogger().severe("An event wasn't handled correctly...");
                break;
        }
    }
}
