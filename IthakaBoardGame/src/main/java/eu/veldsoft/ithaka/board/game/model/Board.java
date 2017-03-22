package eu.veldsoft.ithaka.board.game.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Object-oriented representation of the game board.
 *
 * @author Todor Balabanov
 */
public class Board implements Serializable {
    /**
     * Number of rows on the game board.
     */
    public static final int ROWS = 4;

    /**
     * Number of columns on the game board.
     */
    public static final int COLS = 4;

    /**
     * Win line size.
     */
    protected static final int WIN_LINE_LENGTH = 3;

    /**
     * Total number of players in the game.
     */
    public static final int NUMBER_OF_PLAYERS = 2;

    /**
     * Pieces on the bard.
     */
    private Piece pieces[][] = {
            {Piece.BLUE, Piece.BLUE, Piece.FUCHSIA, Piece.FUCHSIA},
            {Piece.BLUE, Piece.EMPTY, Piece.EMPTY, Piece.FUCHSIA},
            {Piece.ORANGE, Piece.EMPTY, Piece.EMPTY, Piece.GREEN},
            {Piece.ORANGE, Piece.ORANGE, Piece.GREEN, Piece.GREEN},};

    /**
     * Selected pieces on the board.
     */
    private boolean selection[][] = {{false, false, false, false},
            {false, false, false, false}, {false, false, false, false},
            {false, false, false, false},};

    /**
     * Turns counter.
     */
    private int turn = 0;

    /**
     * Game over flag.
     */
    private boolean gameOver = false;

    /**
     * Turn over flag.
     */
    private boolean turnOver = false;

    /**
     * Moves history.
     */
    private Vector<Move> history = new Vector<Move>();

    /**
     * Unselect all piece on the board.
     */
    private void unselect() {
        for (int i = 0; i < selection.length; i++) {
            for (int j = 0; j < selection[i].length; j++) {
                selection[i][j] = false;
            }
        }
    }

    /**
     * Check for available path for piece traveling during the turn.
     *
     * @param move Move to be checked.
     * @return True if the path is available, false otherwise.
     */
    private boolean hasEmptyPath(Move move) {
        /*
         * Move to itself is not possible.
		 */
        if (move.getStartX() == move.getEndX()
                && move.getStartY() == move.getEndY()) {
            return false;
        }

		/*
         * Starting cell should not be empty.
		 */
        if (pieces[move.getStartX()][move.getStartY()] == Piece.EMPTY) {
            return false;
        }

		/*
         * Destination cell should be empty.
		 */
        if (pieces[move.getEndX()][move.getEndY()] != Piece.EMPTY) {
            return false;
        }

        int xStep = move.getEndX() - move.getStartX();
        int yStep = move.getEndY() - move.getStartY();

		/*
         * Move can be orthogonal or diagonal.
		 */
        if (xStep != 0 && yStep != 0 && Math.abs(xStep) != Math.abs(yStep)) {
            return false;
        }

		/*
         * Scale to -1, 0 or +1.
		 */
        if (xStep != 0) {
            xStep /= Math.abs(xStep);
        }
        if (yStep != 0) {
            yStep /= Math.abs(yStep);
        }

        // TODO Do not check correctly!
        /*
         * Full path should be only empty cells.
		 */
        for (int i = move.getStartX() + xStep, j = move.getStartY() + yStep; (i == move
                .getEndX() && j == move.getEndY()) == false; i += xStep, j += yStep) {
            if (pieces[i][j] != Piece.EMPTY) {
                return false;
            }
        }

        return true;
    }

    /**
     * Form move from the selected cell and destination coordinates.
     *
     * @param x Destination x coordinate.
     * @param y Destination y coordinate.
     * @return Move object.
     */
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

