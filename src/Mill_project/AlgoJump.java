package Mill_project;

public class AlgoJump extends AbstractJump {

    private final HouseInBoard takenPiece;

    public AlgoJump(HouseInBoard src, HouseInBoard dest, HouseInBoard takenPiece) {
        super(src, dest);
        this.takenPiece = takenPiece;
    }

    public HouseInBoard getTakenPiece() {
        return takenPiece;
    }
}
