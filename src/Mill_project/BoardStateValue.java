package Mill_project;

public final class BoardStateValue {
    private final int value;
    private final int remainingDepth;
    private final AbstractJump foundBestJump;
    private final boolean hasBeenCut;
    private final boolean couldHaveBeenCutDeeper;

    public BoardStateValue(int value, int remainingDepth, AbstractJump foundBestJump, boolean hasBeenCut, boolean couldHaveBeenCutDeeper) {
        this.value = value;
        this.remainingDepth = remainingDepth;
        this.foundBestJump = foundBestJump;
        this.hasBeenCut = hasBeenCut;
        this.couldHaveBeenCutDeeper = couldHaveBeenCutDeeper;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (couldHaveBeenCutDeeper ? 1231 : 1237);
        result = prime * result
                + ((foundBestJump == null) ? 0 : foundBestJump.hashCode());
        result = prime * result + (hasBeenCut ? 1231 : 1237);
        result = prime * result + remainingDepth;
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
        BoardStateValue other = (BoardStateValue) obj;
        if (couldHaveBeenCutDeeper != other.couldHaveBeenCutDeeper) {
            return false;
        }
        if (foundBestJump == null) {
            if (other.foundBestJump != null) {
                return false;
            }
        } else if (!foundBestJump.equals(other.foundBestJump)) {
            return false;
        }
        if (hasBeenCut != other.hasBeenCut) {
            return false;
        }
        if (remainingDepth != other.remainingDepth) {
            return false;
        }
        return value == other.value;
    }

    public int getValue() {
        return value;
    }

    public int getRemainingDepth() {
        return remainingDepth;
    }

    public AbstractJump getFoundBestJump() {
        return foundBestJump;
    }

    public boolean hasBeenCut() {
        return hasBeenCut;
    }

    public boolean couldHaveBeenCutDeeper() {
        return couldHaveBeenCutDeeper;
    }
}

