package cloud.kilpatrick.minecraft;

import cloud.kilpatrick.minecraft.fish.Fish;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static org.reflections.scanners.Scanners.SubTypes;

public class ExtraFishPlugin extends JavaPlugin implements Listener {

    private Set<Fish> fish = new HashSet<>();

    @Override
    public void onEnable() {
        getLogger().info("onEnable is called!");

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
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        // Check if the fish was caught (event state FISHING is only cast)
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            Random random = new Random();

            for (Fish fish : this.fish) {
                double rarity = fish.getRarity();
                double roll = random.nextDouble();
                getLogger().info("Rolled a " + roll);
                if (roll < rarity) {

                    ItemStack pluginFish = new ItemStack(Material.SALMON); // Use salmon as the base item
                    ItemMeta meta = pluginFish.getItemMeta();
                    if (meta != null) {

                        meta.setDisplayName(fish.getClass().getSimpleName());
                        pluginFish.setItemMeta(meta);
                    }

                    // Replace the caught item with the sturgeon
                    ((Item) event.getCaught()).setItemStack(pluginFish);
                }
            }
        }
    }

}