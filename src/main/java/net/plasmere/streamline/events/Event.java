package net.plasmere.streamline.events;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.events.enums.Action;
import net.plasmere.streamline.events.enums.Condition;
import net.plasmere.streamline.objects.lists.SingleSet;

import java.io.File;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

public class Event {
    public File path = StreamLine.getInstance().getEDir();

    public Configuration configuration;
    public List<String> tags;
    public TreeMap<Integer, SingleSet<SingleSet<Condition, String>, SingleSet<Action, String>>> compiled = new TreeMap<>();

    public Event(File file){
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(path, file.getName()));

            tags = configuration.getStringList("tags");

            compiled = compile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TreeMap<Integer, SingleSet<SingleSet<Condition, String>, SingleSet<Action, String>>> compile() {
        TreeMap<Integer, SingleSet<SingleSet<Condition, String>, SingleSet<Action, String>>> c = new TreeMap<>();

        Configuration conditions = configuration.getSection("conditions");
        Configuration actions = configuration.getSection("actions");
        int i = 1;

//        StreamLine.getInstance().getLogger().info("Configuration : " + configuration.getKeys());

        for (String string : conditions.getKeys()) {
            try {
                Configuration cond = conditions.getSection(string);
                Configuration act = actions.getSection(string);

                c.put(i,
                        new SingleSet<>(
                                new SingleSet<>(
                                        Condition.fromString(cond.getString("type")),
                                        cond.getString("value")
                                ),
                                new SingleSet<>(
                                        Action.fromString(act.getString("type")),
                                        act.getString("value")
                                )
                        )
                );

//                StreamLine.getInstance().getLogger().info("Put: " + i + " : ( ( " +
//                        Condition.fromString(cond.getString("type")) + " , " +
//                        cond.getString("value") + " ) , ( " +
//                        Action.fromString(act.getString("type")) + " , " +
//                        act.getString("value") + " ) )"
//                );
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            i ++;
        }

//        StreamLine.getInstance().getLogger().info("Event#compile():");
//        for (Integer it : c.keySet()) {
//            StreamLine.getInstance().getLogger().info("   > " + it + " : ( ( " +
//                    c.get(it).key.key + " , " +
//                    c.get(it).key.value + " ) , ( " +
//                    c.get(it).value.key + " , " +
//                    c.get(it).value.value + " ) )"
//            );
//        }

        return c;
    }

    @Override
    public String toString() {
        return "Event{" +
                "path=" + path +
                ", configuration=" + configuration +
                ", tags=" + tags +
                ", compiled=" + compiled +
                '}';
    }
}
