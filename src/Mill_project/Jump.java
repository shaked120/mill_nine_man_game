package Mill_project;

public class Jump extends AbstractJump {

    //check if the jump is possible
    public boolean check(int src, int dest){
        if ( Board.getInstance().minHouseId <= dest && dest <=Board.getInstance().maxHouseId){
            if (Board.getInstance().getHouses().get(src).getMan().getColor()
                    != MainGame.getInstance().currentTurn.getColor()){
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