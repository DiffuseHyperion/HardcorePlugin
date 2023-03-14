package me.diffusehyperion.hardcoreplugin;


import org.bukkit.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.Objects;

import static me.diffusehyperion.hardcoreplugin.DataManager.data;
import static me.diffusehyperion.hardcoreplugin.DataManager.setPlayerMaxHP;
import static me.diffusehyperion.hardcoreplugin.HardcorePlugin.*;

public class ReviveGUI implements Listener {
    private final Inventory inv;
    private final ReviveItem item;

    public ReviveGUI(ReviveItem item) {
        inv = Bukkit.createInventory(null, 27, item.getName());
        this.item = item;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    // You can call this whenever you want to put the items in
    public void initializeItems() {
        for (BanEntry entry : Bukkit.getBanList(BanList.Type.NAME).getBanEntries()) {
            String name = entry.getTarget();
            inv.addItem(createSkullItem("Revive " + name, name));
        }
    }

    // Nice little method to create a gui item with a custom name, and description
    protected ItemStack createSkullItem(final String name, final String owner, final String... lore) {
        final ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        final SkullMeta meta = (SkullMeta) item.getItemMeta();

        // Set the name of the item
        assert meta != null;
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));
        meta.setOwnerProfile(Bukkit.getServer().createPlayerProfile(owner));
        item.setItemMeta(meta);

        return item;
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent) {
        inv.clear();
        initializeItems();
        ent.openInventory(inv);
    }

    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inv)) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player p = (Player) e.getWhoClicked();

        // Using slots click is a best option for your inventory click's
        ItemStack item = e.getInventory().getItem(e.getRawSlot());
        if (item != null && item.getType() == Material.PLAYER_HEAD) {
            String target = Objects.requireNonNull(item.getItemMeta()).getDisplayName().replace("Revive ", ""); // cursed af lmao

            // actual effects
            Bukkit.getBanList(BanList.Type.NAME).pardon(target);
            setPlayerMaxHP(target, this.item.getEffectiveness());
            p.closeInventory();
            p.getInventory().removeItem(this.item.getItem());

            // flair
            Bukkit.broadcastMessage(ChatColor.YELLOW + ReviveItem.getReviveMessage(this.item, target, p.getDisplayName()));
            sendDiscordMessage("***" + ReviveItem.getReviveMessage(this.item, target, p.getDisplayName()) + "***");
            for (Player p1 : Bukkit.getOnlinePlayers()) {
                p1.playSound(p1.getLocation(), this.item.getReviveSound(), 1, 0.75F);
            }
        }
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerUseReviveItem(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.hasItem() && Objects.equals(Objects.requireNonNull(e.getItem()).getItemMeta(), this.item.getMeta())) {
                openInventory(e.getPlayer());
            }
        }
    }
}
