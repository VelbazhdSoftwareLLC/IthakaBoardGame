package eu.veldsoft.ithaka.board.game.model.ai;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Root of A.I. class tree.
 *
 * @author Todor Balabanov
 */
public interface ArtificialIntelligence {
	/**
	 * Number of rows on the game board.
	 */
	static final int ROWS = 4;

	/**
	 * Number of columns on the game board.
	 */
	static final int COLS = 4;

	/**
	 * Total number of players on the board.
	 */
	static final int NUMBER_OF_PLAYERS = 2;

	/**
	 * Size of the win line
	 */
	static final int WIN_LINE_LENGTH = 3;

	/**
	 * Mapping of the board information in integer numbers.
	 */
	static final Set<Integer> STATE_VALUES = new HashSet<Integer>(
			  Arrays.asList(new Integer[] { 0, 1, 2, 3, 4 }));

	/**
	 * A.I. move offer.
	 *
	 * @param state State of the game board.
	 *
	 * @return Coordinates for a move x1 and y1 pice to move, x2 and y2 cell to move to.
	 *
	 * @throws NoValidMoveException If move offer is not possible
	 */
	int[] move(int[][] state) throws NoValidMoveException;
}