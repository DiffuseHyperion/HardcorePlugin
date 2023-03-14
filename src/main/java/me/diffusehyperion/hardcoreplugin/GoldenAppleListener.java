package me.diffusehyperion.hardcoreplugin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class GoldenAppleListener implements Listener {
    @EventHandler
    public void onGoldenAppleAte(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();
        int currentHP = (int) p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        if (e.getItem().getType() == Material.GOLDEN_APPLE && currentHP < 20) {
            int newHP = currentHP + 2;
            if (newHP > 20) {
                newHP = 20;
            }
            DataManager.setPlayerMaxHP(p.getDisplayName(), newHP);
            p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "You regained a heart for eating a golden apple!");
        }
    }
}
