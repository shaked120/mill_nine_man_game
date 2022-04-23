package gui;
// interface that keep 4 functions for algo to use them
import mill_classes.AbstractJump;

public interface JumpExecutorCallback {
	void makeJump(AbstractJump jump);
	void makeJump(AbstractJump jump, boolean togglePlayer);
	void togglePlayer();
	void terminate();
}
