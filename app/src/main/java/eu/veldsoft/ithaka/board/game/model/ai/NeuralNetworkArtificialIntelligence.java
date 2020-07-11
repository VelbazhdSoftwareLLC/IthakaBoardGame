package eu.veldsoft.ithaka.board.game.model.ai;

import eu.veldsoft.ithaka.board.game.model.Board;
import eu.veldsoft.ithaka.board.game.model.Move;
import eu.veldsoft.ithaka.board.game.model.Piece;

import org.encog.engine.network.activation.ActivationTANH;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;

/**
 * Artificial neural network for a bot.
 *
 * @author Veselin Gospodinov
 */
public class NeuralNetworkArtificialIntelligence extends AbstractArtificialIntelligence {
    /**
     * Artificial neural network object.
     */
    private BasicNetwork network = new BasicNetwork();

    /**
     * Constructor without parameters.
     */
    public NeuralNetworkArtificialIntelligence() {
        super();

        network.addLayer(new BasicLayer(new ActivationTANH(), true, 16));
        network.addLayer(new BasicLayer(new ActivationTANH(), true, 17));
        network.addLayer(new BasicLayer(new ActivationTANH(), false, 16));

        network.getStructure().finalizeStructure();

        network.reset();
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
