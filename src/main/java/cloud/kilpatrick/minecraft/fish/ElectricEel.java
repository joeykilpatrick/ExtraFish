package cloud.kilpatrick.minecraft.fish;

public class ElectricEel extends Fish {

    private final static int customModelData = 1005;
    private final static double rarity = 0.4;

    public ElectricEel() {
        super(customModelData, rarity);
    }

    @Override
    public boolean hurtsToCatch() {
        return true;
    }

}
