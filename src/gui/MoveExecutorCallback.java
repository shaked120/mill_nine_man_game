package gui;

import Mill_project.AbstractJump;

public interface MoveExecutorCallback {
	void makeMove(AbstractJump move);
	void terminate();
}
