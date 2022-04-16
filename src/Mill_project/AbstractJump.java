package Mill_project;

public abstract class AbstractJump {
    protected ManSoldier man = null;
    protected HouseInBoard destination = null;
    protected HouseInBoard source = null;

    protected AbstractJump() {}

    protected AbstractJump(HouseInBoard h, boolean isSource) {
        if (isSource) {
            this.source = h;
        } else {
            this.destination = h;
        }
    }

    protected AbstractJump(HouseInBoard src, HouseInBoard dest) {
        this.source = src;
        this.destination = dest;
    }

    public abstract void exec();
    public ManSoldier getMan(){return this.man;}
    public HouseInBoard getDestination(){return this.destination;}
    public HouseInBoard getSource(){return this.source;}
}