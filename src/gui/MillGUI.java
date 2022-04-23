package gui;

import mill_classes.AbstractJump;
import mill_classes.Board;
import mill_classes.Color;
import algoritmas.AlphaBetaAlgo;

import javax.swing.*;
import java.awt.*;

public class MillGUI extends JFrame {
	private static final long serialVersionUID = -514606427157467570L;

	private final MillBoard boardPanel;
	private final JLabel statusLabel;

	private volatile JumpExecutorCallback jumpExecutor;

	private Board currentGame;
	private AlphaBetaAlgo solver;

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
				String text = currentGame.getCurrentPlayer().getColor() == Color.Black ? "You won!" : "You lost!";
				statusLabel.setText(text);
			} else if (currentGame.getCurrentPlayer().getColor() == Color.Black) {
				runSolver();
			} else {
				statusLabel.setText("Your jump");
				boardPanel.makeJump();
			}
		}

		@Override
		public synchronized void togglePlayer() {
			currentGame.togglePlayer();
			if (currentGame.getCurrentPlayer().getColor() == Color.Black) {
				runSolver();
			}
		}

		private void runSolver() {
			statusLabel.setText("Making jump...");

			new Thread(() -> JumpExecutor.this.makeJump(solver.searchForBestJump())).start();
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

		solver = (AlphaBetaAlgo) currentGame.getOtherPlayer();
		boardPanel.makeJump();
	}

	public MillGUI() {
		super("Nine Men's Morris");

		boardPanel = new MillBoard();
		add(boardPanel, BorderLayout.CENTER);

		JPanel controls = new JPanel();
		controls.setLayout(new FlowLayout());
		JButton newGameButton = new JButton("New game");
		newGameButton.addActionListener(e -> startNewGame());
		controls.add(newGameButton);
		controls.add(new JLabel("Status:"));
		statusLabel = new JLabel("Your jump");
		controls.add(statusLabel);
		add(controls, BorderLayout.SOUTH);

		startNewGame();
	}

	public static void main(String[] args) {
		JFrame game = new MillGUI();
		game.setSize(600, 700);
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setVisible(true);
	}
}
