package eu.veldsoft.ithaka.board.game.model;

import java.io.Serializable;

/**
 * Single move.
 *
 * @author Todor Balabanov
 */
public class Move implements Serializable {
	/**
	 * Move x from coordinate.
	 */
	private int startX;

	/**
	 * Move y from coordinate.
	 */
	private int startY;

	/**
	 * Move x to coordinate.
	 */
	private int endX;

	/**
	 * Move y to coordinate.
	 */
	private int endY;

	/**
	 * Constructor with all parameters.
	 *
	 * @param startX Move x from coordinate.
	 * @param startY Move y from coordinate.
	 * @param endX   Move x to coordinate.
	 * @param endY   Move y to coordinate.
	 */
	public Move(int startX, int startY, int endX, int endY) {
		super();
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
	}

	/**
	 * Copy constructor.
	 *
	 * @param coordinates Object to copy from.
	 */
	public Move(int coordinates[]) {
		this(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
	}

	/**
	 * Copy constructor.
	 *
	 * @param move Object to copy from.
	 */
	public Move(Move move) {
		this(move.startX, move.startY, move.endX, move.endY);
	}

	/**
	 * Get from x coordinate.
	 *
	 * @return From x coordinate.
	 */
	public int getStartX() {
		return startX;
	}

	/**
	 * Get from y coordinate.
	 *
	 * @return From y coordinate.
	 */
	public int getStartY() {
		return startY;
	}

	/**
	 * Get to y coordinate.
	 *
	 * @return To y coordinate.
	 */
	public int getEndX() {
		return endX;
	}

	/**
	 * Get to y coordinate.
	 *
	 * @return To y coordinate.
	 */
	public int getEndY() {
		return endY;
	}


	/**
	 * Provide binary encoding of the move.
	 *
	 * @return String representation of the binary encoding.
	 */
	public Long toBinary() {
		return (long) (startX * Board.COLS + startY) << 16 | (long) (endX * Board.COLS + endY);
	}

	/**
	 * Provide move object from binary encoding.
	 *
	 * @param binary Binary representation of the board.
	 * @return Board object.
	 */
	public Move fromBinary(long binary) {
		long start = (binary >> 16) & 0xFFFF;
		long end = binary & 0xFFFF;

		startX = (int) (start / Board.COLS);
		startY = (int) (start % Board.COLS);

		endX = (int) (end / Board.COLS);
		endY = (int) (end % Board.COLS);

		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + endX;
		result = prime * result + endY;
		result = prime * result + startX;
		result = prime * result + startY;
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		if (endX != other.endX)
			return false;
		if (endY != other.endY)
			return false;
		if (startX != other.startX)
			return false;
		return startY == other.startY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Move [startX=" + startX + ", startY=" + startY + ", endX="
				  + endX + ", endY=" + endY + "]";
	}
}
