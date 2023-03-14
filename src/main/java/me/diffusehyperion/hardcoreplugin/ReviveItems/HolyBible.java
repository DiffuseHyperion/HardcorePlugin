package me.diffusehyperion.hardcoreplugin.ReviveItems;

import me.diffusehyperion.hardcoreplugin.ReviveItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.recipe.CraftingBookCategory;

import java.util.ArrayList;
import java.util.List;

import static me.diffusehyperion.hardcoreplugin.HardcorePlugin.*;

public class HolyBible implements ReviveItem {
    private final ItemStack item;
    private final ItemMeta meta;

    public HolyBible() {
        item = new ItemStack(Material.BOOK, 1);
        item.setAmount(1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName("Holy Bible");
        List<String> lore = new ArrayList<>();
        lore.add("Right click to choose who to revive.");
        lore.add("Keep this in your inventory to revive yourself when you die.");
        lore.add("");
        lore.add("A strengthened book of necromancy due to prayers.");
        lore.add("Improves the effectiveness of the book.");
        meta.setLore(lore);
        this.meta = meta;
        item.setItemMeta(meta);

        NamespacedKey key1 = new NamespacedKey(plugin, "holy_bible1");
        ShapedRecipe recipe1 = new ShapedRecipe(key1, item);
        recipe1.shape("SGS",
                      "GBG",
                      "SGS");
        recipe1.setCategory(CraftingBookCategory.MISC);
        recipe1.setIngredient('S', Material.WITHER_SKELETON_SKULL);
        recipe1.setIngredient('G', Material.GHAST_TEAR);
        recipe1.setIngredient('B', new RecipeChoice.ExactChoice(reviveT1.getItem()));

        NamespacedKey key2 = new NamespacedKey(plugin, "holy_bible2");
        ShapedRecipe recipe2 = new ShapedRecipe(key2, item);
        recipe2.shape("GSG",
                      "SBS",
                      "GSG");
        recipe2.setCategory(CraftingBookCategory.MISC);
        recipe2.setIngredient('S', Material.WITHER_SKELETON_SKULL);
        recipe2.setIngredient('G', Material.GHAST_TEAR);
        recipe2.setIngredient('B', new RecipeChoice.ExactChoice(reviveT1.getItem()));

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
        return 15;
    }

    @Override
    public String getRawReviveMessage() {
        return "From the heavens, %target% was revived by %savior%!";
    }

    public String getRawSelfReviveMessage() {
        return "...but %target% got his second wind!";
    }

    @Override
    public String getName() {
        return "Holy Bible";
    }

    @Override
    public Sound getReviveSound() {
        return Sound.BLOCK_BEACON_ACTIVATE;
    }
}
