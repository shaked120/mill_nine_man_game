package gui;

import Mill_project.AbstractJump;
import Mill_project.Board;
import Mill_project.Color;
import algoritmas.AlphaBetaPruning;

import javax.swing.*;
import java.awt.*;

public class NineMensMorrisGUI extends JFrame {
	private static final long serialVersionUID = -514606427157467570L;
	private Board currentGame;
	private final NineMensMorrisBoard boardPanel;
	private final JPanel controls;
	private final JButton newGameButton;
	private final JLabel statusLabel;
	private AlphaBetaPruning solver;
	private volatile JumpExecutorCallback jumpExecutor;

	private class JumpExecutor implements JumpExecutorCallback {
		private boolean terminate = false;
		
		public synchronized void terminate() {
			this.terminate = true;
			solver.terminateSearch();
		}

		@Override
		public synchronized void makeJump(AbstractJump jump, boolean togglePlayer) {
			if (terminate) {
				return;
			}
			currentGame.makeJump(jump, togglePlayer);
			boardPanel.repaint();
			if (currentGame.hasCurrentPlayerLost()) {
				if (currentGame.getCurrentPlayer().getColor() == Color.White) {
					statusLabel.setText("You won!");
				} else {
					statusLabel.setText("You lost!");
				}
			} else if (currentGame.getCurrentPlayer().getColor() == Color.Black) {
				statusLabel.setText("Making jump...");

				new Thread(() -> {
					AbstractJump jump1 = solver.searchForBestJump();
					JumpExecutor.this.makeJump(jump1);
				}).start();
			} else {
				statusLabel.setText("Your jump");
				boardPanel.makeJump();
			}
		}

		@Override
		public synchronized void togglePlayer() {
			currentGame.togglePlayer();
			if (currentGame.getCurrentPlayer().getColor() == Color.Black) {
				statusLabel.setText("Making jump...");

				new Thread(() -> {
					AbstractJump jump1 = solver.searchForBestJump();
					JumpExecutor.this.makeJump(jump1);
				}).start();
			}
		}

		@Override
		public synchronized void makeJump(AbstractJump jump) {
			makeJump(jump, true);
		}
	}


	private void startNewGame() {
		if (jumpExecutor != null) {
			jumpExecutor.terminate();
		}
		currentGame = Board.clearBoard();
		jumpExecutor = new JumpExecutor();
		boardPanel.setBoard(currentGame, jumpExecutor);
		statusLabel.setText("Your jump");

		solver = (AlphaBetaPruning) currentGame.getOtherPlayer();
		boardPanel.makeJump();
	}


	public NineMensMorrisGUI() {
		super("Nine Men's Morris");
		boardPanel = new NineMensMorrisBoard();
		add(boardPanel, BorderLayout.CENTER);
		controls = new JPanel();
		controls.setLayout(new FlowLayout());
		newGameButton = new JButton("New game");
		newGameButton.addActionListener(e -> startNewGame());
		controls.add(newGameButton);
		controls.add(new JLabel("Status:"));
		statusLabel = new JLabel("Your jump");
		controls.add(statusLabel);
		add(controls, BorderLayout.SOUTH);
		startNewGame();
	}


	public static void main(String[] args) {
		JFrame game = new NineMensMorrisGUI();
		game.setSize(600, 700);
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setVisible(true);
	}
}