    /**
     * Check for straight horizontal line.
     *
     * @param i Line start x coordinate.
     * @param j Line start y coordinate.
     * @return True if there is straight line, false otherwise.
     */
    private boolean hasHorizontalLine(int i, int j) {
        Piece current = pieces[i][j];

		/*
		 * Empty lines are not possible.
		 */
        if (current == Piece.EMPTY) {
            return false;
        }

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

    /**
     * Check for straight vertical line.
     *
     * @param i Line start x coordinate.
     * @param j Line start y coordinate.
     * @return True if there is straight line, false otherwise.
     */
    private boolean hasVerticalLine(int i, int j) {
        Piece current = pieces[i][j];

		/*
		 * Empty lines are not possible.
		 */
        if (current == Piece.EMPTY) {
            return false;
        }

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

    /**
     * Check for straight diagonal line.
     *
     * @param i Line start x coordinate.
     * @param j Line start y coordinate.
     * @return True if there is straight line, false otherwise.
     */
    private boolean hasFirstDiagonalLine(int i, int j) {
        Piece current = pieces[i][j];

		/*
		 * Empty lines are not possible.
		 */
        if (current == Piece.EMPTY) {
            return false;
        }

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

    /**
     * Check for straight diagonal line.
     *
     * @param i Line start x coordinate.
     * @param j Line start y coordinate.
     * @return True if there is straight line, false otherwise.
     */
    private boolean hasSecondDiagonalLine(int i, int j) {
        Piece current = pieces[i][j];

		/*
		 * Empty lines are not possible.
		 */
        if (current == Piece.EMPTY) {
            return false;
        }

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

    /**
     * Check for neighbors with the same color.
     *
     * @param x Piece x coordinate.
     * @param y Piece y coordinate.
     * @return True if it has neighbors from the same color, false otherwise.
     */
    private boolean hasOwnColorNeighbors(int x, int y) {
        int count = 0;
        Piece piece = pieces[x][y];

		/*
		 * It is completely wrong object-oriented design EMPTY to be in the bunch of pieces.
		 */
        if (piece == Piece.EMPTY) {
            return false;
        }

        for (int i = x - 1; i <= x + 1; i++) {
            if (i < 0 || i >= COLS) {
                continue;
            }

            for (int j = y - 1; j <= y + 1; j++) {
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

        return true;
    }

    /**
     * Turn getter.
     *
     * @return Turn as number.
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Game over flag setter.
     *
     * @param gameOver True if the game is over, false otherwise.
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * Is game over getter.
     *
     * @return True if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Is turn over getter.
     *
     * @return True if the turn is over, false otherwise.
     */
    public boolean isTurnOver() {
        return turnOver;
    }

    /**
     * Default constructor.
     */
    public Board() {
        super();
    }

    /**
     * Copy constructor.
     *
     * @param original Original object.
     */
    public Board(Board original) {
        //TODO Make deep copies.
        this.pieces = original.pieces;
        this.selection = original.selection;

        this.turn = original.turn;
        this.gameOver = original.gameOver;
        this.turnOver = original.turnOver;
        this.history = new Vector<Move>();
        for (Move move : original.history) {
            this.history.add(move);
        }
    }

    /**
     * Constructor for bytes serialization.
     *
     * @param bytes Binary representation of board state.
     */
    public Board(byte[] bytes) {
        this();
        fromBinary(bytes);
    }

    /**
     * Constructor for two dimensional state array.
     *
     * @param state Board state two dimensional array.
     */
    public Board(int[][] state) {
        this();

		/*
		 * Do nothing if null pointers presented and dimensions.
		 */
        if (state == null || state.length != pieces.length) {
            //TODO Rise exception.
            return;
        }
        for (int i = 0; i < state.length; i++) {
            if (state[i] == null || state[i].length != pieces[i].length) {
                //TODO Rise exception.
                return;
            }
        }

		/*
		 * Fill state content.
		 */
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                pieces[i][j] = Piece.valueOf(state[i][j]);
            }
        }
    }

    /**
     * Pieces on the board getter.
     *
     * @return Two-dimensional array with the pieces on the board.
     */
    public Piece[][] getPieces() {
        // TODO Do a deep copy of the array.
        return pieces;
    }

    /**
     * Selected pieces on the board.
     *
     * @return Two-dimensional array with the true for the selected pieces and
     * false for non selected pieces.
     */
    public boolean[][] getSelection() {
        // TODO Do a deep copy of the array.
        return selection;
    }

    /**
     * Check for selected pieces on the board.
     *
     * @return True if there is a selection on the board, false otherwise.
     */
    public boolean hasSelection() {
        for (int i = 0; i < selection.length; i++) {
            for (int j = 0; j < selection[i].length; j++) {
                if (selection[i][j] == true) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Last move coordinates getter.\
     *
     * @return String representation of the last move coordinates.
     */
    public String lastMoveCoordiantes() {
        if (history.size() == 0) {
            return "";
        }

        Move move = history.lastElement();
        return "" + move.getStartX() + " " + move.getStartY() + " " + move.getEndX() + " " + move.getEndY();
    }

    /**
     * Reset the board in the initial conditions.
     */
    public void reset() {
        turn = 0;
        gameOver = false;
        turnOver = false;
        history.clear();

        pieces = new Piece[][]{
                {Piece.BLUE, Piece.BLUE, Piece.FUCHSIA, Piece.FUCHSIA},
                {Piece.BLUE, Piece.EMPTY, Piece.EMPTY, Piece.FUCHSIA},
                {Piece.ORANGE, Piece.EMPTY, Piece.EMPTY, Piece.GREEN},
                {Piece.ORANGE, Piece.ORANGE, Piece.GREEN, Piece.GREEN},};

        selection = new boolean[][]{{false, false, false, false},
                {false, false, false, false}, {false, false, false, false},
                {false, false, false, false},};
    }

    /**
     * Check the piece on the specific coordinates for participation in the last
     * move.
     *
     * @param x Piece x coordinate.
     * @param y Piece y coordinate.
     * @return True if the piece was part of the last move, false otherwise.
     */
    private boolean lastMoved(int x, int y) {
		/*
		 * Last moved piece can not be moved again.
		 */
        if (history.size() > 0 && x == history.lastElement().getEndX()
                && y == history.lastElement().getEndY()) {
            return true;
        }

        return false;
    }

    /**
     * Manipulate board with a click on the specific coordinates.
     *
     * @param x Coordinate x of the click.
     * @param y Coordinate y of the click.
     * @return True if the click was successful, false otherwise.
     */
    public boolean click(int x, int y) {
		/*
		 * If there is a selected piece it should be moved.
		 */
        if (hasSelection() == true) {
			/*
			 * Piece can be moved only in the empty cell.
			 */
            if (pieces[x][y] != Piece.EMPTY) {
                unselect();
                turnOver = false;
                return false;
            }

			/*
			 * Try to do the move.
			 */
            if (pieces[x][y] == Piece.EMPTY) {
                Move move = formMove(x, y);

				/*
				 * If move is not generated there is no valid turn to be done.
				 */
                if (move == null) {
                    unselect();
                    turnOver = false;
                    return false;
                }

				/*
				 * Do the move if it is a valid move.
				 */
                if (isValid(move) == true) {
					/*
					 * If the move is valid - finish the turn.
					 */
                    history.add(move);
                    pieces[move.getEndX()][move.getEndY()] = pieces[move
                            .getStartX()][move.getStartY()];
                    pieces[move.getStartX()][move.getStartY()] = Piece.EMPTY;
                    unselect();
                    turnOver = true;
                    return true;
                } else {
					/*
					 * If the move is not valid - remove selection.
					 */
                    unselect();
                    turnOver = false;
                    return false;
                }
            }
			/*
			 * If there is a no selected piece it should selected.
			 */
        } else {
			/*
			 * Nothing to be done.
			 */
            if (pieces[x][y] == Piece.EMPTY) {
                turnOver = false;
                return false;
            }

			/*
			 * Do piece selection.
			 */
            if (pieces[x][y] != Piece.EMPTY && lastMoved(x, y) == false
                    && hasOwnColorNeighbors(x, y) == true) {
                selection[x][y] = true;
                turnOver = false;
                return true;
            }
        }

        return false;
    }

    /**
     * Steps needed to finish the turn.
     */
    public void turnFinish() {
        turnOver = false;
    }

    /**
     * Increment turn counter.
     */
    public void next() {
        turn++;
    }

    /**
     * Check is the move valid.
     *
     * @param move Move to be checked.
     * @return True if the move is valid, false otherwise.
     */
    public boolean isValid(Move move) {
		/*
		 * Last moved piece can not be moved again.
		 */
        if (lastMoved(move.getStartX(), move.getStartY()) == true) {
            return false;
        }

		/*
		 * Check for neighbors with the same color. Observe array boundaries.
		 */
        if (hasOwnColorNeighbors(move.getStartX(), move.getStartY()) == false) {
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

    /**
     * Check for third move repetition.
     *
     * @return True if the move was repeated, false otherwise.
     */
    public boolean hasThirdMoveRepetition() {
        int count = 0;

        if (history.size() <= 0) {
            return false;
        }

		/*
		 * Last move will be compared with moves before that.
		 */
        Move lastMove = history.lastElement();
        for (int i = history.size() - 1; i >= 0; i -= 2) {
            if (lastMove.equals(history.elementAt(i)) == false) {
                break;
            }

            count++;
        }

        if (count >= 3) {
            return true;
        }

        return false;
    }

    /**
     * Check for winner combination on the board.
     *
     * @return True if there is a winner combination, false otherwise.
     */
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

    /**
     * Check for available valid move.
     *
     * @return True if there is a valid move, false otherwise.
     */
    public boolean hasValidMove() {
        for (int sx = 0; sx < COLS; sx++) {
            for (int sy = 0; sy < ROWS; sy++) {
                for (int ex = 0; ex < COLS; ex++) {
                    for (int ey = 0; ey < ROWS; ey++) {
                        if (isValid(new Move(sx, sy, ex, ey)) == true) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Check for game over conditions according to the game rules.
     *
     * @return True if there are game over condition, false otherwise.
     */
    public boolean checkForGameOver() {
        if (hasWinner() == true) {
            return true;
        }

        if (hasThirdMoveRepetition() == true) {
            return true;
        }

        if (hasValidMove() == false) {
            return true;
        }

        return false;
    }

    /**
     * Provide two-dimensional array with the winners combinations.
     *
     * @return Two-dimensional array with true in the combination and false if
     * it is not in the combination.
     */
    public boolean[][] winners() {
        boolean winners[][] = {{false, false, false, false},
                {false, false, false, false}, {false, false, false, false},
                {false, false, false, false},};

        for (int i = 0; i < winners.length; i++) {
            for (int j = 0; j < winners[i].length; j++) {
                if (hasHorizontalLine(i, j) == true) {
                    for (int k = 0; k < WIN_LINE_LENGTH; k++) {
                        winners[i + k][j] = true;
                    }
                }
                if (hasVerticalLine(i, j) == true) {
                    for (int k = 0; k < WIN_LINE_LENGTH; k++) {
                        winners[i][j + k] = true;
                    }
                }
                if (hasFirstDiagonalLine(i, j) == true) {
                    for (int k = 0; k < WIN_LINE_LENGTH; k++) {
                        winners[i + k][j + k] = true;
                    }
                }
                if (hasSecondDiagonalLine(i, j) == true) {
                    for (int k = 0; k < WIN_LINE_LENGTH; k++) {
                        winners[i - k][j + k] = true;
                    }
                }
            }
        }

        return winners;
    }

    /**
     * Generate all valid moves for the currrent state.
     *
     * @return List of all valid moves.
     */
    public List<Move> allValidMoves() {
        List<Move> moves = new ArrayList<Move>();

        for (int x1 = 0; x1 < Board.COLS; x1++) {
            for (int y1 = 0; y1 < Board.ROWS; y1++) {
                for (int x2 = 0; x2 < Board.COLS; x2++) {
                    for (int y2 = 0; y2 < Board.ROWS; y2++) {
                        Move move = new Move(x1, y1, x2, y2);

                        if (isValid(move) == true) {
                            moves.add(move);
                        }
                    }
                }
            }
        }

        return moves;
    }

    /**
     * Provide binary encoding of the board stated.
     *
     * @return String representation of the binary encoding.
     */
    public byte[] toBinary() {
        byte bytes[] = {};

        try {
            ByteArrayOutputStream out = null;
            (new ObjectOutputStream(out = new ByteArrayOutputStream())).writeObject(this);
            out.flush();
            bytes = out.toByteArray();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    /**
     * Provide board object from bytes encoding.
     *
     * @param bytes Binary representation of the board.
     * @return Board object.
     */
    public Board fromBinary(byte[] bytes) {
        Board board = this;

        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
            board = (Board) in.readObject();
            in.close();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        this.turn = board.turn;
        this.turnOver = board.turnOver;
        this.gameOver = board.gameOver;
        this.pieces = board.pieces;
        this.selection = board.selection;
        this.history = board.history;

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < COLS; i++) {
            for (int j = 0; j < ROWS; j++) {
                result += pieces[i][j];
                result += " ";
            }
            result = result.trim();
            result += "\n";
        }
        result = result.trim();

        return result;
    }
}
