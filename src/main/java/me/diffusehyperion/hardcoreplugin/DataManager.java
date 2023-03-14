package me.diffusehyperion.hardcoreplugin;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

import static me.diffusehyperion.hardcoreplugin.HardcorePlugin.plugin;

public class DataManager implements Listener {
    public static FileConfiguration data;
    public static File dataFile;

    public static final String dataName = "data.yml";
    public static final long delay = HardcorePlugin.config.getInt("RegenDelay"); // delay in seconds between each hp regen (1/2 hour)

    public DataManager() {
        File dir = plugin.getDataFolder();

        if(!dir.exists()) {
            dir.mkdirs();
        }

        dataFile = new File(dir, dataName);

        if (!dataFile.exists()) {
            plugin.saveResource(dataName, false);
        }

        data = new YamlConfiguration();

        try {
            data.load(dataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String maxHPPath = p.getDisplayName() + ".maxHP";
        if (!data.contains(p.getDisplayName())) {
            data.createSection(p.getDisplayName());
            data.createSection(maxHPPath);
            data.set(maxHPPath, 20);
            saveData();
        }
        p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(data.getInt(maxHPPath));
        new BukkitRunnable() {
            @Override
            public void run() {
                if (p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() < 20) {
                    p.playSound(p, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1);
                    p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "You regained half a heart!");
                    int newHP = (int) p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + 1;
                    p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newHP);
                    data.set(maxHPPath, newHP);
                    saveData();
                }
            }
        }.runTaskTimer(plugin, delay * 20, delay * 20);
    }

    public static void saveData() {
        try {
            data.save(dataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setPlayerMaxHP(String pname, int newHP) {
        String maxHPPath = pname + ".maxHP";
        data.set(maxHPPath, newHP);
        saveData();
    }
}
