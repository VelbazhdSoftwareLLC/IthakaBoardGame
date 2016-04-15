package eu.veldsoft.ithaka.board.game;

/**
 * Single move.
 * 
 * @author Todor Balabanov
 */
class Move {
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
	 * @param startX
	 *            Move x from coordinate.
	 * @param startY
	 *            Move y from coordinate.
	 * @param endX
	 *            Move x to coordinate.
	 * @param endY
	 *            Move y to coordinate.
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
	 * @param move
	 *            Object to copy from.
	 */
	public Move(Move move) {
		super();
		this.startX = move.startX;
		this.startY = move.startY;
		this.endX = move.endX;
		this.endY = move.endY;
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
		if (startY != other.startY)
			return false;
		return true;
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
