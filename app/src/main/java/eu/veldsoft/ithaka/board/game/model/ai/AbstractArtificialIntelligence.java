package eu.veldsoft.ithaka.board.game.model.ai;

import java.util.Random;

import eu.veldsoft.ithaka.board.game.model.Board;
import eu.veldsoft.ithaka.board.game.model.Move;

/**
 * Root of AI classes.
 *
 * @author Todor Balabanov
 */
abstract class AbstractArtificialIntelligence implements ArtificialIntelligence {
	/**
	 * Pseudo-random number generator object.
	 */
	protected static final Random PRNG = new Random();

	/**
	 * @{inheritDoc}
	 */
	@Override
	public Move move(Board board) throws NoValidMoveException {
		//TODO Check for valid move.
		return null;
	}
}