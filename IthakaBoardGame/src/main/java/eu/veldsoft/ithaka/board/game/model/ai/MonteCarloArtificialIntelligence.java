package eu.veldsoft.ithaka.board.game.model.ai;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.veldsoft.ithaka.board.game.model.Board;
import eu.veldsoft.ithaka.board.game.model.Move;
import eu.veldsoft.ithaka.board.game.model.Piece;
import eu.veldsoft.ithaka.board.game.model.Util;

/**
 * Monte Carlo tree search for the most stupid bot.
 *
 * @author Todor Balabanov
 */
public class MonteCarloArtificialIntelligence extends AbstractArtificialIntelligence {
    /**
     * Milliseconds used for move calculation.
     */
    private int time = 0;

    /**
     * Constructor with parameters.
     *
     * @param time Time for calculations.
     */
    public MonteCarloArtificialIntelligence(int time) {
        super();

        this.time = time;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Move move(Board board) throws NoValidMoveException {
        super.move(board);

        Board original = board;
        board = new Board(original);

        Map<Move, Integer> counters = new HashMap<Move, Integer>();
        List<Move> valid = board.allValidMoves();

        /*
         * Initialize counters.
         */
        for (Move m : valid) {
            counters.put(m, 0);
        }

		/*
         * Calculate when to stop.
		 */
        long time = this.time + System.currentTimeMillis();

        Move move = null;

		/*
         * Experiments are limited according to the available time.
		 */
        while (System.currentTimeMillis() < time) {
            Collections.shuffle(valid);
            move = valid.get(0);

			/*
             * Try to click all cells in the board.
			 */
            if (board.click(move.getStartX(), move.getStartY()) == true && board.click(move.getEndX(), move.getEndY()) == true) {
                board.next();

						/*
                         * Play until someone win.
						 */
                while (board.hasWinner() == false) {
                            /*
                             * Select random cell to play.
							 */
                    if (true == board.click(Util.PRNG.nextInt(Board.COLS), Util.PRNG.nextInt(Board.ROWS))) {
                                /*
								 * Move to next player if the turn was valid.
								 */
                        board.next();
                    }
                }
                board.setGameOver();

                // TODO Use counters.
                move = new Move(i, j, true);
                if (counters.containsKey(move) == false) {
                    counters.put(move, 0);
                }

						/*
						 * Calculate total score.
						 */
                Map<Type, Integer> score = board.score();
                int others = 0;
                for (Type key : score.keySet()) {
                    others += score.get(key);
                }

						/*
						 * Others have current player score that is why it
						 * should be multiplied by two.
						 */
                counters.put(move, counters.get(move) + 2 * score.get(player) - others);

						/*
						 * Reinitialize the board for the next experiment.
						 */
                board = new Board(state);
            }
        }

		/*
		 * Evaluate the possible moves by finding the biggest number.
		 */
        int max = (Collections.max(counters.values()));
        for (Map.Entry<Move, Integer> entry : counters.entrySet()) {
            if (entry.getValue() != max) {
                continue;
            }

            move = entry.getKey();

			/*
			 * There is no need to loop over the entire map.
			 */
            break;
        }

        return move;
    }
}
