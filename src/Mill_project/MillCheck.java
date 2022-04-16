package Mill_project;
import java.util.ArrayList;
public class MillCheck {

    //Checking if Mills happened
    public static boolean checkMills(int id){
        HouseInBoard home =  Board.getInstance().getHouses().get(id);
        if (home==null)
            return false;
        HouseInBoard right = home.getRight();
        HouseInBoard farRight = null;
        if(right!=null)
            farRight = right.getRight();
        HouseInBoard left = home.getLeft();
        HouseInBoard farLeft=null;
        if (left!=null)
            farLeft= left.getLeft();
        HouseInBoard up = home.getUp();
        HouseInBoard farUp = null;
        if(up!=null)
            farUp = up.getUp();
        HouseInBoard down = home.getDown();
        HouseInBoard farDown = null;
        if(down!=null)
            farDown = down.getDown();

        if(right!=null && left!=null)
            if(home.getMan().getColor()==right.getMan().getColor()
                    && home.getMan().getColor()==left.getMan().getColor())
                return true;
        if(down!=null && up!=null)
            if(home.getMan().getColor()==up.getMan().getColor()
                    && home.getMan().getColor()==down.getMan().getColor())
                return true;
        if(down!=null && farDown!=null)
            if(home.getMan().getColor()==down.getMan().getColor()
                    && home.getMan().getColor()==farDown.getMan().getColor())
                return true;
        if(up!=null && farUp!=null)
            if(home.getMan().getColor()==up.getMan().getColor()
                    && home.getMan().getColor()==farUp.getMan().getColor())
                return true;
        if(right!=null && farRight!=null)
            if(home.getMan().getColor()==right.getMan().getColor()
                    && home.getMan().getColor()==farRight.getMan().getColor())
                return true;
        if(left!=null && farLeft!=null)
            if(home.getMan().getColor()==left.getMan().getColor()
                    && home.getMan().getColor()==farLeft.getMan().getColor())
                return true;
        return false;
    }

    //Check if there are any moves available
    public static boolean canMove(ArrayList<ManSoldier> men){
        int numberOfMen = Board.getInstance().howManyMen(men);
        int trappedMen = 0 ;

        for(int i = 0; i<= Board.getInstance().maxHouseId; i++){
            HouseInBoard current = Board.getInstance().getHouses().get(i);

            //if the house is taken by the man of same color
            if (current.getMan()!=null
                    && current.getMan().getColor() == men.get(0).getColor()){
                //if the neighbors are free
                if ((current.getRight() == null || current.getRight().getMan().getColor() != null)
                        && (current.getLeft() == null || current.getLeft().getMan().getColor() != null)
                        && (current.getUp() == null || current.getUp().getMan().getColor() != null)
                        && (current.getDown() == null || current.getDown().getMan().getColor() != null)) {
                    trappedMen++;
                }
                //if no neighbor is free hence there's no space to move to
            }
        }
        //Check if the number of men that are unable to move is same as total men (Or higher to avoid mistakes and glitches)
        //Also considering that this situation only applies to slide, and not while jump is available.
        if ((numberOfMen != MainGame.MINIMUM_MAN_ON_BOARD) && (trappedMen >= numberOfMen) ){
            MainGame.win = true;
            return false;
        }
        return true;
    }
}