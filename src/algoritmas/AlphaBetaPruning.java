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

    private final Board board;
    private final int maxDepth;
    private final int maxTime;
    private long startTime;
    private final Map<UUID, BoardStateValue> transpositionTable;
    private Jump currentBestMove;
    private int currentBestMoveValue;
    private final MoveEvaluationFunction moveEvaluationFunction;
    private Board currentBoard;
    private boolean doTerminateMove;

    public AlphaBetaPruning(Board boardState, int maxDepth, int maxTime, Color color) {
        super(color);
        this.board = boardState;
        this.maxDepth = maxDepth;
        this.maxTime = maxTime;
        this.moveEvaluationFunction = new SimpleMoveEvaluationFunction();
        this.transpositionTable = new HashMap<>();
        this.doTerminateMove = false;

        this.currentBoard = null;
        this.startTime = 0;
        this.currentBestMove = null;
        currentBestMoveValue = -INFINITY;
    }

    public void setBoard(Board board) {
        this.currentBoard = board;
    }

    public Board getBoard() {
        return currentBoard;
    }

    private int getNumberOfAdjacentMoves(char playerToken) {
        int result = 0;

        for (int i = 0; i < Board.NUMBER_OF_POSITIONS; i++) {
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

//	private int getNumberOfFormableMills(int player) {
//		int result = 0;
//
//		millCordsLoop:
//		for (List<Integer> millCords : BoardState.possibleMills) {
//			int emptyPosition = -1;
//
//			for (int i : millCords) {
//				if (currentBoard.getPositionState(i) != player + 1) {
//					if (currentBoard.getPositionState(i) == 0 && emptyPosition == -1) {
//						emptyPosition = i;
//					} else {
//						continue millCordsLoop;
//					}
//				}
//			}
//
//			if (emptyPosition == -1) {
//				continue millCordsLoop;
//			}
//
//			if (currentBoard.getUnputPiecesOfPlayer(player) > 0) {
//				result++;
//			}
//
//			for (int neighbour : BoardState.positionToNeighbours.get(emptyPosition)) {
//				if (currentBoard.getPositionState(neighbour) == player + 1) {
//					result++;
//				}
//			}
//		}
//
//		return result;
//	}

    private int evaluateCurrentBoard() {
        int result = 0;

        result += 10 * (currentBoard.howManyMenCurrentPlayer() - currentBoard.howManyMenOtherPlayer());
        result += 2 * (getNumberOfAdjacentMoves(currentBoard.getCurrentPlayer().getToken()) - getNumberOfAdjacentMoves(currentBoard.getOtherPlayer().getToken()));
        result += 8 * (getNumberOfMills(currentBoard.getCurrentPlayer().getToken()) - getNumberOfMills(currentBoard.getOtherPlayer().getToken()));
        //result += 2 * (getNumberOfFormableMills(currentBoard.getCurrentPlayer()) - getNumberOfFormableMills(currentBoard.getOtherPlayer()));

        return result;
    }

    int hits = 0;
    private int alphaBetaPrunningSearch(int alpha, int beta, int currentDepth, int remainingDepth) {
        if ((System.currentTimeMillis() - startTime > maxTime && currentBestMove != null) || doTerminateMove) {
            doTerminateMove = false;
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
                    currentBestMove = boardComputedValue.getFoundBestMove();
                    currentBestMoveValue = boardComputedValue.getValue();
                }

                return boardComputedValue.getValue();
            }
        }

        List<Jump> validMoves = currentBoard.getValidMoves(moveEvaluationFunction);
        if (currentBoard.howManyMenCurrentPlayer() < 3 || validMoves.isEmpty()) {
            return -WIN_BOARD_VALUE;
        }

        if (remainingDepth == 0) {
            return evaluateCurrentBoard();
        } else {
            Jump nodeBestMove = null;
            int nodeBestValue = -INFINITY;

            for (Jump move : validMoves) {
                currentBoard.makeMove(move);

                int value = -alphaBetaPrunningSearch(-beta, -alpha, currentDepth + 1, remainingDepth -1);

                currentBoard.undoMove(move);

                if (Math.abs(value) == END_SEARCH) {
                    return END_SEARCH;
                }

                if (value > nodeBestValue) {
                    nodeBestValue = value;
                    nodeBestMove = move;
                }

                if (value > alpha) {
                    alpha = value;

                    if (currentDepth == 0) {
                        currentBestMove = move;
                        currentBestMoveValue = alpha;
                    }
                }

                if (alpha >= beta) {
                    break;
                }
            }

            transpositionTable.put(currentBoard.getBoardID(), new BoardStateValue(nodeBestValue, remainingDepth, nodeBestMove, alpha >= beta, nodeBestValue < alpha));

            return nodeBestValue;
        }
    }

    public Jump searchForBestMove() {
        currentBestMove = null;
        startTime = System.currentTimeMillis();
        currentBoard = new Board(board);
        currentBestMoveValue = -INFINITY;
        Jump prevBestMove = currentBestMove;
        int prevBestMoveValue = currentBestMoveValue;


        for (int depth = Math.min(2, maxDepth); depth <= maxDepth; depth += 2) {
            int value = alphaBetaPrunningSearch(-INFINITY, INFINITY, 0, depth);

            if (Math.abs(value) == END_SEARCH) {
                if (currentBestMoveValue <= prevBestMoveValue) {
                    currentBestMove = prevBestMove;
                    currentBestMoveValue = prevBestMoveValue;
                }

                break;
            }

            prevBestMove = currentBestMove;
            prevBestMoveValue = value;
        }

        return currentBestMove;

//		List<Move> moves = boardState.getValidMoves(moveEvaluationFunction);
//		Random random = new Random();
//
//		return moves.get(random.nextInt(moves.size()));
    }

    public synchronized void terminateSearch() {
        doTerminateMove = true;
    }

    @Override
    public int readInt() {
        return 0;
    }

    public Color getColor() {
        return color;
    }
}
