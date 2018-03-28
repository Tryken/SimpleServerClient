package de.sveh.simpleserverclient.config;

import java.io.*;
import java.util.Properties;

public class PropertyConfiguration implements IConfiguration {

    private Properties properties;
    private File file;

    public PropertyConfiguration(String fileName) {
        this.file = new File(fileName);
        properties = new Properties();
        InputStream in;
        try {
            in = new FileInputStream(file);
            properties.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean addProperty(String key, String value) {
        if(properties.contains(key)) return false;

        properties.put(key, value);
        return true;
    }

    @Override
    public boolean save(){
        try {
            properties.store(new FileOutputStream(file), null);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
