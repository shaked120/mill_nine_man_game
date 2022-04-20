package Mill_project;

public class PlaceMan extends AbstractJump {

    public PlaceMan(HouseInBoard dest) {
        super(dest, false);
    }

    public boolean check(int adrs, ManSoldier m) {
        if (MIN_HOUSE_ID <= adrs && adrs <= MAX_HOUSE_ID) {
            if (Board.getInstance().getHouses().get(adrs).getMan().getColor() == null) {
                destination = Board.getInstance().getHouses().get(adrs);
                man = m;
                return true;
            } else {
                System.out.println("The house you're choosing has already been filled."
                        + "\nChoose a different House.");
                return false;
            }
        } else
            System.out.println("Chosen destination is not on the board.");
        return false;
    }

    public void exec() {
        destination.setMan(man);
        man.setHouse(destination);
    }
}