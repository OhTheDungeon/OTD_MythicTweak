package com.ohthedungeon.mythictweak;

import com.ohthedungeon.mythictweak.commands.CommandInfo;
import com.ohthedungeon.mythictweak.commands.CommandPreventWalled;
import com.ohthedungeon.mythictweak.commands.CommandReload;
import com.ohthedungeon.mythictweak.config.SpawnerConfig;
import com.ohthedungeon.mythictweak.util.MythicSpawnerHandler;
import forge_sandbox.greymerk.roguelike.worldgen.spawners.Spawnable;
import java.lang.reflect.Field;
import java.util.logging.Level;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author shadow_wind
 */
public class OTD_MythicTweak extends JavaPlugin {
    public static JavaPlugin instance;
    public boolean api_ready = false;
    
    public static void consoleError(String msg) {
        console(Level.SEVERE, msg);
    }
    
    public static void consoleInfo(String msg) {
        console(Level.INFO, msg);
    }
    
    private static void console(Level level, String msg) {
        Bukkit.getLogger().log(level, msg);
    }
    
    
    @Override
    public void onEnable() {
        OTD_MythicTweak.instance = this;
        try {
            Class otd = Class.forName("otd.Main");
            Field api = otd.getDeclaredField("api_version");
            api.setAccessible(true);
            Integer api_version = (Integer) api.get(null);
            if(api_version >= 1) this.api_ready = true;
        } catch(ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException ex) {
            consoleError(ChatColor.RED + "Reflection failed... Are you using the latest version of [Oh The Dungeons You'll Go]?");
            this.api_ready = false;
        }
        
        if(!this.api_ready) return;
        
        SpawnerConfig.loadConfig();
        
        consoleInfo(ChatColor.BLUE + "[OTD_MythicTweak] Start to hook [Oh The Dungeons You''ll Go] API");
        
        Spawnable.handler = new MythicSpawnerHandler();
        consoleInfo(ChatColor.BLUE + "[OTD_MythicTweak] Hook success");
        
        this.getCommand("otd_mt_info").setExecutor(new CommandInfo());
        this.getCommand("otd_mt_preventwalled").setExecutor(new CommandPreventWalled());
        this.getCommand("otd_mt_reload").setExecutor(new CommandReload());
        
        Bukkit.getScheduler().runTaskLater(this, () -> {
            String[] strs = CommandInfo.getInfo();
            for(String str : strs) {
                consoleInfo(ChatColor.BLUE + str);
            }
        }, 1L);
    }
    
    @Override
    public void onDisable() {
        this.api_ready = false;
        consoleInfo(this.getDescription().getName() + " is disabled");
    }
    
    
}
