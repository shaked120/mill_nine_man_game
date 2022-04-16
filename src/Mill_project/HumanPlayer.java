package Mill_project;

import java.util.Scanner;

public class HumanPlayer extends AbstractPlayer {
    private final Scanner in = new Scanner(System.in);

    public HumanPlayer(Color c){
        super(c);
    }

    public int readInt(){
        return in.nextInt();
    }
}