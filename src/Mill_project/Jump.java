package Mill_project;

public class Jump extends AbstractJump {

    public Jump(HouseInBoard src, HouseInBoard dest) {
        super(src, dest);
    }

    //check if the jump is possible
    public boolean check(int src, int dest){
        if (MIN_HOUSE_ID <= dest && dest <= MAX_HOUSE_ID){
            if (Board.getInstance().getHouses().get(src).getMan().getColor()
                    != Board.getInstance().getCurrentPlayer().getColor()){
                System.out.println("Choose a house that belongs to you.");
                return false;
            }
            if (Board.getInstance().getHouses().get(dest).getMan().getColor()== null){
                source = Board.getInstance().getHouses().get(src);
                destination = Board.getInstance().getHouses().get(dest);
                man = source.getMan();
                return true;
            }
            else{
                System.out.println("the destination you have chosen is not empty");
                return false;
            }
        }
        else
            System.out.println("the destination you have chosen is not on the board");
        return false;
    }
    //execute jump
    public void exec(){
        destination.setMan(man);
        man.setHouse(destination);
        source.setMan(HouseInBoard.empty);
    }
}