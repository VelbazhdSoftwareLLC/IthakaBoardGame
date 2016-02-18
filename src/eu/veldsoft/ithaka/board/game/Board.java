package eu.veldsoft.ithaka.board.game;

import java.util.Vector;

class Board {
	static final int ROWS = 4;
	static final int COLS = 4;
	static final int WIN_LINE_LENGTH = 3;

	private Piece pieces[][] = { { Piece.BLUE, Piece.BLUE, Piece.FUCHSIA, Piece.FUCHSIA },
			{ Piece.BLUE, Piece.EMPTY, Piece.EMPTY, Piece.FUCHSIA },
			{ Piece.ORANGE, Piece.EMPTY, Piece.EMPTY, Piece.GREEN },
			{ Piece.ORANGE, Piece.ORANGE, Piece.GREEN, Piece.GREEN }, };

	private boolean selection[][] = { { false, false, false, false }, { false, false, false, false },
			{ false, false, false, false }, { false, false, false, false }, };

	private int turn = 0;

	private boolean gameOver = false;

	private Vector<Move> moves = new Vector<Move>();

	private boolean hasSelection() {
		for (int i = 0; i < selection.length; i++) {
			for (int j = 0; j < selection[i].length; j++) {
				if (selection[i][j] == true) {
					return true;
				}
			}
		}

		return false;
	}

	private void unselect() {
		for (int i = 0; i < selection.length; i++) {
			for (int j = 0; j < selection[i].length; j++) {
				selection[i][j] = false;
			}
		}
	}

	private boolean hasEmptyPath(Move move) {
		/*
		 * Move to itself is not possible.
		 */
		if (move.startX == move.endX && move.startY == move.endY) {
			return false;
		}

		int xStep = move.endX - move.startX;
		int yStep = move.endY - move.startY;

		/*
		 * Move can be orthogonal or diagonal.
		 */
		if (xStep != 0 && yStep != 0 && Math.abs(xStep) != Math.abs(yStep)) {
			return false;
		}

		if (xStep != 0) {
			xStep /= Math.abs(xStep);
		}
		if (yStep != 0) {
			yStep /= Math.abs(yStep);
		}

		/*
		 * Full path should be only empty cells.
		 */
		for (int i = move.startX + xStep, j = move.startY + yStep; i <= move.endX
				&& j <= move.endY; i += xStep, j += yStep) {
			if (pieces[i][j] != Piece.EMPTY) {
				return false;
			}
		}

		return true;
	}

	private Move formMove(int x, int y) {
		for (int i = 0; i < selection.length; i++) {
			for (int j = 0; j < selection[i].length; j++) {
				if (selection[i][j] == true) {
					return new Move(i, j, x, y);
				}
			}
		}

		return null;
	}

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

	public int getTurn() {
		return turn;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public Piece[][] getPieces() {
		return pieces;
	}

	public boolean[][] getSelection() {
		return selection;
	}

	public void reset() {
		turn = 0;
		gameOver = false;
		moves = new Vector<Move>();

		pieces = new Piece[][] { { Piece.BLUE, Piece.BLUE, Piece.FUCHSIA, Piece.FUCHSIA },
				{ Piece.BLUE, Piece.EMPTY, Piece.EMPTY, Piece.FUCHSIA },
				{ Piece.ORANGE, Piece.EMPTY, Piece.EMPTY, Piece.GREEN },
				{ Piece.ORANGE, Piece.ORANGE, Piece.GREEN, Piece.GREEN }, };

		selection = new boolean[][] { { false, false, false, false }, { false, false, false, false },
				{ false, false, false, false }, { false, false, false, false }, };
	}

	public boolean click(int x, int y) {
		if (hasSelection() == true) {
			if (pieces[x][y] != Piece.EMPTY) {
				unselect();
				return false;
			}

			if (pieces[x][y] == Piece.EMPTY) {
				Move move = formMove(x, y);

				/*
				 * If move is not generated there is no valid turn to be done.
				 */
				if (move == null) {
					unselect();
					return false;
				}

				if (isValid(move) == true) {
					/*
					 * If the move is valid - finish the turn.
					 */
					moves.add(move);
					pieces[move.endX][move.endY] = pieces[move.startX][move.startY];
					pieces[move.startX][move.startY] = Piece.EMPTY;
					unselect();
					return true;
				} else {
					/*
					 * If the move is not valid - remove selection.
					 */
					unselect();
					return false;
				}
			}
		} else {
			if (pieces[x][y] == Piece.EMPTY) {
				/*
				 * Nothing to be done.
				 */
				return false;
			}

			if (pieces[x][y] != Piece.EMPTY) {
				selection[x][y] = true;
				return true;
			}
		}

		return false;
	}

	public void next() {
		turn++;
	}

	public boolean isValid(Move move) {
		/*
		 * Last moved piece can not be moved again.
		 */
		if (moves.size() > 0 && move.startX == moves.lastElement().endX && move.startY == moves.lastElement().endY) {
			return false;
		}

		/*
		 * Check for neighbors with the same color. Observe array boundaries.
		 */
		int count = 0;
		Piece piece = pieces[move.startX][move.startY];
		for (int i = move.startX - 1; i < move.startX + 1; i++) {
			if (i < 0 || i >= COLS) {
				continue;
			}

			for (int j = move.startY - 1; j < move.startY + 1; j++) {
				if (j < 0 || j >= ROWS) {
					continue;
				}

				if (piece == pieces[i][j]) {
					count++;
				}
			}
		}

		if (count <= 1) {
			return false;
		}

		/*
		 * Check for empty cells between start and end.
		 */
		if (hasEmptyPath(move) == false) {
			return false;
		}

		return true;
	}

	public boolean hasThirdMoveRepetition() {
		int count = 0;

		/*
		 * Last move will be compared with moves before that.
		 */
		Move lastMove = moves.lastElement();
		for (int i = moves.size() - 1; i >= 0; i -= 2) {
			if (lastMove.equals(moves.elementAt(i)) == false) {
				break;
			}

			count++;
		}

		if (count >= 3) {
			return true;
		}

		return false;
	}

	public boolean hasWinner() {
		for (int i = 0; i < pieces.length; i++) {
			for (int j = 0; j < pieces[i].length; j++) {
				if (pieces[i][j] == Piece.EMPTY) {
					continue;
				}

				if (hasHorizontalLine(i, j) == true) {
					return true;
				}
				if (hasVerticalLine(i, j) == true) {
					return true;
				}
				if (hasFirstDiagonalLine(i, j) == true) {
					return true;
				}
				if (hasSecondDiagonalLine(i, j) == true) {
					return true;
				}
			}
		}

		return false;
	}
}
