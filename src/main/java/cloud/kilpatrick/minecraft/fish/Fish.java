package cloud.kilpatrick.minecraft.fish;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Fish {

    double rarity;
    ItemStack itemStack;

    protected Fish(int customModelData, double rarity) {
        this.rarity = rarity;

        this.itemStack = new ItemStack(Material.SALMON); // Use salmon as the base item
        ItemMeta meta = this.itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(this.getClass().getSimpleName());
        meta.setCustomModelData(customModelData);
        this.itemStack.setItemMeta(meta);
    }

    public double getRarity() {
        return rarity;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public boolean hurtsToCatch() {
        return false;
    }

    public boolean isPoisonous() {
        return false;
    }

    public boolean hurtsToEat() {
        return false;
    }

}
