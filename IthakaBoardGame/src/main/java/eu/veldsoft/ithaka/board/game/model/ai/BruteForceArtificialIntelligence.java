package eu.veldsoft.ithaka.board.game.model.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.veldsoft.ithaka.board.game.model.Board;
import eu.veldsoft.ithaka.board.game.model.Move;
import eu.veldsoft.ithaka.board.game.model.Piece;

/**
 * Brute force search for the most stupid bot.
 *
 * @author Todor Balabanov
 */
public class BruteForceArtificialIntelligence  extends AbstractArtificialIntelligence {
	/**
	 * Game tree stored as associative array. The key is a board state node. The value is a connections list of edges.
	 */
	private static Map<Long,List<Long>> tree = new HashMap<Long,List<Long>>();

	/**
	 * Static constrictor is used to build game tree.
	 */
	static {
		//TODO Build the tree in separate thread.
		Board board = new Board();
		List<Long> unhandled = new ArrayList<Long>();

		/*
		 * Add tree root.
		 */
		Long root = board.toBinary();
		tree.put(root, new ArrayList<Long>());
		unhandled.add(root);

		/*
		 * Add sub-trees.
		 */
		while (unhandled.isEmpty() == false) {
			Long node = unhandled.get(0);

			List<Move> moves = board.allValidMoves();
			//TODO Add as edges.

			unhandled.remove(0);
		}
	}

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
			move = new Move(x1,y1,x2,y2);
		}while(board.isValid(move) == false);

		return move;
	}
}
