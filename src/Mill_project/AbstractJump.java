package Mill_project;

public abstract class AbstractJump {
    protected ManSoldier man = null;
    protected HouseInBoard destination = null;
    protected HouseInBoard source = null;

    public abstract void exec();
    public ManSoldier getMan(){return this.man;}
    public HouseInBoard getDestination(){return this.destination;}
    public HouseInBoard getSource(){return this.source;}
}