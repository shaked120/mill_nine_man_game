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
	private JPanel controls;
	private JButton newGameButton;
	private JTextField maxTimeTextField;
	private JTextField maxDepthTextField;
	private final JLabel statusLabel;
	private AlphaBetaPruning solver;
	private volatile MoveExecutorCallback moveExecutor;

	private class MoveExecutor implements MoveExecutorCallback {
		private boolean terminate = false;
		
		public synchronized void terminate() {
			this.terminate = true;
			solver.terminateSearch();
		}
		
		@Override
		public synchronized void makeMove(AbstractJump move) {
			if (terminate) {
				return;
			}

			currentGame.makeMove(move);
			boardPanel.repaint();
			
			if (currentGame.hasCurrentPlayerLost()) {
				if (currentGame.getCurrentPlayer().getColor() == Color.White) {
					statusLabel.setText("You won!");
				} else {
					statusLabel.setText("You lost!");
				}
			} else if (currentGame.getCurrentPlayer().getColor() == Color.White) {
				statusLabel.setText("Making move...");
				
				new Thread(() -> {
					AbstractJump move1 = solver.searchForBestMove();
					MoveExecutor.this.makeMove(move1);
				}).start();
			} else {
				statusLabel.setText("Your move");
				boardPanel.makeMove();
			}
		}
	}
	
	private void startNewGame() {
		if (moveExecutor != null) {
			moveExecutor.terminate();
		}

		currentGame = Board.clearBoard();
		moveExecutor = new MoveExecutor();
		boardPanel.setBoard(currentGame, moveExecutor);
		statusLabel.setText("Your move");

		solver = (AlphaBetaPruning) currentGame.getOtherPlayer();
		boardPanel.makeMove();
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
		controls.add(new JLabel("Max move time:"));
		maxTimeTextField = new JTextField(3);
		maxTimeTextField.setText("30");
		controls.add(maxTimeTextField);
		controls.add(new JLabel("Max searching depth:"));
		maxDepthTextField = new JTextField(3);
		maxDepthTextField.setText("15");
		controls.add(maxDepthTextField);
		controls.add(new JLabel("Status:"));
		statusLabel = new JLabel("Your move");
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
