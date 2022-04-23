package mill_classes;

// we must implement this interface for algo player (alpha beta pruning)
// if we want to implement min-max algo we should implement new function
public interface JumpEvaluationFunctionInterface {
	int evaluate(Board board, AbstractJump jump);
}
