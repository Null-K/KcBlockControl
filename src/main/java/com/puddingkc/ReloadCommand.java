package com.puddingkc;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    private final KcBlockControl plugin;
    public ReloadCommand(KcBlockControl plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender.hasPermission("kcblockcontrol.reload")) {
            plugin.loadRestrictions();
            sender.sendMessage(ChatColor.WHITE + "配置文件重载完成");
            return true;
        }
        return false;
    }

}
