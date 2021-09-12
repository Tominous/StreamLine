package net.plasmere.streamline.utils.api;

import net.plasmere.streamline.events.Event;

public class StreamCommand {

    public String commandLabel;
    public String[] permissionsNeeded;
    public Event[] events;

    /**
     *
     * @param commandLabel
     * @param permissionsNeeded
     * @param events
     */
    public StreamCommand(String commandLabel, String[] permissionsNeeded, Event[] events) {
        this.commandLabel = commandLabel;
        this.events = events;
        this.permissionsNeeded = permissionsNeeded;
    }

    /**
     *
     * @param commandLabel
     * @param permissionsNeeded
     */
    public StreamCommand(String commandLabel, String[] permissionsNeeded) {
        this.commandLabel = commandLabel;
        this.permissionsNeeded = permissionsNeeded;
    }

    /**
     *
     * @param commandLabel
     */
    public StreamCommand(String commandLabel) {
        this.commandLabel = commandLabel;
        this.permissionsNeeded = permissionsNeeded;
    }

    /**
     *
     * @return -> The permissions needed by a player for the command to be executed.
     */
    public String[] getPermissionsNeeded() {
        return permissionsNeeded;
    }

    /**
     *
     * @param permissionsNeeded
     */
    public void setPermissionsNeeded(String[] permissionsNeeded) {
        this.permissionsNeeded = permissionsNeeded;
    }

    /**
     *
     * @return -> The label needed for the command to be executed.
     */
    public String getCommandLabel() {
        return commandLabel;
    }

    /**
     *
     * @param commandLabel
     */
    public void setCommandLabel(String commandLabel) {
        this.commandLabel = commandLabel;
    }

    /**
     *
     * @return -> The list of events the command is going to execute.
     */
    public Event[] getEvents() {
        return events;
    }

    /**
     *
     * @param events
     */
    public void setEvents(Event[] events) {
        this.events = events;
    }
}
