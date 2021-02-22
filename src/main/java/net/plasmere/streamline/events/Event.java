package net.plasmere.streamline.events;

import java.util.List;

public class Event {
    public enum Condition {
        JOIN,
        LEAVE,
        MESSAGE_EXACT,
        MESSAGE_CONTAINS,
        MESSAGE_STARTS_WITH,
        MESSAGE_ENDS_WITH,
        COMMAND
    }

    public enum Action {
        SEND_MESSAGE_TO,
        SEND_MESSAGE_AS,
        SEND_SERVER,
        KICK,
        RUN_COMMAND_AS_OP,
        RUN_COMMAND_AS_SELF
    }

    public static String conditionToString(Condition condition){
        switch (condition) {
            case JOIN:
                return "JOIN";
            case LEAVE:
                return "LEAVE";
            case MESSAGE_EXACT:
                return "MESSAGE_EXACT";
            case MESSAGE_CONTAINS:
                return "MESSAGE_CONTAINS";
            case MESSAGE_STARTS_WITH:
                return "MESSAGE_STARTS_WITH";
            case MESSAGE_ENDS_WITH:
                return "MESSAGE_ENDS_WITH";
            case COMMAND:
                return "COMMAND";
            default:
                return "";
        }
    }

    public static Condition stringToCondition(String condition){
        switch (condition) {
            case "JOIN":
                return Condition.JOIN;
            case "LEAVE":
                return Condition.LEAVE;
            case "MESSAGE_EXACT":
                return Condition.MESSAGE_EXACT;
            case "MESSAGE_CONTAINS":
                return Condition.MESSAGE_CONTAINS;
            case "MESSAGE_STARTS_WITH":
                return Condition.MESSAGE_STARTS_WITH;
            case "MESSAGE_ENDS_WITH":
                return Condition.MESSAGE_ENDS_WITH;
            case "COMMAND":
                return Condition.COMMAND;
            default:
                return null;
        }
    }

    public static String actionToString(Action action){
        switch (action) {
            case SEND_MESSAGE_TO:
                return "SEND_MESSAGE_TO";
            case SEND_MESSAGE_AS:
                return "SEND_MESSAGE_AS";
            case SEND_SERVER:
                return "SEND_SERVER";
            case KICK:
                return "KICK";
            case RUN_COMMAND_AS_OP:
                return "RUN_COMMAND_AS_OP";
            case RUN_COMMAND_AS_SELF:
                return "RUN_COMMAND_AS_SELF";
            default:
                return "";
        }
    }

    public static Action stringToAction(String action){
        switch (action) {
            case "SEND_MESSAGE_TO":
                return Action.SEND_MESSAGE_TO;
            case "SEND_MESSAGE_AS":
                return Action.SEND_MESSAGE_AS;
            case "SEND_SERVER":
                return Action.SEND_SERVER;
            case "KICK":
                return Action.KICK;
            case "RUN_COMMAND_AS_OP":
                return Action.RUN_COMMAND_AS_OP;
            case "RUN_COMMAND_AS_SELF":
                return Action.RUN_COMMAND_AS_SELF;
            default:
                return null;
        }
    }

    public List<String> tags;
    public Condition condition;
    public String conVal;
    public Action action;
    public String actVal;

    public Event(List<String> tags, Condition condition, String conditionValue, Action action, String actionValue){
        this.tags = tags;
        this.condition = condition;
        this.conVal = conditionValue;
        this.action = action;
        this.actVal = actionValue;
    }

    @Override
    public String toString() {
        return "Event{" +
                "tags=" + tags +
                ", condition=" + conditionToString(condition) +
                ", conVal='" + conVal + '\'' +
                ", action=" + actionToString(action) +
                ", actVal='" + actVal + '\'' +
                '}';
    }
}
