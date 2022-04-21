package algoritmas;

import Mill_project.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AlphaBetaPruning extends AbstractPlayer {
    private static final int INFINITY = 1001;
    private static final int WIN_BOARD_VALUE = 1000;
    private static final int END_SEARCH = 10000;
    private static final int NUMBER_OF_POSITIONS = 24;

    private final Board board;
    private final int maxDepth;
    private final int maxTime;
    private long startTime;
    private final Map<UUID, BoardStateValue> transpositionTable;
    private AbstractJump currentBestJump;
    private int currentBestJumpValue;
    private final JumpEvaluationFunction jumpEvaluationFunction;
    private Board currentBoard;
    private boolean doTerminateJump;

    public AlphaBetaPruning(Board boardState, int maxDepth, int maxTime, Color color) {
        super(color);
        this.board = boardState;
        this.maxDepth = maxDepth;
        this.maxTime = maxTime;
        this.jumpEvaluationFunction = new SimpleJumpEvaluationFunction();
        this.transpositionTable = new HashMap<>();
        this.doTerminateJump = false;

        this.currentBoard = null;
        this.startTime = 0;
        this.currentBestJump = null;
        currentBestJumpValue = -INFINITY;
    }

    private int getNumberOfAdjacentJumps(char playerToken) {
        int result = 0;
        for (int i = 0; i < NUMBER_OF_POSITIONS; i++) {
            if (currentBoard.getHouses().get(i).getMan().getToken() == playerToken ) {
                for (int j : Board.POSITION_TO_NEIGHBOURS.get(i)) {
                    if (currentBoard.getHouses().get(j).getMan().getToken() == 0) {
                        result++;
                    }
                }
            }
        }

        return result;
    }

    private int getNumberOfMills(char playerToken) {
        int result = 0;
        millCordsLoop:
        for (List<Integer> millCords : Board.POSSIBLE_MILLS) {
            for (int i : millCords) {
                if (currentBoard.getHouses().get(i).getMan().getToken() != playerToken ) {
                    continue millCordsLoop;
                }
            }

            result++;
        }
        return result;
    }


    private int evaluateCurrentBoard() {
        int result = 0;

        result += 10 * (currentBoard.howManyMenCurrentPlayer() - currentBoard.howManyMenOtherPlayer());
        result += 2 * (getNumberOfAdjacentJumps(currentBoard.getCurrentPlayer().getToken()) - getNumberOfAdjacentJumps(currentBoard.getOtherPlayer().getToken()));
        result += 8 * (getNumberOfMills(currentBoard.getCurrentPlayer().getToken()) - getNumberOfMills(currentBoard.getOtherPlayer().getToken()));
        //result += 2 * (getNumberOfFormableMills(currentBoard.getCurrentPlayer()) - getNumberOfFormableMills(currentBoard.getOtherPlayer()));
        return result;
    }

    int hits = 0;

    private int alphaBetaPrunningSearch(int alpha, int beta, int currentDepth, int remainingDepth) {
        if ((System.currentTimeMillis() - startTime > maxTime && currentBestJump != null) || doTerminateJump) {
            doTerminateJump = false;
            return END_SEARCH;
        }
        BoardStateValue boardComputedValue = transpositionTable.get(currentBoard.getBoardID());
        if (boardComputedValue != null && boardComputedValue.getRemainingDepth() >= remainingDepth) { hits++;
            if (boardComputedValue.hasBeenCut()) {
                alpha = Math.max(alpha, boardComputedValue.getValue());
            }
            if (boardComputedValue.couldHaveBeenCutDeeper()) {
                beta = Math.min(beta, boardComputedValue.getValue());
            }
            if (!(boardComputedValue.couldHaveBeenCutDeeper() || boardComputedValue.hasBeenCut())
                    || alpha >= beta) {
                if (currentDepth == 0) {
                    currentBestJump = boardComputedValue.getFoundBestJump();
                    currentBestJumpValue = boardComputedValue.getValue();
                }
                return boardComputedValue.getValue();
            }
        }
        List<AbstractJump> validJumps = currentBoard.getValidJumps(jumpEvaluationFunction);
        if (currentBoard.getUnputPiecesOfCurrentPlayer() == 0 && (currentBoard.howManyMenCurrentPlayer() < 3 || validJumps.isEmpty())) {
            return -WIN_BOARD_VALUE;
        }
        if (remainingDepth == 0) {
            return evaluateCurrentBoard();
        } else {
            AbstractJump nodeBestJump = null;
            int nodeBestValue = -INFINITY;
            for (AbstractJump jump : validJumps) {
                currentBoard.makeJump(jump, true);
                int value = -alphaBetaPrunningSearch(-beta, -alpha, currentDepth + 1, remainingDepth -1);
                currentBoard.undoJump(jump);
                if (Math.abs(value) == END_SEARCH) {
                    return END_SEARCH;
                }
                if (value > nodeBestValue) {
                    nodeBestValue = value;
                    nodeBestJump = jump;
                }
                if (value > alpha) {
                    alpha = value;
                    if (currentDepth == 0) {
                        currentBestJump = jump;
                        currentBestJumpValue = alpha;
                    }
                }
                if (alpha >= beta) {
                    break;
                }
            }
            transpositionTable.put(currentBoard.getBoardID(), new BoardStateValue(nodeBestValue, remainingDepth, nodeBestJump, alpha >= beta, nodeBestValue < alpha));
            return nodeBestValue;
        }
    }

    public AbstractJump searchForBestJump() {
        currentBestJump = null;
        startTime = System.currentTimeMillis();
        currentBoard = new Board(board);
        currentBestJumpValue = -INFINITY;
        AbstractJump prevBestJump = currentBestJump;
        int prevBestJumpValue = currentBestJumpValue;
        for (int depth = Math.min(2, maxDepth); depth <= maxDepth; depth += 2) {
            int value = alphaBetaPrunningSearch(-INFINITY, INFINITY, 0, depth);
            if (Math.abs(value) == END_SEARCH) {
                if (currentBestJumpValue <= prevBestJumpValue) {
                    currentBestJump = prevBestJump;
                    currentBestJumpValue = prevBestJumpValue;
                }
                break;
            }
            prevBestJump = currentBestJump;
            prevBestJumpValue = value;
        }

        return currentBestJump;
    }

    public synchronized void terminateSearch() {
        doTerminateJump = true;
    }

    public Color getColor() {
        return color;
    }
}
