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
    public TreeMap<Integer, SingleSet<Condition, String>> conditions = new TreeMap<>();
    public TreeMap<Integer, SingleSet<Action, String>> actions = new TreeMap<>();

    public Event(File file){
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(path, file.getName()));

            tags = configuration.getStringList("tags");

            this.conditions = compileCond();
            this.actions = compileAction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TreeMap<Integer, SingleSet<Condition, String>> compileCond() {
        TreeMap<Integer, SingleSet<Condition, String>> c = new TreeMap<>();

        Configuration conditionsConf = configuration.getSection("conditions");
        int i = 1;

        for (String string : conditionsConf.getKeys()) {
            try {
                Configuration cond = conditionsConf.getSection(string);

                c.put(i,
                        new SingleSet<>(
                                Condition.fromString(cond.getString("type")),
                                cond.getString("value")
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            i ++;
        }

        return c;
    }

    public TreeMap<Integer, SingleSet<Action, String>> compileAction() {
        TreeMap<Integer, SingleSet<Action, String>> a = new TreeMap<>();

        Configuration conditionsConf = configuration.getSection("conditions");
        Configuration actionsConf = configuration.getSection("actions");
        int i = 1;

        for (String string : conditionsConf.getKeys()) {
            try {
                Configuration act = actionsConf.getSection(string);

                a.put(i,
                        new SingleSet<>(
                                Action.fromString(act.getString("type")),
                                act.getString("value")
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            i ++;
        }

        return a;
    }

    @Override
    public String toString() {
        return "Event{ " +
                "path=" + path +
                ", configuration=" + configuration +
                ", tags=" + tags +
                ", compiled=(" + conditions + " , " + actions +
                ") }";
    }
}
