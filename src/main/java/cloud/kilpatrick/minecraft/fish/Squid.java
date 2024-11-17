package cloud.kilpatrick.minecraft.fish;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;

public class Squid extends Fish {

    private final static int customModelData = 1009;
    private final static double rarity = 1.4;

    public Squid() {
        super(customModelData, rarity);
    }

    @Override
    public Set<Recipe> getRecipes(Plugin plugin) {
        Set<Recipe> recipes = new HashSet<>();

        ShapelessRecipe inkSackRecipe = new ShapelessRecipe(new NamespacedKey(plugin, "Squid-InkSac"), new ItemStack(Material.INK_SAC));
        inkSackRecipe.addIngredient(new RecipeChoice.ExactChoice(this.itemStack));
        recipes.add(inkSackRecipe);

        return recipes;
    }

}
