package Mill_project;

public class HouseInBoard {
    public static final ManSoldier empty = new ManSoldier(null);
    private ManSoldier man = empty;
    private int id;

    public ManSoldier getMan() {
        return man;
    }

    public void setMan(ManSoldier m) {
        man = m;
    }

    public int getId() {
        return id;
    }

    public void setId(int i) {
        id = i;
    }

    public boolean isEmpty() {
        return man.equals(empty);
    }
}