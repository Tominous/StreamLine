package net.plasmere.streamline.objects.savable.users;

import net.md_5.bungee.api.*;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;

import java.util.*;

public class ConsolePlayer extends SavableUser {
    public ProxyServer server;

    public List<String> savedKeys = new ArrayList<>();

    public ConsolePlayer() {
        super("%");
    }

    public ConsolePlayer(boolean create){
        super("%", create);
    }

    @Override
    public void preConstruct(String string) {
        this.uuid = "%";

        this.latestName = ConfigUtils.consoleName;
        this.displayName = ConfigUtils.consoleDisplayName;
        this.tagList = ConfigUtils.consoleDefaultTags;

        this.server = StreamLine.getInstance().getProxy();
    }

    @Override
    public int getPointsFromConfig(){
        return ConfigUtils.consoleDefaultPoints;
    }

    @Override
    public TreeSet<String> addedProperties() {
        return new TreeSet<>();
    }

    @Override
    public List<String> getTagsFromConfig(){
        return ConfigUtils.consoleDefaultTags;
    }

    @Override
    public void loadMoreVars() {
    }

    @Override
    TreeMap<String, String> addedUpdatableKeys() {
        return new TreeMap<>();
    }
}
