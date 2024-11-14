package cloud.kilpatrick.minecraft.fish;

public class Urchin extends Fish {

    private final static int customModelData = 1002;
    private final static double rarity = 0.2;

    public Urchin() {
        super(customModelData, rarity);
    }

    @Override
    public boolean isPoisonous() {
        return true;
    }

    @Override
    public boolean hurtsToEat() {
        return true;
    }



}
