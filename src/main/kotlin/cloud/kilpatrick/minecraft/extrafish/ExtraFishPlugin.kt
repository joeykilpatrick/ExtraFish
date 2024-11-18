package cloud.kilpatrick.minecraft.extrafish

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.FurnaceRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.random.Random

class ExtraFishPlugin : JavaPlugin(), Listener {

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)

        // Remove existing Salmon->CookedSalmon recipe
        val recipes = server.recipeIterator()
        while (recipes.hasNext()) {
            val recipe = recipes.next()

            if (recipe is FurnaceRecipe && recipe.result.type == Material.COOKED_SALMON) {
                recipes.remove()
                break
            }
        }

        // Add back "exact" Salmon->CookedSalmon recipe
        val cookedSalmonRecipe = FurnaceRecipe(
            NamespacedKey(this, "CookedSalmon"),
            ItemStack(Material.COOKED_SALMON),
            RecipeChoice.ExactChoice(ItemStack(Material.SALMON)),
            0.35f,
            200
        )
        server.addRecipe(cookedSalmonRecipe)

        val inkSacRecipe = ShapelessRecipe(
            NamespacedKey(this, "Squid-InkSac"),
            ItemStack(Material.INK_SAC),
        )
        inkSacRecipe.addIngredient(RecipeChoice.ExactChoice(Fish.Squid.getItemStack()))
        server.addRecipe(inkSacRecipe)

        val glowInkSacRecipe = ShapelessRecipe(
            NamespacedKey(this, "GlowingSquid-GlowInkSac"),
            ItemStack(Material.GLOW_INK_SAC),
        )
        glowInkSacRecipe.addIngredient(RecipeChoice.ExactChoice(Fish.GlowingSquid.getItemStack()))
        server.addRecipe(glowInkSacRecipe)

    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val version = description.version
        event.player.setResourcePack("https://github.com/joeykilpatrick/ExtraFish/releases/download/v$version/ExtraFish-$version-resource-pack.zip")
    }

    @EventHandler
    fun onPlayerFish(event: PlayerFishEvent) {
        // Check if the fish was caught (event state FISHING is only cast)
        if (event.state != PlayerFishEvent.State.CAUGHT_FISH) {
            return;
        }

        // Leave a 30% chance to leave caught fish unchanged
        if (Random.nextDouble() < 0.30) {
            return;
        }

        val totalRarity = Fish.values().map(Fish::rarity).sum()
        var roll = Random.nextDouble() * totalRarity

        var caughtFish: Fish? = null;

        for (fish in Fish.values()) {
            if (roll < fish.rarity) {
                caughtFish = fish
                // Replace the caught item with the plugin fish
                val caught = event.caught as Item
                caught.itemStack = fish.getItemStack()
                break;
            } else {
                roll -= fish.rarity;
            }
        }

        // Could be null in rare cases due to floating point arithmetic
        if (caughtFish == null) {
            return;
        }

        if (caughtFish.hurtsToCatch) {
            event.player.damage(2.0);
        }
    }

    @EventHandler
    fun onItemConsume(event: PlayerItemConsumeEvent) {
        val eaten = event.item
        val player = event.player

        for (fish in Fish.values()) {
            if (eaten.isSimilar(fish.getItemStack())) {
                if (fish.hurtsToEat) {
                    player.damage(4.0);
                }
                if (fish.isPoisonous) {
                    player.addPotionEffect(PotionEffect(PotionEffectType.POISON, 300, 1));
                }
                break;
            }
        }
    }

}