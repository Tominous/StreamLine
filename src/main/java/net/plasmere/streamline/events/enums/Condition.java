package net.plasmere.streamline.events.enums;

import java.util.Locale;

public enum Condition {
    JOIN,
    LEAVE,
    MESSAGE_EXACT,
    MESSAGE_CONTAINS,
    MESSAGE_STARTS_WITH,
    MESSAGE_ENDS_WITH,
    COMMAND
    ;

    public static String toString(Condition condition){
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

    public static Condition fromString(String condition){
        condition = condition.toUpperCase(Locale.ROOT);

        switch (condition) {
            case "JOIN":
                return JOIN;
            case "LEAVE":
                return LEAVE;
            case "MESSAGE_EXACT":
                return MESSAGE_EXACT;
            case "MESSAGE_CONTAINS":
                return MESSAGE_CONTAINS;
            case "MESSAGE_STARTS_WITH":
                return MESSAGE_STARTS_WITH;
            case "MESSAGE_ENDS_WITH":
                return MESSAGE_ENDS_WITH;
            case "COMMAND":
                return COMMAND;
            default:
                return null;
        }
    }
}
