package Mill_project;

import algoritmas.AlphaBetaPruning;
import algoritmas.ValuedJump;

import java.util.*;

public class Board {
    // UUID is unique identifier across all system (based on current millisecond)
    private final UUID boardID;

    public static final int NUMBER_OF_POSITIONS = 24;
    public static final int NUMBER_OF_STARTING_PIECES = 9;
    public static final boolean IS_FLYING_ALLOWED = false;

    private static Board board = new Board();

    public static final List<List<Integer>> POSSIBLE_MILLS;
    public static final List<List<Integer>> POSITION_TO_NEIGHBOURS;

    private static final Integer[][] positionToNeighboursArray = {
            {1, 9},
            {0, 2, 4},
            {1, 14},
            {4, 10},
            {1, 3, 5, 7},
            {4, 13},
            {7, 11},
            {4, 6, 8},
            {7, 12},
            {0, 10, 21},
            {3, 9, 11, 18},
            {6, 10, 15},
            {8, 13, 17},
            {5, 12, 14, 20},
            {2, 13, 23},
            {11, 16},
            {15, 17, 19},
            {12, 16},
            {10, 19},
            {16, 18, 20, 22},
            {13, 19},
            {9, 22},
            {19, 21, 23},
            {14, 22},
    };
    private static final Integer[][] possibleMillsArray = {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {9, 10, 11},
            {12, 13, 14},
            {15, 16, 17},
            {18, 19, 20},
            {21, 22, 23},
            {0, 9, 21},
            {3, 10, 18},
            {6, 11, 15},
            {1, 4, 7 },
            {16, 19, 22},
            {8, 12, 17},
            {5, 13, 20},
            {2, 14, 23},
    };

    static {
        List<List<Integer>> posToNeigh = new ArrayList<>();

        for (Integer[] value : positionToNeighboursArray) {
            posToNeigh.add(Collections.unmodifiableList(Arrays.asList(value)));
        }

        POSITION_TO_NEIGHBOURS = Collections.unmodifiableList(posToNeigh);

        List<List<Integer>> possMills = new ArrayList<>();

        for (Integer[] integers : possibleMillsArray) {
            possMills.add(Collections.unmodifiableList(Arrays.asList(integers)));
        }

        POSSIBLE_MILLS = Collections.unmodifiableList(possMills);
    }

    private AbstractPlayer currentPlayer;
    private AbstractPlayer otherPlayer;
    private final Map<Color, ArrayList<ManSoldier>> soldiers = new HashMap<>();

    private Board(){
        boardID = UUID.randomUUID();
        currentPlayer = new HumanPlayer(Color.White);
        otherPlayer = new AlphaBetaPruning(this, 5, 10, Color.Black);
        soldiers.put(Color.White, new ArrayList<>());
        soldiers.put(Color.Black, new ArrayList<>());
    }

    private void togglePlayer() {
        AbstractPlayer temp = currentPlayer;
        currentPlayer = otherPlayer;
        otherPlayer = temp;
    }

    public Board(Board b) {
        boardID = UUID.randomUUID();
        // copy constructor for alphabeta algo - need to copy fields
    }

    public static Board getInstance(){
        return board;
    }

    public static Board clearBoard() {
        board = new Board();
        return board;
    }

    public UUID getBoardID() {
        return boardID;
    }

    private final ArrayList<HouseInBoard> houses = new ArrayList<>();
    public int minHouseId = 0;
    public int maxHouseId = 23;

    public AbstractPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public AbstractPlayer getOtherPlayer() {
        return otherPlayer;
    }

    public int howManyMen(ArrayList<ManSoldier> men) {
        int i = 0;
        for (ManSoldier m : men) {
            if (!m.isOut)
                i++;
        }

        return i;
    }

    public boolean hasCurrentPlayerLost() {
        return howManyMenCurrentPlayer() < 3 || getValidJumps(null).isEmpty();
    }

    public int howManyMenCurrentPlayer() {
        return howManyMen(soldiers.get(currentPlayer.color));
    }

    public int howManyMenOtherPlayer() {
        return howManyMen(soldiers.get(otherPlayer.color));
    }

    public ArrayList<HouseInBoard> getHouses(){
        return houses;
    }

    public List<ManSoldier> getSoldiersCurrentPlayer() {
        return soldiers.get(currentPlayer.getColor());
    }

