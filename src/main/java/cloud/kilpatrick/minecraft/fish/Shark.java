package cloud.kilpatrick.minecraft.fish;

public class Shark extends Fish {

    private final static int customModelData = 1013;
    private final static double rarity = 1.4;

    public Shark() {
        super(customModelData, rarity);
    }

    @Override
    public boolean hurtsToCatch() {
        return true;
    }

}
