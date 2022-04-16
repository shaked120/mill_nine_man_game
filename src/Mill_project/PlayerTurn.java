package Mill_project;

import java.util.ArrayList;

public class PlayerTurn {
    ArrayList<AbstractJump> actions = new ArrayList<>();

    public PlayerTurn(ManSoldier man) {
        makeJumpPhaseOne(man);
    }

    public PlayerTurn() {
        makeJumpPhaseTwo();
    }

    public void makeJumpPhaseTwo() {
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

    public void makeJumpPhaseOne(ManSoldier m) {
        boolean success = false;
        while (!success) {
            System.out.println("Choose a house for a " + MainGame.getInstance().currentTurn.getColor() + " Man:");
            int destination = MainGame.getInstance().currentTurn.readInt();
            PlaceMan jump = new PlaceMan();
            if (jump.check(destination, m)) {
                jump.exec();
                actions.add(jump);
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
            RemoveMan jump = new RemoveMan(Board.getInstance().getHouses().get(src));
            while (!jump.check(src)) {
                src = MainGame.getInstance().currentTurn.readInt();
            }
            jump.exec();
            actions.add(jump);
        }
    }

    boolean makeSlide() {
        boolean checking = false;
        SlideMill jump = new SlideMill();
        while (!checking) {
            System.out.println("Slide one of your men from its house."
                    + " Choose a house that contains your man and a direction to slide to:"
                    + "\n{Up:" + jump.UP + ", Down:" + jump.DOWN + ", Right:" + jump.RIGHT + ", left:" + jump.LEFT + "}");
            int src = MainGame.getInstance().currentTurn.readInt();
            int dir = MainGame.getInstance().currentTurn.readInt();
            checking = jump.check(src, dir);
        }
        jump.exec();
        actions.add(jump);
        makeTake(jump.getDestination().getId(), actions);
        return true;
    }

    boolean makeHop() {
        boolean checking = false;
        Jump jump = new Jump();
        while (!checking) {
            System.out.println("You can jump. "
                    + "Choose a house that contains your man and a destination house:");
            int src = MainGame.getInstance().currentTurn.readInt();
            int dst = MainGame.getInstance().currentTurn.readInt();
            checking = jump.check(src, dst);
        }
        jump.exec();
        actions.add(jump);
        makeTake(jump.getDestination().getId(), actions);
        return true;
    }
}