    public void setUp(){
//        for (int i = 0; i < NUMBER_OF_STARTING_PIECES; i++) {
//            ManSoldier m = new ManSoldier(Color.White);
//            m.setHouse(null);
//            white.add(m);
//        }
//        for (int i = 0; i < NUMBER_OF_STARTING_PIECES; i++) {
//            ManSoldier m = new ManSoldier(Color.Black);
//            m.setHouse(null);
//            black.add(m);
//        }

        for (int i = 0; i <= maxHouseId; i++) {
            houses.add(new HouseInBoard());
            houses.get(i).setId(i);
        }
        houses.get(0).setRight(houses.get(1));
        houses.get(0).setDown(houses.get(9));
//--------------------------------------------------- split every house
        houses.get(1).setRight(houses.get(2));
        houses.get(1).setLeft(houses.get(0));
        houses.get(1).setDown(houses.get(4));
//---------------------------------------------------
        houses.get(2).setLeft(houses.get(1));
        houses.get(2).setDown(houses.get(14));
//---------------------------------------------------
        houses.get(3).setRight(houses.get(4));
        houses.get(3).setDown(houses.get(10));
//---------------------------------------------------
        houses.get(4).setLeft(houses.get(3));
        houses.get(4).setRight(houses.get(5));
        houses.get(4).setUp(houses.get(1));
        houses.get(4).setDown(houses.get(7));
//---------------------------------------------------
        houses.get(5).setLeft(houses.get(4));
        houses.get(5).setDown(houses.get(13));
//---------------------------------------------------
        houses.get(6).setRight(houses.get(7));
        houses.get(6).setDown(houses.get(11));
//---------------------------------------------------
        houses.get(7).setLeft(houses.get(6));
        houses.get(7).setRight(houses.get(8));
        houses.get(7).setUp(houses.get(4));
//---------------------------------------------------
        houses.get(8).setLeft(houses.get(7));
        houses.get(8).setDown(houses.get(12));
//---------------------------------------------------
        houses.get(9).setRight(houses.get(10));
        houses.get(9).setUp(houses.get(0));
        houses.get(9).setDown(houses.get(21));
//---------------------------------------------------
        houses.get(10).setRight(houses.get(11));
        houses.get(10).setLeft(houses.get(9));
        houses.get(10).setUp(houses.get(3));
        houses.get(10).setDown(houses.get(18));
//---------------------------------------------------
        houses.get(11).setLeft(houses.get(10));
        houses.get(11).setUp(houses.get(6));
        houses.get(11).setDown(houses.get(15));
//---------------------------------------------------
        houses.get(12).setRight(houses.get(13));
        houses.get(12).setUp(houses.get(8));
        houses.get(12).setDown(houses.get(17));
//---------------------------------------------------
        houses.get(13).setRight(houses.get(14));
        houses.get(13).setLeft(houses.get(12));
        houses.get(13).setUp(houses.get(5));
        houses.get(13).setDown(houses.get(20));
//---------------------------------------------------
        houses.get(14).setLeft(houses.get(13));
        houses.get(14).setUp(houses.get(2));
        houses.get(14).setDown(houses.get(23));
//---------------------------------------------------
        houses.get(15).setRight(houses.get(16));
        houses.get(15).setUp(houses.get(11));
//---------------------------------------------------
        houses.get(16).setLeft(houses.get(15));
        houses.get(16).setRight(houses.get(17));
        houses.get(16).setDown(houses.get(19));
//---------------------------------------------------
        houses.get(17).setLeft(houses.get(16));
        houses.get(17).setUp(houses.get(12));
//---------------------------------------------------
        houses.get(18).setRight(houses.get(19));
        houses.get(18).setUp(houses.get(10));
//---------------------------------------------------
        houses.get(19).setRight(houses.get(20));
        houses.get(19).setLeft(houses.get(18));
        houses.get(19).setUp(houses.get(16));
        houses.get(19).setDown(houses.get(22));
//---------------------------------------------------
        houses.get(20).setLeft(houses.get(19));
        houses.get(20).setUp(houses.get(13));
//---------------------------------------------------
        houses.get(21).setRight(houses.get(22));
        houses.get(21).setUp(houses.get(9));
//---------------------------------------------------
        houses.get(22).setRight(houses.get(23));
        houses.get(22).setLeft(houses.get(21));
        houses.get(22).setUp(houses.get(19));
//---------------------------------------------------
        houses.get(23).setLeft(houses.get(22));
        houses.get(23).setUp(houses.get(14));
    }

    private void putOnBoard(HouseInBoard position, AbstractPlayer player) {
        ManSoldier m = new ManSoldier(player.color);
        m.setHouse(position);
        soldiers.get(player.color).add(m);
    }

    private void removeFromBoard(HouseInBoard position) {
        position.setMan(HouseInBoard.empty);
    }

    public void makeJump(AbstractJump jump) {
        if (jump.getSource() != null) {
            removeFromBoard(jump.getSource());
        }

        putOnBoard(jump.getDestination(), currentPlayer);

        if (jump instanceof RemoveMan) {
            removeFromBoard(jump.getSource());
        }

        togglePlayer();
    }

    public void undoJump(AbstractJump jump) {
        togglePlayer();

        if (jump.getSource() != null) {
            putOnBoard(jump.getSource(), currentPlayer);
        }

        removeFromBoard(jump.getDestination());

        if (jump instanceof RemoveMan) {
            putOnBoard(jump.getSource(), otherPlayer);
        }
    }

