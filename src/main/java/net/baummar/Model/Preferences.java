package net.baummar.Model;

import com.google.gson.Gson;


import java.io.*;

public class Preferences {

    private static final String CONFIG_FILE = "config.txt";
    private String path;

    public Preferences() {
        path = null;
    }

    public String getPath() {
        return path;
    }

    public Preferences setPath(String path) {
        this.path = path;
        return this;
    }

    public static void initConfig() {
        Preferences perseverance= new Preferences();
        Gson gson = new Gson();
        Writer writer = null;
        try {
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(perseverance, writer);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setPerseverance(Preferences perseverance) {
        Gson gson = new Gson();
        Writer writer = null;
        try {
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(perseverance, writer);
            writer.close();
        } catch (IOException e) {
        }
    }

    public static Preferences getPreference() {
        Gson gson = new Gson();
        Preferences perseverance = new Preferences();
        try {
            perseverance = gson.fromJson(new FileReader(CONFIG_FILE), Preferences.class);

        } catch (FileNotFoundException e) {
            initConfig();
            e.printStackTrace();
        }

        return perseverance;
    }
}