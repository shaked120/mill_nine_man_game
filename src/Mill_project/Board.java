package Mill_project;

import algoritmas.AlphaBetaPruning;
import algoritmas.ValuedJump;

import java.util.*;

public class Board {
    // Universal Unique Identifier
    // UUID is unique identifier across all system (based on current millisecond)
    private final UUID boardID;

    private static final int NUMBER_OF_STARTING_PIECES = 9;
    private static final int MAX_HOUSE_ID = 23;

    // all possible mills in game (houses ids)
    public static final List<List<Integer>> POSSIBLE_MILLS;
    // every house neighbours ids
    public static final List<List<Integer>> POSITION_TO_NEIGHBOURS;

    // singleton instance
    private static Board board = new Board();

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
    // dictionary of color's soldiers
    private final Map<Color, ArrayList<ManSoldier>> soldiers = new HashMap<>();
    // this is for counting the placed pieces when we're at insert mode
    private final Map<Color, Integer> placedPiecesCounters = new HashMap<>();
    private final ArrayList<HouseInBoard> houses = new ArrayList<>();

    private Board(){
        boardID = UUID.randomUUID();
        currentPlayer = new HumanPlayer(Color.White);
        otherPlayer = new AlphaBetaPruning(this, 10, 10, Color.Black);
        soldiers.put(Color.White, new ArrayList<>());
        soldiers.put(Color.Black, new ArrayList<>());
        placedPiecesCounters.put(Color.White, 0);
        placedPiecesCounters.put(Color.Black, 0);
    }

    public void togglePlayer() {
        AbstractPlayer temp = currentPlayer;
        currentPlayer = otherPlayer;
        otherPlayer = temp;
    }

    // copy constructor for algo
    public Board(Board b) {
        boardID = UUID.randomUUID();
        currentPlayer = b.currentPlayer;
        otherPlayer = b.otherPlayer;
        soldiers.clear();
        soldiers.putAll(b.soldiers);
        placedPiecesCounters.clear();
        placedPiecesCounters.putAll(b.placedPiecesCounters);
        houses.clear();
        houses.addAll(b.houses);
    }

    public static Board getInstance(){
        return board;
    }

    // for new game
    public static Board clearBoard() {
        board = new Board();
        return board;
    }

    public UUID getBoardID() {
        return boardID;
    }

    public AbstractPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public AbstractPlayer getOtherPlayer() {
        return otherPlayer;
    }

    // counting how many soldiers the player has
    public int howManyMen(ArrayList<ManSoldier> men) {
        int i = 0;
        for (ManSoldier m : men) {
            if (!m.isOut)
                i++;
        }

        return i;
    }

