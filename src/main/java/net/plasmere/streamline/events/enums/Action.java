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
    ADD_TAG,
    REM_TAG,
    SEND_MESSAGE_TO_FRIENDS,
    SEND_MESSAGE_TO_PARTY_MEMBERS,
    SEND_MESSAGE_TO_GUILD_MEMBERS,
    SEND_MESSAGE_TO_STAFF,
    SEND_MESSAGE_TO_PERMISSION,
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
            case ADD_TAG:
                return "ADD_TAG";
            case REM_TAG:
                return "REM_TAG";
            case SEND_MESSAGE_TO_FRIENDS:
                return "SEND_MESSAGE_TO_FRIENDS";
            case SEND_MESSAGE_TO_PARTY_MEMBERS:
                return "SEND_MESSAGE_TO_PARTY_MEMBERS";
            case SEND_MESSAGE_TO_GUILD_MEMBERS:
                return "SEND_MESSAGE_TO_GUILD_MEMBERS";
            case SEND_MESSAGE_TO_STAFF:
                return "SEND_MESSAGE_TO_STAFF";
            case SEND_MESSAGE_TO_PERMISSION:
                return "SEND_MESSAGE_TO_PERMISSION";
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
            case "ADD_TAG":
                return ADD_TAG;
            case "REM_TAG":
                return REM_TAG;
            case "SEND_MESSAGE_TO_FRIENDS":
                return SEND_MESSAGE_TO_FRIENDS;
            case "SEND_MESSAGE_TO_PARTY_MEMBERS":
                return SEND_MESSAGE_TO_PARTY_MEMBERS;
            case "SEND_MESSAGE_TO_GUILD_MEMBERS":
                return SEND_MESSAGE_TO_GUILD_MEMBERS;
            case "SEND_MESSAGE_TO_STAFF":
                return SEND_MESSAGE_TO_STAFF;
            case "SEND_MESSAGE_TO_PERMISSION":
                return SEND_MESSAGE_TO_PERMISSION;
            default:
                return null;
        }
    }
}
