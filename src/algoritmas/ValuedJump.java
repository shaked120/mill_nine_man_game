package algoritmas;

import mill_classes.AbstractJump;
import mill_classes.RemoveMan;

// how much each jump is valued
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
        } else if (jump.getSourceId() - o.jump.getSourceId() != 0) {
            return jump.getSourceId() - o.jump.getSourceId();
        } else if (jump.getDestinationId() - o.jump.getDestinationId() != 0) {
            return jump.getDestinationId() - o.jump.getDestinationId();
        } else if (jump instanceof RemoveMan && o.jump instanceof RemoveMan) {
            return jump.getSourceId() - o.jump.getSourceId();
        } else {
            return 0;
        }
    }
}
