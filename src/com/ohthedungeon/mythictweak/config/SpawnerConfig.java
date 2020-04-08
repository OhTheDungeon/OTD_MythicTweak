package com.ohthedungeon.mythictweak.config;

import com.ohthedungeon.mythictweak.OTD_MythicTweak;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import com.ohthedungeon.mythictweak.com.google.gson.Gson;
import com.ohthedungeon.mythictweak.com.google.gson.GsonBuilder;
import com.ohthedungeon.mythictweak.util.MythicSpawnerType;
import java.util.Collections;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author shadow_wind
 */
public class SpawnerConfig {
    private static SpawnerConfig config = new SpawnerConfig();
    
    public final static String CONFIG_FILE_NAME = "spawner_config.json";

    private Map<Integer, Boolean> roguelike_status;
    private Map<Integer, Map<String, List<String>>> roguelike;
    private Map<Integer, SpawnerSetting> roguelike_settings;
    
    public static Map<Integer, Boolean> getRoguelikeStatus() {
        return Collections.unmodifiableMap(config.roguelike_status);
    }
    
    public static Map<Integer, Map<String, List<String>>> getRoguelikeMobList() {
        return Collections.unmodifiableMap(config.roguelike);
    }
    
    public static Map<Integer, SpawnerSetting> getRoguelikeSetting() {
        return Collections.unmodifiableMap(config.roguelike_settings);
    }
    
    public static void setStatus(int level, boolean enabled) {
        config.roguelike_status.put(level, enabled);
    }
    
    private SpawnerConfig() {
        roguelike_status = new HashMap<>();
        roguelike = new HashMap<>();
        roguelike_settings = new HashMap<>();
        for(int i = 0; i <= 4; i++) {
            roguelike.put(i, new HashMap<>());
            roguelike_status.put(i, Boolean.FALSE);
            roguelike_settings.put(i, new SpawnerSetting());
        }
    }
    
    public static void saveConfig(File file) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(config);
        try (OutputStreamWriter oStreamWriter = new OutputStreamWriter(new FileOutputStream(file), "utf-8")) {
            oStreamWriter.append(json);
        }
    }
    
    public static void saveConfig() {
        JavaPlugin plugin = OTD_MythicTweak.instance;
        File configDir = plugin.getDataFolder();
	if(!configDir.exists()){
            configDir.mkdir();
	}
        
        String world_config_file = configDir.toString() + File.separator + CONFIG_FILE_NAME;
        File file = new File(world_config_file);
        try {
            saveConfig(file);
        } catch(IOException ex) {
            OTD_MythicTweak.consoleError("[OTD_MythicTweak] Cannot save config file.");
        }
    }
    
    public static void loadConfig(File cfile) throws IOException {
//        File cfile = new File(world_config_file);
        if(!cfile.exists()){
            saveConfig(cfile);
        }
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(cfile), "UTF8"))) {
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
            config = (new Gson()).fromJson(sb.toString(), SpawnerConfig.class);
            if(checkFormat()) {
                saveConfig(cfile);
            }
        }
    }
    
    public static void loadConfig() {
        JavaPlugin plugin = OTD_MythicTweak.instance;
        // make sure file exists
        File configDir = plugin.getDataFolder();
	if(!configDir.exists()){
            configDir.mkdir();
	}
        
        String world_config_file = configDir.toString() + File.separator + CONFIG_FILE_NAME;
        File cfile = new File(world_config_file);
        if(!cfile.exists()){
            saveConfig();
        }
        try {
            loadConfig(cfile);
        } catch(IOException ex) {
            OTD_MythicTweak.consoleError("[OTD_MythicTweak] Cannot load config file.");
            config = new SpawnerConfig();
            checkFormat();
        }
    }
    
    private static boolean checkFormat() {
        boolean res = false;
        if(config.roguelike == null) {
            res = true;
            config.roguelike = new HashMap<>();
        }
        if(config.roguelike_status == null) {
            res = true;
            config.roguelike_status = new HashMap<>();
        }
        if(config.roguelike_settings == null) {
            res = true;
            config.roguelike_settings = new HashMap<>();
        }
        for(int i = 0; i < 5; i++) {
            if(!config.roguelike.containsKey(i)) {
                res = true;
                config.roguelike.put(i, new HashMap<>());
            }
            if(!config.roguelike_status.containsKey(i)) {
                res = true;
                config.roguelike_status.put(i, false);
            }
            if(!config.roguelike_settings.containsKey(i)) {
                res = true;
                config.roguelike_settings.put(i, new SpawnerSetting());
            }
            
            Map<String, List<String>> mobmap = config.roguelike.get(i);
            for(MythicSpawnerType mob : MythicSpawnerType.values()) {
                String name = mob.toString();
                if(!mobmap.containsKey(name)) {
                    res = true;
                    mobmap.put(name, new ArrayList<>());
                }
            }
        }
        return res;
    }
}
