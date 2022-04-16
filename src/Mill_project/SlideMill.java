package Mill_project;

public class SlideMill extends AbstractJump {
    //The following numbering is based on number-pad location of numbers.
    //Increase of usability quality aspect.
    final int UP = 2;
    final int LEFT = 4;
    final int RIGHT = 6;
    final int DOWN = 8;

    //check if the slide is possible
    public boolean check(int src, int direction){
        if ( Board.getInstance().minHouseId <= src && src <= Board.getInstance().maxHouseId){
            source = Board.getInstance().getHouses().get(src);

            if (Board.getInstance().getHouses().get(src).getMan().getColor()
                    != MainGame.getInstance().currentTurn.getColor()){
                System.out.println("Choose a house that belongs to you.");
                return false;
            }
            man = source.getMan();
            //or, change it to a house on one of the neighbors id
            switch(direction){
                case UP:
                    destination = source.getUp();
                    break;
                case LEFT:
                    destination = source.getLeft();
                    break;
                case RIGHT:
                    destination = source.getRight();
                    break;
                case DOWN:
                    destination = source.getDown();
                    break;
                default:
                    //print out something is wrong
                    System.out.println("Enter a valid direction.");
                    return false;
            }

            if (destination!= null)
                if (destination.getMan().getColor() == null)
                    return true;
                else
                    System.out.println("Chosen destination is not empty.");
            else
                System.out.println("Chose an existing neighbour!");
            return false;
        }
        else
            System.out.println("The chosen destination is not on the board.");
        return false;
    }

    //execute the move
    public void exec(){
        destination.setMan(man);
        man.setHouse(destination);
        source.setMan(HouseInBoard.empty);
    }
}