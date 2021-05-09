package net.plasmere.streamline.events;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.events.enums.Action;
import net.plasmere.streamline.events.enums.Condition;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.objects.lists.SingleSet;
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

        for (Integer i : event.actions.keySet()) {
            switch (event.actions.get(i).key) {
                case SEND_MESSAGE_TO:
                    MessagingUtils.sendBUserMessage(p, adjust(event, player, i));
                    return;
                case SEND_MESSAGE_AS:
                    MessagingUtils.sendBUserAsMessage(p, adjust(event, player, i));
                    return;
                case SEND_SERVER:
                    p.connect(StreamLine.getInstance().getProxy().getServerInfo(adjust(event, player, i)));
                    return;
                case KICK:
                    p.disconnect(TextUtils.codedText(adjust(event, player, i)));
                    return;
                case RUN_COMMAND_AS_OP:
                    StreamLine.getInstance().getProxy().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), adjust(event, player, i));
                    return;
                case RUN_COMMAND_AS_SELF:
                    StreamLine.getInstance().getProxy().getPluginManager().dispatchCommand(p, adjust(event, player, i));
                    return;
                default:
                    StreamLine.getInstance().getLogger().severe("An event wasn't handled correctly...");
                    break;
            }
        }
    }

    public static void runEvent(Event event, Player player, String context){
        ProxiedPlayer p = UUIDFetcher.getPPlayerByUUID(player.uuid);

        if (p == null) return;

        for (Integer i : event.actions.keySet()) {
            switch (event.actions.get(i).key) {
                case SEND_MESSAGE_TO:
                    MessagingUtils.sendBUserMessage(p, adjust(event, player, i, context));
                    return;
                case SEND_MESSAGE_AS:
                    MessagingUtils.sendBUserAsMessage(p, adjust(event, player, i, context));
                    return;
                case SEND_SERVER:
                    p.connect(StreamLine.getInstance().getProxy().getServerInfo(adjust(event, player, i, context)));
                    return;
                case KICK:
                    p.disconnect(TextUtils.codedText(adjust(event, player, i, context)));
                    return;
                case RUN_COMMAND_AS_OP:
                    StreamLine.getInstance().getProxy().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), adjust(event, player, i, context));
                    return;
                case RUN_COMMAND_AS_SELF:
                    StreamLine.getInstance().getProxy().getPluginManager().dispatchCommand(p, adjust(event, player, i, context));
                    return;
                default:
                    StreamLine.getInstance().getLogger().severe("An event wasn't handled correctly...");
                    break;
            }
        }
    }

    public static String adjust(Event event, Player player, int i){
        return event.actions.get(i).value
                .replace("%player%", PlayerUtils.getOffOnDisplayBungee(player))
                .replace("%player_default%", player.getName())
                .replace("%uniques%", String.valueOf(StreamLine.getInstance().getPlDir().listFiles().length))
                .replace("%time%", String.valueOf(new Date()))
                ;
    }

    public static String adjust(Event event, Player player, int i, String context){
        return event.actions.get(i).value
                .replace("%player%", PlayerUtils.getOffOnDisplayBungee(player))
                .replace("%player_default%", player.getName())
                .replace("%uniques%", String.valueOf(StreamLine.getInstance().getPlDir().listFiles().length))
                .replace("%time%", String.valueOf(new Date()))
                .replace(("%arg:" + findArgAmount(event.actions.get(i).value) + "%"), extractArg(event, context, i))
                ;
    }

    public static String getArgFromEvent(Event event, int i, int index){
        return event.conditions.get(i).value.split(" ")[index];
    }

    public static String getArgFromMsg(String msg, int index){
        return msg.split(" ")[index];
    }

    public static String extractArg(Event event, String msg, int i){
        int index = Integer.parseInt(findArgAmount(event.actions.get(i).value));
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

    public static List<Condition> softConditions(){
        List<Condition> conditions = new ArrayList<>();

        conditions.add(Condition.SERVER);
        conditions.add(Condition.NAME_EXACT);
        conditions.add(Condition.NAME_CONTAINS);
        conditions.add(Condition.NAME_STARTS_WITH);
        conditions.add(Condition.NAME_ENDS_WITH);

        return conditions;
    }

    public static boolean eventHasSoftCondition(Event event){
        for (SingleSet<Condition, String> thing : event.conditions.values()) {
            for (Condition condition : softConditions()) {
                if (thing.key.equals(condition)) return true;
            }
        }

        return false;
    }

    public static boolean checkEventConditions(Event event, Player triggerer){
        ProxiedPlayer player = UUIDFetcher.getPPlayerByUUID(triggerer.uuid);

        for (SingleSet<Condition, String> thing : event.conditions.values()) {
            switch (thing.key) {
                case SERVER:
                    return player.getServer().getInfo().getName().equals(thing.value);
                case NAME_EXACT:
                    return triggerer.latestName.equals(thing.value);
                case NAME_CONTAINS:
                    return triggerer.latestName.contains(thing.value);
                case NAME_STARTS_WITH:
                    return triggerer.latestName.startsWith(thing.value);
                case NAME_ENDS_WITH:
                    return triggerer.latestName.endsWith(thing.value);
            }
        }

        return false;
    }

    public static boolean checkEventConditions(Event event, Player triggerer, Condition hardCondition, String hardString){
        if (! eventHasSoftCondition(event)) return checkIfHasConditionWithContext(event, hardCondition, hardString);

        ProxiedPlayer player = UUIDFetcher.getPPlayerByUUID(triggerer.uuid);

        for (SingleSet<Condition, String> thing : event.conditions.values()) {
            switch (thing.key) {
                case SERVER:
                    return player.getServer().getInfo().getName().equals(thing.value) && checkIfHasConditionWithContext(event, hardCondition, hardString);
                case NAME_EXACT:
                    return triggerer.latestName.equals(thing.value) && checkIfHasConditionWithContext(event, hardCondition, hardString);
                case NAME_CONTAINS:
                    return triggerer.latestName.contains(thing.value) && checkIfHasConditionWithContext(event, hardCondition, hardString);
                case NAME_STARTS_WITH:
                    return triggerer.latestName.startsWith(thing.value) && checkIfHasConditionWithContext(event, hardCondition, hardString);
                case NAME_ENDS_WITH:
                    return triggerer.latestName.endsWith(thing.value) && checkIfHasConditionWithContext(event, hardCondition, hardString);
            }
        }

        return false;
    }

    public static boolean checkEventConditions(Event event, Player triggerer, Condition hardCondition, Iterable<String> hardString){
        if (! eventHasSoftCondition(event)) return checkIfHasConditionWithContext(event, hardCondition, hardString);

        ProxiedPlayer player = UUIDFetcher.getPPlayerByUUID(triggerer.uuid);

        for (SingleSet<Condition, String> thing : event.conditions.values()) {
            switch (thing.key) {
                case SERVER:
                    return player.getServer().getInfo().getName().equals(thing.value) && checkIfHasConditionWithContext(event, hardCondition, hardString);
                case NAME_EXACT:
                    return triggerer.latestName.equals(thing.value) && checkIfHasConditionWithContext(event, hardCondition, hardString);
                case NAME_CONTAINS:
                    return triggerer.latestName.contains(thing.value) && checkIfHasConditionWithContext(event, hardCondition, hardString);
                case NAME_STARTS_WITH:
                    return triggerer.latestName.startsWith(thing.value) && checkIfHasConditionWithContext(event, hardCondition, hardString);
                case NAME_ENDS_WITH:
                    return triggerer.latestName.endsWith(thing.value) && checkIfHasConditionWithContext(event, hardCondition, hardString);
            }
        }

        return false;
    }

    public static boolean checkIfHasCondition(Event event, Condition condition){
        for (SingleSet<Condition, String> thing : event.conditions.values()) {
            if (thing.key.equals(condition)) return true;
        }

        return false;
    }

    public static boolean checkIfHasConditionWithContext(Event event, Condition condition, String context){
        boolean conditionEqual = false;
        String value = "";

        for (SingleSet<Condition, String> thing : event.conditions.values()) {
            if (thing.key.equals(condition)) {
                conditionEqual = true;
                value = thing.value;
            }
        }

        if (conditionEqual) {
            switch (condition) {
                case JOIN:
                case LEAVE:
                case MESSAGE_EXACT:
                case COMMAND:
                    if (context.equals(value)) return true;
                    break;
                case MESSAGE_CONTAINS:
                    if (context.contains(value)) return true;
                    break;
                case MESSAGE_STARTS_WITH:
                    if (context.startsWith(value)) return true;
                    break;
                case MESSAGE_ENDS_WITH:
                    if (context.endsWith(value)) return true;
                    break;
            }
        }

        return false;
    }

    public static boolean checkIfHasConditionWithContext(Event event, Condition condition, Iterable<String> context){
        boolean conditionEqual = false;
        String value = "";

        for (SingleSet<Condition, String> thing : event.conditions.values()) {
            if (thing.key.equals(condition)) {
                conditionEqual = true;
                value = thing.value;
            }
        }

        if (conditionEqual) {
            switch (condition) {
                case JOIN:
                case LEAVE:
                case MESSAGE_EXACT:
                case COMMAND:
                    for (String c : context) {
                        if (c.equals(value)) return true;
                    }
                    break;
                case MESSAGE_CONTAINS:
                    for (String c : context) {
                        if (c.contains(value)) return true;
                    }
                    break;
                case MESSAGE_STARTS_WITH:
                    for (String c : context) {
                        if (c.startsWith(value)) return true;
                    }
                    break;
                case MESSAGE_ENDS_WITH:
                    for (String c : context) {
                        if (c.endsWith(value)) return true;
                    }
                    break;
            }
        }

        return false;
    }
}
