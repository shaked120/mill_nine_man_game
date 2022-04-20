package algoritmas;

import Mill_project.AbstractJump;
import Mill_project.RemoveMan;

public class ValuedJump implements Comparable<ValuedJump>{
    private final AbstractJump jump;
    private final int value;

    public ValuedJump(AbstractJump jump, int value) {
        this.jump = jump;
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((jump == null) ? 0 : jump.hashCode());
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
        ValuedJump other = (ValuedJump) obj;
        if (jump == null) {
            if (other.jump != null) {
                return false;
            }
        } else if (!jump.equals(other.jump)) {
            return false;
        }
        return value == other.value;
    }

    public AbstractJump getJump() {
        return jump;
    }

    @SuppressWarnings("unused")
    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(ValuedJump o) {
        if (value > o.value) {
            return -1;
        } else if (value < o.value) {
            return 1;
        } else if (jump.hashCode() < o.jump.hashCode()) {
            return -1;
        } else if (jump.hashCode() > o.jump.hashCode()) {
            return 1;
        } else if (jump.getSource().getId() - o.jump.getSource().getId() != 0) {
            return jump.getSource().getId() - o.jump.getSource().getId();
        } else if (jump.getDestination().getId() - o.jump.getDestination().getId() != 0) {
            return jump.getDestination().getId() - o.jump.getDestination().getId();
        } else if (jump instanceof RemoveMan && o.jump instanceof RemoveMan) {
            return jump.getSource().getId() - o.jump.getSource().getId();
        } else {
            return 0;
        }
    }
}