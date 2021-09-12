package net.plasmere.streamline.utils.api;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.plasmere.streamline.events.Event;
import net.plasmere.streamline.events.EventsHandler;
import net.plasmere.streamline.objects.savable.users.Player;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PlayerUtils;
import net.plasmere.streamline.utils.PluginUtils;

import java.util.List;
import java.util.TreeSet;

public class StreamCommand extends Command {

    public String commandLabel;
    public String permissionNeeded;
    public String permissionMessage;
    public List<Event> events;
    public TreeSet<String> aliases;
    private boolean enabled = true;

    /**
     *
     * Create a stream command
     *
     * @param commandLabel -> The command label.
     * @param permissionNeeded -> The permission needed to execute the command.
     * @param events -> All the events that are going to be executed when executing the command.
     */
    public StreamCommand(String commandLabel, String commandDescription, String permissionNeeded, String permissionMessage, List<Event> events, TreeSet<String> aliases) {
        //define default values for command
        super(commandLabel, permissionNeeded, PluginUtils.stringListToArray(aliases));
        this.commandLabel = commandLabel;
        this.events = events;
        this.permissionNeeded = permissionNeeded;
        this.permissionMessage = permissionMessage;
        this.setPermissionMessage(permissionMessage);
    }

    /**
     *
     * Only commandLabel and permission
     *
     * @param commandLabel -> The command label.
     * @param permissionNeeded -> The permission needed to execute the command.
     */
    public StreamCommand(String commandLabel, String permissionNeeded) {
        // Define default values for command
        super(commandLabel, permissionNeeded);
        this.commandLabel = commandLabel;
        this.permissionNeeded = permissionNeeded;
    }

    /**
     *
     * Create a command with no permissions needed
     * and with given events.
     *
     * @param commandLabel -> The command label.
     * @param events -> All the events that are going to be executed when executing the command.
     */
    public StreamCommand(String commandLabel,List<Event> events) {
        super(commandLabel);
        this.commandLabel = commandLabel;
        this.events = events;
    }

    /**
     *
     * Create a command with no permissions needed
     * and only the command label given.
     *
     * @param commandLabel
     */
    public StreamCommand(String commandLabel) {
        super(commandLabel);
        this.commandLabel = commandLabel;
    }

    /**
     * Execute the command.
     *
     * @param sender -> The sender of the command.
     * @param args -> The arguments of the command.
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender.hasPermission(getPermissionNeeded())) {
            executeEvents(PlayerUtils.getPlayerStat(sender));
        }
    }

    private void executeEvents(Player player) {
        // Run all events from the command.
        for(Event event : getEvents())
            EventsHandler.runEvent(event,player);
    }

    /**
     *
     * @return -> The permissions needed by a player for the command to be executed.
     */
    public String getPermissionNeeded() {
        return permissionNeeded;
    }

    /**
     *
     * Set the permission needed to execute the command.
     *
     * @param permissionNeeded
     */
    public void setPermissionNeeded(String permissionNeeded) {
        this.permissionNeeded = permissionNeeded;
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
    public List<Event> getEvents() {
        return events;
    }

    /**
     * Set the events of the command.
     * @param events
     */
    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void addEvent(Event event){
        if(!events.contains(event))
            events.add(event);
        else
            MessagingUtils.logInfo("Command " + getCommandLabel() + " is already implementing " + event.name);
    }

    public void removeEvent(Event event){
        if(events.contains(event))
            MessagingUtils.logInfo("Command " + getCommandLabel() + " is not implementing " + event.name);
        else
            events.remove(event);
    }

    /**
     * Toggle the enabled value of the command.
     */
    public void toggleCommand(){
        // Toggle the command on or off
        enabled = !enabled;
        MessagingUtils.logInfo("Command " + getCommandLabel() + " set to " + enabled);
    }

    /**
     * Set the command to enabled.
     */
    public void setEnabled(){
        enabled = true;
    }

    /**
     * Set the command to disabled.
     */
    public void setDisabled(){
        enabled = false;
    }

    /**
     * Get the enabled value of the command
     * @return -> returns the enabled value of the command.
     */
    public boolean getEnabled(){
        return enabled;
    }
}