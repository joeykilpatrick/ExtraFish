package cloud.kilpatrick.minecraft.fish;

public abstract class Fish {

    int metadataNum;
    double rarity;

    protected Fish(int metadataNum, double rarity) {
        this.metadataNum = metadataNum;
        this.rarity = rarity;
    }

    public int getMetadataNum() {
        return metadataNum;
    }

    public double getRarity() {
        return rarity;
    }

}
