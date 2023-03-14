package me.diffusehyperion.hardcoreplugin;

import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public interface ReviveItem {

    ItemStack getItem();

    ItemMeta getMeta();

    int getEffectiveness();

    String getRawReviveMessage();

    String getRawSelfReviveMessage();

    String getName();

    Sound getReviveSound();

    static String getReviveMessage(ReviveItem item, String target, String savior) {
        return item.getRawReviveMessage().replace("%target%", target).replace("%savior%", savior);
    }

    static String getSelfReviveMessage(ReviveItem item, String target) {
        return item.getRawSelfReviveMessage().replace("%target%", target);
    }

}
