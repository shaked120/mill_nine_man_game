package algoritmas;

import Mill_project.AbstractJump;
import Mill_project.RemoveMan;

public class ValuedMove implements Comparable<ValuedMove>{
    private final AbstractJump move;
    private final int value;

    public ValuedMove(AbstractJump move, int value) {
        this.move = move;
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((move == null) ? 0 : move.hashCode());
        result = prime * result + value;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ValuedMove other = (ValuedMove) obj;
        if (move == null) {
            if (other.move != null) {
                return false;
            }
        } else if (!move.equals(other.move)) {
            return false;
        }
        return value == other.value;
    }

    public AbstractJump getMove() {
        return move;
    }

    @SuppressWarnings("unused")
    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(ValuedMove o) {
        if (value > o.value) {
            return -1;
        } else if (value < o.value) {
            return 1;
        } else if (move.hashCode() < o.move.hashCode()) {
            return -1;
        } else if (move.hashCode() > o.move.hashCode()) {
            return 1;
        } else if (move.getSource().getId() - o.move.getSource().getId() != 0) {
            return move.getSource().getId() - o.move.getSource().getId();
        } else if (move.getDestination().getId() - o.move.getDestination().getId() != 0) {
            return move.getDestination().getId() - o.move.getDestination().getId();
        } else if (move instanceof RemoveMan && o.move instanceof RemoveMan) {
            return move.getSource().getId() - o.move.getSource().getId();
        } else {
            return 0;
        }
    }
}
