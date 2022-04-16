package Mill_project;

import java.util.ArrayList;

public class PlayerTurn {
    ArrayList<AbstractJump> actions = new ArrayList<>();

    public PlayerTurn(ManSoldier man) {
        makeMovePhaseOne(man);
    }

    public PlayerTurn() {
        makeMovePhaseTwo();
    }

    public void makeMovePhaseTwo() {
        boolean success = false;
        while (!success) {
            int menLeft = Board.getInstance().howManyMenCurrentPlayer();
            if (menLeft < MainGame.MINIMUM_MAN_ON_BOARD) {
                MainGame.win = true;
                break;
            }

            System.out.println("Player " + MainGame.getInstance().currentTurn.getColor() + ", it is your turn.");
            //for more than 3, Slide
            if (menLeft > MainGame.MINIMUM_MAN_ON_BOARD)
                success = makeSlide();

            //when 3 left, can jump
            if (menLeft == MainGame.MINIMUM_MAN_ON_BOARD)
                success = makeHop();

        }
    }

    public void makeMovePhaseOne(ManSoldier m) {
        boolean success = false;
        while (!success) {
            System.out.println("Choose a house for a " + MainGame.getInstance().currentTurn.getColor() + " Man:");
            int destination = MainGame.getInstance().currentTurn.readInt();
            PlaceMan move = new PlaceMan();
            if (move.check(destination, m)) {
                move.exec();
                actions.add(move);
                makeTake(destination, actions);
                success = true;
            }
        }
    }

    void makeTake(int src, ArrayList<AbstractJump> actions) {
        if (MillCheck.checkMills(src)) {
            KindOfGui.getInstance().UpdateBoard();
            System.out.println("You can take an opponent man out. "
                    + "\nChoose a house that contains an opponent man:");
            src = MainGame.getInstance().currentTurn.readInt();
            RemoveMan move = new RemoveMan();
            while (!move.check(src)) {
                src = MainGame.getInstance().currentTurn.readInt();
            }
            move.exec();
            actions.add(move);
        }
    }

    boolean makeSlide() {
        boolean checking = false;
        SlideMill move = new SlideMill();
        while (!checking) {
            System.out.println("Slide one of your men from its house."
                    + " Choose a house that contains your man and a direction to slide to:"
                    + "\n{Up:" + move.UP + ", Down:" + move.DOWN + ", Right:" + move.RIGHT + ", left:" + move.LEFT + "}");
            int src = MainGame.getInstance().currentTurn.readInt();
            int dir = MainGame.getInstance().currentTurn.readInt();
            checking = move.check(src, dir);
        }
        move.exec();
        actions.add(move);
        makeTake(move.getDestination().getId(), actions);
        return true;
    }

    boolean makeHop() {
        boolean checking = false;
        Jump move = new Jump();
        while (!checking) {
            System.out.println("You can jump. "
                    + "Choose a house that contains your man and a destination house:");
            int src = MainGame.getInstance().currentTurn.readInt();
            int dst = MainGame.getInstance().currentTurn.readInt();
            checking = move.check(src, dst);
        }
        move.exec();
        actions.add(move);
        makeTake(move.getDestination().getId(), actions);
        return true;
    }
}