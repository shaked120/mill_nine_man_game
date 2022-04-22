package Mill_project;

// used by algo player, combines jump and removing piece
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
