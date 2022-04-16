package Mill_project;

public class HouseInBoard {
    public static final ManSoldier empty = new ManSoldier(null);
    private HouseInBoard up = null;
    private HouseInBoard down = null;
    private HouseInBoard left = null;
    private HouseInBoard right = null;
    private ManSoldier man = empty;
    private int id;

    //neighbors getters and setters
    public HouseInBoard getUp() {return up;}
    public void setUp(HouseInBoard h) {up = h;}
    public HouseInBoard getDown() {return down;}
    public void setDown(HouseInBoard h) {down = h;}
    public HouseInBoard getLeft() {return left;}
    public void setLeft(HouseInBoard h) {left = h;}
    public HouseInBoard getRight() {return right;}
    public void setRight(HouseInBoard h) {right = h;}

    public ManSoldier getMan() {return man;}
    public void setMan(ManSoldier m) {man = m;}
    public int getId() {return id;}
    public void setId(int i) {id = i;}
    public boolean isEmpty(){return man.equals(empty);}

}