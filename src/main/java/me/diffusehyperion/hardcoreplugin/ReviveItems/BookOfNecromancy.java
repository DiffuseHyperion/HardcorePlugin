package me.diffusehyperion.hardcoreplugin.ReviveItems;

import me.diffusehyperion.hardcoreplugin.ReviveItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.recipe.CraftingBookCategory;

import java.util.ArrayList;
import java.util.List;

import static me.diffusehyperion.hardcoreplugin.HardcorePlugin.plugin;

public class BookOfNecromancy implements ReviveItem {
    private final ItemStack item;
    private final ItemMeta meta;

    public BookOfNecromancy() {
        item = new ItemStack(Material.BOOK, 1);
        item.setAmount(1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName("Book of Necromancy");
        List<String> lore = new ArrayList<>();
        lore.add("Right click to choose who to revive.");
        lore.add("Keep this in your inventory to revive yourself when you die.");
        lore.add("");
        lore.add("Used to bring people from the dead...");
        lore.add("...but just barely.");
        meta.setLore(lore);
        this.meta = meta;
        item.setItemMeta(meta);

        // forgive me lord
        NamespacedKey key1 = new NamespacedKey(plugin, "book_of_necromancy1");
        ShapedRecipe recipe1 = new ShapedRecipe(key1, item);
        recipe1.shape("DGD",
                      "GBG",
                      "DGD");
        recipe1.setCategory(CraftingBookCategory.MISC);
        recipe1.setIngredient('D', Material.DIAMOND);
        recipe1.setIngredient('G', Material.GOLD_INGOT);
        recipe1.setIngredient('B', Material.BOOK);

        NamespacedKey key2 = new NamespacedKey(plugin, "book_of_necromancy2");
        ShapedRecipe recipe2 = new ShapedRecipe(key2, item);
        recipe2.shape("GDG",
                      "DBD",
                      "GDG");
        recipe2.setCategory(CraftingBookCategory.MISC);
        recipe2.setIngredient('D', Material.DIAMOND);
        recipe2.setIngredient('G', Material.GOLD_INGOT);
        recipe2.setIngredient('B', Material.BOOK);

        Bukkit.addRecipe(recipe1);
        Bukkit.addRecipe(recipe2);
    }

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public ItemMeta getMeta() {
        return meta;
    }

    @Override
    public int getEffectiveness() {
        // measures how much max hearts it will give to the target.
        return 10;
    }

    @Override
    public String getRawReviveMessage() {
        return "From the ashes, %target% was revived by %savior%!";
    }

    @Override
    public String getRawSelfReviveMessage() {
        return "...but %target% rises from the ground!";
    }

    @Override
    public String getName() {
        return "Book of Necromancy";
    }

    @Override
    public Sound getReviveSound() {
        return Sound.ENTITY_WARDEN_EMERGE;
    }


}
