package com.almasb.checkers;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class MoveResult {

    private MoveType type;

    public MoveType getType() {
        return type;
    }

    private Piece piece;

    public Piece getPiece() {
        return piece;
    }

    public MoveResult(MoveType type) {
        this(type, null);
    }

    public MoveResult(MoveType type, Piece piece) {
        this.type = type;
        this.piece = piece;
    }
}
