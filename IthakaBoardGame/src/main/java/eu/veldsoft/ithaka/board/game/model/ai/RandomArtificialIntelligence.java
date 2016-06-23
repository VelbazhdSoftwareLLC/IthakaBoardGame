package eu.veldsoft.ithaka.board.game.model.ai;

/**
 *
 */
public class RandomArtificialIntelligence implements ArtificialIntelligence {
	@Override
	public int[] move(int[][] state) throws NoValidMoveException {
		return new int[]{-1, -1, -1, -1};
	}
}
