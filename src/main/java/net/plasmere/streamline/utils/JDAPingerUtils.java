package net.plasmere.streamline.utils;

import net.plasmere.streamline.StreamLine;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ServerInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JDAPingerUtils {
    private static JDA jda = null;
    private static final EmbedBuilder eb = new EmbedBuilder();

    private static int i = 0;

    private static String doPing(ServerInfo server, StreamLine plugin){
        i++;
        StringBuilder text = new StringBuilder();

        try {
            Socket sock = new Socket(server.getAddress().getAddress(), server.getAddress().getPort());

            DataOutputStream out = new DataOutputStream(sock.getOutputStream());
            DataInputStream in = new DataInputStream(sock.getInputStream());

            out.write(0xFE);

            int b;
            StringBuilder str = new StringBuilder();

            try {
                while ((b = in.read()) != -1) {
                    if (b > 16 && b != 255 && b != 23 && b != 24) {
                        str.append((char) b);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }

            String[] data = str.toString().split("§");

            text.append((data[0] == null) ? "not reachable" : "reachable");
        } catch (Exception e) {
            text.append("not reachable");
            plugin.getLogger().info("[ " + server.getName().toLowerCase() + " : " + i + " / " + plugin.getProxy().getServers().size() + " ] " + server.getAddress().getAddress() + ":" +
                    server.getAddress().getPort() + " (" + server.getPlayers().size() + ") : " + text + " --> \n" + e.getMessage());
            return text.toString();
        }

        plugin.getLogger().info("[ " + server.getName().toLowerCase() + " : " + i + " / " + plugin.getProxy().getServers().size() + " ] " + server.getAddress().getAddress() + ":" +
                server.getAddress().getPort() + " (" + server.getPlayers().size() + ") : " + text);

        return text.toString();
    }

    public static void sendMessage(TextChannel channel){
        StreamLine plugin = StreamLine.getInstance();

        eb.setDescription("Pinging " + plugin.getProxy().getServers().size() + " servers... Give me about " + plugin.getProxy().getServers().size() * 1.2 + " seconds...");
        channel.sendMessage(eb.build()).queue();
        Map<String, ServerInfo> serversM = plugin.getProxy().getServers();
        Set<ServerInfo> servers = new HashSet<>(serversM.values());
        try {
            i = 0;

            // DEBUG
            int it = 0;
            for (ServerInfo server : servers){
                it++;
                plugin.getLogger().info("DEBUG : [ " + server.getName().toLowerCase() + " : " + it + " / " + plugin.getProxy().getServers().size() + " ] " + server.getAddress().getAddress() + ":" +
                        server.getAddress().getPort() + " (" + server.getPlayers().size() + ")");
            }

            for (ServerInfo server : servers) {
                String msg = "";
                try {
                    msg = server.getName().toUpperCase() + " " + i + " / " + plugin.getProxy().getServers().size() + " [ " + server.getAddress().getAddress() + server.getAddress().getPort() + " ] (Online: " +
                            server.getPlayers().size() + ") : " + doPing(server, plugin) + "\n";
                    channel.sendMessage(eb.setDescription(msg).build()).queue();
                } catch (Exception e){
                    channel.sendMessage(eb.setDescription("Sorry, but the ports couldn't be checked...").build()).queue();
                }
            }
        } catch (NullPointerException n){
            n.printStackTrace();
        } catch (Exception e){
            plugin.getLogger().warning("An unknown error occurred with sending JDAPinger message...");
            e.printStackTrace();
        }
        plugin.getLogger().info("Sent ping message!");
    }
}
