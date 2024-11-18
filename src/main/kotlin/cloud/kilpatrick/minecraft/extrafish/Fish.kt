package cloud.kilpatrick.minecraft.extrafish

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class Fish (
    val customModelData: Int,
    val rarity: Double = 1.0,
    val isPoisonous: Boolean = false,
    val hurtsToCatch: Boolean = false,
    val hurtsToEat: Boolean = false,
) {
    Sturgeon(1001),
    Urchin(1002, isPoisonous = true),
    Walleye(1003),
    Shrimp(1004),
    ElectricEel(1005, hurtsToCatch = true, hurtsToEat = true),
    Oyster(1006),
    Flounder(1007),
    Koi(1008),
    Squid(1009),
    GlowingSquid(1010),
    Octopus(1011),
    Lobster(1012),
    Shark(1013, hurtsToCatch = true),
    Bass(1014),
    BlueTang(1015),
    Clam(1016),
    Crab(1017),
    Gar(1018),
    Pike(1019),
    Piranha(1020),
    Seahorse(1021),
    Snail(1022),
    ;

    fun getDisplayName(): String {
        // Convert PascalCase names to separate words
        return this.name.replace("([a-z])([A-Z])".toRegex(), "$1 $2")
    }

    fun getItemStack(): ItemStack {
        val itemStack = ItemStack(Material.SALMON)
        val meta = itemStack.itemMeta!!
        meta.setDisplayName(this.getDisplayName())
        meta.setCustomModelData(this.customModelData)
        itemStack.itemMeta = meta
        return itemStack
    }

}
