package Mill_project;

public class RemoveMan extends AbstractJump {

    public RemoveMan (HouseInBoard src) {
        super(src, true);
    }

    //check if the move is valid.
    public boolean check(int src){
        if (MIN_HOUSE_ID <= src && src <= MAX_HOUSE_ID){
            //if the chosen house is empty or if the chosen man is same as player
            if (Board.getInstance().getHouses().get(src).getMan().getColor() == null){
                return false;
            }

            if (Board.getInstance().getHouses().get(src).getMan().getColor() == Board.getInstance().getCurrentPlayer().getColor()){
                return false;
            }
            //if are all in mills, then the man can be removed
            if(!allMills() && MillCheck.checkMills(src)){
                return false;
            }
            source = Board.getInstance().getHouses().get(src);
            man = source.getMan();
            return true;
        }
        return false;
    }

    //Execute the move. Remove the man
    public void exec(){
        man.isOut=true;
        man.setHouse(null);
        //set the house to null man
        source.setMan(HouseInBoard.empty);
    }

    public boolean allMills(){
        //Should also check to see if all are in Mill, remove from Mill
        int counter = 0;
        int howmany = 0;
        for (ManSoldier m : Board.getInstance().getSoldiersCurrentPlayer()) {
            // if the man has not already been removed
            if(m.getHouse()!=null){
                if(MillCheck.checkMills(m.getHouse().getId()))
                    counter++;
                howmany++;
            }
        }

        return counter >= howmany;
    }

}