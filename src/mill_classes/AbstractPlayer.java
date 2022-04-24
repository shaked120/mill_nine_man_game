package mill_classes;

public abstract class AbstractPlayer {
    protected final Color color;
//constructor
    protected AbstractPlayer(Color color) {
        this.color = color;
    }

    public char getToken(){
        return color == null ? ' ' : color.toString().charAt(0);
    }

    public Color getColor() {return this.color;}
}