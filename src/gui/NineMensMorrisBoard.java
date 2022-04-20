package gui;

import Mill_project.*;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NineMensMorrisBoard extends JPanel {
	private static final long serialVersionUID = 2961261317989680041L;

	private Board board;
	private int positionSelected;
	private boolean millFormed;
	private AbstractJump jump;
	private JumpExecutorCallback jumpExecutor;
	private boolean doMakeJump;
	
	public NineMensMorrisBoard() {
		addMouseListener(new Controller());
	}
	
	public void setBoard(Board board, JumpExecutorCallback jumpExecutor) {
		this.board = board;
		this.positionSelected = -1;
		this.millFormed = false;
		this.jump = null;
		this.jumpExecutor = jumpExecutor;
		this.doMakeJump = false;
		this.board.setUp();
		repaint();
	}
	
	public void makeJump() {
		this.doMakeJump = true;
	}
	
	Point getPositionCoords(int position) {
		Point result = new Point();
		int margin = 30;
		int width = getSize().width - 2 * margin;
		int height = getSize().height - 2 * margin;
		int metric = Math.min(width, height);
		int positionSpace = metric / 6;
		int row = position / 3;
		if (row < 3) {
			result.x = row * positionSpace + (position % 3) * (metric - 2 * row * positionSpace) / 2;
			result.y = row * positionSpace;
		} else if (row == 3) {
			result.x = (position % 3) * positionSpace;
			result.y = row * positionSpace;
		} else {
			Point point = getPositionCoords(23 - position);
			point.x -= margin;
			point.y -= margin;
			result.x = metric - point.x;
			result.y = metric - point.y;
		}
		result.x += margin;
		result.y += margin;
		return result;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < 24; i++) {
			for (int j : Board.POSITION_TO_NEIGHBOURS.get(i)) {
				Point start = getPositionCoords(i);
				Point end = getPositionCoords(j);
				g.drawLine(start.x, start.y, end.x, end.y);
			}
		}
		for (int i = 0; i < 24; i++) {
			Point coords = getPositionCoords(i);
			g.fillOval(coords.x - 5, coords.y - 5, 10, 10);
		}
		for (int i = 0; i < 24; i++) {
			if (jump != null && i == jump.getSourceId()) {
				continue;
			}
			if (!board.getHouses().get(i).isEmpty()) {
				if (positionSelected == i) {
					g.setColor(Color.RED);
				} else if (board.getHouses().get(i).getMan().getColor() == Mill_project.Color.White
						|| (jump != null && jump.getDestinationId() == i && board.getCurrentPlayer().getColor() == Mill_project.Color.White)) {
					g.setColor(Color.WHITE);
				} else {
					g.setColor(Color.BLACK);
				}
				Point coords = getPositionCoords(i);
				g.fillOval(coords.x - 20, coords.y - 20, 40, 40);
				g.setColor(Color.BLACK);
				g.drawOval(coords.x - 20, coords.y - 20, 40, 40);
			}
		}
	}

	private class Controller extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (!doMakeJump || board.getCurrentPlayer().getColor() == Mill_project.Color.Black || board.hasCurrentPlayerLost()) {
				return;
			}
			int x = e.getX();
			int y = e.getY();
			for (int i = 0; i < 24; i++) {
				Point coords = getPositionCoords(i);
				if (coords.x - 20 <= x && x <= coords.x + 20
						&& coords.y - 20 <= y && y <= coords.y + 20) {
					if (millFormed) {
						if (board.getHouses().get(i).getMan().getColor() == board.getOtherPlayer().getColor()) {
							boolean areAllOtherPlayerPiecesFromMill = board.areAllPiecesFromMill(board.getOtherPlayer());
							if (areAllOtherPlayerPiecesFromMill || !board.doesPieceCompleteMill(-1, i, board.getOtherPlayer())) {
								jump = new RemoveMan(jump.getSource());
								if (board.isJumpValid(jump)) {
									jumpExecutor.makeJump(jump);
									jump = null;
									millFormed = false;
									doMakeJump = false;
								}									
							}
						}
					} else {
						if (board.getHouses().get(i).isEmpty()) {
							if (positionSelected == -1) {
								if (board.getUnputPiecesOfCurrentPlayer() > 0) {
									jump = new PlaceMan(board.getHouses().get(i));
								}
							} else {
								jump = new Jump(board.getHouses().get(positionSelected), board.getHouses().get(i));
							}
						} else if (board.getHouses().get(i).getMan().getColor() == board.getCurrentPlayer().getColor()) {
							if (positionSelected == -1) {
								positionSelected = i;
							} else if (positionSelected == i) {
								positionSelected = -1;
							} else {
								positionSelected = i;
							}
						}
						if (jump != null) {
							if (board.isJumpValid(jump)) {
								positionSelected = -1;
								if (board.doesPieceCompleteMill(jump.getSourceId(), jump.getDestinationId(), board.getCurrentPlayer())) {
									millFormed = true;
								} else {
									jumpExecutor.makeJump(jump);
									jump = null;
									doMakeJump = false;
								}
							} else {
								jump = null;
							}
						}
					}
					repaint();
					break;
				}
			}
		}
	}
}
