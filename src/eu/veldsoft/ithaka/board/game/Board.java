package eu.veldsoft.ithaka.board.game;

class Board {
	static final int ROWS = 4;
	static final int COLS = 4;
	static final int WIN_LINE_LENGTH = 3;

	private Piece pieces[][] = new Piece[COLS][ROWS];
	
	private int turn = 0;
	
	private boolean gameOver = false;
	
	
}
