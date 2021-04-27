package net.plasmere.streamline.objects.configs;

import net.md_5.bungee.api.config.ServerInfo;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.objects.lists.SingleSet;
import org.apache.commons.collections4.list.TreeList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Lobbies {
    private TreeMap<Integer, SingleSet<String, String>> info = new TreeMap<>();
    private final String filePrePath = StreamLine.getInstance().getDataFolder() + File.separator + "configs" + File.separator;

    public String defaultAllow = "1.4,1.5,1.6,1.7,1.8,1.9,1.10,1.11,1.12,1.13,1.14,1.15,1.16,1.17";
    public File file;
    public TreeMap<String, List<String>> servers = new TreeMap<>();

    public Lobbies(boolean createNew){
        construct(createNew);
    }

    private void construct(boolean createNew){
        this.file = new File(filePrePath + ConfigUtils.lobbiesFile);

        if (createNew) {
            try {
                this.updateWithNewDefaults();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            getFromConfigFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TreeMap<Integer, SingleSet<String, String>> getInfo() {
        return info;
    }
    public void remKey(String key){
        info.remove(key);
    }
    public String getValue(int number){
        return info.get(number).value;
    }
    public void updateKey(int number, String key, Object value) {
        info.put(number, new SingleSet<>(key, String.valueOf(value)));
        loadVars();
    }
    public File getFile() { return file; }

    public boolean hasProperty(String property) {
        for (String info : getInfoAsPropertyList().values()) {
            if (info.startsWith(property)) return true;
        }

        return false;
    }

    public SingleSet<String, String> getSet(String key){
        for (SingleSet<String, String> set : info.values()){
            if (set.key.equals(key)) return set;
        }

        return null;
    }

    public HashMap<Integer, String> getInfoAsPropertyList() {
        HashMap<Integer, String> infoList = new HashMap<>();
        int i = 1;
        for (SingleSet<String, String> set : info.values()){
            infoList.put(i, set.key + "=" + set.value);
            i ++;
        }

        return infoList;
    }

    public String getFullProperty(String key) {
        try {
            if (hasProperty(key)) {
                return key + "=" + getSet(key).value;
            } else {
                throw new Exception("No property saved!");
            }
        } catch (Exception e) {
            return null;
        }
    }

    public void flushInfo(){
        this.info = new TreeMap<>();
    }

    public void addKeyValuePair(String key, String value){
        info.put(info.size() + 1, new SingleSet<>(key, value));
    }

    public void getFromConfigFile() throws IOException {
        if (file.exists()){
            Scanner reader = new Scanner(file);

            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                while (data.startsWith("#")) {
                    data = reader.nextLine();
                }
                String[] dataSplit = data.split("=", 2);
                addKeyValuePair(dataSplit[0], dataSplit[1]);
            }

            reader.close();

            loadVars();
        } else {
            updateWithNewDefaults();
        }
    }

    public void updateWithNewDefaults() throws IOException {
        file.delete();
        FileWriter writer = new FileWriter(file);

        for (String p : propertiesDefaults()) {
            String[] propSplit = p.split("=", 2);

            String property = propSplit[0];

            String write = "";
            try {
                if (getFullProperty(property) != null) {
                    write = getFullProperty(property);
                } else {
                    write = p;
                }
            } catch (Exception e) {
                write = p;
            }

            writer.write(write + "\n");
        }

        writer.close();

        flushInfo();

        Scanner reader = new Scanner(file);

        while (reader.hasNextLine()) {
            String data = reader.nextLine();
            while (data.startsWith("#")) {
                data = reader.nextLine();
            }
            String[] dataSplit = data.split("=", 2);
            addKeyValuePair(dataSplit[0], dataSplit[1]);
        }

        reader.close();

        loadVars();
    }

    public void loadVars(){
        try {
            loadServers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadServers(){
        for (SingleSet<String, String> s : info.values()) {
            //StreamLine.getInstance().getLogger().info("Set: <" + s.key  + " , " + s.value + ">");

            servers.put(s.key, parseServers(s.key));
        }
    }

    public List<String> parseServers(String key){
        String raw = getSet(key).value;
        String[] split = raw.split(",");
        List<String> list = new ArrayList<>();

        for (String s : split) {
            if (! isVersion(s)) continue;

            list.add(s);
        }

        return list;
    }

    public boolean isVersion(String version){
        return version.equals("1.8") || version.equals("1.9") || version.equals("1.10") ||
                version.equals("1.11") || version.equals("1.12") || version.equals("1.13") ||
                version.equals("1.14") || version.equals("1.15") || version.equals("1.16") ||
                version.equals("1.17");
    }

    public TreeList<String> getServers(){
        TreeList<String> defaults = new TreeList<>();

        for (ServerInfo si : StreamLine.getInstance().getProxy().getServers().values()){
            defaults.add(si.getName() + "=" + defaultAllow);
        }

        return defaults;
    }

    public TreeList<String> propertiesDefaults() {
        TreeList<String> defaults = new TreeList<>();
        defaults.add("### Remove the versions you don't want for each lobby.");
        defaults.add("### These are the list (in order) of which your player will be connected to first (if they can) by version.");
        defaults.add("### Remove lobbies and change the order of lobbies until you are satisfied.");
        defaults.addAll(getServers());
        //defaults.add("");
        return defaults;
    }

    public void saveInfo() throws IOException {
        file.delete();

        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        for (String s : getInfoAsPropertyList().values()){
            writer.write(s + "\n");
        }
        writer.close();

        //StreamLine.getInstance().getLogger().info("Just saved Guild info for leader (UUID): " + leaderUUID);
    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder("[");

        for (String server : servers.keySet()){
            stringBuilder.append(server).append("(");
            int i = 1;
            for (String allowed : servers.get(server)) {
                if (i != servers.get(server).size()) {
                    stringBuilder.append(allowed).append(", ");
                } else {
                    stringBuilder.append(allowed);
                }
                i++;
            }
            stringBuilder.append(")");
        }

        return stringBuilder.toString();
    }

    public String getVersionString(int version){
        String v = StreamLine.viaHolder.getVersion(version).getName();

        if (v.startsWith("1.4")) return "1.4";
        if (v.startsWith("1.5")) return "1.5";
        if (v.startsWith("1.6")) return "1.6";
        if (v.startsWith("1.7")) return "1.7";
        if (v.startsWith("1.8")) return "1.8";
        if (v.startsWith("1.9")) return "1.9";
        if (v.startsWith("1.10")) return "1.10";
        if (v.startsWith("1.11")) return "1.11";
        if (v.startsWith("1.12")) return "1.12";
        if (v.startsWith("1.13")) return "1.13";
        if (v.startsWith("1.14")) return "1.14";
        if (v.startsWith("1.15")) return "1.15";
        if (v.startsWith("1.16")) return "1.16";
        if (v.startsWith("1.17")) return "1.17";

        return "";
    }

    public boolean isAllowed(int version, String server){
        for (String v : servers.get(server)){
            String ver = getVersionString(version);
            if (ver.equals("")) {
                //StreamLine.getInstance().getLogger().severe("A player's version couldn't be determined so we let them join...");
                return true;
            }

            if (v.equals(ver)) {
                //StreamLine.getInstance().getLogger().info("Letting a player join " + server + " with version: " + ver);
                return true;
            }
        }
        return false;
    }
}
