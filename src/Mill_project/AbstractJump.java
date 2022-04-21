package Mill_project;

public abstract class AbstractJump {
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

    public HouseInBoard getDestination(){return this.destination;}
    public HouseInBoard getSource(){return this.source;}

    public int getSourceId() {
        return source == null ? -1 : source.getId();
    }

    public int getDestinationId() {
        return destination == null ? -1 : destination.getId();
    }
}