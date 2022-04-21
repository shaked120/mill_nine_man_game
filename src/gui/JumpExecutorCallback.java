package gui;

import Mill_project.AbstractJump;

public interface JumpExecutorCallback {
	void makeJump(AbstractJump jump);
	void makeJump(AbstractJump jump, boolean togglePlayer);
	void togglePlayer();
	void terminate();
}
