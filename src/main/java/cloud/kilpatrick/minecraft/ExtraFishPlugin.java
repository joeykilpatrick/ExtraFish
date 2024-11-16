package cloud.kilpatrick.minecraft;

import cloud.kilpatrick.minecraft.fish.Fish;
import cloud.kilpatrick.minecraft.fish.Urchin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.reflections.Reflections;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static org.reflections.scanners.Scanners.SubTypes;

public class ExtraFishPlugin extends JavaPlugin implements Listener {

    private Set<Fish> fish;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        Reflections reflections = new Reflections("cloud.kilpatrick.minecraft.fish");
        this.fish = reflections.get(SubTypes.of(Fish.class).asClass())
                        .stream()
                        .map(clazz -> {
                            try {
                                return (Fish) clazz.getDeclaredConstructor().newInstance();
                            } catch (ReflectiveOperationException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toSet());

        // Remove existing Salmon->CookedSalmon recipe
        Iterator<Recipe> recipes = getServer().recipeIterator();
        while (recipes.hasNext()) {
            Recipe recipe = recipes.next();

            if (recipe instanceof FurnaceRecipe && recipe.getResult().getType() == Material.COOKED_SALMON) {
                recipes.remove();
            }
        }

        // Add back "exact" Salmon->CookedSalmon recipe
        Recipe cookedSalmonRecipe = new FurnaceRecipe(new NamespacedKey(this, "cooked-salmon"), new ItemStack(Material.COOKED_SALMON),  new RecipeChoice.ExactChoice(new ItemStack(Material.SALMON)), 0.35f, 200);
        getServer().addRecipe(cookedSalmonRecipe);
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        // Check if the fish was caught (event state FISHING is only cast)
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {

            Random random = new Random();

            // Leave a 30% chance to leave caught fish unchanged
            if (random.nextDouble() < 0.30) {
                return;
            }

            double totalRarity = this.fish.stream().map(Fish::getRarity).reduce(0.0, Double::sum);
            double roll = random.nextDouble() * totalRarity;

            Fish caughtFish = null;

            for (Fish fish : this.fish) {
                double rarity = fish.getRarity();
                if (roll < rarity) {
                    caughtFish = fish;
                    // Replace the caught item with the plugin fish
                    Item caught = (Item) event.getCaught();
                    assert caught != null;
                    caught.setItemStack(fish.getItemStack());
                    break;
                } else {
                    roll -= rarity;
                }
            }

            assert caughtFish != null;

            if (caughtFish.hurtsToCatch()) {
                event.getPlayer().damage(2.0);
            }
        }
    }

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent event) {
        ItemStack eaten = event.getItem();
        Player player = event.getPlayer();

        for (Fish fish : this.fish) {
            if (eaten.isSimilar(fish.getItemStack())) {
                if (fish.hurtsToEat()) {
                    player.damage(4.0);
                }
                if (fish.isPoisonous()) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 300, 1));
                }
                break;
            }
        }


    }

}