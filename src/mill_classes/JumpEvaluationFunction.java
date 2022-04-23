package mill_classes;

public class JumpEvaluationFunction implements JumpEvaluationFunctionInterface {
	@Override
	public int evaluate(Board board, AbstractJump jump) {
		if (board.doesPieceCompleteMill(jump.getSourceId(), jump.getDestinationId(), board.getCurrentPlayer())) {
			return 9;
		}

		if (board.doesPieceCompleteMill(jump.getSourceId(), jump.getDestinationId(), board.getOtherPlayer())) {
			for (int neighbour : Board.POSITION_TO_NEIGHBOURS.get(jump.getDestinationId())) {
				if (board.getHouses().get(neighbour).getMan().getToken() == board.getOtherPlayer().getToken() ) {
					return 8;
				}
			}

			return 4;
		}

		if (board.doesPieceCompleteMill(-1, jump.getDestinationId(), board.getOtherPlayer())) {
			for (int neighbour : Board.POSITION_TO_NEIGHBOURS.get(jump.getDestinationId())) {
				if (board.getHouses().get(neighbour).getMan().getToken() == board.getOtherPlayer().getToken()) {
					return -2;
				}
			}

			return -1;
		}

		return 0;
	}
}