    public boolean hasCurrentPlayerLost() {
        return getUnputPiecesOfCurrentPlayer() == 0 && (howManyMenCurrentPlayer() < 3 || getValidJumps(null).isEmpty());
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

    public void setUp(){
        for (int i = 0; i <= MAX_HOUSE_ID; i++) {
            houses.add(new HouseInBoard());
            houses.get(i).setId(i);
        }
    }

    // put the piece in position by player's color
    private void putOnBoard(HouseInBoard position, AbstractPlayer player) {
        ManSoldier m = new ManSoldier(player.color);
        m.setHouse(position);
        position.setMan(m);
        soldiers.get(player.color).add(m);
    }

    // remove the piece by the player's color
    private void removeFromBoard(HouseInBoard position, AbstractPlayer player) {
        ManSoldier man = position.getMan();
        soldiers.get(player.color).removeIf(s -> s.getHouseId() == man.getHouseId());
        man.isOut = true;
        position.setMan(HouseInBoard.empty);
    }

    public void makeJump(AbstractJump jump, boolean togglePlayer) {
        if (jump.getSource() != null) {
            removeFromBoard(jump.getSource(), currentPlayer);
        }

        if (jump.getDestination() != null) {
            putOnBoard(jump.getDestination(), currentPlayer);
        }

        if (jump instanceof PlaceMan) {
            placedPiecesCounters.put(currentPlayer.color, placedPiecesCounters.get(currentPlayer.color) + 1);
        }

        if (jump instanceof RemoveMan) {
            removeFromBoard(jump.getSource(), otherPlayer);
        }

        if (jump instanceof AlgoJump) {
            // created mill by algo + take piece when we at insert mode
            if (jump.getSource() == null) {
                placedPiecesCounters.put(currentPlayer.color, placedPiecesCounters.get(currentPlayer.color) + 1);
            }
            removeFromBoard(((AlgoJump) jump).getTakenPiece(), otherPlayer);
        }

        if (togglePlayer) {
            togglePlayer();
        }
    }

    // undo jumps performed by the algo player when running the search for best move
    public void undoJump(AbstractJump jump) {
        togglePlayer();

        if (jump.getSource() != null) {
            putOnBoard(jump.getSource(), currentPlayer);
        }

        if (jump.getDestination() != null) {
            removeFromBoard(jump.getDestination(), currentPlayer);
        }

        if (jump instanceof PlaceMan) {
            placedPiecesCounters.put(currentPlayer.color, placedPiecesCounters.get(currentPlayer.color) - 1);
        }

        if (jump instanceof RemoveMan) {
            putOnBoard(jump.getSource(), otherPlayer);
        }

        if (jump instanceof AlgoJump) {
            // created mill by algo + take piece when we at insert mode
            if (jump.getSource() == null) {
                placedPiecesCounters.put(currentPlayer.color, placedPiecesCounters.get(currentPlayer.color) - 1);
            }
            putOnBoard(((AlgoJump) jump).getTakenPiece(), otherPlayer);
        }
    }

    // check if the jump creates mill
    public boolean doesPieceCompleteMill(int removeFromPosition, int position, AbstractPlayer player) {
        for (List<Integer> millCoords : POSSIBLE_MILLS) {
            if (millCoords.contains(position)) {
                boolean result = true;
                for (int i : millCoords) {
                    if (i == removeFromPosition) {
                        result = false;
                    } else if (i != position && houses.get(i).getMan().getColor() != player.getColor()) {
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

    // check if player has mill
    public boolean areAllPiecesFromMill(AbstractPlayer player) {
        for (int i = 0; i < houses.size(); i++) {
            if (houses.get(i).getMan().getColor() == player.getColor() && !doesPieceCompleteMill(-1, i, player)) {
                return false;
            }
        }
        return true;
    }

    // check for removing piece of other player after completing mill
    private void addPossibleMillTakes(SortedSet<ValuedJump> sortedJumps,
                                      AbstractJump jump, JumpEvaluationFunction evaluationFunction) {
        boolean areAllOtherPlayerPiecesFromMill = areAllPiecesFromMill(getOtherPlayer());
        for (int i = 0; i < houses.size(); i++) {
            if (houses.get(i).getMan().getColor() == getOtherPlayer().getColor()) {
                if (areAllOtherPlayerPiecesFromMill || !doesPieceCompleteMill(-1, i, getOtherPlayer())) {
                    jump = new AlgoJump(jump.getSource(), jump.getDestination(), houses.get(i));
                    sortedJumps.add(new ValuedJump(jump, evaluationFunction.evaluate(this, jump)));
                }
            }
        }
    }

    public int getUnputPiecesOfCurrentPlayer() {
        return NUMBER_OF_STARTING_PIECES - placedPiecesCounters.get(currentPlayer.getColor());
    }

    public List<AbstractJump> getValidJumps(JumpEvaluationFunction evaluationFunction) {
        if (evaluationFunction == null) {
            evaluationFunction = (board, jump) -> 0;
        }

        SortedSet<ValuedJump> sortedJumps = new TreeSet<>();
        if (getUnputPiecesOfCurrentPlayer() > 0) {
            // insert mode
            for (int i = 0; i < houses.size(); i++) {
                if (houses.get(i).isEmpty()) {
                    PlaceMan jump = new PlaceMan(houses.get(i));
                    if (doesPieceCompleteMill(-1, i, currentPlayer)) {
                        addPossibleMillTakes(sortedJumps, jump, evaluationFunction);
                    } else {
                        sortedJumps.add(new ValuedJump(jump, evaluationFunction.evaluate(this, jump)));
                    }
                }
            }
        } else {
            // jump mode
            if (howManyMenCurrentPlayer() > 3) {
                // regular jump
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
                // flying mode
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
        if (jump instanceof PlaceMan) {
            return jump.getDestination().isEmpty();
        }

        if (jump instanceof RemoveMan) {
            if (houses.get(jump.getSourceId()).getMan().getColor() != getOtherPlayer().getColor()) {
                return false;
            }
            return !isPieceFromMill(jump.getSourceId()) || areAllPiecesFromMill(getOtherPlayer());
        }

        if (!houses.get(jump.getDestinationId()).isEmpty()) {
            return false;
        }

        if (!jump.getSource().isEmpty()) {
            // check if destination is not in neighbours, and we are not in flying mode (more than 3 pieces remaining)
            if (howManyMenCurrentPlayer() > 3 && !POSITION_TO_NEIGHBOURS.get(jump.getSourceId()).contains(jump.getDestinationId())) {
                return false;
            }

            return getUnputPiecesOfCurrentPlayer() == 0;
        } else {
            return getUnputPiecesOfCurrentPlayer() != 0;
        }
    }

    // check if position is part of mill
    public boolean isPieceFromMill(int position) {
        if (!houses.get(position).isEmpty()) {
            AbstractPlayer p = houses.get(position).getMan().getColor() == currentPlayer.getColor() ? currentPlayer : otherPlayer;
            return doesPieceCompleteMill(-1, position, p);
        }
        return false;
    }
}