package eu.veldsoft.ithaka.board.game.model.ai;

import eu.veldsoft.ithaka.board.game.model.Board;
import eu.veldsoft.ithaka.board.game.model.Move;
import eu.veldsoft.ithaka.board.game.model.Piece;

/**
 * Random search for the most stupid bot.
 *
 * @author Todor Balabanov
 */
public class RandomArtificialIntelligence extends AbstractArtificialIntelligence {
	/**
	 * @{inheritDoc}
	 */
	@Override
	public Move move(Board board) throws NoValidMoveException {
		Move move = null;
		Piece state[][] = board.getPieces();
		do {
			int x1 = PRNG.nextInt(state.length);
			int y1 = PRNG.nextInt(state[x1].length);
			int x2 = PRNG.nextInt(state.length);
			int y2 = PRNG.nextInt(state[x2].length);
			move = new Move(x1, y1, x2, y2);
		} while (board.isValid(move) == false);

		return move;
	}
}
