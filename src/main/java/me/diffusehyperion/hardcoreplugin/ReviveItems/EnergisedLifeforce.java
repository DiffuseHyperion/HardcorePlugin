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
import static me.diffusehyperion.hardcoreplugin.HardcorePlugin.reviveT2;

public class EnergisedLifeforce implements ReviveItem {
    private final ItemStack item;
    private final ItemMeta meta;

    public EnergisedLifeforce() {
        item = new ItemStack(Material.NETHER_STAR, 1);
        item.setAmount(1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName("Energised Lifeforce");
        List<String> lore = new ArrayList<>();
        lore.add("Right click to choose who to revive.");
        lore.add("Keep this in your inventory to revive yourself when you die.");
        lore.add("");
        lore.add("Collects and purifies the souls in the wither.");
        lore.add("No penalties to the target... but there is room for improvement!");
        meta.setLore(lore);
        this.meta = meta;
        item.setItemMeta(meta);

        NamespacedKey key1 = new NamespacedKey(plugin, "energised_lifeforce1");
        ShapedRecipe recipe1 = new ShapedRecipe(key1, item);
        recipe1.shape("SNS",
                      "NBN",
                      "SNS");
        recipe1.setCategory(CraftingBookCategory.MISC);
        recipe1.setIngredient('S', Material.NETHER_STAR);
        recipe1.setIngredient('N', Material.NETHERITE_INGOT);
        recipe1.setIngredient('B', new RecipeChoice.ExactChoice(reviveT2.getItem()));

        NamespacedKey key2 = new NamespacedKey(plugin, "energised_lifeforce2");
        ShapedRecipe recipe2 = new ShapedRecipe(key2, item);
        recipe2.shape("NSN",
                      "SBS",
                      "NSN");
        recipe2.setCategory(CraftingBookCategory.MISC);
        recipe2.setIngredient('S', Material.NETHER_STAR);
        recipe2.setIngredient('N', Material.NETHERITE_INGOT);
        recipe2.setIngredient('B', new RecipeChoice.ExactChoice(reviveT2.getItem()));

        Bukkit.addRecipe(recipe1);
        Bukkit.addRecipe(recipe2);
    }

    public ItemStack getItem() {
        return item;
    }

    public ItemMeta getMeta() {
        return meta;
    }

    public int getEffectiveness() {
        return 20;
    }

    @Override
    public String getRawReviveMessage() {
        return "%target% wakes back up with the help of %savior%!";
    }

    @Override
    public String getRawSelfReviveMessage() {
        return "...but %target% regains his footing!";
    }

    @Override
    public String getName() {
        return "Energised Lifeforce";
    }

    @Override
    public Sound getReviveSound() {
        return Sound.ENTITY_ENDER_DRAGON_GROWL;
    }
}
