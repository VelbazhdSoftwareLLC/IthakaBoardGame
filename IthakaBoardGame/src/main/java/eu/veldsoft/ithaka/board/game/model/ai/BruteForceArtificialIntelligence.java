package eu.veldsoft.ithaka.board.game.model.ai;

import java.io.OutputStream;
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
public class BruteForceArtificialIntelligence extends AbstractArtificialIntelligence {
    /**
     * Game tree stored as associative array. The key is a board state node. The value is a connections list of edges.
     */
    private static Map<Long, List<Long>> tree = new HashMap<Long, List<Long>>();

    /**
     * Static constrictor is used to build game tree.
     */
    static {
        //TODO Build the tree in separate thread.
        List<Long> unhandled = new ArrayList<Long>();

		/*
         * Add tree root.
		 */
        long root = (new Board()).toBinary();
        tree.put(root, new ArrayList<Long>());
        unhandled.add(root);

		/*
		 * Add sub-trees.
		 */
        while (unhandled.isEmpty() == false) {
            Long node = unhandled.get(0);
            List<Move> moves = (new Board(node)).allValidMoves();
            for (Move move : moves) {
				/*
				 * Generate next state.
				 */
                Board board = new Board(node);
                board.click(move.getStartX(), move.getStartY());
                board.click(move.getEndX(), move.getEndY());
                long state = board.toBinary();

				/*
				 * Build a tree not a graph.
				 */
                if (tree.containsKey(state) == true) {
                    continue;
                }

                tree.get(node).add(move.toBinary());
                tree.put(state, new ArrayList<Long>());

                if (board.hasWinner() == false) {
                    unhandled.add(state);
                }
            }

            unhandled.remove(0);
        }
    }

    /**
     * Store in a stream generated game tree.
     *
     * @param out Output stream object.
     */
    public static void storeTree(OutputStream out) {
        //TODO Store game tree.
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
            move = new Move(x1, y1, x2, y2);
        } while (board.isValid(move) == false);

        return move;
    }
}
