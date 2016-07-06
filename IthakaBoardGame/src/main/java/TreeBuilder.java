import eu.veldsoft.ithaka.board.game.model.Board;
import eu.veldsoft.ithaka.board.game.model.ai.BruteForceArtificialIntelligence;

public class TreeBuilder {
	public static void main(String args[]) {
		BruteForceArtificialIntelligence ai = new BruteForceArtificialIntelligence();
		ai.move(new Board());
	}
}