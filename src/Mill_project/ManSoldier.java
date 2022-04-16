package Mill_project;

public class ManSoldier {
    public boolean isOut = false;
    private HouseInBoard house = null;
    private final Color color;

    public char getToken() {
        return color == null ? ' ' : color.toString().charAt(0);
    }

    public void setHouse(HouseInBoard h) {
        house = h;
    }

    public ManSoldier(Color c) {
        color = c;
    }

    public HouseInBoard getHouse() {
        return house;
    }

    public Color getColor() {
        return color;
    }
}