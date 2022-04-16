package gui;

import Mill_project.AbstractJump;

public interface JumpExecutorCallback {
	void makeJump(AbstractJump jump);
	void terminate();
}
