package com.ohthedungeon.mythictweak.commands;

import com.ohthedungeon.mythictweak.config.SpawnerConfig;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandInfo implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender == null) return true;
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(!p.hasPermission("otd_mt.admin")) {
                p.sendMessage(ChatColor.RED + "You don't have permission to do that");
                return true;
            }
        }
        
        String[] strs = getInfo();
        
        for(String str : strs) {
            sender.sendMessage(ChatColor.BLUE + str);
        }
        
        sender.sendMessage(ChatColor.BLUE + "Done");
        return true;
    }
    
    public static String[] getInfo() {
        Map<Integer, Boolean> map = SpawnerConfig.getRoguelikeStatus();
        String[] strs = new String[] {
            "Roguelike Dungeon MythicMobs Information",
            "y=50~60, " + Boolean.toString(map.get(0)),
            "y=40~50, " + Boolean.toString(map.get(1)),
            "y=30~40, " + Boolean.toString(map.get(2)),
            "y=20~30, " + Boolean.toString(map.get(3)),
            "y=10~20, " + Boolean.toString(map.get(4)),
            "Use /otd_mt_reload to reload config",
        };
        return strs;
    }
}

