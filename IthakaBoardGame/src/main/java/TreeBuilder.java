import eu.veldsoft.ithaka.board.game.model.Board;
import eu.veldsoft.ithaka.board.game.model.ai.BruteForceArtificialIntelligence;

public class TreeBuilder {
	public static void main(String args[]) {
		System.err.println("Test point 1 ...");
		BruteForceArtificialIntelligence ai = new BruteForceArtificialIntelligence();
		ai.move(new Board());
		System.err.println("Test point 2 ...");
	}
}