package net.plasmere.streamline.objects;

import net.md_5.bungee.api.*;
import net.md_5.bungee.api.chat.BaseComponent;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;

import java.io.File;
import java.util.*;

public class ConsolePlayer extends SavableUser {
    public ProxyServer server;
    public CommandSender user;

    public List<String> savedKeys = new ArrayList<>();

    public ConsolePlayer() {
        super("console");
    }

    public ConsolePlayer(boolean create){
        super("console", create);
    }

    @Override
    public void preConstruct(String string) {
        this.uuid = "%";

        this.latestName = ConfigUtils.consoleName;
        this.tagList = ConfigUtils.consoleDefaultTags;

        this.server = StreamLine.getInstance().getProxy();
        this.user = this.server.getConsole();
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
    public TreeMap<String, String> updatableKeys() {
        return new TreeMap<>();
    }

    @Deprecated
    public void sendMessage(String message) {
        user.sendMessage(message);
    }

    @Deprecated
    public void sendMessages(String... messages) {
        user.sendMessages(messages);
    }

    
    public void sendMessage(BaseComponent... message) {
        user.sendMessage(message);
    }

    
    public void sendMessage(BaseComponent message) {
        user.sendMessage(message);
    }

    
    public Collection<String> getGroups() {
        return user.getGroups();
    }

    
    public void addGroups(String... groups) {
        user.addGroups(groups);
    }

    
    public void removeGroups(String... groups) {
        user.removeGroups(groups);
    }

    
    public boolean hasPermission(String permission) {
        return user.hasPermission(permission);
    }

    
    public void setPermission(String permission, boolean value) {
        user.setPermission(permission, value);
    }

    
    public Collection<String> getPermissions() {
        return user.getPermissions();
    }
}
