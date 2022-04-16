package Mill_project;

public class MainGame {
    private static final MainGame game = new MainGame();

    private MainGame() {
    }

    public static MainGame getInstance() {
        return game;
    }

    public static boolean win = false;
    AbstractPlayer currentTurn;
    AbstractPlayer Player1;
    AbstractPlayer Player2;
    int mode = 0;
    int numberOfMen = 9; //number of men left that can allow a man to jump
    final static int MINIMUM_MAN_ON_BOARD = 3;

    private void In_Moving_Mode() {
        int end_Counter = 150;
        while (!win) {
            end_Counter--;
//            if (MillCheck.canMove(Board.getInstance().white)) {
//                currentTurn = Player1;
//                new PlayerTurn();
//                KindOfGui.getInstance().UpdateBoard();
//                Board.getInstance().howManyMen(Board.getInstance().white);
//
//            } else
//                win = true;
//            if (MillCheck.canMove(Board.getInstance().black)) {
//                currentTurn = Player2;
//                new PlayerTurn();
//                KindOfGui.getInstance().UpdateBoard();
//                Board.getInstance().howManyMen(Board.getInstance().black);
//            } else
//                win = true;
            if (end_Counter <= 0)
                System.out.println("Draw.");
        }
        if (currentTurn == Player1)
            System.out.println("Game finished! Player " + Color.values()[1] + " won!");
        else
            System.out.println("Game finished! Player " + Color.values()[0] + " won!");
    }

    private void inReplaceMode() {
        System.out.println("Each Player gets " + numberOfMen + " Men to locate");
        int WhiteIndex = 0;
        int BlackIndex = 0;
        for (int i = 0; i < (2 * numberOfMen); i++) {
            if (i % 2 == 0) {
//                currentTurn = Player1;
//                new PlayerTurn(Board.getInstance().white.get(WhiteIndex));
//                KindOfGui.getInstance().UpdateBoard();
//                WhiteIndex++;

            } else {
//                currentTurn = Player2;
//                new PlayerTurn(Board.getInstance().black.get(BlackIndex));
//                KindOfGui.getInstance().UpdateBoard();
//                BlackIndex++;
            }
        }
    }

    private void set_Up() {
        Player1 = new HumanPlayer(Color.values()[0]);
        Player2 = new HumanPlayer(Color.values()[1]);
        Board.getInstance().setUp();
        KindOfGui.getInstance().UpdateBoard();
    }

    public void play() {
        set_Up();
        mode = 1;
        inReplaceMode();
        mode = 2;
        In_Moving_Mode();
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the game nine man's morris");
        MainGame.getInstance().play();
        System.out.println("the game has ended. good bye");
    }

}