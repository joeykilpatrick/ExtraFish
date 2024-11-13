package cloud.kilpatrick.minecraft.fish;

public abstract class Fish {

    double rarity;

    protected Fish(double rarity) {
        this.rarity = rarity;
    }

    public double getRarity() {
        return rarity;
    }

}