    public boolean doesPieceCompleteMill(int removeFromPosition, int position, AbstractPlayer player) {
        char positionState = player.getToken();

        for (List<Integer> millCoords : POSSIBLE_MILLS) {
            if (millCoords.contains(position)) {
                boolean result = true;
                for (int i : millCoords) {
                    if (i == removeFromPosition) {
                        result = false;
                    } else if (i != position && houses.get(i).getMan().getToken() != positionState) {
                        result = false;
                    }
                }
                if (result) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean areAllPiecesFromMill(AbstractPlayer player) {
        for (int i = 0; i < houses.size(); i++) {
            if (houses.get(i).getMan().getColor() == player.getColor() && !doesPieceCompleteMill(-1, i, player)) {
                return false;
            }
        }

        return true;
    }

    private void addPossibleMillTakes(SortedSet<ValuedJump> sortedJumps,
                                      AbstractJump jump, JumpEvaluationFunction evaluationFunction) {
        boolean areAllOtherPlayerPiecesFromMill = areAllPiecesFromMill(getOtherPlayer());

        for (int i = 0; i < houses.size(); i++) {
            if (houses.get(i).getMan().getColor() == getOtherPlayer().getColor()) {
                if (areAllOtherPlayerPiecesFromMill || !doesPieceCompleteMill(-1, i, getOtherPlayer())) {
                    jump = new Jump(jump.getSource(), jump.getDestination());
                    sortedJumps.add(new ValuedJump(jump, evaluationFunction.evaluate(this, jump)));
                }
            }
        }
    }

    public int getUnputPiecesOfCurrentPlayer() {
        return NUMBER_OF_STARTING_PIECES - soldiers.get(currentPlayer.getColor()).size();
    }

    public List<AbstractJump> getValidJumps(JumpEvaluationFunction evaluationFunction) {
        if (evaluationFunction == null) {
            evaluationFunction = (board, jump) -> 0;
        }

        SortedSet<ValuedJump> sortedJumps = new TreeSet<>();

        if (getUnputPiecesOfCurrentPlayer() > 0) {
            for (int i = 0; i < houses.size(); i++) {
                if (houses.get(i).isEmpty()) {
                    Jump jump = new Jump();
                    if (doesPieceCompleteMill(-1, i, currentPlayer)) {
                        addPossibleMillTakes(sortedJumps, jump, evaluationFunction);
                    } else {
                        sortedJumps.add(new ValuedJump(jump, evaluationFunction.evaluate(this, jump)));
                    }
                }
            }
        } else {
            if (howManyMenCurrentPlayer() > 3 || !IS_FLYING_ALLOWED) {
                for (int i = 0; i < houses.size(); i++) {
                    if (houses.get(i).getMan().getColor() == currentPlayer.getColor()) {
                        for (int neighbour : POSITION_TO_NEIGHBOURS.get(i)) {
                            if (houses.get(neighbour).isEmpty()) {
                                Jump jump = new Jump(houses.get(i), houses.get(neighbour));
                                if (doesPieceCompleteMill(i, neighbour, currentPlayer)) {
                                    addPossibleMillTakes(sortedJumps, jump, evaluationFunction);
                                } else {
                                    sortedJumps.add(new ValuedJump(jump, evaluationFunction.evaluate(this, jump)));
                                }
                            }
                        }
                    }
                }
            } else {
                for (int i = 0; i < houses.size(); i++) {
                    if (houses.get(i).getMan().getColor() == currentPlayer.getColor()) {
                        for (int j = 0; j < houses.size(); j++) {
                            if (houses.get(j).isEmpty()) {
                                Jump jump = new Jump(houses.get(i), houses.get(j));
                                if (doesPieceCompleteMill(i, j, currentPlayer)) {
                                    addPossibleMillTakes(sortedJumps, jump, evaluationFunction);
                                } else {
                                    sortedJumps.add(new ValuedJump(jump, evaluationFunction.evaluate(this, jump)));
                                }
                            }
                        }
                    }
                }
            }
        }

        List<AbstractJump> result = new ArrayList<>();

        for (ValuedJump valuedJump : sortedJumps) {
            result.add(valuedJump.getJump());
        }

        return result;
    }

    public boolean isJumpValid(AbstractJump jump) {
        if (!houses.get(jump.getDestination().getId()).isEmpty()) {
            return false;
        }

        if (jump.getSource().isEmpty()) {
            if (houses.get(jump.getDestination().getId()).getMan().getColor() != currentPlayer.getColor()) {
                return false;
            }
            if ((howManyMenCurrentPlayer() > 3 || !IS_FLYING_ALLOWED)
                    && !POSITION_TO_NEIGHBOURS.get(jump.getSource().getId()).contains(jump.getDestination().getId())) {
                return false;
            }
            if (getUnputPiecesOfCurrentPlayer() > 0) {
                return false;
            }
        } else {
            if (getUnputPiecesOfCurrentPlayer() == 0) {
                return false;
            }
        }

        if (jump instanceof RemoveMan) {
            if (houses.get(jump.getSource().getId()).getMan().getColor() != getOtherPlayer().getColor()) {
                return false;
            }

            return !isPieceFromMill(jump.getSource().getId()) || areAllPiecesFromMill(getOtherPlayer());
        }

        return true;
    }

    public boolean isPieceFromMill(int position) {
        if (!houses.get(position).isEmpty()) {
            AbstractPlayer p = houses.get(position).getMan().getColor() == currentPlayer.getColor() ? currentPlayer : otherPlayer;
            return doesPieceCompleteMill(-1, position, p);
        }

        return false;
    }
}