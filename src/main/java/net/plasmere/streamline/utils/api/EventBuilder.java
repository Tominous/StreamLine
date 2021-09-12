package net.plasmere.streamline.utils.api;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.events.Event;
import net.plasmere.streamline.events.EventsHandler;
import net.plasmere.streamline.events.enums.Action;
import net.plasmere.streamline.events.enums.Condition;
import net.plasmere.streamline.objects.lists.SingleSet;
import net.plasmere.streamline.utils.MessagingUtils;

import java.io.File;
import java.util.List;
import java.util.TreeMap;

public class EventBuilder {

    // The path that the event builder will use.
    public File path = StreamLine.getInstance().getEDir();

    /**
     * Default EventBuilder Constructor.
     * No parameters needed.
     */
    public EventBuilder(){
        this.path = StreamLine.getInstance().getEDir();
    }

    /**
     *
     * EventBuilder Constructor.
     * Using a custom path.
     *
     * @param path -> The path you want to use.
     */
    public EventBuilder(File path){
        this.path = path;
    }

    /**
     *
     * Method to create an event using the api.
     * This will create the file in the default or
     * custom path specified when creating the EventBuilder.
     *
     * @param name -> The name you want to give to the event.
     * @param tags -> The tags you want to use for the event.
     * @param conditions -> The conditions you want the event to have.
     * @param actions -> The actions you want the event to have.
     */
    public void createEvent(String name, List<String> tags, TreeMap<Integer, SingleSet<Condition, String>> conditions, TreeMap<Integer, SingleSet<Action, String>> actions){
        createEventFile(name,tags,conditions,actions);
        EventsHandler.reloadEvents();
    }

    /**
     *
     * Method to remove an event from the current events.
     *
     * @param eventToRemove -> The event you want to remove.
     */
    public void removeEvent(Event eventToRemove){
        File f = new File(path, eventToRemove.name + ".yml");

        if(!StreamLine.getInstance().getDataFolder().exists()){
            MessagingUtils.logInfo("Event parent folder doesn't exist.");
            return;
        }

        if(!f.exists()){
            MessagingUtils.logInfo("Event file doesn't exist.");
            return;
        } else{
            // Delete the file and reload events.
            f.delete();
            EventsHandler.reloadEvents();
        }
    }

    /**
     *
     * Private method to create an event file.
     *
     * @param name -> The name of the event.
     * @param tags -> The tags of the event.
     * @param conditions -> The conditions of the event / SingleSet parameters ->(Condition Enum Value, Value of the action)
     * @param actions -> The actions of the event / SingleSet parameters ->(Action Enum Value, Value of the action)
     */
    private void createEventFile(String name, List<String> tags, TreeMap<Integer, SingleSet<Condition, String>> conditions, TreeMap<Integer, SingleSet<Action, String>> actions) {
        File f = new File(path,name + ".yml");

        if(!StreamLine.getInstance().getDataFolder().exists())
            StreamLine.getInstance().getDataFolder().mkdirs();

        if(!f.exists()){
            try{
                f.createNewFile();
                Configuration conf = ConfigurationProvider.getProvider(YamlConfiguration.class).load(f);
                conf.set("tags",tags);

                for(int i = 0; i<conditions.size(); i++){
                    conf.set("conditions." + (i+1) + ".type", conditions.get(i).key);
                    conf.set("conditions." + (i+1) + ".value", conditions.get(i).value);
                }

                for(int i = 0; i<actions.size(); i++){
                    conf.set("actions." + (i+1) + ".type", actions.get(i).key);
                    conf.set("actions." + (i+1) + ".value", actions.get(i).value);
                }
            }catch(Exception e){
                MessagingUtils.logInfo("Unable to create file " + f.getName() + ". Returning error " + e.getMessage());
            }
        }
    }
}
