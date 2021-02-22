package net.plasmere.streamline.objects.configs;

import net.md_5.bungee.api.config.ServerInfo;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import us.myles.ViaVersion.api.protocol.ProtocolVersion;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ServerPermissions {
    private HashMap<String, String> info = new HashMap<>();
    private final String filePrePath = StreamLine.getInstance().getDataFolder() + File.separator + "configs" + File.separator;

    public String defaultAllow = "1.4,1.5,1.6,1.7,1.8,1.9,1.10,1.11,1.12,1.13,1.14,1.15,1.16,1.17";
    public File file;
    public HashMap<String, List<String>> servers = new HashMap<>();

    public ServerPermissions(boolean createNew){
        construct(createNew);
    }

    private void construct(boolean createNew){
        this.file = new File(filePrePath + ConfigUtils.vbServerFile);

        if (createNew || file.exists()) {
            //StreamLine.getInstance().getLogger().info("Guild file: " + file.getName() + " (In the \"guilds\" folder.)");

            try {
                getFromConfigFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public HashMap<String, String> getInfo() {
        return info;
    }
    public void remKey(String key){
        info.remove(key);
    }
    public String getFromKey(String key){
        return info.get(key);
    }
    public void updateKey(String key, Object value) {
        info.put(key, String.valueOf(value));
        loadVars();
    }
    public File getFile() { return file; }

    public boolean hasProperty(String property) {
        for (String info : getInfoAsPropertyList()) {
            if (info.startsWith(property)) return true;
        }

        return false;
    }

    public List<String> getInfoAsPropertyList() {
        List<String> infoList = new ArrayList<>();
        for (String key : info.keySet()){
            infoList.add(key + "=" + getFromKey(key));
        }

        return infoList;
    }

    public String getFullProperty(String key) throws Exception {
        if (hasProperty(key)) {
            return key + "=" + getFromKey(key);
        } else {
            throw new Exception("No property saved!");
        }
    }

    public void flushInfo(){
        this.info = new HashMap<>();
    }

    public void addKeyValuePair(String key, String value){
        info.put(key, value);
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

            if (needUpdate()) {
                updateWithNewDefaults();
            }

            loadVars();
        } else {
            updateWithNewDefaults();
        }
    }

    public boolean needUpdate() {
        if (info.size() != propertiesDefaults().size()) return true;

        int i = 0;
        for (String p : getInfoAsPropertyList()) {
            if (! p.startsWith(propertiesDefaults().get(i).split("=", 2)[0])) return true;
            i++;
        }

        return false;
    }

    public void updateWithNewDefaults() throws IOException {
        file.delete();
        FileWriter writer = new FileWriter(file);

        for (String p : propertiesDefaults()) {
            String[] propSplit = p.split("=", 2);

            String property = propSplit[0];

            String write = "";
            try {
                write = getFullProperty(property);
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
        for (String s : info.keySet()) {
            servers.put(s, parseServers(s));
        }
    }

    public List<String> parseServers(String key){
        String raw = info.get(key);
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

    public List<String> getServers(){
        List<String> defaults = new ArrayList<>();

        for (ServerInfo si : StreamLine.getInstance().getProxy().getServers().values()){
            defaults.add(si.getName() + "=" + defaultAllow);
        }

        return defaults;
    }

    public List<String> propertiesDefaults() {
        List<String> defaults = new ArrayList<>();
        defaults.add("### Remove the versions you don't want for each server.");
        defaults.addAll(getServers());
        //defaults.add("");
        return defaults;
    }

    public void saveInfo() throws IOException {
        file.delete();

        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        for (String s : getInfoAsPropertyList()){
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
        ProtocolVersion key = ProtocolVersion.getProtocol(version);

        String v = key.getName();

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
                StreamLine.getInstance().getLogger().severe("A player's version couldn't be determined so we let them join...");
                return true;
            }

            if (v.equals(ver)) {
                StreamLine.getInstance().getLogger().info("Letting a player join " + server + " with version: " + ver);
                return true;
            }
        }
        return false;
    }
}
