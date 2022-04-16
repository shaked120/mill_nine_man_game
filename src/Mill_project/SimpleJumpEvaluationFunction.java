package Mill_project;

public class SimpleJumpEvaluationFunction implements JumpEvaluationFunction {
	@Override
	public int evaluate(Board board, AbstractJump jump) {
		if (board.doesPieceCompleteMill(jump.getSource().getId(), jump.getDestination().getId(), board.getCurrentPlayer())) {
			return 9;
		}
		
		if (board.doesPieceCompleteMill(jump.getSource().getId(), jump.getDestination().getId(), board.getOtherPlayer())) {
			for (int neighbour : Board.POSITION_TO_NEIGHBOURS.get(jump.getDestination().getId())) {
				if (board.getHouses().get(neighbour).getMan().getToken() == board.getOtherPlayer().getToken() ) {
					return 8;
				}
			}
			
			return 4;
		}
		
		if (board.doesPieceCompleteMill(-1, jump.getDestination().getId(), board.getOtherPlayer())) {
			for (int neighbour : Board.POSITION_TO_NEIGHBOURS.get(jump.getDestination().getId())) {
				if (board.getHouses().get(neighbour).getMan().getToken() == board.getOtherPlayer().getToken()) {
					return -2;
				}
			}
			
			return -1;
		}

		return 0;
	}
}
