package mill_classes;

public class ManSoldier {
    public boolean isOut = false;
    private HouseInBoard house = null;
    private final Color color;
//return the token by the color of the soldier
    public char getToken() {
        return color == null ? ' ' : color.toString().charAt(0);
    }

    public void setHouse(HouseInBoard h) {
        house = h;
    }
//constructor
    public ManSoldier(Color c) {
        color = c;
    }

    public int getHouseId() {
        return house == null ? -1 : house.getId();
    }

    public Color getColor() {
        return color;
    }
}