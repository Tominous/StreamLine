package net.plasmere.streamline.objects.lists;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Collection;
import java.util.TreeMap;

public class ProxiedPlayerSet {
    TreeMap<String, ProxiedPlayer> sorted = new TreeMap<>();

    public ProxiedPlayerSet(){

    }

    public ProxiedPlayerSet(Collection<? extends ProxiedPlayer> oldProxiedPlayers){
        addForProxiedPlayers(oldProxiedPlayers);
    }

    public void addForProxiedPlayers(Collection<? extends ProxiedPlayer> oldProxiedPlayers){
        for (ProxiedPlayer player : oldProxiedPlayers) {
            addProxiedPlayer(player);
        }
    }

    public void addProxiedPlayer(ProxiedPlayer player) {
        sorted.put(player.getName(), player);
    }

    public void remProxiedPlayer(ProxiedPlayer player){
        sorted.remove(player.getName());
    }

    public ProxiedPlayer[] getAll() {
        ProxiedPlayer[] players = new ProxiedPlayer[sorted.size()];

        int i = 0;
        for (ProxiedPlayer player : sorted.values()){
            players[i] = player;
            i ++;
        }

        return players;
    }
}