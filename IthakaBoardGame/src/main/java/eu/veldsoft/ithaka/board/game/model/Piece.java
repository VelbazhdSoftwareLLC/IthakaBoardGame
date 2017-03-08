package eu.veldsoft.ithaka.board.game.model;

import android.content.res.Resources;

/**
 * Board pieces.
 *
 * @author Todor Balabanov
 */
public enum Piece {
    //TODO It is completely wrong object-oriented design EMPTY to be in the bunch of pieces.
    EMPTY(0), BLUE(1), FUCHSIA(2), GREEN(3), ORANGE(4);

    /**
     * Constant identifier.
     */
    private int id;

    /**
     * Factory function giving object by numerical value.
     *
     * @param id Piece identifier.
     * @return Object by identifier.
     * @throws RuntimeException Rise if it is not found.
     */
    static public Piece valueOf(long id) throws RuntimeException {
        for (Piece piece : Piece.values()) {
            if (piece.id == id) {
                return piece;
            }
        }

        throw new RuntimeException("Constant value not found for id +" + id + "!");
    }

    /**
     * Enum values are initialized only in private constructor.
     *
     * @param id Identifier value.
     */
    private Piece(int id) {
        this.id = id;
    }

    /**
     * Identifier getter.
     *
     * @return Numeric value of the enum constant.
     */
    public int getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "" + id;
    }
}
