package net.plasmere.streamline.events;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
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

        if (! ConfigUtils.events) return;

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
                    continue;
                case RUN_COMMAND_AS_SELF:
                case SEND_MESSAGE_AS:
//                    MessagingUtils.sendBUserAsMessage(p, adjust(event, player, i, context));
                    StreamLine.getInstance().getProxy().getPluginManager().dispatchCommand(p, adjust(event, player, i));
                    continue;
                case SEND_SERVER:
                    p.connect(StreamLine.getInstance().getProxy().getServerInfo(adjust(event, player, i)));
                    continue;
                case KICK:
                    p.disconnect(TextUtils.codedText(adjust(event, player, i)));
                    continue;
                case RUN_COMMAND_AS_OP:
                    StreamLine.getInstance().getProxy().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), adjust(event, player, i));
                    continue;
//                case RUN_COMMAND_AS_SELF:
//                    StreamLine.getInstance().getProxy().getPluginManager().dispatchCommand(p, adjust(event, player, i, context));
//                    continue;
                case GIVE_POINTS:
                    player.addPoints(Integer.parseInt(event.actions.get(i).value));
                    continue;
                case TAKE_POINTS:
                    player.remPoints(Integer.parseInt(event.actions.get(i).value));
                    continue;
                case SET_POINTS:
                    player.setPoints(Integer.parseInt(event.actions.get(i).value));
                    continue;
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
            if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info("EventsHandler#runEvent() --> i = " + i);

            switch (event.actions.get(i).key) {
                case SEND_MESSAGE_TO:
                    MessagingUtils.sendBUserMessage(p, adjust(event, player, i, context));
                    continue;
                case RUN_COMMAND_AS_SELF:
                case SEND_MESSAGE_AS:
//                    MessagingUtils.sendBUserAsMessage(p, adjust(event, player, i, context));
                    StreamLine.getInstance().getProxy().getPluginManager().dispatchCommand(p, adjust(event, player, i, context));
                    continue;
                case SEND_SERVER:
                    p.connect(StreamLine.getInstance().getProxy().getServerInfo(adjust(event, player, i, context)));
                    continue;
                case KICK:
                    p.disconnect(TextUtils.codedText(adjust(event, player, i, context)));
                    continue;
                case RUN_COMMAND_AS_OP:
                    StreamLine.getInstance().getProxy().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), adjust(event, player, i, context));
                    continue;
