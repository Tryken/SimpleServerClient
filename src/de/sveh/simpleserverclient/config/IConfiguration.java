package de.sveh.simpleserverclient.config;

public interface IConfiguration {
    boolean save();
    boolean addProperty(String key, String value);
    String getProperty(String key);
}
