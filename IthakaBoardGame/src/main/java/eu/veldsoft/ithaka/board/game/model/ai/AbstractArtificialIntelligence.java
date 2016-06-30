package eu.veldsoft.ithaka.board.game.model.ai;

import java.util.Random;

/**
 * Root of AI classes.
 *
 * @author Todor Balabanov
 */
abstract class AbstractArtificialIntelligence implements ArtificialIntelligence {
	/**
	 * Pseudo-random number generator object.
	 */
	protected static Random PRNG = new Random();
}