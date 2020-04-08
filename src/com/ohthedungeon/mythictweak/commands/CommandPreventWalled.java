package com.ohthedungeon.mythictweak.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandPreventWalled implements CommandExecutor {
    
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
        sender.sendMessage(ChatColor.BLUE + "Run `OTD_MythicTweak.jar`, and click `How to Prevent Mob Stuck in Walls`");
        return true;
    }
}

