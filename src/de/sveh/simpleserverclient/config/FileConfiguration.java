package de.sveh.simpleserverclient.config;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class FileConfiguration {

    private File file;

    public String load(String path) {

        file = new File(path);

        try {
            if(!file.exists())
                if(file.getParentFile().mkdirs()) {
                    //noinspection ResultOfMethodCallIgnored
                    file.createNewFile();
                    return "";
                }

            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void save(String value) {
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
