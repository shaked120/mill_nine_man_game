package Mill_project;

// we must implement this interface for algo player (alpha beta pruning)
// if we want to implement min-max algo we should implement new function
public interface JumpEvaluationFunction {
	int evaluate(Board board, AbstractJump jump);
}
