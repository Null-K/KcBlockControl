package com.puddingkc;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class KcBlockControl extends JavaPlugin implements Listener {

    private Set<Material> restrictedBlocks;
    private Set<String> restrictedWorlds;
    private String restrictedMessage;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadRestrictions();

        getServer().getPluginManager().registerEvents(this,this);
        Objects.requireNonNull(getCommand("blockcontrol")).setExecutor(new ReloadCommand(this));

        getLogger().info("KcBlockControl 启用成功");
        getLogger().info("作者QQ: 3116078709");
    }

    public void loadRestrictions() {
        reloadConfig();
        FileConfiguration config = getConfig();

        restrictedBlocks = new HashSet<>();
        for (String blockName : config.getStringList("restricted-blocks")) {
            try {
                Material material = Material.valueOf(blockName.toUpperCase());
                restrictedBlocks.add(material);
            } catch (IllegalArgumentException e) {
                getLogger().warning("无效的方块ID: " + blockName);
            }
        }
        restrictedWorlds = new HashSet<>(config.getStringList("restricted-worlds"));
        restrictedMessage = config.getString("restricted-message","").replace("&","§");
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockInteract(PlayerInteractEvent event) {
        if (event.getPlayer().hasPermission("kcblockcontrol.bypass")) { return; }
        if (event.getClickedBlock() != null && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (isRestrictedBlock(event.getClickedBlock().getType(), event.getPlayer().getWorld().getName())) {
                event.setCancelled(true);
                if (restrictedMessage != null && !restrictedMessage.isEmpty()) {
                    event.getPlayer().sendMessage(restrictedMessage);
                }
            }
        }
    }

    private boolean isRestrictedBlock(Material material, String worldName) {
        if (restrictedWorlds.contains("all")) {
            return restrictedBlocks.contains(material);
        }
        return restrictedBlocks.contains(material) && restrictedWorlds.contains(worldName);
    }
}