//                case RUN_COMMAND_AS_SELF:
//                    StreamLine.getInstance().getProxy().getPluginManager().dispatchCommand(p, adjust(event, player, i, context));
//                    continue;
                case GIVE_POINTS:
                    player.addPoints(Integer.parseInt(event.actions.get(i).value));
                    continue;
                case TAKE_POINTS:
                    player.remPoints(Integer.parseInt(event.actions.get(i).value));
                    continue;
                case SET_POINTS:
                    player.setPoints(Integer.parseInt(event.actions.get(i).value));
                    continue;
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

        conditions.add(Condition.IN_SERVER);
        conditions.add(Condition.NAME_EXACT);
        conditions.add(Condition.NAME_CONTAINS);
        conditions.add(Condition.NAME_STARTS_WITH);
        conditions.add(Condition.NAME_ENDS_WITH);
        conditions.add(Condition.HAS_POINTS_EXACT);
        conditions.add(Condition.HAS_POINTS_LESS_THAN);
        conditions.add(Condition.HAS_POINTS_LESS_THAN_EQUAL);
        conditions.add(Condition.HAS_POINTS_GREATER_THAN);
        conditions.add(Condition.HAS_POINTS_GREATER_THAN_EQUAL);

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
                case IN_SERVER:
                    try {
                        if (player == null) {
                            if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info("EventsHandler#checkEventConditions$1 : case IN_SERVER : player == null");
                            return false;
                        }
                        ServerInfo server = player.getServer().getInfo();
                        if (server == null) {
                            if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info("EventsHandler#checkEventConditions$1 : case IN_SERVER : server == null");
                            return false;
                        }

                        if (! checkWildCards(server.getName(), thing.value, WildCardCheck.EQUALS)) return false;
                    } catch (Exception e) {
                        return false;
                    }
                    break;
                case NAME_EXACT:
                    if (! checkWildCards(triggerer.latestName, thing.value, WildCardCheck.EQUALS)) return false;
                    break;
                case NAME_CONTAINS:
                    if (! checkWildCards(triggerer.latestName, thing.value, WildCardCheck.CONTAINS)) return false;
                    break;
                case NAME_STARTS_WITH:
                    if (! checkWildCards(triggerer.latestName, thing.value, WildCardCheck.STARTS_WITH)) return false;
                    break;
                case NAME_ENDS_WITH:
                    if (! checkWildCards(triggerer.latestName, thing.value, WildCardCheck.ENDS_WITH)) return false;
                    break;
                case HAS_POINTS_EXACT:
                    if (triggerer.points != Integer.parseInt(thing.value)) return false;
                    break;
                case HAS_POINTS_LESS_THAN:
                    if (! (triggerer.points < Integer.parseInt(thing.value))) return false;
                    break;
                case HAS_POINTS_LESS_THAN_EQUAL:
                    if (! (triggerer.points <= Integer.parseInt(thing.value))) return false;
                    break;
                case HAS_POINTS_GREATER_THAN:
                    if (! (triggerer.points > Integer.parseInt(thing.value))) return false;
                    break;
                case HAS_POINTS_GREATER_THAN_EQUAL:
                    if (! (triggerer.points >= Integer.parseInt(thing.value))) return false;
                    break;
            }
        }

        return true;
    }

    public static boolean checkEventConditions(Event event, Player triggerer, Condition hardCondition, String hardString){
        if (! eventHasSoftCondition(event)) return checkIfHasConditionWithContext(event, hardCondition, hardString);

        ProxiedPlayer player = UUIDFetcher.getPPlayerByUUID(triggerer.uuid);

        for (SingleSet<Condition, String> thing : event.conditions.values()) {
            switch (thing.key) {
                case IN_SERVER:
                    try {
                        if (player == null) {
                            if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info("EventsHandler#checkEventConditions$2 : case IN_SERVER : player == null");
                            return false;
                        }
                        ServerInfo server = player.getServer().getInfo();
                        if (server == null) {
                            if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info("EventsHandler#checkEventConditions$2 : case IN_SERVER : server == null");
                            return false;
                        }

                        if (! checkWildCards(server.getName(), thing.value, WildCardCheck.EQUALS)) return false;
                    } catch (Exception e) {
                        return false;
                    }
                    break;
                case NAME_EXACT:
                    if (! checkWildCards(triggerer.latestName, thing.value, WildCardCheck.EQUALS)) return false;
                    break;
                case NAME_CONTAINS:
                    if (! checkWildCards(triggerer.latestName, thing.value, WildCardCheck.CONTAINS)) return false;
                    break;
                case NAME_STARTS_WITH:
                    if (! checkWildCards(triggerer.latestName, thing.value, WildCardCheck.STARTS_WITH)) return false;
                    break;
                case NAME_ENDS_WITH:
                    if (! checkWildCards(triggerer.latestName, thing.value, WildCardCheck.ENDS_WITH)) return false;
                    break;
                case HAS_POINTS_EXACT:
                    if (triggerer.points != Integer.parseInt(thing.value)) return false;
                    break;
                case HAS_POINTS_LESS_THAN:
                    if (! (triggerer.points < Integer.parseInt(thing.value))) return false;
                    break;
                case HAS_POINTS_LESS_THAN_EQUAL:
                    if (! (triggerer.points <= Integer.parseInt(thing.value))) return false;
                    break;
                case HAS_POINTS_GREATER_THAN:
                    if (! (triggerer.points > Integer.parseInt(thing.value))) return false;
                    break;
                case HAS_POINTS_GREATER_THAN_EQUAL:
                    if (! (triggerer.points >= Integer.parseInt(thing.value))) return false;
                    break;
            }
        }

        return checkIfHasConditionWithContext(event, hardCondition, hardString);
    }

    public static boolean checkEventConditions(Event event, Player triggerer, Condition hardCondition, Iterable<String> hardString){
        if (! eventHasSoftCondition(event)) return checkIfHasConditionWithContext(event, hardCondition, hardString);

        ProxiedPlayer player = UUIDFetcher.getPPlayerByUUID(triggerer.uuid);

        for (SingleSet<Condition, String> thing : event.conditions.values()) {
            if (ConfigUtils.debug) {
                StreamLine.getInstance().getLogger().info("Condition == " + thing.key);
                StreamLine.getInstance().getLogger().info("Cond.val == " + thing.value);
            }

            switch (thing.key) {
                case IN_SERVER:
                    try {
                        if (player == null) {
                            if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info("EventsHandler#checkEventConditions$3 : case IN_SERVER : player == null");
                            return false;
                        }
                        ServerInfo server = player.getServer().getInfo();
                        if (server == null) {
                            if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info("EventsHandler#checkEventConditions$3 : case IN_SERVER : server == null");
                            return false;
                        }

                        if (! checkWildCards(server.getName(), thing.value, WildCardCheck.EQUALS)) return false;
                    } catch (Exception e) {
                        return false;
                    }
                    break;
                case NAME_EXACT:
                    if (! checkWildCards(triggerer.latestName, thing.value, WildCardCheck.EQUALS)) return false;
                    break;
                case NAME_CONTAINS:
                    if (! checkWildCards(triggerer.latestName, thing.value, WildCardCheck.CONTAINS)) return false;
                    break;
                case NAME_STARTS_WITH:
                    if (! checkWildCards(triggerer.latestName, thing.value, WildCardCheck.STARTS_WITH)) return false;
                    break;
                case NAME_ENDS_WITH:
                    if (! checkWildCards(triggerer.latestName, thing.value, WildCardCheck.ENDS_WITH)) return false;
                    break;
                case HAS_POINTS_EXACT:
                    if (triggerer.points != Integer.parseInt(thing.value)) return false;
                    break;
                case HAS_POINTS_LESS_THAN:
                    if (! (triggerer.points < Integer.parseInt(thing.value))) return false;
                    break;
                case HAS_POINTS_LESS_THAN_EQUAL:
                    if (! (triggerer.points <= Integer.parseInt(thing.value))) return false;
                    break;
                case HAS_POINTS_GREATER_THAN:
                    if (! (triggerer.points > Integer.parseInt(thing.value))) return false;
                    break;
                case HAS_POINTS_GREATER_THAN_EQUAL:
                    if (! (triggerer.points >= Integer.parseInt(thing.value))) return false;
                    break;
            }
        }

        return checkIfHasConditionWithContext(event, hardCondition, hardString);
    }

    public enum WildCardCheck {
        EQUALS,
        CONTAINS,
        STARTS_WITH,
        ENDS_WITH,
    }

    public static boolean checkWildCards(String toMatch, String input, WildCardCheck check){
        String[] toMatchArray = toMatch.split(" ");
        String[] inputArray = input.split(" ");

        if (check.equals(WildCardCheck.EQUALS)) {
            int cards = 0;

            for (String string : inputArray) {
                if (string.equals("*")) cards ++;
            }

            if (cards <= 0) {
                return toMatch.equals(input);
            }

            for (int i = 0; i < toMatchArray.length; i++) {
                if (!toMatchArray[i].equals(inputArray[i]) && !inputArray[i].equals("*")) return false;
            }

            return true;
        }

        if (check.equals(WildCardCheck.CONTAINS)) {
            int cards = 0;

            for (String string : inputArray) {
                if (string.equals("*")) cards ++;
            }

            if (cards <= 0) {
                return toMatch.contains(input);
            }

//            if (startOf(toMatchArray, inputArray, 0) == -1) return false;
//
//            int start = startOf(toMatchArray, inputArray, 0);
//            while (start < toMatchArray.length) {
//                if (isContained(toMatchArray, inputArray, start)) return true;
//
//                if (startOf(toMatchArray, inputArray, start) == -1) return false;
//
//                start = startOf(toMatchArray, inputArray, start);
//            }

            return false;
        }

        if (check.equals(WildCardCheck.STARTS_WITH)) {
            int cards = 0;

            for (String string : inputArray) {
                if (string.equals("*")) cards ++;
            }

            if (cards <= 0) {
                return toMatch.startsWith(input);
            }

            int ii = 0;
            for (int i = 0; i < toMatchArray.length; i++) {
                if (ii >= inputArray.length) return true;
                if (! toMatchArray[i].equals(inputArray[ii]) && ! inputArray[ii].equals("*")) return false;
                ii ++;
            }

            return false;
        }

        if (check.equals(WildCardCheck.ENDS_WITH)) {
            int cards = 0;

            for (String string : inputArray) {
                if (string.equals("*")) cards ++;
            }

            if (cards <= 0) {
                return toMatch.endsWith(input);
            }

            int ii = inputArray.length - 1;
            for (int i = toMatchArray.length - 1; i >= 0; i--) {
                if (ii < 0) return true;
                if (! toMatchArray[i].equals(inputArray[ii]) && ! inputArray[ii].equals("*")) return false;
                ii --;
            }

            return false;
        }

        return false;
    }

    public static boolean isContained(String[] toMatchArray, String[] inputArray, int startAt) {
        int ii = 0;
        for (int i = startAt; i < toMatchArray.length; i++) {
            if (ii >= inputArray.length) return true;
            if (! toMatchArray[i].equals(inputArray[ii]) && ! inputArray[ii].equals("*")) return false;
            ii ++;
        }

        return false;
    }

    public static int startOf(String[] toMatchArray, String[] inputArray, int from){
        for (int i = from; i < toMatchArray.length; i++) {
            if (toMatchArray[i].equals(inputArray[0])) return i;
        }

        return -1;
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
                    if (checkWildCards(context, value, WildCardCheck.EQUALS)) return true;
                    break;
                case MESSAGE_CONTAINS:
                    if (checkWildCards(context, value, WildCardCheck.CONTAINS)) return true;
                    break;
                case MESSAGE_STARTS_WITH:
                    if (checkWildCards(context, value, WildCardCheck.STARTS_WITH)) return true;
                    break;
                case MESSAGE_ENDS_WITH:
                    if (checkWildCards(context, value, WildCardCheck.ENDS_WITH)) return true;
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
                        if (checkWildCards(c, value, WildCardCheck.EQUALS)) return true;
                    }
                    break;
                case MESSAGE_CONTAINS:
                    for (String c : context) {
                        if (checkWildCards(c, value, WildCardCheck.CONTAINS)) return true;
                    }
                    break;
                case MESSAGE_STARTS_WITH:
                    for (String c : context) {
                        if (checkWildCards(c, value, WildCardCheck.STARTS_WITH)) return true;
                    }
                    break;
                case MESSAGE_ENDS_WITH:
                    for (String c : context) {
                        if (checkWildCards(c, value, WildCardCheck.ENDS_WITH)) return true;
                    }
                    break;
            }
        }

        return false;
    }
}
