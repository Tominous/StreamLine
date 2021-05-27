package net.plasmere.streamline.events.enums;

import java.util.Locale;

public enum Action {
    SEND_MESSAGE_TO,
    SEND_MESSAGE_AS,
    SEND_SERVER,
    KICK,
    RUN_COMMAND_AS_OP,
    RUN_COMMAND_AS_SELF,
    GIVE_POINTS,
    TAKE_POINTS,
    SET_POINTS,
    ;

    public static String toString(Action action){
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
            case GIVE_POINTS:
                return "GIVE_POINTS";
            case TAKE_POINTS:
                return "TAKE_POINTS";
            case SET_POINTS:
                return "SET_POINTS";
            default:
                return "";
        }
    }

    public static Action fromString(String action){
        action = action.toUpperCase(Locale.ROOT);

        switch (action) {
            case "SEND_MESSAGE_TO":
                return SEND_MESSAGE_TO;
            case "SEND_MESSAGE_AS":
                return SEND_MESSAGE_AS;
            case "SEND_SERVER":
                return SEND_SERVER;
            case "KICK":
                return KICK;
            case "RUN_COMMAND_AS_OP":
                return RUN_COMMAND_AS_OP;
            case "RUN_COMMAND_AS_SELF":
                return RUN_COMMAND_AS_SELF;
            case "GIVE_POINTS":
                return GIVE_POINTS;
            case "TAKE_POINTS":
                return TAKE_POINTS;
            case "SET_POINTS":
                return SET_POINTS;
            default:
                return null;
        }
    }
}
