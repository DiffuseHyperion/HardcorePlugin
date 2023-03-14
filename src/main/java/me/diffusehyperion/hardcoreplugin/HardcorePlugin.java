package me.diffusehyperion.hardcoreplugin;

import github.scarsz.discordsrv.util.DiscordUtil;
import me.diffusehyperion.hardcoreplugin.ReviveItems.BookOfNecromancy;
import me.diffusehyperion.hardcoreplugin.ReviveItems.EnergisedLifeforce;
import me.diffusehyperion.hardcoreplugin.ReviveItems.HolyBible;
import me.diffusehyperion.hardcoreplugin.ReviveItems.RevitalizedSoul;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import static me.diffusehyperion.hardcoreplugin.DataManager.setPlayerMaxHP;

public final class HardcorePlugin extends JavaPlugin implements Listener {

    public static Plugin plugin;
    public static ReviveItem reviveT1;
    public static ReviveItem reviveT2;
    public static ReviveItem reviveT3;
    public static ReviveItem reviveT4;
    public static ItemStack oldReviveItem1;
    public static ItemMeta oldReviveMeta1;
    public static ItemStack oldReviveItem2;
    public static ItemMeta oldReviveMeta2;
    public static FileConfiguration config;

    @Override
    public void onEnable() {
        plugin = this;
        this.saveDefaultConfig();
        config = getConfig();

        getServer().getPluginManager().registerEvents(new GoldenAppleListener(), this);
        getServer().getPluginManager().registerEvents(new DataManager(), this);
        getServer().getPluginManager().registerEvents(this, this);

        reviveT1 = new BookOfNecromancy();
        reviveT2 = new HolyBible();
        reviveT3 = new EnergisedLifeforce();
        reviveT4 = new RevitalizedSoul();

        new ReviveGUI(reviveT1);
        new ReviveGUI(reviveT2);
        new ReviveGUI(reviveT3);
        new ReviveGUI(reviveT4);
        createOldItems();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (containsReviveItem(p.getInventory())) {
            ReviveItem reviveItem = getReviveItem(p.getInventory());
            assert reviveItem != null;

            // actual effects
            p.getInventory().removeItem(reviveItem.getItem());
            e.getDrops().remove(reviveItem.getItem());
            Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(reviveItem.getEffectiveness());
            setPlayerMaxHP(p.getDisplayName(), reviveItem.getEffectiveness());

            // flair
            e.setDeathMessage(e.getDeathMessage() + "\n" + ReviveItem.getSelfReviveMessage(reviveItem, p.getDisplayName()));
            p.playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
        } else {
            // da ban
            Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Singapore"));
            c.add(Calendar.DATE, 1);
            Date d = c.getTime();

            p.getInventory().clear();

            Bukkit.getBanList(BanList.Type.NAME).addBan(p.getDisplayName(),
                    e.getDeathMessage() + "\nYou will be automatically revived in 1 day, or when someone uses a revive on you.",
                    d, null);
            p.kickPlayer(e.getDeathMessage() + "\nYou will be automatically revived at " + d + ", or when someone uses a revive on you.");

            e.setDeathMessage(e.getDeathMessage() + " and will be revived at " + d);
        }
    }

    public static ReviveItem getReviveItem(Inventory inv) {
        int tier = 0;
        for (ItemStack i : inv.getContents()) {
            if (Objects.nonNull(i)) {
                if (Objects.equals(i.getItemMeta(), reviveT1.getMeta())) {
                    tier = Math.max(tier, 1);
                } else if (Objects.equals(i.getItemMeta(), reviveT2.getMeta())) {
                    tier = Math.max(tier, 2);
                } else if (Objects.equals(i.getItemMeta(), reviveT3.getMeta())) {
                    tier = Math.max(tier, 3);
                } else if (Objects.equals(i.getItemMeta(), reviveT4.getMeta())) {
                    tier = 4;
                }
            }
        }
        switch (tier) {
            case 1:
                return reviveT1;
            case 2:
                return reviveT2;
            case 3:
                return reviveT3;
            case 4:
                return reviveT4;
            default:
                return null;
        }
    }

    public static boolean containsReviveItem(Inventory inv) {
        for (ItemStack i : inv.getContents()) {
            if (Objects.nonNull(i) && (
                    Objects.equals(i.getItemMeta(), reviveT1.getMeta()) ||
                            Objects.equals(i.getItemMeta(), reviveT2.getMeta()) ||
                            Objects.equals(i.getItemMeta(), reviveT3.getMeta()) ||
                            Objects.equals(i.getItemMeta(), reviveT4.getMeta()))) {
                return true;
            }
        }
        return false;
    }

    public static void sendDiscordMessage(String s) {
        if (Bukkit.getPluginManager().getPlugin("DiscordSRV") != null && config.getString("DiscordSRV.channelID") != null) {
            DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(config.getString("DiscordSRV.channelID")), s);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        for (ItemStack i : e.getPlayer().getInventory()) {
            if (Objects.nonNull(i) && (Objects.equals(i.getItemMeta(), oldReviveMeta1) || Objects.equals(i.getItemMeta(), oldReviveMeta2))) {
                i.setItemMeta(reviveT1.getMeta());
            }
        }
    }

    @EventHandler
    public void onInvOpen(InventoryOpenEvent e) {
        for (ItemStack i : e.getInventory()) {
            if (Objects.nonNull(i) && (Objects.equals(i.getItemMeta(), oldReviveMeta1) || Objects.equals(i.getItemMeta(), oldReviveMeta2))) {
                i.setItemMeta(reviveT1.getMeta());
            }
        }
    }

    private void createOldItems() {
        oldReviveItem1 = new ItemStack(Material.BOOK, 1);
        oldReviveItem1.setAmount(1);
        ItemMeta oldReviveMeta1 = oldReviveItem1.getItemMeta();
        assert oldReviveMeta1 != null;
        oldReviveMeta1.addEnchant(Enchantment.DURABILITY, 1, true);
        oldReviveMeta1.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        oldReviveMeta1.setDisplayName("Book of Necromancy");
        List<String> oldLore1 = new ArrayList<>();
        oldLore1.add("Used to bring people from the dead.");
        oldLore1.add("Right click to see your options.");
        oldReviveMeta1.setLore(oldLore1);
        HardcorePlugin.oldReviveMeta1 = oldReviveMeta1;
        oldReviveItem1.setItemMeta(oldReviveMeta1);

        oldReviveItem2 = new ItemStack(Material.BOOK, 1);
        oldReviveItem2.setAmount(1);
        ItemMeta oldReviveMeta2 = oldReviveItem2.getItemMeta();
        assert oldReviveMeta2 != null;
        oldReviveMeta2.addEnchant(Enchantment.DURABILITY, 1, true);
        oldReviveMeta2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        oldReviveMeta2.setDisplayName("Book of Necromancy");
        List<String> oldLore2 = new ArrayList<>();
        oldLore2.add("Used to bring people from the dead.");
        oldLore2.add("Right click to see your options.");
        oldLore2.add("");
        oldLore2.add("Keep this in your inventory to revive yourself when you die.");
        oldReviveMeta2.setLore(oldLore2);
        HardcorePlugin.oldReviveMeta2 = oldReviveMeta2;
        oldReviveItem2.setItemMeta(oldReviveMeta2);
    }
}
