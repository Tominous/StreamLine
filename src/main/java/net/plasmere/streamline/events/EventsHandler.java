package net.plasmere.streamline.events;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PlayerUtils;
import net.plasmere.streamline.utils.TextUtils;
import net.plasmere.streamline.utils.UUIDFetcher;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventsHandler {
    private static List<Event> events = new ArrayList<>();

    public static List<Event> getEvents() {
        return events;
    }
    public static void addEvent(Event event){
        //StreamLine.getInstance().getLogger().info("Added Event: " + event.toString());

        if (! ConfigUtils.tagsEvents) return;

        events.add(event);
    }
    public static void remEvent(Event event){
        events.remove(event);
    }

    public static void unloadEvents() {
        events = new ArrayList<>();
    }

    public static void runEvent(Event event, Player player){
        ProxiedPlayer p = UUIDFetcher.getPPlayerByUUID(player.uuid);

        if (p == null) return;

        switch (event.action) {
            case SEND_MESSAGE_TO:
                MessagingUtils.sendBUserMessage(p, adjust(event, player));
                return;
            case SEND_MESSAGE_AS:
                MessagingUtils.sendBUserAsMessage(p, adjust(event, player));
                return;
            case SEND_SERVER:
                p.connect(StreamLine.getInstance().getProxy().getServerInfo(adjust(event, player)));
                return;
            case KICK:
                p.disconnect(TextUtils.codedText(adjust(event, player)));
                return;
            case RUN_COMMAND_AS_OP:
                StreamLine.getInstance().getProxy().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), adjust(event, player));
                return;
            case RUN_COMMAND_AS_SELF:
                StreamLine.getInstance().getProxy().getPluginManager().dispatchCommand(p, adjust(event, player));
                return;
            default:
                StreamLine.getInstance().getLogger().severe("An event wasn't handled correctly...");
                break;
        }
    }

    public static void runEvent(Event event, Player player, String context){
        ProxiedPlayer p = UUIDFetcher.getPPlayerByUUID(player.uuid);

        if (p == null) return;

        switch (event.action) {
            case SEND_MESSAGE_TO:
                MessagingUtils.sendBUserMessage(p, adjust(event, player, context));
                return;
            case SEND_MESSAGE_AS:
                MessagingUtils.sendBUserAsMessage(p, adjust(event, player, context));
                return;
            case SEND_SERVER:
                p.connect(StreamLine.getInstance().getProxy().getServerInfo(adjust(event, player, context)));
                return;
            case KICK:
                p.disconnect(TextUtils.codedText(adjust(event, player, context)));
                return;
            case RUN_COMMAND_AS_OP:
                StreamLine.getInstance().getProxy().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), adjust(event, player, context));
                return;
            case RUN_COMMAND_AS_SELF:
                StreamLine.getInstance().getProxy().getPluginManager().dispatchCommand(p, adjust(event, player, context));
                return;
            default:
                StreamLine.getInstance().getLogger().severe("An event wasn't handled correctly...");
                break;
        }
    }

    public static String adjust(Event event, Player player){
        return event.actVal
                .replace("%player%", PlayerUtils.getOffOnDisplayBungee(player))
                .replace("%player_default%", player.getName())
                .replace("%uniques%", String.valueOf(StreamLine.getInstance().getPlDir().listFiles().length))
                .replace("%time%", String.valueOf(new Date()))
                ;
    }

    public static String adjust(Event event, Player player, String context){
        return event.actVal
                .replace("%player%", PlayerUtils.getOffOnDisplayBungee(player))
                .replace("%player_default%", player.getName())
                .replace("%uniques%", String.valueOf(StreamLine.getInstance().getPlDir().listFiles().length))
                .replace("%time%", String.valueOf(new Date()))
                .replace(("%arg:" + findArgAmount(event.actVal) + "%"), extractArg(event, context))
                ;
    }

    public static String getArgFromEvent(Event event, int index){
        return event.conVal.split(" ")[index];
    }

    public static String getArgFromMsg(String msg, int index){
        return msg.split(" ")[index];
    }

    public static String extractArg(Event event, String msg){
        int index = Integer.parseInt(findArgAmount(event.actVal));
        return getArgFromMsg(msg, index);
    }

    public static String findArgAmount(String actionValue){
        try {
            String[] split = actionValue.split(" ");
            for (String s : split) {
                if (! s.contains("%arg:")) continue;

                String[] find = s.split("arg:");

                return find[1].substring(0, find[1].indexOf("%"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }

        return "0";
    }

    public static boolean checkTags(Event event, Player check){
        int success = 0;

        for (String tag : event.tags){
            for (String t : check.tags){
                if (tag.equals(t)) {
                    success++;
                }
            }
        }

        return success >= event.tags.size();
    }
}
