package eu.veldsoft.ithaka.board.game;

class Board {
	static final int ROWS = 4;
	static final int COLS = 4;
	static final int WIN_LINE_LENGTH = 3;

	private Piece pieces[][] = new Piece[COLS][ROWS];

	private int turn = 0;

	private boolean gameOver = false;

	private boolean hasHorizontalLine(int i, int j) {
		Piece current = pieces[i][j];

		for (int k = 0; k < WIN_LINE_LENGTH; k++) {
			/*
			 * Keep array limits strict.
			 */
			if (i + k >= COLS) {
				return false;
			}

			if (pieces[i + k][j] != current) {
				return false;
			}
		}

		return true;
	}

	private boolean hasVerticalLine(int i, int j) {
		Piece current = pieces[i][j];

		for (int k = 0; k < WIN_LINE_LENGTH; k++) {
			/*
			 * Keep array limits strict.
			 */
			if (j + k >= ROWS) {
				return false;
			}

			if (pieces[i][j + k] != current) {
				return false;
			}
		}

		return true;
	}

	private boolean hasFirstDiagonalLine(int i, int j) {
		Piece current = pieces[i][j];

		for (int k = 0; k < WIN_LINE_LENGTH; k++) {
			/*
			 * Keep array limits strict.
			 */
			if (i + k >= COLS) {
				return false;
			}
			if (j + k >= ROWS) {
				return false;
			}

			if (pieces[i + k][j + k] != current) {
				return false;
			}
		}

		return true;
	}

	private boolean hasSecondDiagonalLine(int i, int j) {
		Piece current = pieces[i][j];

		for (int k = 0; k < WIN_LINE_LENGTH; k++) {
			/*
			 * Keep array limits strict.
			 */
			if (i - k < 0) {
				return false;
			}
			if (j + k >= ROWS) {
				return false;
			}

			if (pieces[i - k][j + k] != current) {
				return false;
			}
		}

		return true;
	}

	public boolean hasWinner() {
		for (int i = 0; i < pieces.length; i++) {
			for (int j = 0; j < pieces[i].length; j++) {
				if (pieces[i][j] == Piece.EMPTY) {
					continue;
				}

				if (hasHorizontalLine(i, j) == true) {
					gameOver = true;
					return true;
				}
				if (hasVerticalLine(i, j) == true) {
					gameOver = true;
					return true;
				}
				if (hasFirstDiagonalLine(i, j) == true) {
					gameOver = true;
					return true;
				}
				if (hasSecondDiagonalLine(i, j) == true) {
					gameOver = true;
					return true;
				}
			}
		}

		return false;
	}
}
