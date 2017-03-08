package eu.veldsoft.ithaka.board.game.model.ai;

import eu.veldsoft.ithaka.board.game.model.Board;
import eu.veldsoft.ithaka.board.game.model.Move;

/**
 * Root of A.I. class tree.
 *
 * @author Todor Balabanov
 */
public interface ArtificialIntelligence {
    /**
     * A.I. move offer.
     *
     * @param board State of the game board.
     * @return Coordinates for a move x1 and y1 pice to move, x2 and y2 cell to move to.
     * @throws NoValidMoveException If move offer is not possible
     */
    Move move(Board board) throws NoValidMoveException;
}
