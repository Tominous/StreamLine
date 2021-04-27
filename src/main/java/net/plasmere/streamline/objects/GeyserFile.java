package net.plasmere.streamline.objects;

import net.plasmere.streamline.StreamLine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GeyserFile {
    private TreeMap<String, String> info = new TreeMap<>();
    public File playerPath = new File(StreamLine.getInstance().getPlDir(), "geyser" + File.separator);

    public File file;

    public GeyserFile(boolean createNew){
        construct(createNew);
    }

    private void construct(boolean createNew) {
        this.file = new File(playerPath, "geyser.uuids");

        if (createNew || ! file.exists()) {
            try {
                this.file.delete();
                if (! this.file.createNewFile()) StreamLine.getInstance().getLogger().severe("Could not create Geyser file!");
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

    public TreeMap<String, String> getInfo() {
        return info;
    }
    public void remKey(String key){
        info.remove(key);
    }
    public void updateKey(String uuid, String name) {
        info.put(uuid, name);
        try {
            saveInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getFile() { return file; }

    public boolean hasProperty(String property) {
        for (String info : info.keySet()) {
            if (info.equals(property)) return true;
        }

        return false;
    }

    public String getName(String key){
        return info.get(key);
    }

    public String getUUID(String username){
        for (String uuid : info.keySet()) {
            if (getName(uuid).equals(username)) return uuid;
        }

        return null;
    }

    public String getFullProperty(String key) {
        try {
            if (hasProperty(key)) {
                return key + "=" + getName(key);
            } else {
                throw new Exception("No property saved!");
            }
        } catch (Exception e) {
            return null;
        }
    }

    public TreeSet<String> getInfoAsPropertyList() {
        TreeSet<String> thing = new TreeSet<>();

        for (String uuid : info.keySet()) {
            thing.add(uuid.toString() + "=" + getName(uuid));
        }

        return thing;
    }

    public void flushInfo(){
        this.info = new TreeMap<>();
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
        } else {
            if (! file.createNewFile()) StreamLine.getInstance().getLogger().severe("Couldn't create Geyser file!");
        }
    }

    public void saveInfo() throws IOException {
        file.delete();

        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        for (String s : getInfoAsPropertyList()){
            writer.write(s + "\n");
        }
        writer.close();

        //StreamLine.getInstance().getLogger().info("Just saved Guild info for leader (String): " + leaderString);
    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder("[");

        int i = 1;
        for (String uuid : info.keySet()){
            stringBuilder.append(uuid).append("=").append(getName(uuid));
            if (i < info.size()) {
                stringBuilder.append(",");
            }
            i ++;
        }

        stringBuilder.append("]");

        return stringBuilder.toString();
    }
}
