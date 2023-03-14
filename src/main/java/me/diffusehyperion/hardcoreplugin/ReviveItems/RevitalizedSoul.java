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

import static me.diffusehyperion.hardcoreplugin.HardcorePlugin.plugin;
import static me.diffusehyperion.hardcoreplugin.HardcorePlugin.reviveT3;

public class RevitalizedSoul implements ReviveItem {
    private final ItemStack item;
    private final ItemMeta meta;

    public RevitalizedSoul() {
        item = new ItemStack(Material.POPPED_CHORUS_FRUIT, 1);
        item.setAmount(1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName("Revitalized Soul");
        List<String> lore = new ArrayList<>();
        lore.add("Right click to choose who to revive.");
        lore.add("Keep this in your inventory to revive yourself when you die.");
        lore.add("");
        lore.add("Combines the power of the two bosses.");
        lore.add("It leaves the target stronger than before!");
        meta.setLore(lore);
        this.meta = meta;
        item.setItemMeta(meta);

        NamespacedKey key1 = new NamespacedKey(plugin, "revitalized_soul1");
        ShapedRecipe recipe1 = new ShapedRecipe(key1, item);
        recipe1.shape("BHB",
                      "CLC",
                      "BEB");
        recipe1.setCategory(CraftingBookCategory.MISC);
        recipe1.setIngredient('H', Material.DRAGON_HEAD);
        recipe1.setIngredient('E', Material.ELYTRA);
        recipe1.setIngredient('C', Material.POPPED_CHORUS_FRUIT);
        recipe1.setIngredient('B', Material.DRAGON_BREATH);
        recipe1.setIngredient('L', new RecipeChoice.ExactChoice(reviveT3.getItem()));

        NamespacedKey key2 = new NamespacedKey(plugin, "revitalized_soul2");
        ShapedRecipe recipe2 = new ShapedRecipe(key2, item);
        recipe2.shape("BEB",
                      "CLC",
                      "BHB");
        recipe2.setCategory(CraftingBookCategory.MISC);
        recipe2.setIngredient('H', Material.DRAGON_HEAD);
        recipe2.setIngredient('E', Material.ELYTRA);
        recipe2.setIngredient('C', Material.POPPED_CHORUS_FRUIT);
        recipe2.setIngredient('B', Material.DRAGON_BREATH);
        recipe2.setIngredient('L', new RecipeChoice.ExactChoice(reviveT3.getItem()));

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
        return 25;
    }

    @Override
    public String getRawReviveMessage() {
        return "%target% jolts awake with the help of %savior%!";
    }

    public String getRawSelfReviveMessage() {
        return "...but %target% comes back stronger than before!";
    }

    @Override
    public String getName() {
        return "Revitalized Soul";
    }

    @Override
    public Sound getReviveSound() {
        return Sound.ENTITY_WITHER_DEATH;
    }
}
