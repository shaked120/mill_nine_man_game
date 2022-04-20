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


}