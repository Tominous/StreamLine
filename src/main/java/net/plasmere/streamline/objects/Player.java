package net.plasmere.streamline.objects;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.utils.UUIDFetcher;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Player  {
    private HashMap<String, String> info = new HashMap<>();
    private final String filePrePath = StreamLine.getInstance().getDataFolder() + File.separator + "players" + File.separator;

    public File file;
    public ProxiedPlayer player;
    public UUID uuid;
    public int xp;
    public int lvl;
    public String ips;
    public String names;
    public String latestIP;
    public String latestName;
    public List<String> ipList;
    public List<String> nameList;

    public Player(ProxiedPlayer player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.latestIP = player.getSocketAddress().toString();
        this.latestName = player.getName();
        this.ips = player.getSocketAddress().toString();
        this.names = player.getName();
        construct(player.getUniqueId(), true);
    }

    public Player(ProxiedPlayer player, boolean create){
        this.player = player;
        this.uuid = player.getUniqueId();
        this.latestIP = player.getSocketAddress().toString();
        this.latestName = player.getName();
        this.ips = player.getSocketAddress().toString();
        this.names = player.getName();
        construct(player.getUniqueId(), create);
    }

    public Player(String username){
        this.player = null;
        construct(Objects.requireNonNull(UUIDFetcher.fetch(username)), false);
    }

    private void construct(UUID uuid, boolean createNew){
        this.file = new File(filePrePath + uuid.toString() + ".properties");

        if (createNew || file.exists()) {

            StreamLine.getInstance().getLogger().info("Player file: " + file.getName() + " (In the \"players\" folder.)");

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
        this.uuid = UUID.fromString(getFromKey("uuid"));
        this.player = UUIDFetcher.getProxiedPlayer(this.uuid);
        this.ips = getFromKey("ips");
        this.names = getFromKey("names");
        this.latestIP = getFromKey("latestip");
        this.latestName = getFromKey("latestname");
        this.ipList = loadIPs();
        this.nameList = loadNames();
        this.xp = Integer.parseInt(getFromKey("xp"));
        this.lvl = Integer.parseInt(getFromKey("lvl"));
    }

    public List<String> loadIPs(){
        List<String> thing = new ArrayList<>();

        String search = "ips";

        try {
            if (getFromKey(search).equals("") || getFromKey(search) == null) return thing;
            if (! getFromKey(search).contains(".")) {
                thing.add(getFromKey(search));
                return thing;
            }

            for (String t : getFromKey(search).split(",")) {
                try {
                    thing.add(t);
                } catch (Exception e) {
                    //continue;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return thing;
    }

    public List<String> loadNames(){
        List<String> thing = new ArrayList<>();

        String search = "names";

        try {
            if (getFromKey(search).equals("") || getFromKey(search) == null) return thing;
            if (! getFromKey(search).contains(".")) {
                thing.add(getFromKey(search));
                return thing;
            }

            for (String t : getFromKey(search).split(",")) {
                try {
                    thing.add(t);
                } catch (Exception e) {
                    //continue;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return thing;
    }

    public List<String> propertiesDefaults() {
        List<String> defaults = new ArrayList<>();
        defaults.add("uuid=" + this.uuid);
        defaults.add("ips=" + this.ips);
        defaults.add("names=" + this.names);
        defaults.add("latestip=" + this.ips.split(",")[0]);
        defaults.add("latestname=" + this.names.split(",")[0]);
        defaults.add("xp=0");
        defaults.add("lvl=1");
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

        //StreamLine.getInstance().getLogger().info("Just saved Player info for player: " + player.getName());
    }

    /*
   Experience required =
   2 × current_level + 7 (for levels 0–15)
   5 × current_level – 38 (for levels 16–30)
   9 × current_level – 158 (for levels 31+)
    */
    public int getNeededXp(){
        int needed = 0;
        if (this.lvl <= 15){
            needed = 2 * this.lvl + 7;
        } else if (this.lvl >= 16 && this.lvl <= 30){
            needed = 5 * this.lvl - 38;
        } else if (this.lvl > 30) {
            needed = 9 * this.lvl - 158;
        } else {
            needed = 100;
        }

        return needed;
    }

    public void addXp(int amount){
        int setAmount = this.xp + amount;

        while (setAmount >= getNeededXp()) {
            setAmount -= getNeededXp();
            int setLevel = this.lvl + 1;
            updateKey("lvl", setLevel);
        }

        updateKey("xp", setAmount);
    }

    public void setXp(int amount){
        int setAmount = amount;

        while (setAmount >= getNeededXp()) {
            setAmount -= getNeededXp();
            int setLevel = this.lvl + 1;
            updateKey("lvl", setLevel);
        }

        updateKey("xp", setAmount);
    }

    public void dispose() throws Throwable {
        this.uuid = null;
        this.finalize();
    }
}
